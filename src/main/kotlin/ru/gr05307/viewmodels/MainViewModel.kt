package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.geometry.Size
import ru.gr05307.painting.CartesianPainter
import ru.gr05307.painting.FunctionPainter
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import kotlin.math.abs
import kotlin.math.pow

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)


    var xMin: Double? by mutableStateOf(plain.xMin)
    var xMax: Double? by mutableStateOf(plain.xMax)
    var yMin: Double? by mutableStateOf(plain.yMin)
    var yMax: Double? by mutableStateOf(plain.yMax)


    var points by mutableStateOf(listOf<Pair<Double, Double>>())

    private val cartesianPainter = CartesianPainter(
        size = Size(0f, 0f),
        plain = plain
    )


    fun addPoint(click: Offset) {
        val x = Converter.xScr2Crt(click.x, plain)
        val y = Converter.yScr2Crt(click.y, plain)

        // Проверяем, что нет другой точки слишком близко по X
        val existsNearby = points.any { abs(it.first - x) < 0.2 }
        if (existsNearby) return

        points = points + (x to y)
    }


    fun removePoint(click: Offset) {
        if (points.isEmpty()) return

        val toRemove = points.minByOrNull {
            val sx = Converter.xCrt2Scr(it.first, plain)
            val sy = Converter.yCrt2Scr(it.second, plain)
            (click - Offset(sx, sy)).getDistance()
        }

        if (toRemove != null) {
            val dist = (click - Offset(
                Converter.xCrt2Scr(toRemove.first, plain),
                Converter.yCrt2Scr(toRemove.second, plain)
            )).getDistance()

            if (dist < 10f) { // если клик близко
                points = points - toRemove
            }
        }
    }

    fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = scope.size.width
        plain.height = scope.size.height

        cartesianPainter.draw(scope, measurer)

        val funcPainter = FunctionPainter(
            size = scope.size,
            plain = plain,
            f = { x -> x.pow(4) },
            color = Color.Red,
            strokeWidth = 2f
        )
        funcPainter.draw(scope, measurer)


        for ((x, y) in points) {
            val sx = Converter.xCrt2Scr(x, plain)
            val sy = Converter.yCrt2Scr(y, plain)
            scope.drawCircle(
                color = Color.Green,
                radius = 6f,
                center = Offset(sx, sy)
            )
        }
    }
}
