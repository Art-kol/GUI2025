package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import java.math.MathContext

class CartesianPainter(
    val plain: Plain
) {

    var axesColor: Color = Color.Black

    fun draw(scope: DrawScope,){
        drawAxes(scope)
        drawXTicks(scope)
        drawYTicks(scope)
    }

    private fun drawXTicks(scope: DrawScope) {
        var xDot = plain.xMin
        while (xDot <= plain.xMax){
            val xPos = Converter.xCrt2Scr(xDot, plain)
            drawXTick(scope, xPos, when {
                (xDot * 10).toInt() % 10 == 0 -> TickType.MAX
                (xDot * 10).toInt() % 5 == 0 -> TickType.MID
                else -> TickType.MIN
            })
            xDot = (xDot + 0.1).toBigDecimal().round(MathContext.DECIMAL32).setScale(1).toDouble()
        }

    }

    private fun drawXTick(scope: DrawScope, pos: Float, type: TickType) {
        Converter.yCrt2Scr(.0, plain).let { zero ->
            scope.drawLine(
                type.color,
                Offset(pos, zero+type.halfSize),
                Offset(pos, zero-type.halfSize)
            )
        }
    }


    private fun drawYTicks(scope: DrawScope) {
        var yDot = plain.yMin
        while (yDot <= plain.yMax){
            val yPos = Converter.yCrt2Scr(yDot, plain)
            drawYTick(scope, yPos, when {
                (yDot * 10).toInt() % 10 == 0 -> TickType.MAX
                (yDot * 10).toInt() % 5 == 0 -> TickType.MID
                else -> TickType.MIN
            })
            yDot = (yDot + 0.1).toBigDecimal().round(MathContext.DECIMAL32).setScale(1).toDouble()
        }
    }

    private fun drawYTick(scope: DrawScope, pos: Float, type: TickType) {
        Converter.xCrt2Scr(.0, plain).let { zero ->
            scope.drawLine(
                type.color,
                Offset(zero+type.halfSize, pos),
                Offset(zero-type.halfSize, pos)
            )
        }
    }

    private fun drawXLabel(scope: DrawScope, value: String){

    }

    private fun drawYLabel(scope: DrawScope, value: String){

    }

    private fun drawAxes(scope: DrawScope) {
        Converter.yCrt2Scr(.0, plain).let { zero ->
            scope.drawLine(
                axesColor,
                Offset(0f, zero),
                Offset(plain.width, zero)
            )
        }
        Converter.xCrt2Scr(.0, plain).let { zero ->
            scope.drawLine(
                axesColor,
                Offset(zero, 0f),
                Offset(zero, plain.height)
            )
        }
    }


}

enum class TickType(var color: Color, var halfSize: Float){
    MIN(Color.Black, 3f),
    MID(Color.Blue, 5f),
    MAX(Color.Red, 8f)
}