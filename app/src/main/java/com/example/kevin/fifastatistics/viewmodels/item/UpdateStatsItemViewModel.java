package com.example.kevin.fifastatistics.viewmodels.item;

import android.content.Context;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.FifaApplication;
import com.example.kevin.fifastatistics.R;
import com.example.kevin.fifastatistics.databinding.ItemStatUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.interfaces.Predicate;
import com.example.kevin.fifastatistics.interfaces.StatUpdater;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;
import com.example.kevin.fifastatistics.viewmodels.FifaBaseViewModel;

import lombok.Builder;

import static com.example.kevin.fifastatistics.activities.MatchUpdateActivity.MatchEditType;

public class UpdateStatsItemViewModel extends FifaBaseViewModel implements StatUpdater {

    private static final float UPDATED_ALPHA = 0.5f;
    private static final int CURRENT_VALUE_WIDTH;

    static {
        Context c = FifaApplication.getContext();
        float width = c.getResources().getDimension(R.dimen.match_card_current_width);
        CURRENT_VALUE_WIDTH = Math.round(width);
    }

    private ItemStatUpdateBinding binding;
    private final Consumer<Integer> forConsumer;
    private final Consumer<Integer> againstConsumer;
    private final Predicate<Integer> forPredicate;
    private final Predicate<Integer> againstPredicate;
    private final String label;
    private final String errorMessage;
    private final int statFor;
    private final int statAgainst;
    private final boolean arePredicatesLinked;

    private final MatchEditType type;
    private final Integer updateFor;
    private final Integer updateAgainst;

    private boolean mIsForError;
    private boolean mIsAgainstError;
    private boolean mIsCheckingLinked;
    private float mAlphaFor = 1f;
    private float mAlphaAgainst = 1f;

    @Builder
    public UpdateStatsItemViewModel(Consumer<Integer> forConsumer, Consumer<Integer> againstConsumer,
                                    Predicate<Integer> forPredicate, Predicate<Integer> againstPredicate,
                                    ItemStatUpdateBinding binding, String label, String errorMessage,
                                    int statFor, int statAgainst, boolean arePredicatesLinked,
                                    Integer updateFor, Integer updateAgainst, MatchEditType type) {
        this.binding = binding;
        this.forConsumer = forConsumer;
        this.againstConsumer = againstConsumer;
        this.forPredicate = forPredicate;
        this.againstPredicate = againstPredicate;
        this.label = label;
        this.errorMessage = errorMessage;
        this.statFor = statFor;
        this.statAgainst = statAgainst;
        this.arePredicatesLinked = arePredicatesLinked;
        this.updateFor = updateFor;
        this.updateAgainst = updateAgainst;
        this.type = type;
        initAlpha();
        initEditTextValue();
    }

    private void initAlpha() {
        float alpha = 1f;
        if (isReviewing()) {
            alpha = UPDATED_ALPHA;
        }
        mAlphaFor = alpha;
        mAlphaAgainst = alpha;
    }

    private void initEditTextValue() {
        if (type == MatchEditType.CREATE) {
            if (updateFor != null) {
                binding.statForEdittext.setText(String.valueOf(updateFor));
            }
            if (updateAgainst != null) {
                binding.statAgainstEdittext.setText(String.valueOf(updateAgainst));
            }
        }
    }

    public String getStatFor() {
       return String.valueOf(statFor);
    }

    public String getStatAgainst() {
        return String.valueOf(statAgainst);
    }

