package com.kausthubh.basis.home.views

import android.content.Context
import android.util.TypedValue


fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    ).toInt()
}

fun spToPx(sp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        sp,
        context.resources.displayMetrics
    ).toInt()
}


fun getDisplayWidth(context: Context) = context.resources.displayMetrics.widthPixels
fun getDisplayHeight(context: Context) = context.resources.displayMetrics.heightPixels
