package com.example.kevin.fifastatistics.viewmodels;

import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.example.kevin.fifastatistics.BR;
import com.example.kevin.fifastatistics.databinding.ItemStatUpdateBinding;
import com.example.kevin.fifastatistics.interfaces.Consumer;
import com.example.kevin.fifastatistics.interfaces.Predicate;
import com.example.kevin.fifastatistics.models.databasemodels.match.MatchUpdate;

import lombok.Builder;

public class UpdateStatsItemViewModel extends FifaBaseViewModel {

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

    private boolean mIsForError;
    private boolean mIsAgainstError;
    private float mAlphaFor = 1f;
    private float mAlphaAgainst = 1f;

    @Builder
    public UpdateStatsItemViewModel(Consumer<Integer> forConsumer, Consumer<Integer> againstConsumer,
                                    Predicate<Integer> forPredicate, Predicate<Integer> againstPredicate,
                                    ItemStatUpdateBinding binding, String label, String errorMessage,
                                    int statFor, int statAgainst) {
        this.binding = binding;
        this.forConsumer = forConsumer;
        this.againstConsumer = againstConsumer;
        this.forPredicate = forPredicate;
        this.againstPredicate = againstPredicate;
        this.label = label;
        this.errorMessage = errorMessage;
        this.statFor = statFor;
        this.statAgainst = statAgainst;
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
        if (newVal == null) {
            forConsumer.accept(MatchUpdate.Builder.REMOVE_VAL);
        } else if (statFor == newVal) {
            binding.statForEdittext.setText("");
        } else if (!mIsForError) {
            forConsumer.accept(newVal);
        }
        updateAlphaFor(newVal);
        updateError();
    }

    private void updateAlphaFor(Integer newVal) {
        float alpha = binding.statForTextview.getAlpha();
        if (isError() && alpha == UPDATED_ALPHA) {
            setAlphaFor(1f);
        } else if (!isError() && isForEdited() && alpha == 1f) {
            setAlphaFor(UPDATED_ALPHA);
        } else if (!isForEdited() && alpha == UPDATED_ALPHA) {
            setAlphaFor(1f);
        } else if (newVal != null && newVal == statFor && alpha == UPDATED_ALPHA) {
            setAlphaFor(1f);
        }
    }

    private boolean isForEdited() {
        Editable e = binding.statForEdittext.getText();
        return e != null && !TextUtils.isEmpty(e.toString());
    }

    private void setAlphaFor(float alpha) {
        mAlphaFor = alpha;
        notifyPropertyChanged(BR.alphaFor);
    }

    public void onStatAgainstChanged(Editable s) {
        Integer newVal = TextUtils.isEmpty(s.toString()) ? null : Integer.valueOf(s.toString());
        mIsAgainstError = againstPredicate != null && !againstPredicate.test(newVal);
        if (newVal == null) {
            againstConsumer.accept(MatchUpdate.Builder.REMOVE_VAL);
        } else if (statAgainst == newVal) {
            binding.statAgainstEdittext.setText("");
        } else if (!mIsAgainstError) {
            againstConsumer.accept(newVal);
        }
        updateAlphaAgainst(newVal);
        updateError();
    }

    private void updateAlphaAgainst(Integer newVal) {
        float alpha = binding.statAgainstTextview.getAlpha();
        if (isError() && alpha == UPDATED_ALPHA) {
            setAlphaAgainst(1f);
        } else if (!isError() && isAgainstEdited() && alpha == 1f) {
            setAlphaAgainst(UPDATED_ALPHA);
        } else if (!isAgainstEdited() && alpha == UPDATED_ALPHA) {
            setAlphaAgainst(1f);
        } else if (newVal != null && newVal == statAgainst && alpha == UPDATED_ALPHA) {
            setAlphaAgainst(1f);
        }
    }

    private boolean isAgainstEdited() {
        Editable e = binding.statAgainstEdittext.getText();
        return e != null && !TextUtils.isEmpty(e.toString());
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

    private boolean isError() {
        return mIsAgainstError || mIsForError;
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