    public String getLabel() {
        return label;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setEditTextValues(float valueFor, float valueAgainst) {
        binding.statForEdittext.setText(String.valueOf(Math.round(valueFor)));
        binding.statAgainstEdittext.setText(String.valueOf(Math.round(valueAgainst)));
    }

    @Override
    public void onStatForChanged(Editable s) {
        Integer newVal = TextUtils.isEmpty(s.toString()) ? null : Integer.valueOf(s.toString());
        mIsForError = forPredicate != null && !forPredicate.test(newVal);
        consumeValue(newVal, forConsumer, binding.statForEdittext, statFor, mIsForError);
        checkLinkedPredicate(newVal, forConsumer, binding.statAgainstEdittext, this::onStatAgainstChanged);
        updateAlpha(newVal, statFor, binding.statForTextview, binding.statForEdittext, this::setAlphaFor);
        updateError();
    }

    @Override
    public void onStatAgainstChanged(Editable s) {
        Integer newVal = TextUtils.isEmpty(s.toString()) ? null : Integer.valueOf(s.toString());
        mIsAgainstError = againstPredicate != null && !againstPredicate.test(newVal);
        consumeValue(newVal, againstConsumer, binding.statAgainstEdittext, statAgainst, mIsAgainstError);
        checkLinkedPredicate(newVal, againstConsumer, binding.statForEdittext, this::onStatForChanged);
        updateAlpha(newVal, statAgainst, binding.statAgainstTextview, binding.statAgainstEdittext, this::setAlphaAgainst);
        updateError();
    }

    private void consumeValue(Integer newVal, Consumer<Integer> consumer, EditText editText, int stat, boolean error) {
        if (newVal == null) {
            consumer.accept(MatchUpdate.Builder.REMOVE_VAL);
        } else if (isUpdatedStatSameAsCurrent(newVal, stat)) {
            editText.setText("");
        } else if (!error) {
            consumer.accept(newVal);
        }
    }

    private boolean isUpdatedStatSameAsCurrent(int newStat, int current) {
        return newStat == current && !isCreating();
    }

    private void checkLinkedPredicate(Integer newVal, Consumer<Integer> consumer, EditText editText,
                                      Consumer<Editable> statConsumer) {
        if (arePredicatesLinked && !mIsCheckingLinked) {
            if (newVal != null) {
                consumer.accept(newVal);
            }
            mIsCheckingLinked = true;
            statConsumer.accept(editText.getText());
            mIsCheckingLinked = false;
        }
    }

    private void updateAlpha(Integer newVal, int stat, TextView textView, EditText editText,
                             Consumer<Float> alphaConsumer) {
        float alpha = textView.getAlpha();
        if (isError() && alpha == UPDATED_ALPHA) {
            alphaConsumer.accept(1f);
        } else if (!isError() && isEdited(editText) && alpha == 1f) {
            alphaConsumer.accept(UPDATED_ALPHA);
        } else if (!isEdited(editText) && alpha == UPDATED_ALPHA) {
            alphaConsumer.accept(1f);
        } else if (newVal != null && newVal == stat && alpha == UPDATED_ALPHA) {
            alphaConsumer.accept(1f);
        }
    }

    private boolean isEdited(EditText text) {
        Editable e = text.getText();
        return e != null && !TextUtils.isEmpty(e.toString());
    }

    private void setAlphaFor(float alpha) {
        mAlphaFor = alpha;
        notifyPropertyChanged(BR.alphaFor);
    }

    private void setAlphaAgainst(float alpha) {
        mAlphaAgainst = alpha;
        notifyPropertyChanged(BR.alphaAgainst);
    }

    private void updateError() {
        int errorVisibility = binding.errorMessage.getVisibility();
        boolean needToShowError = isError() && errorVisibility == View.GONE;
        boolean needToHideError = !isError() && errorVisibility == View.VISIBLE;
        if (needToHideError || needToShowError) {
            notifyPropertyChanged(BR.errorMessageVisibility);
        }
    }

    public boolean isError() {
        return mIsAgainstError || mIsForError;
    }

    public boolean isValid() {
        onStatForChanged(binding.statForEdittext.getText());
        if (!arePredicatesLinked) {
            onStatAgainstChanged(binding.statAgainstEdittext.getText());
        }
        if (isError()) {
            binding.errorMessage.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    public boolean areEditTextsFilled() {
        boolean forFilled = !TextUtils.isEmpty(binding.statForEdittext.getText());
        boolean againstFilled = !TextUtils.isEmpty(binding.statAgainstEdittext.getText());
        if (!forFilled) {
            binding.statForEdittext.requestFocus();
        } else if (!againstFilled) {
            binding.statAgainstEdittext.requestFocus();
        }
        return forFilled && againstFilled;
    }

    public void onStatForClicked() {
        binding.statForEdittext.requestFocus();
    }

    public void onStatAgainstClicked() {
        binding.statAgainstEdittext.requestFocus();
    }

    @Bindable
    public int getErrorMessageVisibility() {
        return isError() ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public float getAlphaFor() {
        return mAlphaFor;
    }

    @Bindable
    public float getAlphaAgainst() {
        return mAlphaAgainst;
    }

    public float getBaseAlpha() {
        return isReviewing() ? UPDATED_ALPHA : 1f;
    }

    public int getEditTextVisibility() {
        return isReviewing() ? View.GONE : View.VISIBLE;
    }

    public int getUpdateVisibility() {
        return isReviewing() ? View.VISIBLE : View.GONE;
    }

    public int getCurrentValueVisibility() {
        return isCreating() ? View.INVISIBLE : View.VISIBLE;
    }

    public int getCurrentValueWidth() {
        return isCreating() ? 0 : CURRENT_VALUE_WIDTH;
    }

    private boolean isReviewing() {
        return type == MatchEditType.REVIEW;
    }

    private boolean isCreating() {
        return type == MatchEditType.CREATE;
    }

    public String getUpdateFor() {
        return updateFor != null ? String.valueOf(updateFor) : null;
    }

    public String getUpdateAgainst() {
        return updateAgainst != null ? String.valueOf(updateAgainst) : null;
    }
}
