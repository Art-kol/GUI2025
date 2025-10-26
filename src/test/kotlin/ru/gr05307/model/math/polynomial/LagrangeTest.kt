package ru.gr05307.model.math.polynomial

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LagrangeTest {
    @Test
    fun createLagrange() {
        assertEquals(Polynomial(1), Lagrange(mapOf(0.0 to 1.0)))
        assertEquals(Polynomial(0, 0, 1), Lagrange(mapOf(-1.0 to 1.0, 0.0 to 0.0, 1.0 to 1.0)))
        assertEquals(Polynomial(1, 0, 0, 1), Lagrange(mapOf(-1.0 to 0.0, 0.0 to 1.0, 1.0 to 2.0, 2.0 to 9.0)))
    }
}