package ru.gr05307.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.round

@Composable
fun NumericUpDown(
    value: Double? = null,
    onValueChange: (Double?) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd,
    ) {
        OutlinedTextField(
            value = textValue,
            onValueChange = {
                if (it.isEmpty() || it == "-" || it.toDoubleOrNull() != null) {
                    textValue = it
                }
                onValueChange(textValue.toDoubleOrNull())
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        Column(modifier = Modifier.padding(end = 4.dp)) {
            androidx.compose.material.Icon(
                Icons.Default.KeyboardArrowUp,
                "Up",
                modifier = Modifier.clickable(true) {
                    textValue = textValue.toDoubleOrNull()?.let {
                        round((it + 0.1) * 10.0) / 10.0
                    }?.toString() ?: textValue
                    onValueChange(textValue.toDoubleOrNull())
                }
            )
            androidx.compose.material.Icon(
                Icons.Default.KeyboardArrowDown,
                "Down",
                modifier = Modifier.clickable(true) {
                    textValue = textValue.toDoubleOrNull()?.let {
                        round((it - 0.1) * 10.0) / 10.0
                    }?.toString() ?: textValue
                    onValueChange(textValue.toDoubleOrNull())
                }
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
    onMaxValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("$axis min:")
        NumericUpDown(minValue, onMinValueChange, Modifier.width(80.dp))
        Text("$axis max:")
        NumericUpDown(maxValue, onMaxValueChange, Modifier.width(80.dp))
    }
}

@Composable
fun ColorSelector(
    selected: Color,
    onSelect: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Magenta, Color.Black, Color.Cyan)
    Row(modifier = modifier) {
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
fun ControlPanel(
    xMin: Double?,
    xMax: Double?,
    yMin: Double?,
    yMax: Double?,
    showPoints: Boolean,
    showPolynomial: Boolean,
    showDerivative: Boolean,
    polyColor: Color,
    pointsColor: Color,
    derivativeColor: Color,
    onXMinChange: (Double?) -> Unit,
    onXMaxChange: (Double?) -> Unit,
    onYMinChange: (Double?) -> Unit,
    onYMaxChange: (Double?) -> Unit,
    onShowPointsChange: (Boolean) -> Unit,
    onShowPolynomialChange: (Boolean) -> Unit,
    onShowDerivativeChange: (Boolean) -> Unit,
    onPolyColorChange: (Color) -> Unit,
    onPointsColorChange: (Color) -> Unit,
    onDerivativeColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Edges(
                axis = "x",
                minValue = xMin,
                maxValue = xMax,
                onMinValueChange = onXMinChange,
                onMaxValueChange = onXMaxChange,
                modifier = Modifier
            )
            Edges(
                axis = "y",
                minValue = yMin,
                maxValue = yMax,
                onMinValueChange = onYMinChange,
                onMaxValueChange = onYMaxChange,
                modifier = Modifier
            )
        }

        Divider(color = Color.Gray)

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            androidx.compose.material.Checkbox(showPoints, onShowPointsChange)
            Text("Точки")

            androidx.compose.material.Checkbox(showPolynomial, onShowPolynomialChange)
            Text("Полином")

            androidx.compose.material.Checkbox(showDerivative, onShowDerivativeChange)
            Text("Производная")
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Цвет полинома:")
            ColorSelector(
                selected = polyColor,
                onSelect = onPolyColorChange
            )

            Text("Цвет точек:")
            ColorSelector(
                selected = pointsColor,
                onSelect = onPointsColorChange
            )

            Text("Цвет производной:")
            ColorSelector(
                selected = derivativeColor,
                onSelect = onDerivativeColorChange
            )
        }
    }
}
