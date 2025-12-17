package ru.gr05307.viewmodels

import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.geometry.Size
import ru.gr05307.painting.CartesianPainter
import ru.gr05307.painting.RobustFunctionPainter
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import ru.gr05307.model.math.polynomial.Newton
import ru.gr05307.model.math.Function
import kotlin.math.abs

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)

    var xMin by mutableStateOf(plain.xMin)
    var xMax by mutableStateOf(plain.xMax)
    var yMin by mutableStateOf(plain.yMin)
    var yMax by mutableStateOf(plain.yMax)

    var showPoints by mutableStateOf(true)
    var showPolynomial by mutableStateOf(true)
    var showDerivative by mutableStateOf(false)

    var polyColor by mutableStateOf(Color.Red)
    var pointsColor by mutableStateOf(Color.Green)
    var derivativeColor by mutableStateOf(Color.Blue)

    var points by mutableStateOf(listOf<Pair<Double, Double>>())
    private val newton = Newton()

    private val cartesianPainter = CartesianPainter(
        size = Size(0f, 0f),
        plain = plain
    )

    private fun rebuildPolynomial() {
        try {
            val map = points.associate { it.first to it.second }.toSortedMap()
            val updated = Newton(map)
            newton.copyFrom(updated)
        } catch (e: Exception) {
            println("Ошибка при пересчёте полинома: ${e.message}")
        }
    }

    fun addPoint(click: Offset) {
        val x = Converter.xScr2Crt(click.x, plain)
        val y = Converter.yScr2Crt(click.y, plain)
        if (points.any { abs(it.first - x) < 0.1 }) return
        points = (points + (x to y)).sortedBy { it.first }
        rebuildPolynomial()
    }

    fun removePoint(click: Offset) {
        if (points.isEmpty()) return
        val toRemove = points.minByOrNull {
            val sx = Converter.xCrt2Scr(it.first, plain)
            val sy = Converter.yCrt2Scr(it.second, plain)
            (click - Offset(sx, sy)).getDistance()
        } ?: return

        val dist = (click - Offset(
            Converter.xCrt2Scr(toRemove.first, plain),
            Converter.yCrt2Scr(toRemove.second, plain)
        )).getDistance()

        if (dist < 10f) {
            points = points - toRemove
            rebuildPolynomial()
        }
    }

    /* private fun newtonFunction(): ((Double) -> Double)? {
        if (points.size < 2) return null
        val coeffs = newton.coeffs
        return { x ->
            var sum = 0.0
            for ((power, coef) in coeffs) {
                sum += coef * x.pow(power)
            }
            sum
        }
    }
    // Подстановку обработать в Newton

    private fun derivativeFunction(): ((Double) -> Double)? {
        val f = newtonFunction() ?: return null
        val h = 1e-5
        return { x -> (f(x + h) - f(x - h)) / (2 * h) }
    }
    // Убрать из ViewModela, перенести в отдельный абстрактный класс Function
    // Абстрактный метод invoke, унаследовать полином от Function */

    fun updateBounds() {
        if (xMin >= xMax) xMax = xMin + 0.1
        if (yMin >= yMax) yMax = yMin + 0.1
        plain.xMin = xMin
        plain.xMax = xMax
        plain.yMin = yMin
        plain.yMax = yMax
    }

    fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = scope.size.width
        plain.height = scope.size.height
        updateBounds()

        cartesianPainter.draw(scope, measurer)

        if (showPolynomial && points.size >= 2) {
            RobustFunctionPainter(
                scope.size,
                plain,
                newton,
                polyColor,
                2f
            ).draw(scope, measurer)
        }

        if (showDerivative && points.size >= 2) {
            val derivativeFunction = newton.derivative()
            RobustFunctionPainter(
                scope.size,
                plain,
                derivativeFunction,
                derivativeColor,
                2f
            ).draw(scope, measurer)
        }

        if (showPoints) {
            for ((x, y) in points) {
                val sx = Converter.xCrt2Scr(x, plain)
                val sy = Converter.yCrt2Scr(y, plain)
                if (sx in 0f..scope.size.width && sy in 0f..scope.size.height) {
                    scope.drawCircle(pointsColor, 5f, Offset(sx, sy))
                }
            }
        }
    }
}

