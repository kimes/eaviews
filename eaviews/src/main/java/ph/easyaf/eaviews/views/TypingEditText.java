package ph.easyaf.eaviews.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import ph.easyaf.eaviews.R;

public abstract class TypingEditText extends AppCompatEditText {

    protected static final int TYPING_INTERVAL = 800;

    protected OnTypingChangedListener onTypingChangedListener;
    public void setOnTypingChangedListener(OnTypingChangedListener onTypingChangedListener) {
        this.onTypingChangedListener = onTypingChangedListener;
    }

    protected Handler handler = new Handler();

    protected Runnable notifier = () -> {
        if (onTypingChangedListener != null) onTypingChangedListener.onTyping(this, false);
    };

    public TypingEditText(Context context) {
        super(context);
    }

    public TypingEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypingEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface OnTypingChangedListener {
        void onTyping(TypingEditText editText, boolean isTyping);
    }
}
