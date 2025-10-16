package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.gr05307.painting.CartesianPainter
import ru.gr05307.ui.convertation.Plain

class MainViewModel {
    private val plain = Plain(-5.0, 5.0, -5.0, 5.0, 0f, 0f)

    var xMin: Double? by mutableStateOf(plain.xMin)
    var xMax: Double? by mutableStateOf(plain.xMax)
    var yMin: Double? by mutableStateOf(plain.yMin)
    var yMax: Double? by mutableStateOf(plain.yMax)

    private val cartesianPainter = CartesianPainter(plain)

    fun draw(scope: DrawScope){
        plain.width = scope.size.width
        plain.height = scope.size.height
        cartesianPainter.draw(scope)
    }
}