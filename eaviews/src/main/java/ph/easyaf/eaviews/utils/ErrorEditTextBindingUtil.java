package ph.easyaf.eaviews.utils;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import ph.easyaf.eaviews.views.ErrorEditText;

public class ErrorEditTextBindingUtil {

    @BindingAdapter("hasErrorAttrChanged")
    public static void setHasErrorListener(ErrorEditText editText,
                                           final InverseBindingListener bindingListener) {
        editText.setOnHasErrorChangedListener((editText1, hasError) -> {
            if (bindingListener != null) bindingListener.onChange();
        });
    }

    @BindingAdapter("hasError")
    public static void setHasError(ErrorEditText editText, boolean hasError) {
        if (editText.getHasError() != hasError) {
            editText.setHasError(hasError);
        }
    }

    @InverseBindingAdapter(attribute = "hasError", event = "hasErrorAttrChanged")
    public static boolean getHasError(ErrorEditText editText) {
        return editText.getHasError();
    }
}
