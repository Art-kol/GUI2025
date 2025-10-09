package ru.gr05307.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.IconToggleButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import kotlin.math.round

@Composable
fun NumericUpDown(
    modifier: Modifier = Modifier,
    value: Double? = null,
    onValueChange: (Double?) -> Unit = {},
){
    var textValue by remember { mutableStateOf(value?.toString() ?: "") }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterEnd,
    ){
        OutlinedTextField(textValue,
            onValueChange = {
                if (it.isEmpty() || it == "-" || it.toDoubleOrNull() != null) {
                    textValue = it
                }
                onValueChange(textValue.toDoubleOrNull())
            },
            modifier = modifier,
            singleLine = true,
        )
        Column(modifier = Modifier.padding(end = 4.dp)) {
            Icon(
                Icons.Default.KeyboardArrowUp,
                "Up",
                modifier = Modifier.clickable(true){
                    textValue = textValue.toDoubleOrNull()?.let{
                        round((it + 0.1) * 10.0) / 10.0
                    }?.toString() ?: textValue
                    onValueChange(textValue.toDoubleOrNull())
                }
            )
            Icon(
                Icons.Default.KeyboardArrowDown,
                "Down",
                modifier = Modifier.clickable(true){
                    textValue = textValue.toDoubleOrNull()?.let{
                        round((it - 0.1) * 10.0) / 10.0
                    }?.toString() ?: textValue
                    onValueChange(textValue.toDoubleOrNull())
                }
            )
        }
    }
}
