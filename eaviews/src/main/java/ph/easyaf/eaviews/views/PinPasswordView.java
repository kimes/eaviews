package ph.easyaf.eaviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

public class PinPasswordView extends PinView {

    public PinPasswordView(Context context) {
        super(context);
        init();
    }

    public PinPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("ResourceAsColor")
    protected void init() {
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOrientation(HORIZONTAL);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        // convert the DP into pixel
        int pixel =  (int)(10 * scale + 0.5f);

        for (int i = 0; i < pinLength; i++) {
            AppCompatTextView textView = new AppCompatTextView(getContext());
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, pinPaddingTop, 0, pinPaddingBottom);

            //textView.setWidth(120);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

            switch (state) {
                case DEFAULT:
                    textView.setBackgroundResource(stateDefaultBackground);
                    break;
                case SUCCESS:
                    textView.setBackgroundResource(stateSuccessBackground);
                    break;
                case ERROR:
                    textView.setBackgroundResource(stateErrorBackground);
                    break;
            }

            LinearLayoutCompat.LayoutParams params =
                    new LinearLayoutCompat.LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT);
            if (pinWidth <= 0) {
                params = new LayoutParams(0,
                        LayoutParams.WRAP_CONTENT);
                params.weight = 1;
            } else {
                textView.setWidth(pinWidth);
            }

            if (i < pinLength - 1) {
                params.rightMargin = pinMarginRight;
            }
            textView.setLayoutParams(params);

            addView(textView);
        }

        setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (onTypingChangedListener != null) {
                    onTypingChangedListener.onTyping(this, true);
                    handler.removeCallbacks(notifier);
                    handler.postDelayed(notifier, TYPING_INTERVAL);
                }

                switch (event.getKeyCode()) {
                    case KeyEvent.KEYCODE_DEL:
                        if (pin.length() > 0) {
                            ((AppCompatTextView)getChildAt(pin.length() - 1)).setText(" ");
                            pin = pin.substring(0, pin.length() - 1);

                            highlightIndicator();

                            if (onPinCodeChangeListener != null)
                                onPinCodeChangeListener.onPinCodeChange(this, pin);
                        }
                        break;
                    case KeyEvent.KEYCODE_ENTER:
                        break;
                    case KeyEvent.KEYCODE_SHIFT_LEFT:
                        caps = true;
                        break;
                    default:
                        char c = (char)event.getUnicodeChar();
                        char b = (char)event.getUnicodeChar(event.getMetaState());

                        if (pin.length() < pinLength) {
                            char key = c;
                            if (keyCode >= 7 && keyCode <= 16) {
                                key = NUMBERS[keyCode - 7];
                            }

                            if (keyCode >= 29 && keyCode <= 54) {
                                if (caps) key = LETTERS_CAP[keyCode - 29];
                                else key = LETTERS[keyCode - 29];
                            }
                            String textToPut = password ? "*" : (key + "");
                            ((AppCompatTextView)getChildAt(pin.length())).setText(textToPut);
                            pin += (key + "");

                            highlightIndicator();

                            if (onPinCodeChangeListener != null)
                                onPinCodeChangeListener.onPinCodeChange(this, pin);
                        }

                        caps = false;
                        break;
                }
                return true;
            }
            return false;
        });
    }

    public String getPin() { return pin; }
    public void setPin(String pin) {
        if (pin == null) return;
        //if (pin.length() < pinLength) return;
        this.pin = pin;
        for (int i = 0; i < pin.length(); i++) {
            String textToPut = password ? "*" : (pin.charAt(i) + "");
            ((AppCompatTextView)getChildAt(i)).setText(textToPut);
        }

        for (int i = pin.length(); i < pinLength; i++) {
            ((AppCompatTextView)getChildAt(i)).setText("");
        }

        if (onPinCodeChangeListener != null) onPinCodeChangeListener.onPinCodeChange(this, this.pin);
    }

    public int getState() { return state; }
    public void setState(int state) {
        this.state = state;

        unhighlightIndicator();

        if (hasFocus) highlightIndicator();
    }

    private void unhighlightIndicator() {
        for (int i = 0; i < getChildCount(); i++) {
            AppCompatTextView textView = ((AppCompatTextView)getChildAt(i));
            if (textView == null) break;

            switch (state) {
                case DEFAULT:
                    textView.setBackgroundResource(stateDefaultBackground);
                    break;
                case SUCCESS:
                    textView.setBackgroundResource(stateSuccessBackground);
                    break;
                case ERROR:
                    textView.setBackgroundResource(stateErrorBackground);
                    break;
            }
        }
    }

    private void highlightIndicator() {
        if (pin.length() < pinLength) {
            ((AppCompatTextView)getChildAt(pin.length()))
                    .setBackgroundResource(stateDefaultBackground);
        }

        int backgroundResource = stateDefaultBackground;
        switch (state) {
            case DEFAULT:
                backgroundResource = stateDefaultBackground;
                break;
            case SUCCESS:
                backgroundResource = stateSuccessBackground;
                break;
            case ERROR:
                backgroundResource = stateErrorBackground;
                break;
        }

        if (pin.length() > 0) {
            ((AppCompatTextView)getChildAt(pin.length() - 1))
                    .setBackgroundResource(backgroundResource);
        }

        if (pin.length() + 1 < pinLength) {
            ((AppCompatTextView)getChildAt(pin.length() + 1))
                    .setBackgroundResource(backgroundResource);
        }
    }
}
