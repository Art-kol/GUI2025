import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.gr05307.ui.NumericUpDown
import ru.gr05307.viewmodels.MainViewModel

@Composable
@Preview
fun App() {
    val viewModel = remember { MainViewModel() }

    MaterialTheme {
        Content(viewModel, Modifier.fillMaxSize())
    }
}

@Composable
fun Content(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val measurer = rememberTextMeasurer()

    Column(modifier = modifier.padding(10.dp)) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.dp, Color.Black)
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val offset = event.changes.first().position
                            when {
                                event.buttons.isPrimaryPressed -> viewModel.addPoint(offset)
                                event.buttons.isSecondaryPressed -> viewModel.removePoint(offset)
                            }
                        }
                    }
                }
        ) {
            viewModel.draw(this, measurer)
        }

        ControlPanel(viewModel)
    }
}

@Composable
fun ControlPanel(viewModel: MainViewModel) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Границы
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Edges("x", viewModel.xMin, viewModel.xMax,
                { viewModel.xMin = it ?: -5.0; viewModel.updateBounds() },
                { viewModel.xMax = it ?: 5.0; viewModel.updateBounds() }
            )
            Edges("y", viewModel.yMin, viewModel.yMax,
                { viewModel.yMin = it ?: -5.0; viewModel.updateBounds() },
                { viewModel.yMax = it ?: 5.0; viewModel.updateBounds() }
            )
        }

        Divider(color = Color.Gray)

        // Переключатели отображения
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(viewModel.showPoints, { viewModel.showPoints = it })
            Text("Точки")

            Checkbox(viewModel.showPolynomial, { viewModel.showPolynomial = it })
            Text("Полином")

            Checkbox(viewModel.showDerivative, { viewModel.showDerivative = it })
            Text("Производная")
        }

        // Цвета
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Цвет полинома:")
            ColorSelector(viewModel.polyColor) { viewModel.polyColor = it }

            Text("Цвет точек:")
            ColorSelector(viewModel.pointsColor) { viewModel.pointsColor = it }

            Text("Цвет производной:")
            ColorSelector(viewModel.derivativeColor) { viewModel.derivativeColor = it }
        }
    }
}

@Composable
fun ColorSelector(selected: Color, onSelect: (Color) -> Unit) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Black, Color.Cyan)
    Row {
        for (c in colors) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .padding(2.dp)
                    .background(c)
                    .border(2.dp, if (selected == c) Color.Black else Color.Gray)
                    .clickable { onSelect(c) }
            )
        }
    }
}

@Composable
fun Edges(
    axis: String,
    minValue: Double?,
    maxValue: Double?,
    onMinValueChange: (Double?) -> Unit,
    onMaxValueChange: (Double?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$axis min:")
        NumericUpDown(Modifier.width(80.dp), minValue, onMinValueChange)
        Text("$axis max:")
        NumericUpDown(Modifier.width(80.dp), maxValue, onMaxValueChange)
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Интерполяция функций"
    ) {
        App()
    }
}