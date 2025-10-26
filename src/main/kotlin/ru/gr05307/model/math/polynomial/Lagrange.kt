package ru.gr05307.model.math.polynomial

import ru.gr05307.model.math.neq


class Lagrange (private val points: Map<Double, Double>) : Polynomial() {
    init{
        coeffs = points.keys.fold(Polynomial()){acc, v ->
            acc + points[v]!! * fundamental(v)
        }.coeffs
    }

    private fun fundamental(x: Double) = points.keys
        .fold(Polynomial(1.0)) {acc, v ->
            if (x neq v)
                acc * Polynomial(-v, 1.0)/(x-v)
            else acc
        }


}