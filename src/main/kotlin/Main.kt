import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ru.gr05307.ui.NumericUpDown
import ru.gr05307.viewmodels.MainViewModel

@Composable
@Preview
fun App() {
    val viewModel = MainViewModel()
    MaterialTheme {
        Content(viewModel, Modifier.fillMaxWidth(),)
    }
}

@Composable
fun Content(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(10.dp)
        ){
            viewModel.draw(this)
        }
        Box(modifier = Modifier.fillMaxWidth().padding(10.dp).border(width = 1.dp, color = Color.Black)
            ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                Edges(
                    "x",
                    modifier=Modifier.fillMaxWidth(),
                    viewModel.xMin,
                    viewModel.xMax,
                    onMinValueChange = { viewModel.xMin = it },
                    onMaxValueChange = { viewModel.xMax = it },
                )
                Edges("y",
                    modifier=Modifier.fillMaxWidth(),
                     viewModel.yMin,
                    viewModel.yMax,
                    onMinValueChange = { viewModel.yMin = it },
                    onMaxValueChange = { viewModel.yMax = it },
                )
            }
        }
    }
}

@Composable
fun Edges(
    edgeName: String,
    modifier: Modifier = Modifier,
    minValue: Double? = -5.0,
    maxValue: Double? = 5.0,
    onMinValueChange: (Double?) -> Unit = {},
    onMaxValueChange: (Double?) -> Unit = {},
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("${edgeName}Min:")
        NumericUpDown (
            Modifier.weight(1f),
            minValue,
            onMinValueChange,
        )
        Text("${edgeName}Max:")
        NumericUpDown (
            Modifier.weight(1f),
            maxValue,
            onMaxValueChange,
        )
    }
}

fun main(): Unit = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Интерполирование функций"
    ) {
        App()
    }
}
