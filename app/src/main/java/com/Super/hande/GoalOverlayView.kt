package com.Super.hande

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GoalOverlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var acertos: MutableList<Pair<Float, Float>> = mutableListOf()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        // Definir pontos estratégicos da trave
        val pontos = listOf(
            Pair(width * 0.1f, height * 0.1f),  // Canto superior esquerdo
            Pair(width * 0.9f, height * 0.1f),  // Canto superior direito
            Pair(width * 0.1f, height * 0.9f),  // Canto inferior esquerdo
            Pair(width * 0.9f, height * 0.9f)   // Canto inferior direito
        )

        // Desenha os pontos iniciais em vermelho
        paint.color = Color.RED
        pontos.forEach { (x, y) ->
            canvas.drawCircle(x, y, 20f, paint)
        }

        // Desenha os pontos acertados em verde
        paint.color = Color.GREEN
        acertos.forEach { (x, y) ->
            canvas.drawCircle(x, y, 25f, paint)
        }
    }

    // Método para atualizar pontos acertados
    fun atualizarPontos(acertosNovos: List<Pair<Float, Float>>) {
        acertos = acertosNovos.toMutableList()
        invalidate() // Redesenha a tela
    }
}
