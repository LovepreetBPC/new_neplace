package com.example.neplacecustomer.utils.button

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton


@SuppressLint("NewApi")
    class Button700 (context: Context?, attrs: AttributeSet?) : AppCompatButton(context!!, attrs) {

    init {
        val typeface = Typeface.createFromAsset(getContext().assets, "fonts/inter_bold.ttf")
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB ||
            android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
            setTypeface(typeface)
        }
    }

    }

