package com.example.nikejosecaballero.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class SineView @kotlin.jvm.JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var path = Path()
    private var rect = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setUp(canvas)
    }

    private fun setUp(canvas: Canvas) {
        val eighth = rect.width() / 8
        val sixth = rect.width() / 6
        val fourth = rect.width() / 4
        val half = rect.width() / 2

        path.moveTo(rect.left, rect.bottom - sixth)
        path.lineTo(rect.left, rect.bottom - eighth)
        path.lineTo(rect.right, rect.bottom - half)
        path.lineTo(rect.right, rect.bottom - (half + eighth))

        //path.quadTo(rect.left, rect.bottom - widthFraction, rect.right, rect.centerY())

        val paint = Paint()
        paint.style = Paint.Style.FILL_AND_STROKE
        canvas.drawPath(path, paint)
    }
}