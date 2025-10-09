package ru.gr05307.ui.convertation

data class Plain(
    var xMin: Double,
    var xMax: Double,
    var yMin: Double,
    var yMax: Double,
    var width: Float,
    var height: Float,
){
    val xDen = width / (xMax - xMin)
}
