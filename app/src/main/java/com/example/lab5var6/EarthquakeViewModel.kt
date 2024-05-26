package com.example.lab5var6

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EarthquakeViewModel(application: Application) : AndroidViewModel(application) {
    private val db = EarthquakeDb(application)
    private val _earthquakes = MutableStateFlow<List<Earthquake>>(emptyList())
    val earthquakes: StateFlow<List<Earthquake>> = _earthquakes

    private val _history = MutableStateFlow<List<Earthquake>>(emptyList())
    val history: StateFlow<List<Earthquake>> get() = _history
    init {
        viewModelScope.launch {
            fromDatabase()
        }
    }
    private suspend fun fromDatabase() {
        withContext(Dispatchers.IO) {
            _history.value = db.getAllEarthquakes()

        }
    }
    fun run(startTime: String, endTime: String) {
        viewModelScope.launch {
                val response =
                    RetrofitInstance.api.getEarthquakes(startTime = startTime, endTime = endTime)
                val earthquakePropertiesList = response.features.map { it }
                _earthquakes.value = earthquakePropertiesList
                saveEarthquakesToDatabase(earthquakePropertiesList)
        }
    }

    private suspend fun saveEarthquakesToDatabase(earthquakePropertiesList: List<Earthquake>) {
        withContext(Dispatchers.IO) {
            for (earthquake in earthquakePropertiesList) {
                db.insertEarthquake(earthquake)
            }
            fromDatabase()
        }
    }
    fun clearAllEarthquakes() {
        viewModelScope.launch(Dispatchers.IO) {
            db.clearAllEarthquakes()
            _history.value = db.getAllEarthquakes()
        }
    }
}


data class EarthquakeResponse(
    val features: List<Earthquake>
)





