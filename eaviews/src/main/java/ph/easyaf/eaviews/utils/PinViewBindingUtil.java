package ph.easyaf.eaviews.utils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import ph.easyaf.eaviews.views.PinCodeView;
import ph.easyaf.eaviews.views.PinView;

public class PinViewBindingUtil {

    @BindingAdapter("state")
    public static void setState(PinView pinCodeView, int state) {
        if (pinCodeView.getState() != state) pinCodeView.setState(state);
    }

    @BindingAdapter(value = { "pinCodeChange", "pinAttrChanged" }, requireAll = false)
    public static void setPinListener(PinView pinCodeView,
                                      final PinView.OnPinCodeChangeListener pinCodeChangeListener,
                                      final InverseBindingListener bindingListener) {
        pinCodeView.setOnPinCodeChangeListener((view, pin) -> {
            if (bindingListener != null) bindingListener.onChange();
            if (pinCodeChangeListener != null) pinCodeChangeListener.onPinCodeChange(view, pin);
        });
    }

    @BindingAdapter("pin")
    public static void setPin(PinView pinCodeView, String pin) {
        if (!pinCodeView.getPin().equals(pin)) pinCodeView.setPin(pin);
    }

    @InverseBindingAdapter(attribute = "pin", event = "pinAttrChanged")
    public static String getPin(PinView pinCodeView) {
        return pinCodeView.getPin();
    }
}
