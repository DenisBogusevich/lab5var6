package com.example.lab5var6

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarthquakeApp(viewModel: EarthquakeViewModel) {

    val earthquakes by viewModel.earthquakes.collectAsState()

    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Earthquake Search") },
                Modifier.background(Color.Blue)
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DatePicker(label = "Start Date", onDateSelected = { start = it })
                Spacer(modifier = Modifier.width(8.dp))
                DatePicker(label = "End Date", onDateSelected = { end = it })
            }
            Button(
                onClick = { viewModel.fetchEarthquakes(start, end) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Search")
            }
            Spacer(modifier = Modifier.height(16.dp))
            EarthquakeList(earthquakes)
        }
    }
}

@Composable
fun DatePicker(label: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // State to hold the selected date
    var selectedDate by remember { mutableStateOf<String?>(null) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = "$year-${month + 1}-$dayOfMonth"
            selectedDate = date
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Select a date",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextButton(onClick = { datePickerDialog.show() }) {
            Text(selectedDate ?: label)
        }


    }
}


@Composable
fun EarthquakeList(earthquakes: List<EarthquakeFeature>) {
    LazyColumn {
        items(earthquakes) { earthquake ->
            EarthquakeItem(earthquake)
        }
    }
}

@Composable
fun EarthquakeItem(earthquake: EarthquakeFeature) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(earthquake.properties.time)
    val formattedDate = sdf.format(date)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {

        Column(modifier = Modifier
            .padding(8.dp)) {
            Text(text = "Place: ${earthquake.properties.place}")
            Text(text = "Date: $formattedDate")
        }
    }
}
