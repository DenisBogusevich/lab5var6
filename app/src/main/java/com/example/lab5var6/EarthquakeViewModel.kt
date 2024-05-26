package com.example.lab5var6

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EarthquakeViewModel(application: Application) : AndroidViewModel(application) {
    private val databaseHelper = EarthquakeDatabaseHelper(application)

    private val _earthquakes = MutableStateFlow<List<EarthquakeProperties>>(emptyList())
    val earthquakes: StateFlow<List<EarthquakeProperties>> = _earthquakes

    private val _history = MutableStateFlow<List<EarthquakeProperties>>(emptyList())
    val history: StateFlow<List<EarthquakeProperties>> get() = _history

    init {
        viewModelScope.launch {
            // Fetch earthquakes from the database initially
            fetchEarthquakesFromDatabase()


        }
    }

    private suspend fun fetchEarthquakesFromDatabase() {
        withContext(Dispatchers.IO) {
            val earthquakesFromDb = databaseHelper.getAllEarthquakes()
            // _earthquakes.value = earthquakesFromDb
            _history.value = databaseHelper.getAllEarthquakes()


        }
    }

    fun fetchAndSaveEarthquakes(startTime: String, endTime: String) {
        viewModelScope.launch {
            try {
                val response =
                    RetrofitInstance.api.getEarthquakes(startTime = startTime, endTime = endTime)
                // Extract earthquake properties from the response
                val earthquakePropertiesList = response.features.map { it.properties }

                // Update UI with fetched earthquakes
                _earthquakes.value = earthquakePropertiesList
                fetchEarthquakesFromDatabase()
                // Save fetched earthquakes to the database
                saveEarthquakesToDatabase(earthquakePropertiesList)
            } catch (e: Exception) {
                // Handle exception
            }
        }
    }

    private suspend fun saveEarthquakesToDatabase(earthquakePropertiesList: List<EarthquakeProperties>) {
        withContext(Dispatchers.IO) {
            // Clear existing earthquakes in the database and insert new ones

            for (earthquake in earthquakePropertiesList) {
                databaseHelper.insertEarthquake(earthquake)
            }
        }
    }

    fun clearAllEarthquakes() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseHelper.clearAllEarthquakes()
            _history.value = databaseHelper.getAllEarthquakes()
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