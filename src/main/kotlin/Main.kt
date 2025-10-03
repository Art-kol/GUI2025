import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    MaterialTheme {
        Content(Modifier.fillMaxWidth())
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxWidth().weight(1f)
            .padding(10.dp)) {

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
                    xMin,
                    xMax,
                    onMinValueChange = { xMin = it },
                    onMaxValueChange = { xMax = it },
                )
                Edges("y",
                    modifier=Modifier.fillMaxWidth(),
                    yMin,
                    yMax,
                    onMinValueChange = { yMin = it },
                    onMaxValueChange = { yMax = it },
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
    var minText by remember { mutableStateOf(minValue?.toString() ?: "") }
    var maxText by remember { mutableStateOf(maxValue?.toString() ?: "") }
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("${edgeName}Min:")
        OutlinedTextField(minText, onValueChange = {
            if (it.isEmpty() || it=="-" || it.toDoubleOrNull() != null) {
                minText = it
            }
            onMinValueChange(minText.toDoubleOrNull())
        }, modifier = Modifier.weight(1f))
        Text("${edgeName}Max:")
        OutlinedTextField(maxText, onValueChange = {
            if (it.isEmpty() || it=="-" || it.toDoubleOrNull() != null) {
                maxText = it
            }
            onMaxValueChange(maxText.toDoubleOrNull())
        }, modifier = Modifier.weight(1f))

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
