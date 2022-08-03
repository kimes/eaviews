package ph.easyaf.eaviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import ph.easyaf.eaviews.R;

public abstract class PinView extends LinearLayoutCompat {

    public static final int DEFAULT = 0, SUCCESS = 1, ERROR = 2;

    protected static final char[] NUMBERS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' },
            LETTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
                    'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' },
            LETTERS_CAP = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
                    'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };

    protected static final int TYPING_INTERVAL = 800;

    protected static final int DEFAULT_PIN_LENGTH = 4;

    protected int state = DEFAULT, stateDefaultBackground = R.color.gray30,
            stateSuccessBackground = R.color.success,
            stateErrorBackground = R.color.error;

    protected boolean password = true, isOpenKeyboard = true;
    protected int inputType = EditorInfo.TYPE_CLASS_NUMBER, textFontWeight = 300;
    protected int pinLength = 4;
    protected int pinMarginRight = 10, pinPaddingTop = 20, pinPaddingBottom = 20, pinWidth = 0;
    protected int pinTextBackground = android.R.color.background_dark;
    protected int textColor = android.R.color.white;
    protected float textSize = 24f;
    protected String pin = "";

    protected PinView.OnPinCodeChangeListener onPinCodeChangeListener;
    protected PinView.OnTypingChangedListener onTypingChangedListener;
    public void setOnTypingChangedListener(PinView.OnTypingChangedListener onTypingChangedListener) {
        this.onTypingChangedListener = onTypingChangedListener;
    }

    protected Handler handler = new Handler();
    protected Runnable notifier = () -> {
        if (onTypingChangedListener != null) onTypingChangedListener.onTyping(this, false);
    };

    protected boolean caps = false, hasFocus = false;

    public PinView(Context context) { super(context); }

    @SuppressLint("ResourceAsColor")
    public PinView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinView);

        try {
            state = a.getInt(R.styleable.PinView_state, DEFAULT);
            stateDefaultBackground = a.getResourceId(R.styleable.PinView_stateDefaultBackground,
                    R.color.gray30);
            stateSuccessBackground = a.getResourceId(R.styleable.PinView_stateSuccessBackground,
                    R.color.success);
            stateErrorBackground = a.getResourceId(R.styleable.PinView_stateErrorBackground,
                    R.color.error);

            inputType = a.getInt(R.styleable.PinView_android_inputType, EditorInfo.TYPE_CLASS_NUMBER);
            textFontWeight = a.getInteger(R.styleable.PinView_textFontWeight, 300);

            isOpenKeyboard = a.getBoolean(R.styleable.PinView_isOpenKeyboard, true);
            password = a.getBoolean(R.styleable.PinView_password, true);
            pinLength = a.getInt(R.styleable.PinView_pinLength, DEFAULT_PIN_LENGTH);

            pinTextBackground = a.getResourceId(R.styleable.PinView_pinTextBackground,
                    android.R.color.background_dark);
            pinMarginRight = a.getDimensionPixelSize(R.styleable.PinView_pinMarginRight, 10);
            pinPaddingTop = a.getDimensionPixelSize(R.styleable.PinView_pinPaddingTop, 20);
            pinPaddingBottom = a.getDimensionPixelSize(R.styleable.PinView_pinPaddingBottom, 20);
            pinWidth = a.getDimensionPixelSize(R.styleable.PinView_pinWidth, 0);
            textColor = a.getColor(R.styleable.PinView_textColor, getResources().getColor(android.R.color.white));
            textSize = a.getDimension(R.styleable.PinView_textSize, 24f);
        } finally {
            a.recycle();
        }
    }

    protected abstract void init();

    public boolean onTouchEvent(MotionEvent event) {
        if (isOpenKeyboard) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(this, InputMethodManager.SHOW_FORCED);
            }
        }
        return super.onTouchEvent(event);
    }

    public boolean onCheckIsTextEditor() { return true; }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        BaseInputConnection fic = new BaseInputConnection(this, false);
        outAttrs.actionLabel = null;
        outAttrs.inputType = inputType;
        //outAttrs.inputType = InputType.TYPE_CLASS_NUMBER;
        outAttrs.imeOptions = EditorInfo.IME_ACTION_DONE;
        return fic;
    }

    public String getPin() { return pin; }
    public void setPin(String pin) {
        if (pin == null) return;
        //if (pin.length() < pinLength) return;
        this.pin = pin;
        /*
        for (int i = 0; i < pin.length(); i++) {
            String textToPut = password ? "*" : (pin.charAt(i) + "");
            ((AppCompatEditText)getChildAt(i)).setText(textToPut);
        }

        for (int i = pin.length(); i < pinLength; i++) {
            ((AppCompatEditText)getChildAt(i)).setText("");
        }

        if (onPinCodeChangeListener != null) onPinCodeChangeListener.onPinCodeChange(this, this.pin); */
    }

    public int getState() { return state; }
    public void setState(int state) {
        this.state = state;
    }

    public void setOnPinCodeChangeListener(PinView.OnPinCodeChangeListener listener) {
        onPinCodeChangeListener = listener;
    }

    public interface OnPinCodeChangeListener {
        void onPinCodeChange(PinView view, String pin);
    }

    public interface OnTypingChangedListener {
        void onTyping(PinView view, boolean isTyping);
    }
}
