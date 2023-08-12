package com.example.go2life.utils

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class CustomClickableSpan(private val onClickListener: () -> Unit) : ClickableSpan() {
    override fun onClick(widget: View) {
        onClickListener()
    }

    override fun updateDrawState(ds: TextPaint) {
        // Customize the appearance of the span if needed (e.g., changing text color, underline, etc.)
        // Uncomment and modify the properties below as per your requirements:
        // ds.isUnderlineText = false
        // ds.color = Color.BLUE
    }
}

