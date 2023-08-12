package com.example.go2life.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.format.DateUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}
fun setImageToImageView(context: Context ,imageView: ImageView, drawableResourceId: Int) {
    imageView.setImageDrawable(ContextCompat.getDrawable(context, drawableResourceId))
}

fun View.visible() {
    this.visibility = View.VISIBLE
}
fun setTextColorToPink(textView: TextView) {
    textView.setTextColor(Color.parseColor("#E20DA1"))
}
fun Long.divToMonthsAndDays(): Pair<Long, Long> {
    val daysInMonth = 30L // Assuming an average month has 30 days
    val months = this / (daysInMonth * DateUtils.DAY_IN_MILLIS)
    val days = (this - months * daysInMonth * DateUtils.DAY_IN_MILLIS) / DateUtils.DAY_IN_MILLIS
    return Pair(months, days)
}

fun setTextColorToDefault(
    textView: TextView
) {
    textView.setTextColor(Color.parseColor("#474747"))
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun Context.getColors(color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Bundle?.getStringValue(key: String): String {
    return this?.getString(key, "null").toString()
}

//fun SpannableString.spannableBold(
//    start: Int,
//    end: Int,
//    font: Typeface? = ResourcesCompat.getFont(MyApplication.getContext(), R.font.poppins_bold),
//    color: Int = MyApplication.getContext().getColors(R.color.darkGreen)
//) {
//    this.setSpan(
//        font?.let { CustomTypefaceSpan("", it) },
//        start,
//        end,
//        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
//    )
//    this.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
//}
//
//fun SpannableString.spannableBoldColor(
//    start: Int,
//    end: Int,
//    font: Typeface? = ResourcesCompat.getFont(MyApplication.getContext(), R.font.poppins_bold),
//    color: Int = MyApplication.getContext().getColors(R.color.bgColor)
//) {
//    this.setSpan(
//        font?.let { CustomTypefaceSpan("", it) },
//        start,
//        end,
//        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
//    )
//    this.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
//}
//
//fun SpannableString.spannableRegular(
//    start: Int,
//    end: Int,
//    font: Typeface? = ResourcesCompat.getFont(MyApplication.getContext(), R.font.poppins_regular),
//    color: Int = MyApplication.getContext().getColors(R.color.bgColor)
//) {
//    this.setSpan(
//        font?.let { CustomTypefaceSpan("", it) },
//        start,
//        end,
//        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
//    )
//    this.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
//}
//
//fun SpannableString.spannableLightText(
//    start: Int,
//    end: Int,
//    font: Typeface? = ResourcesCompat.getFont(MyApplication.getContext(), R.font.poppins_light),
//    color: Int = MyApplication.getContext().getColor(R.color.lightGreen)
//) {
//    this.setSpan(
//        font?.let { CustomTypefaceSpan("", it) },
//        start,
//        end,
//        Spanned.SPAN_EXCLUSIVE_INCLUSIVE
//    )
//    this.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
//}
//
fun getCurrentDate(): Calendar {
    return Calendar.getInstance()
}

@SuppressLint("SimpleDateFormat")
fun getCurrentDateString(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(calendar.time)
}

@SuppressLint("SimpleDateFormat")
fun getDateDifferenceInDays(startDate: String, endDate: String): Long {
    val dateFormat =
        SimpleDateFormat("yyyy-MM-dd") // Adjust the date format based on your input date string

    val startDateObj: Date? = dateFormat.parse(startDate)
    val endDateObj: Date? = dateFormat.parse(endDate)

    val startDateCal = Calendar.getInstance()
    startDateCal.time = startDateObj!!

    val endDateCal = Calendar.getInstance()
    endDateCal.time = endDateObj!!

    // Calculate the difference between the two dates in milliseconds
    val timeDifferenceInMillis =
        kotlin.math.abs(endDateCal.timeInMillis - startDateCal.timeInMillis)

    // Convert milliseconds to days
    return timeDifferenceInMillis / (1000 * 60 * 60 * 24)


}

fun getCurrentTimeInAMPM(): String {
    val dateFormat = SimpleDateFormat("hh:mm a", Locale.US)
    val currentTime = Date()
    return dateFormat.format(currentTime)
}

fun createPlaceholderImage(
    name: String, context: Context
): BitmapDrawable {
    var drawable: BitmapDrawable? = null
    try {
        drawable = MaterialTextDrawable.with(context)
            .text(name.uppercase())
            .get()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return drawable!!
}
fun String.underlineSpan(): SpannableString {
    val spannableString = SpannableString(this)
    val underlineSpan = UnderlineSpan()
    spannableString.setSpan(underlineSpan, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return spannableString
}
fun String.underlineColorSpan(underlineColor: Int = Color.RED): SpannableString {
    val spannableString = SpannableString(this)
    val underlineSpan = UnderlineSpan()
    val colorSpan = ForegroundColorSpan(underlineColor)

    spannableString.setSpan(underlineSpan, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(colorSpan, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    return spannableString
}
fun SpannableString.underlineColorLineSpan(lineColor: Int) {
    val underlineSpan = UnderlineSpan()
    val colorSpan = ForegroundColorSpan(lineColor)
    this.setSpan(underlineSpan, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    this.setSpan(colorSpan, 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
}


fun TextView.setClickableAndUnderlinedText(
    fullText: String,
    clickableText: String,
    onClickAction: () -> Unit
) {
    val spannableText = SpannableString(fullText)
    val startIndex = fullText.indexOf(clickableText)

    if (startIndex != -1) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                onClickAction()
            }
        }

        spannableText.setSpan(clickableSpan, startIndex, startIndex + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableText.setSpan(UnderlineSpan(), startIndex, startIndex + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        text = spannableText
        movementMethod = LinkMovementMethod.getInstance()
    } else {
        text = fullText
    }
}

