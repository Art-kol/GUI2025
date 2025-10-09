package ru.gr05307.ui.convertation

object Converter {

    fun xCrt2Scr(x: Double, p: Plain): Float{
        return x.toFloat() * p.xDen.toFloat()
    }
}