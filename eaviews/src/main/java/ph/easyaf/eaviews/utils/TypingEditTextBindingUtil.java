package ph.easyaf.eaviews.utils;

import androidx.databinding.BindingAdapter;

import ph.easyaf.eaviews.views.TypingEditText;

public class TypingEditTextBindingUtil {

    @BindingAdapter("onTypingChanged")
    public static void setOnTypingChangedListener(TypingEditText errorEditText,
                                                  TypingEditText.OnTypingChangedListener onTypingChangedListener) {
        errorEditText.setOnTypingChangedListener((editText, isTyping) -> {
            if (onTypingChangedListener != null) onTypingChangedListener.onTyping(editText, isTyping);
        });
    }
}