/* package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.geometry.Size
import ru.gr05307.painting.CartesianPainter
import ru.gr05307.painting.RobustFunctionPainter
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import ru.gr05307.model.math.polynomial.Newton
import kotlin.math.abs
import kotlin.math.pow

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)

    var xMin by mutableStateOf(plain.xMin)
    var xMax by mutableStateOf(plain.xMax)
    var yMin by mutableStateOf(plain.yMin)
    var yMax by mutableStateOf(plain.yMax)

    var showPoints by mutableStateOf(true)
    var showPolynomial by mutableStateOf(true)
    var showDerivative by mutableStateOf(false)

    var polyColor by mutableStateOf(Color.Red)
    var pointsColor by mutableStateOf(Color.Green)
    var derivativeColor by mutableStateOf(Color.Blue)

    var points by mutableStateOf(listOf<Pair<Double, Double>>())
    private val newton = Newton()

    private val cartesianPainter = CartesianPainter(
        size = Size(0f, 0f),
        plain = plain
    )

    private fun rebuildPolynomial() {
        try {
            val map = points.associate { it.first to it.second }.toSortedMap()
            val updated = Newton(map)
            newton.copyFrom(updated)
        } catch (e: Exception) {
            println("Ошибка при пересчёте полинома: ${e.message}")
        }
    }

    fun addPoint(click: Offset) {
        val x = Converter.xScr2Crt(click.x, plain)
        val y = Converter.yScr2Crt(click.y, plain)
        if (points.any { abs(it.first - x) < 0.1 }) return
        points = (points + (x to y)).sortedBy { it.first }
        rebuildPolynomial()
    }

    fun removePoint(click: Offset) {
        if (points.isEmpty()) return
        val toRemove = points.minByOrNull {
            val sx = Converter.xCrt2Scr(it.first, plain)
            val sy = Converter.yCrt2Scr(it.second, plain)
            (click - Offset(sx, sy)).getDistance()
        } ?: return

        val dist = (click - Offset(
            Converter.xCrt2Scr(toRemove.first, plain),
            Converter.yCrt2Scr(toRemove.second, plain)
        )).getDistance()

        if (dist < 10f) {
            points = points - toRemove
            rebuildPolynomial()
        }
    }

    private fun newtonFunction(): ((Double) -> Double)? {
        if (points.size < 2) return null
        val coeffs = newton.coeffs
        return { x ->
            var sum = 0.0
            for ((power, coef) in coeffs) {
                sum += coef * x.pow(power)
            }
            sum
        }
    }
    // Подстановку обработать в Newton

    //private fun derivativeFunction(): ((Double) -> Double)? {
    //    val f = newtonFunction() ?: return null
    //    val h = 1e-5
    //    return { x -> (f(x + h) - f(x - h)) / (2 * h) }
    //}
    // Убрать из ViewModela, перенести в отдельный абстрактный класс Function
    // Абстрактный метод invoke, унаследовать полином от Function

    fun updateBounds() {
        if (xMin >= xMax) xMax = xMin + 0.1
        if (yMin >= yMax) yMax = yMin + 0.1
        plain.xMin = xMin
        plain.xMax = xMax
        plain.yMin = yMin
        plain.yMax = yMax
    }

    fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = scope.size.width
        plain.height = scope.size.height
        updateBounds()

        cartesianPainter.draw(scope, measurer)

        if (showPolynomial) {
            newtonFunction()?.let { f ->
                RobustFunctionPainter(
                    scope.size,
                    plain,
                    f,
                    polyColor,
                    2f
                ).draw(scope, measurer)
            }
        }

        if (showDerivative) {
            derivativeFunction()?.let { df ->
                RobustFunctionPainter(
                    scope.size,
                    plain,
                    df,
                    derivativeColor,
                    2f
                ).draw(scope, measurer)
            }
        }

        if (showPoints) {
            for ((x, y) in points) {
                val sx = Converter.xCrt2Scr(x, plain)
                val sy = Converter.yCrt2Scr(y, plain)
                if (sx in 0f..scope.size.width && sy in 0f..scope.size.height) {
                    scope.drawCircle(pointsColor, 5f, Offset(sx, sy))
                }
            }
        }
    }
} */

