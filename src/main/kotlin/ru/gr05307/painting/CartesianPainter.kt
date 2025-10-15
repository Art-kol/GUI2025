package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import ru.gr05307.ui.convertation.Plain

class CartesianPainter(val plain: Plain) {
    fun draw(scope: DrawScope,){
        scope.drawLine(
            Color.Black,
            Offset(scope.size.width / 2f, 0f),
            Offset(scope.size.width / 2f, scope.size.height)
        )
        scope.drawLine(
            Color.Black,
            Offset(0f, scope.size.height / 2f),
            Offset(scope.size.width, scope.size.height / 2f)
        )

    }
}