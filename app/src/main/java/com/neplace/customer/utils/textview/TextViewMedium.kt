package com.neplace.customer.utils.textview
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView

@RequiresApi(Build.VERSION_CODES.P)
class TextViewMedium (context: Context?, attrs: AttributeSet?) : AppCompatTextView(context!!, attrs) {
    init {
        val typeface = Typeface.createFromAsset(getContext().assets, "fonts/inter_medium.ttf")
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB ||
            android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
        {
//                val typeface1 = Typeface.create(typeface, 400, false)
            setTypeface(typeface)
        }
    }
}