/* package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.geometry.Size
import ru.gr05307.painting.CartesianPainter
import ru.gr05307.painting.RobustFunctionPainter
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import ru.gr05307.model.math.polynomial.Newton
import kotlin.math.abs
import kotlin.math.pow

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)

    var xMin by mutableStateOf(plain.xMin)
    var xMax by mutableStateOf(plain.xMax)
    var yMin by mutableStateOf(plain.yMin)
    var yMax by mutableStateOf(plain.yMax)

    var showPoints by mutableStateOf(true)
    var showPolynomial by mutableStateOf(true)
    var showDerivative by mutableStateOf(false)

    var polyColor by mutableStateOf(Color.Red)
    var pointsColor by mutableStateOf(Color.Green)
    var derivativeColor by mutableStateOf(Color.Blue)

    var points by mutableStateOf(listOf<Pair<Double, Double>>())
    private val newton = Newton()

    private val cartesianPainter = CartesianPainter(
        size = Size(0f, 0f),
        plain = plain
    )

    private fun rebuildPolynomial() {
        try {
            val map = points.associate { it.first to it.second }.toSortedMap()
            val updated = Newton(map)
            newton.copyFrom(updated)
        } catch (e: Exception) {
            println("Ошибка при пересчёте полинома: ${e.message}")
        }
    }

    fun addPoint(click: Offset) {
        val x = Converter.xScr2Crt(click.x, plain)
        val y = Converter.yScr2Crt(click.y, plain)
        if (points.any { abs(it.first - x) < 0.1 }) return
        points = (points + (x to y)).sortedBy { it.first }
        rebuildPolynomial()
    }

    fun removePoint(click: Offset) {
        if (points.isEmpty()) return
        val toRemove = points.minByOrNull {
            val sx = Converter.xCrt2Scr(it.first, plain)
            val sy = Converter.yCrt2Scr(it.second, plain)
            (click - Offset(sx, sy)).getDistance()
        } ?: return

        val dist = (click - Offset(
            Converter.xCrt2Scr(toRemove.first, plain),
            Converter.yCrt2Scr(toRemove.second, plain)
        )).getDistance()

        if (dist < 10f) {
            points = points - toRemove
            rebuildPolynomial()
        }
    }

    private fun newtonFunction(): ((Double) -> Double)? {
        if (points.size < 2) return null
        val coeffs = newton.coeffs
        return { x ->
            var sum = 0.0
            for ((power, coef) in coeffs) {
                sum += coef * x.pow(power)
            }
            sum
        }
    }
    // Подстановку обработать в Newton

    private fun derivativeFunction(): ((Double) -> Double)? {
        val f = newtonFunction() ?: return null
        val h = 1e-5
        return { x -> (f(x + h) - f(x - h)) / (2 * h) }
    }
    // Убрать из ViewModela, перенести в отдельный абстрактный класс Function
    // Абстрактный метод invoke, унаследовать полином от Function

    fun updateBounds() {
        if (xMin >= xMax) xMax = xMin + 0.1
        if (yMin >= yMax) yMax = yMin + 0.1
        plain.xMin = xMin
        plain.xMax = xMax
        plain.yMin = yMin
        plain.yMax = yMax
    }

    fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = scope.size.width
        plain.height = scope.size.height
        updateBounds()

        cartesianPainter.draw(scope, measurer)

        if (showPolynomial) {
            newtonFunction()?.let { f ->
                RobustFunctionPainter(
                    scope.size,
                    plain,
                    f,
                    polyColor,
                    2f
                ).draw(scope, measurer)
            }
        }

        if (showDerivative) {
            derivativeFunction()?.let { df ->
                RobustFunctionPainter(
                    scope.size,
                    plain,
                    df,
                    derivativeColor,
                    2f
                ).draw(scope, measurer)
            }
        }

        if (showPoints) {
            for ((x, y) in points) {
                val sx = Converter.xCrt2Scr(x, plain)
                val sy = Converter.yCrt2Scr(y, plain)
                if (sx in 0f..scope.size.width && sy in 0f..scope.size.height) {
                    scope.drawCircle(pointsColor, 5f, Offset(sx, sy))
                }
            }
        }
    }
} */


