package ru.gr05307.model.math

abstract class Function {
    abstract operator fun invoke(x: Double): Double

    open fun derivative(h: Double = 1e-5): Function {
        return object : Function() {
            override fun invoke(x: Double): Double {
                return (this@Function(x + h) - this@Function(x - h)) / (2 * h)
            }
        }
    }
}