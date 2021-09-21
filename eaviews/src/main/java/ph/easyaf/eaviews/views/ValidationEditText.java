package ph.easyaf.eaviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Pattern;

import ph.easyaf.eaviews.R;

public class ValidationEditText extends AppCompatEditText implements TextWatcher {

    private static final int[] STATE_ERROR = { R.attr.state_error };
    private static final int[] STATE_TOUCHED = { R.attr.state_touched };
    private static final int[] STATE_SUBMITTED = { R.attr.state_submitted };

    private OnIsSubmittedChangedListener onIsSubmittedChangedListener;
    public void setOnIsSubmittedChangedListener(OnIsSubmittedChangedListener onIsSubmittedChangedListener) {
        this.onIsSubmittedChangedListener = onIsSubmittedChangedListener;
    }

    private boolean hasError = true, hasTouched = false, isSubmitted = false;
    private String regex = "";

    public ValidationEditText(Context context) {
        super(context);
        addTextChangedListener(this);
    }

    public ValidationEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ValidationEditText);
        try {
            regex = a.getString(R.styleable.ValidationEditText_regex);
        } finally {
            a.recycle();
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        // extraSpace + {{ number of custom states }}
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 2);

        if (hasError) {
            mergeDrawableStates(drawableState, STATE_ERROR);
        }
        if (hasTouched) {
            mergeDrawableStates(drawableState, STATE_TOUCHED);
        }
        if (isSubmitted) {
            mergeDrawableStates(drawableState, STATE_SUBMITTED);
        }
        return drawableState;
    }

    public void afterTextChanged(Editable s) {
        String text = s.toString();
        if (!Pattern.matches(regex, text)) hasError = true;
        else hasError = false;
        refreshDrawableState();
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!hasTouched) {
            if (focused) hasTouched = true;
        }
    }

    public boolean getHasError() { return hasError; }
    public boolean getHasTouched() { return hasTouched; }
    public boolean getIsSubmitted() { return isSubmitted; }
    public String getRegex() { return regex; }
    public void setRegex(String regex) { this.regex = regex; }
    public void setIsSubmitted(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
        if (onIsSubmittedChangedListener != null)
            onIsSubmittedChangedListener.onIsSubmittedChanged(this, this.isSubmitted);
        refreshDrawableState();
    }

    public interface OnIsSubmittedChangedListener {
        void onIsSubmittedChanged(ValidationEditText editText, boolean hasSubmitted);
    }
}
