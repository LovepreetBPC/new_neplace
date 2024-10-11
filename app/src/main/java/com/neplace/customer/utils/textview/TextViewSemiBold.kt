package com.neplace.customer.utils.textview
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


@SuppressLint("ObsoleteSdkInt")
class TextViewSemiBold(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context!!, attrs) {

    init {
        val typeface = Typeface.createFromAsset(getContext().assets, "fonts/inter_semi_bold.ttf")
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB ||
            android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            setTypeface(typeface)
        }
    }
}