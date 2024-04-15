package com.example.neplacecustomer.utils.edittext

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EdittextSemiBold  (context: Context?, attrs: AttributeSet?) : AppCompatEditText(context!!, attrs) {
    init {
        val typeface = Typeface.createFromAsset(getContext().assets, "fonts/inter_semi_bold.ttf")
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB ||
            android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2)
            {
//                val typeface1 = Typeface.create(typeface, 400, false)
            setTypeface(typeface)
        }
    }


}


