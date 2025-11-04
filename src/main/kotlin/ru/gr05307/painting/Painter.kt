package ru.gr05307.painting

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer

interface Painter {
    val size: Size

    fun draw(scope: DrawScope, measurer: TextMeasurer)
}