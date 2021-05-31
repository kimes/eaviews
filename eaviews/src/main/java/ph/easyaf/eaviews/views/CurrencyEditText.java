package ph.easyaf.eaviews.views;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CurrencyEditText extends AppCompatEditText {

    private String prevText = "A";
    private TextWatcher textWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        public void afterTextChanged(Editable s) {
            String text = s.toString();

            String cleanString = text.replaceAll("[,.]", "");
            if (cleanString.equals(prevText) || cleanString.isEmpty()) return;

            prevText = cleanString;

            BigDecimal parsed = new BigDecimal(cleanString);
            parsed = parsed.divide(BigDecimal.valueOf(100.0), 2,
                    RoundingMode.HALF_UP);
            // example pattern VND #,###.00
            DecimalFormat formatter = new DecimalFormat("#,##0.00");
            formatter.setRoundingMode(RoundingMode.DOWN);
            String formatText = formatter.format(parsed);

            removeTextChangedListener(this);
            setText(formatText);
            setSelection(formatText.length());
            addTextChangedListener(this);
        }
    };

    public CurrencyEditText(Context context) {
        super(context);
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        setSelection(getText().length());
        //super.onSelectionChanged(selStart, selEnd);
    }

    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            addTextChangedListener(textWatcher);
        } else {
            removeTextChangedListener(textWatcher);
        }
    }
}
