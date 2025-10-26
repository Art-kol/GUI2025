package ru.gr05307.model.math.polynomial

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.gr05307.model.math.polynomial.Lagrange
import ru.gr05307.model.math.polynomial.Newton
import ru.gr05307.model.math.polynomial.Polynomial
import ru.gr05307.model.math.polynomial.times
import kotlin.math.sin
import kotlin.math.cos
import kotlin.math.exp


class PolynomialTest {
    @Test
    fun testPerformanceComparison() {
        /*val points = mapOf(
            -8.0 to 250199979298361.0,
            -7.0 to 29078814248401.0,
            -6.0 to 2418094206391.0,
            -5.0 to 127156575521.0,
            -4.0 to 3435973837.0,
            -3.0 to 32285041.0,
            -2.0 to 43691.0,
            -1.0 to 1.0,
            0.0 to 1.0,
            1.0 to 17.0,
            2.0 to 131071.0,
            3.0 to 64570081.0,
            4.0 to 5726623061.0,
            5.0 to 190734863281.0,
            6.0 to 3385331888947.0,
            7.0 to 38771752331201.0,
            8.0 to 321685687669321.0
        )*/
        val points = generatePoints(500)
        val nStart = System.currentTimeMillis()
        val newton = Newton(points)
        val nTime = System.currentTimeMillis() - nStart

        val lStart = System.currentTimeMillis()
        val lagarage = Lagrange(points)
        val lTime = System.currentTimeMillis() - lStart

        println("Ньютоном: " + nTime.toString() + "мс. Лагранжем: " + lTime.toString() + "мс.")

        val n2Start = System.currentTimeMillis()
        newton.addDot(-9.0, 1667718169966657.0)                                                           // !!!
        val n2Time = System.currentTimeMillis() - n2Start
        println("Ньютоном добавить точку за: " + n2Time.toString() + "мс.")

        val l2Start = System.currentTimeMillis()
        val lagrange2 = Lagrange( points + (-9.0 to 1667718169966657.0))
        val l2Time = System.currentTimeMillis() - l2Start
        println("Лагранжем перестроить за: " + l2Time + "мс.")
    }

    fun generatePoints(count: Int): Map<Double, Double> {
        return (0 until count).associate { i ->
            val x = i.toDouble() // Равномерно распределенные x
            val y = Math.sin(x) + Math.cos(2*x)
            x to y
        }
    }

    @Test
    fun plus() {
        assertEquals(
            Polynomial(-1, 1, 1, 1),
            Polynomial(-3, 0, 0, 1) + Polynomial(2, 1, 1)
        )
    }

    @Test
    fun minus() {
        assertEquals(
            Polynomial(-5, -1, -1, 1),
            Polynomial(-3, 0, 0, 1) - Polynomial(2, 1, 1)
        )
    }

    @Test
    fun timesDouble() {
        assertEquals(
            Polynomial(6, 3, 3),
            Polynomial(2, 1, 1) * 3.0
        )
        assertEquals(
            Polynomial(),
            Polynomial(2, 1, 1) * 0.0
        )
        assertEquals(
            Polynomial(6, 3, 3),
            3.0 * Polynomial(2, 1, 1)
        )
    }
    @Test
    fun times() {
        assertEquals(Polynomial(-6, -3, -3, 2, 1, 1), Polynomial(2, 1, 1) * Polynomial(-3, 0, 0, 1))
    }
    @Test
    fun divDouble() {
        assertEquals(
            Polynomial(2, 1, 1),
            Polynomial(6, 3, 3) / 3.0
        )
        assertEquals(
            Polynomial(
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                Double.POSITIVE_INFINITY
            ),
            Polynomial(2, 1, 1) / 0.0
        )
    }

    @Test
    fun invoke(){
        assertEquals(
            16.0,
            Polynomial(1, 2, 1)(3.0),
            1e-10
        )
    }

    @Test
    fun getCoeffs() {
        assertEquals(mapOf(0 to 0.0), Polynomial().coeffs)

        assertEquals(mapOf(0 to 1.0), Polynomial(1).coeffs)
        assertEquals(mapOf(2 to 1.0), Polynomial(0, 0, 1).coeffs)

        assertEquals(mapOf(0 to 0.0), Polynomial(mapOf(-1 to 1.0)).coeffs)

        assertEquals(mapOf(0 to 1.0, 2 to -2.0, 3 to 3.0), Polynomial(listOf(1.0, 0.0, -2.0, 3.0)).coeffs)

    }

    @Test
    fun toStringTest() {
        assertEquals("0.0", Polynomial().toString())

        assertEquals("x", Polynomial(0, 1).toString())
        assertEquals("-x", Polynomial(0, -1).toString())

        assertEquals("1.0", Polynomial(1).toString())
        assertEquals("-1.0", Polynomial(-1).toString())

        assertEquals("2.5", Polynomial(2.5).toString())
        assertEquals("-2.5", Polynomial(-2.5).toString())

        assertEquals("x^10", Polynomial(mapOf(10 to 1.0)).toString())
        assertEquals("-x^10", Polynomial(mapOf(10 to -1.0)).toString())

        assertEquals("3.0x^3-2.0x^2+1.0", Polynomial(1, 0, -2, 3).toString())

        assertEquals("x", Polynomial(0, 1, 0).toString())
    }

}