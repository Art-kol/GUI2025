package ru.gr05307.ui.convertation

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ConverterTest {

    private val plain = Plain(
        xMin = -5.0,
        xMax = 5.0,
        yMin = -5.0,
        yMax = 5.0,
        width = 500f,
        height = 500f
    )

    private val plain2 = Plain(
        xMin = -12.0,
        xMax = 6.0,
        yMin = -6.0,
        yMax = 12.0,
        width = 1800f,
        height = 1800f
    )


    @Test
    fun xCrt2Scr() {
        assertEquals(250f, Converter.xCrt2Scr(0.0, plain), 0.001f)
        assertEquals(100f, Converter.xCrt2Scr(-3.0, plain), 0.001f)
        assertEquals(350f, Converter.xCrt2Scr(2.0, plain), 0.001f)

        assertEquals(1200f, Converter.xCrt2Scr(0.0, plain2), 0.001f)
        assertEquals(500f, Converter.xCrt2Scr(-7.0, plain2), 0.001f)
        assertEquals(1300f, Converter.xCrt2Scr(1.0, plain2), 0.001f)
    }

    @Test
    fun xScr2Crt() {
        assertEquals(0.0 , Converter.xScr2Crt(250f, plain), 1e-6)
        assertEquals(-3.0 , Converter.xScr2Crt(100f, plain), 1e-6)
        assertEquals(2.0 , Converter.xScr2Crt(350f, plain), 1e-6)

        assertEquals(0.0 , Converter.xScr2Crt(1200f, plain2), 1e-6)
        assertEquals(-7.0 , Converter.xScr2Crt(500f, plain2), 1e-6)
        assertEquals(1.0 , Converter.xScr2Crt(1300f, plain2), 1e-6)
    }

    @Test
    fun yCrt2Scr() {
        assertEquals(250f, Converter.yCrt2Scr(0.0, plain), 0.001f)
        assertEquals(400f, Converter.yCrt2Scr(-3.0, plain), 0.001f)
        assertEquals(150f, Converter.yCrt2Scr(2.0, plain), 0.001f)

        assertEquals(1200f, Converter.yCrt2Scr(0.0, plain2), 0.001f)
        assertEquals(500f, Converter.yCrt2Scr(7.0, plain2), 0.001f)
        assertEquals(1300f, Converter.yCrt2Scr(-1.0, plain2), 0.001f)
    }

    @Test
    fun `test yScr2Crt with manual calculations`() {
        assertEquals(0.0 , Converter.yScr2Crt(250f, plain), 1e-6)
        assertEquals(3.0 , Converter.yScr2Crt(100f, plain), 1e-6)
        assertEquals(-2.0 , Converter.yScr2Crt(350f, plain), 1e-6)

        assertEquals(0.0 , Converter.yScr2Crt(1200f, plain2), 1e-6)
        assertEquals(7.0 , Converter.yScr2Crt(500f, plain2), 1e-6)
        assertEquals(-1.0 , Converter.yScr2Crt(1300f, plain2), 1e-6)
    }
}