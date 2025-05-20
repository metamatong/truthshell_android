package com.example.truthshellapp.ui.camera

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

/**
 * A transparent overlay view that draws a 3x3 grid for camera composition.
 */
class GridOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, android.R.color.white)
        strokeWidth = 2f
        style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val thirdWidth = width / 3f
        val thirdHeight = height / 3f
        for (i in 1..2) {
            // vertical lines
            canvas.drawLine(thirdWidth * i, 0f, thirdWidth * i, height.toFloat(), paint)
            // horizontal lines
            canvas.drawLine(0f, thirdHeight * i, width.toFloat(), thirdHeight * i, paint)
        }
    }
}