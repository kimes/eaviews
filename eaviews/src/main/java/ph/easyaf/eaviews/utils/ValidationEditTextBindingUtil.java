package ph.easyaf.eaviews.utils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import ph.easyaf.eaviews.views.ValidationEditText;

public class ValidationEditTextBindingUtil {

    @BindingAdapter("isSubmittedAttrChanged")
    public static void setIsSubmittedListener(ValidationEditText editText,
                                                final InverseBindingListener bindingListener) {
        editText.setOnIsSubmittedChangedListener((editText1, isSubmitted) -> {
            if (bindingListener != null) bindingListener.onChange();
        });
    }

    @BindingAdapter("isSubmitted")
    public static void setIsSubmitted(ValidationEditText editText, boolean isSubmitted) {
        if (editText.getIsSubmitted() != isSubmitted) {
            editText.setIsSubmitted(isSubmitted);
        }
    }

    @InverseBindingAdapter(attribute = "isSubmitted", event = "isSubmittedAttrChanged")
    public static boolean getIsSubmitted(ValidationEditText editText) {
        return editText.getIsSubmitted();
    }
}
