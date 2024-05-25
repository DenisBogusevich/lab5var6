package com.example.lab5var6

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EarthquakeViewModel : ViewModel() {
    private val _earthquakes = MutableStateFlow<List<EarthquakeFeature>>(emptyList())
    val earthquakes: StateFlow<List<EarthquakeFeature>> = _earthquakes

    fun fetchEarthquakes(startTime: String, endTime: String) {
        viewModelScope.launch {
            println(startTime)
            println(endTime)
            try {
                val response = RetrofitInstance.api.getEarthquakes(startTime = startTime, endTime = endTime)
                println(response)
                _earthquakes.value = response.features
                println(_earthquakes.value)
            } catch (e: Exception) {
                // Handle exception
                println(e)
            }
        }
    }
}


data class EarthquakeResponse(
    val features: List<EarthquakeFeature>
)

data class EarthquakeFeature(
    val properties: EarthquakeProperties
)

data class EarthquakeProperties(
    val place: String,
    val time: Long
)