package ph.easyaf.eaviews.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;

import ph.easyaf.eaviews.R;

public class PinCodeView extends PinView {

    protected int textCursorDrawable = -1;

    public PinCodeView(Context context) {
        super(context);
        init();
    }

    public PinCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinCodeView);

        try {
            textCursorDrawable = a.getResourceId(R.styleable.PinCodeView_textCursorDrawable, -1);
        } finally {
            a.recycle();
        }
        init();
    }

    @SuppressLint("ResourceAsColor")
    protected void init() {
        //LayoutInflater inflater = LayoutInflater.from(getContext());
        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setOrientation(HORIZONTAL);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        // convert the DP into pixel
        int pixel =  (int)(10 * scale + 0.5f);

        for (int i = 0; i < pinLength; i++) {
            //AppCompatTextView textView = new AppCompatTextView(getContext());
            AppCompatEditText textView = new AppCompatEditText(getContext());
            textView.setTextColor(textColor);
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(0, pinPaddingTop, 0, pinPaddingBottom);

            //textView.setWidth(120);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            textView.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1) });
            textView.setInputType(inputType);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                Typeface face = Typeface.create(textView.getTypeface(), textFontWeight, false);
                textView.setTypeface(face);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (textCursorDrawable >= 0) textView.setTextCursorDrawable(textCursorDrawable);
            }

            final int index = i;
            textView.setKeyListener(new KeyListener() {
                public int getInputType() {
                    return inputType;
                }

                public boolean onKeyDown(View view, Editable editable, int keyCode, KeyEvent keyEvent) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DEL:
                            textView.setText("");
                            if (index - 1 >= 0) {
                                AppCompatEditText editText = (AppCompatEditText)getChildAt(index - 1);
                                editText.requestFocus();
                                editText.selectAll();
                            }

                            pin = "";
                            for (int i = 0; i < getChildCount(); i++) {
                                pin += ((AppCompatEditText)getChildAt(i)).getText().toString();
                            }

                            if (onPinCodeChangeListener != null)
                                onPinCodeChangeListener.onPinCodeChange(PinCodeView.this, pin);
                            return true;
                    }
                    return false;
                }

                public boolean onKeyUp(View view, Editable editable, int keyCode, KeyEvent keyEvent) {
                    return false;
                }

                public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
                    return false;
                }

                public void clearMetaKeyState(View view, Editable editable, int states) {}
            });
            textView.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}
                public void afterTextChanged(Editable editable) {}

                public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                    if (onTypingChangedListener != null) {
                        onTypingChangedListener.onTyping(PinCodeView.this, true);
                        handler.removeCallbacks(notifier);
                        handler.postDelayed(notifier, TYPING_INTERVAL);
                    }

                    if (count > 0) {
                        pin = "";
                        for (int i = 0; i < getChildCount(); i++) {
                            pin += ((AppCompatEditText)getChildAt(i)).getText().toString();
                        }

                        if (onPinCodeChangeListener != null)
                            onPinCodeChangeListener.onPinCodeChange(PinCodeView.this, pin);

                        if (index + 1 < pinLength) {
                            AppCompatEditText editText = (AppCompatEditText)getChildAt(index + 1);
                            editText.requestFocus();
                            editText.selectAll();
                        }
                    }

                    /*
                    if (before >= 1) {
                        if (index - 1 >= 0) {
                            AppCompatEditText editText = (AppCompatEditText)getChildAt(index - 1);
                            editText.requestFocus();
                            editText.selectAll();
                        }
                    } */
                }
            });

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
            //textView.setBackgroundResource(pinTextBackground);

            LayoutParams params =
                    new LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
            if (pinWidth <= 0) {
                params = new LayoutParams(0,
                        LayoutParams.WRAP_CONTENT);
                params.weight = 1;
            } else {
                textView.setWidth(pinWidth);
            }

            //MarginLayoutParams s = (MarginLayoutParams)params;
            if (i < pinLength - 1) {
                params.rightMargin = pinMarginRight;
            }
            textView.setLayoutParams(params);

            addView(textView);
        }

        //((AppCompatEditText)getChildAt(0)).requestFocus();

        setOnFocusChangeListener((v, hasFocus) -> {
            this.hasFocus = hasFocus;

            if (this.hasFocus) highlightIndicator();
            else unhighlightIndicator();
        });
    }

    public String getPin() { return pin; }
    public void setPin(String pin) {
        if (pin == null) return;
        //if (pin.length() < pinLength) return;
        this.pin = pin;
        System.out.println("Pin: " + this.pin + " | " + getChildCount());
        for (int i = 0; i < pin.length(); i++) {
            String textToPut = password ? "*" : (pin.charAt(i) + "");
            ((AppCompatEditText)getChildAt(i)).setText(textToPut);
        }

        for (int i = pin.length(); i < pinLength; i++) {
            ((AppCompatEditText)getChildAt(i)).setText("");
        }


        AppCompatEditText editText = null;
        if (pin.length() < pinLength) {
            editText = (AppCompatEditText)getChildAt(pin.length());
        } else if (pin.length() == pinLength) {
            editText = (AppCompatEditText)getChildAt(pin.length() - 1);
        }

        if (editText != null) {
            editText.requestFocus();
            editText.selectAll();
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
            AppCompatEditText textView = ((AppCompatEditText)getChildAt(i));
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
            ((AppCompatEditText)getChildAt(pin.length()))
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
            ((AppCompatEditText)getChildAt(pin.length() - 1))
                    .setBackgroundResource(backgroundResource);
        }

        if (pin.length() + 1 < pinLength) {
            ((AppCompatEditText)getChildAt(pin.length() + 1))
                    .setBackgroundResource(backgroundResource);
        }
    }
}
