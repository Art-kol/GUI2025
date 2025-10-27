package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import java.math.MathContext
import kotlin.math.round

class CartesianPainter(
    override val size: Size,
    val plain: Plain
) : Painter {

    var axesColor: Color = Color.Black

    override fun draw(scope: DrawScope, measurer: TextMeasurer) {
        drawAxes(scope)
        drawXTicks(scope, measurer)
        drawYTicks(scope, measurer)
    }

    private fun drawXTicks(scope: DrawScope, measurer: TextMeasurer) {
        var xDot = plain.xMin
        while (xDot <= plain.xMax) {
            val xPos = Converter.xCrt2Scr(xDot, plain)
            val tickType = when {
                (round(xDot * 10) % 10 == 0.0) -> TickType.MAX
                (round(xDot * 10) % 5 == 0.0) -> TickType.MID
                else -> TickType.MIN
            }

            drawXTick(scope, xPos, tickType)

            if ((tickType == TickType.MAX) and (xDot.toInt() != 0) ) {
                val label = AnnotatedString(xDot.toInt().toString())
                val textLayout = measurer.measure(label)
                val labelWidth = textLayout.size.width.toFloat()

                val yZero = Converter.yCrt2Scr(0.0, plain)
                val textOffset = Offset(
                    xPos - labelWidth / 2f,
                    yZero + TickType.MAX.halfSize + textLayout.size.height / 2f + 4f
                )
                scope.drawText(measurer, label, textOffset)
            }

            xDot = (xDot + 0.1).toBigDecimal()
                .round(MathContext(3))
                .setScale(1)
                .toDouble()
        }
    }

    private fun drawXTick(scope: DrawScope, pos: Float, type: TickType) {
        val zero = Converter.yCrt2Scr(0.0, plain)
        scope.drawLine(
            color = type.color,
            start = Offset(pos, zero + type.halfSize),
            end = Offset(pos, zero - type.halfSize)
        )
    }

    private fun drawYTicks(scope: DrawScope, measurer: TextMeasurer) {
        var yDot = plain.yMin
        while (yDot <= plain.yMax) {
            val yPos = Converter.yCrt2Scr(yDot, plain)
            val tickType = when {
                (round(yDot * 10) % 10 == 0.0) -> TickType.MAX
                (round(yDot * 10) % 5 == 0.0) -> TickType.MID
                else -> TickType.MIN
            }

            drawYTick(scope, yPos, tickType)

            if ( (tickType == TickType.MAX) and (yDot.toInt() != 0) ) {
                val label = AnnotatedString(yDot.toInt().toString())
                val textLayout = measurer.measure(label)
                val labelHeight = textLayout.size.height.toFloat()

                val xZero = Converter.xCrt2Scr(0.0, plain)
                val textOffset = Offset(
                    xZero + TickType.MAX.halfSize + 4f,
                    yPos - labelHeight / 2f
                )
                scope.drawText(measurer, label, textOffset)
            }

            yDot = (yDot + 0.1).toBigDecimal()
                .round(MathContext(3))
                .setScale(1)
                .toDouble()
        }
    }

    private fun drawYTick(scope: DrawScope, pos: Float, type: TickType) {
        val zero = Converter.xCrt2Scr(0.0, plain)
        scope.drawLine(
            color = type.color,
            start = Offset(zero + type.halfSize, pos),
            end = Offset(zero - type.halfSize, pos)
        )
    }

    private fun drawAxes(scope: DrawScope) {
        val yZero = Converter.yCrt2Scr(0.0, plain)
        val xZero = Converter.xCrt2Scr(0.0, plain)

        scope.drawLine(
            color = axesColor,
            start = Offset(0f, yZero),
            end = Offset(plain.width, yZero)
        )

        scope.drawLine(
            color = axesColor,
            start = Offset(xZero, 0f),
            end = Offset(xZero, plain.height)
        )
    }
}

enum class TickType(val color: Color, val halfSize: Float) {
    MIN(Color.Black, 3f),
    MID(Color.Blue, 5f),
    MAX(Color.Red, 8f)
}
