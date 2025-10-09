package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.gr05307.ui.CartesianPainter

class MainViewModel {
    var xMin: Double? by mutableStateOf(-5.0)
    var xMax: Double? by mutableStateOf(5.0)
    var yMin: Double? by mutableStateOf(-5.0)
    var yMax: Double? by mutableStateOf(5.0)

    private val cartesianPainter = CartesianPainter()

    fun draw(scope: DrawScope){
        cartesianPainter.draw(scope)
    }
}