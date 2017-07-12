package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.databinding.ItemStatUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.interfaces.Predicate;
import com.example.kevin.fifastatistics.interfaces.StatUpdater;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

import lombok.Builder;

public class UpdateStatsItemViewModel extends FifaBaseViewModel implements StatUpdater {

    private static final float UPDATED_ALPHA = 0.5f;

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

    private boolean mIsForError;
    private boolean mIsAgainstError;
    private boolean mIsCheckingLinked;
    private float mAlphaFor = 1f;
    private float mAlphaAgainst = 1f;

    @Builder
    public UpdateStatsItemViewModel(Consumer<Integer> forConsumer, Consumer<Integer> againstConsumer,
                                    Predicate<Integer> forPredicate, Predicate<Integer> againstPredicate,
                                    ItemStatUpdateBinding binding, String label, String errorMessage,
                                    int statFor, int statAgainst, boolean arePredicatesLinked) {
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

    public void onStatForChanged(Editable s) {
        Integer newVal = TextUtils.isEmpty(s.toString()) ? null : Integer.valueOf(s.toString());
        mIsForError = forPredicate != null && !forPredicate.test(newVal);
        consumeValue(newVal, forConsumer, binding.statForEdittext, statFor, mIsForError);
        checkLinkedPredicate(newVal, forConsumer, binding.statAgainstEdittext, this::onStatAgainstChanged);
        updateAlpha(newVal, statFor, binding.statForTextview, binding.statForEdittext, this::setAlphaFor);
        updateError();
    }

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
        } else if (stat == newVal) {
            editText.setText("");
        } else if (!error) {
            consumer.accept(newVal);
        }
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
}
