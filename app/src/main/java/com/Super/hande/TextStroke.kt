package com.Super.hande

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class TextStroke @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val text = "Treino de Precisão"

    private val strokePaint = Paint().apply {
        color = Color.BLACK  // Cor da borda
        textSize = 80f
        style = Paint.Style.STROKE
        strokeWidth = 8f
        isAntiAlias = true
    }

    private val fillPaint = Paint().apply {
        color = Color.WHITE  // Cor do texto principal
        textSize = 80f
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val x = width / 2f - strokePaint.measureText(text) / 2
        val y = height / 2f + 20  // Ajuste fino na altura

        // Primeiro desenha o contorno (borda do texto)
        canvas.drawText(text, x, y, strokePaint)
        // Depois desenha o texto principal
        canvas.drawText(text, x, y, fillPaint)
    }
}