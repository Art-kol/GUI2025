package ru.gr05307.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class MainViewModel {
    var xMin: Double? by mutableStateOf(-5.0)
    var xMax: Double? by mutableStateOf(-5.0)
    var yMin: Double? by mutableStateOf(-5.0)
    var yMax: Double? by mutableStateOf(-5.0)


}