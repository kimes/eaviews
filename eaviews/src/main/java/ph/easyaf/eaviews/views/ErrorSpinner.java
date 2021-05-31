package ph.easyaf.eaviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSpinner;

import ph.easyaf.eaviews.R;

public class ErrorSpinner extends AppCompatSpinner {

    private static final int[] STATE_ERROR = { R.attr.state_error };

    private boolean hasError = true, openInitiated = false, open = false;

    private OnSpinnerEventListener onSpinnerEventListener;
    public void setOnSpinnerEventListener(OnSpinnerEventListener onSpinnerEventListener) {
        this.onSpinnerEventListener = onSpinnerEventListener;
    }

    public ErrorSpinner(Context context) {
        super(context);
        checkHasErrors();
    }

    public ErrorSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ErrorEditText,
                0, 0);
        checkHasErrors();
    }

    public void setSelection(int position) {
        super.setSelection(position);
        checkHasErrors();
    }

    public void setSelection(int position, boolean animate) {
        super.setSelection(position, animate);
        checkHasErrors();
    }

    public boolean performClick() {
        openInitiated = true;
        open = true;
        if (onSpinnerEventListener != null) onSpinnerEventListener.onSpinnerOpened();
        return super.performClick();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (openInitiated && hasWindowFocus) {
            open = false;
            if (onSpinnerEventListener != null) onSpinnerEventListener.onSpinnerClosed();
        }
    }

    public boolean getHasError() { return hasError; }
    public boolean isOpen() { return open; }

    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (hasError) {
            mergeDrawableStates(drawableState, STATE_ERROR);
        }
        return drawableState;
    }

    private void checkHasErrors() {
        if (getSelectedItem() != null) hasError = false;
        else hasError = true;
        refreshDrawableState();
    }

    public interface OnSpinnerEventListener {
        void onSpinnerOpened();
        void onSpinnerClosed();
    }
}
