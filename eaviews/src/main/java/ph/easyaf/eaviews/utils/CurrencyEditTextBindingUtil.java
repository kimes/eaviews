package ph.easyaf.eaviews.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyEditTextBindingUtil {

    @BindingAdapter(value = "fareAttrChanged")
    public static void setFareListener(EditText textView,
                                       final InverseBindingListener bindingListener) {
        textView.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) {}
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) {}

            public void afterTextChanged(Editable s) {
                bindingListener.onChange();
            }
        });
    }

    @BindingAdapter("fare")
    public static void setFare(EditText textView, double fare) {
        try {
            //if (textView.getText().toString().isEmpty()) return;
            String nText = textView.getText().toString().replaceAll("[,]", "");
            if (nText.isEmpty()) {
                DecimalFormat formatter = new DecimalFormat("#,##0.00");
                formatter.setRoundingMode(RoundingMode.DOWN);
                String formatText = formatter.format(fare);
                textView.setText(formatText);
            } else {
                double val = Double.parseDouble(nText);
                if (val == fare) return;
                DecimalFormat formatter = new DecimalFormat("#,##0.00");
                formatter.setRoundingMode(RoundingMode.DOWN);
                String formatText = formatter.format(fare);
                textView.setText(formatText);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            if (!textView.getText().equals("0.00")) textView.setText("0.00");
        }
    }

    @InverseBindingAdapter(attribute = "fare", event = "fareAttrChanged")
    public static double getFare(EditText textView) {
        try {
            String nText = textView.getText().toString().replaceAll("[,]", "");
            return Double.parseDouble(nText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
