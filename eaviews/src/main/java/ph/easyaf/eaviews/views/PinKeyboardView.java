package ph.easyaf.eaviews.views;

import android.animation.AnimatorInflater;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.widget.LinearLayoutCompat;

import ph.easyaf.eaviews.R;

public class PinKeyboardView extends LinearLayoutCompat implements View.OnClickListener {

    protected int pinView = 0, buttonBackground = R.drawable.bg_btn_dark,
            textColor = R.color.blue10, stateListAnimator = 0, imageButtonPadding = 10;
    protected float textSize = 24f, elevation = 3;

    public PinKeyboardView(Context context) {
        super(context);
        init();
    }

    @SuppressLint("ResourceAsColor")
    public PinKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PinKeyboardView);
        try {
            textColor = a.getColor(R.styleable.PinKeyboardView_textColor, getResources().getColor(R.color.blue10));
            textSize = a.getDimension(R.styleable.PinKeyboardView_textSize, 24f);
            elevation = a.getDimension(R.styleable.PinKeyboardView_elevation, 3);
            imageButtonPadding = a.getDimensionPixelSize(R.styleable.PinKeyboardView_imageButtonPadding, 10);
            pinView = a.getResourceId(R.styleable.PinKeyboardView_pinView, 0);
            buttonBackground = a.getResourceId(R.styleable.PinKeyboardView_buttonBackground, R.drawable.bg_btn_dark);
            stateListAnimator = a.getResourceId(R.styleable.PinKeyboardView_stateListAnimator, 0);
        } finally {
            a.recycle();
        }

        init();
    }

    public void onClick(View view) {
        View pin = getRootView().findViewById(pinView);

        int keyCode = 0;
        int id = view.getId();
        if (id == R.id.btn_num_0) {
            keyCode = KeyEvent.KEYCODE_0;
        } else if (id == R.id.btn_num_1) {
            keyCode = KeyEvent.KEYCODE_1;
        } else if (id == R.id.btn_num_2) {
            keyCode = KeyEvent.KEYCODE_2;
        } else if (id == R.id.btn_num_3) {
            keyCode = KeyEvent.KEYCODE_3;
        } else if (id == R.id.btn_num_4) {
            keyCode = KeyEvent.KEYCODE_4;
        } else if (id == R.id.btn_num_5) {
            keyCode = KeyEvent.KEYCODE_5;
        } else if (id == R.id.btn_num_6) {
            keyCode = KeyEvent.KEYCODE_6;
        } else if (id == R.id.btn_num_7) {
            keyCode = KeyEvent.KEYCODE_7;
        } else if (id == R.id.btn_num_8) {
            keyCode = KeyEvent.KEYCODE_8;
        } else if (id == R.id.btn_num_9) {
            keyCode = KeyEvent.KEYCODE_9;
        } else if (id == R.id.btn_num_delete) {
            keyCode = KeyEvent.KEYCODE_DEL;
        }

        if (pin != null) {
            BaseInputConnection inputConnection = new BaseInputConnection(pin, true);
            inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
        }
    }

    @SuppressLint("ResourceAsColor")
    private void init() {
        View view = inflate(getContext(), R.layout.view_pin_keyboard, this);

        int[] buttons = { R.id.btn_num_0, R.id.btn_num_1, R.id.btn_num_2,
                        R.id.btn_num_3, R.id.btn_num_4, R.id.btn_num_5,
                        R.id.btn_num_6, R.id.btn_num_7, R.id.btn_num_8,
                        R.id.btn_num_9 },
            imgButtons = { R.id.btn_num_delete, R.id.btn_num_enter };
        for (int i = 0; i < buttons.length; i++) {
            Button child = view.findViewById(buttons[i]);
            child.setTextColor(textColor);
            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            child.setElevation(elevation);
            child.setBackgroundResource(buttonBackground);
            if (stateListAnimator > 0) {
                child.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), stateListAnimator));
            } else child.setStateListAnimator(null);

            child.setOnClickListener(this);
        }

        for (int i = 0; i < imgButtons.length; i++) {
            ImageButton child = view.findViewById(imgButtons[i]);
            child.setColorFilter(textColor);
            child.setElevation(elevation);
            child.setPadding(imageButtonPadding, imageButtonPadding, imageButtonPadding, imageButtonPadding);
            child.setBackgroundResource(buttonBackground);
            if (stateListAnimator > 0) {
                child.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), stateListAnimator));
            } else child.setStateListAnimator(null);

            child.setOnClickListener(this);
        }
        /*
        view.findViewById(R.id.btn_num_0).setOnClickListener(this);
        view.findViewById(R.id.btn_num_1).setOnClickListener(this);
        view.findViewById(R.id.btn_num_2).setOnClickListener(this);
        view.findViewById(R.id.btn_num_3).setOnClickListener(this);
        view.findViewById(R.id.btn_num_4).setOnClickListener(this);
        view.findViewById(R.id.btn_num_5).setOnClickListener(this);
        view.findViewById(R.id.btn_num_6).setOnClickListener(this);
        view.findViewById(R.id.btn_num_7).setOnClickListener(this);
        view.findViewById(R.id.btn_num_8).setOnClickListener(this);
        view.findViewById(R.id.btn_num_9).setOnClickListener(this);
        view.findViewById(R.id.btn_num_delete).setOnClickListener(this); */
    }
}