/* package ru.gr05307.viewmodels

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
import ru.gr05307.model.math.polynomial.Newton
import kotlin.math.abs
import kotlin.math.pow

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)

    var xMin by mutableStateOf(plain.xMin)
    var xMax by mutableStateOf(plain.xMax)
    var yMin by mutableStateOf(plain.yMin)
    var yMax by mutableStateOf(plain.yMax)

    var showPoints by mutableStateOf(true)
    var showPolynomial by mutableStateOf(true)
    var showDerivative by mutableStateOf(false)

    var polyColor by mutableStateOf(Color.Red)
    var pointsColor by mutableStateOf(Color.Green)
    var derivativeColor by mutableStateOf(Color.Blue)

    var points by mutableStateOf(listOf<Pair<Double, Double>>())
    private val newton = Newton()

    private val cartesianPainter = CartesianPainter(
        size = Size(0f, 0f),
        plain = plain
    )

    private fun rebuildPolynomial() {
        try {
            val map = points.associate { it.first to it.second }.toSortedMap()
            val updated = Newton(map)
            newton.copyFrom(updated)
        } catch (e: Exception) {
            println("Ошибка при пересчёте полинома: ${e.message}")
        }
    }

    fun addPoint(click: Offset) {
        val x = Converter.xScr2Crt(click.x, plain)
        val y = Converter.yScr2Crt(click.y, plain)
        if (points.any { abs(it.first - x) < 0.1 }) return
        points = (points + (x to y)).sortedBy { it.first }
        rebuildPolynomial()
    }

    fun removePoint(click: Offset) {
        if (points.isEmpty()) return
        val toRemove = points.minByOrNull {
            val sx = Converter.xCrt2Scr(it.first, plain)
            val sy = Converter.yCrt2Scr(it.second, plain)
            (click - Offset(sx, sy)).getDistance()
        } ?: return

        val dist = (click - Offset(
            Converter.xCrt2Scr(toRemove.first, plain),
            Converter.yCrt2Scr(toRemove.second, plain)
        )).getDistance()

        if (dist < 10f) {
            points = points - toRemove
            rebuildPolynomial()
        }
    }

    private fun newtonFunction(): ((Double) -> Double)? {
        if (points.size < 2) return null
        val coeffs = newton.coeffs
        return { x ->
            var sum = 0.0
            for ((power, coef) in coeffs) {
                sum += coef * x.pow(power)
            }
            sum
        }
    }

    private fun derivativeFunction(): ((Double) -> Double)? {
        val f = newtonFunction() ?: return null
        val h = 1e-5
        return { x -> (f(x + h) - f(x - h)) / (2 * h) }
    }

    fun updateBounds() {
        if (xMin >= xMax) xMax = xMin + 0.1
        if (yMin >= yMax) yMax = yMin + 0.1
        plain.xMin = xMin
        plain.xMax = xMax
        plain.yMin = yMin
        plain.yMax = yMax
    }

    fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = scope.size.width
        plain.height = scope.size.height
        updateBounds()

        cartesianPainter.draw(scope, measurer)

        if (showPolynomial) {
            newtonFunction()?.let { f ->
                FunctionPainter(scope.size, plain, f, polyColor, 2f).draw(scope, measurer)
            }
        }

        if (showDerivative) {
            derivativeFunction()?.let { df ->
                FunctionPainter(scope.size, plain, df, derivativeColor, 2f).draw(scope, measurer)
            }
        }

        if (showPoints) {
            for ((x, y) in points) {
                val sx = Converter.xCrt2Scr(x, plain)
                val sy = Converter.yCrt2Scr(y, plain)
                scope.drawCircle(pointsColor, 5f, Offset(sx, sy))
            }
        }
    }
} */
