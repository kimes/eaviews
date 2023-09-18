package ph.easyaf.eaviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.regex.Pattern;

import ph.easyaf.eaviews.R;

public class ErrorEditText extends TypingEditText implements TextWatcher {

    protected static final int[] STATE_ERROR = { R.attr.state_error };

    private OnHasErrorChangedListener onHasErrorChangedListener;
    public void setOnHasErrorChangedListener(OnHasErrorChangedListener onHasErrorChangedListener) {
        this.onHasErrorChangedListener = onHasErrorChangedListener;
    }

    private boolean hasError = true;

    public ErrorEditText(Context context) {
        super(context);
        addTextChangedListener(this);
    }

    public ErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        addTextChangedListener(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ErrorEditText,
                0, 0);
    }

    public ErrorEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ErrorEditText,
                0, 0);
    }

    public boolean getHasError() { return hasError; }

    public void setHasError(boolean error) {
        hasError = error;
        if (onHasErrorChangedListener != null) onHasErrorChangedListener.onHasErrorChanged(this, hasError);
        refreshDrawableState();
    }


    public void afterTextChanged(Editable s) {
        if (onTypingChangedListener != null) {
            onTypingChangedListener.onTyping(this, true);
            handler.removeCallbacks(notifier);
            handler.postDelayed(notifier, TYPING_INTERVAL);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (hasError) {
            mergeDrawableStates(drawableState, STATE_ERROR);
        }
        return drawableState;
    }

    public interface OnHasErrorChangedListener {
        void onHasErrorChanged(ErrorEditText editText, boolean hasError);
    }
}
