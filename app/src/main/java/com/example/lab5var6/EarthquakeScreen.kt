package com.example.lab5var6

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun EarthquakeApp(navController: NavController,earthquakeViewModel: EarthquakeViewModel) {

    val earthquakes by earthquakeViewModel.earthquakes.collectAsState()

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DatePicker(label = "Start Date", onDateSelected = { startDate = it })
        Spacer(modifier = Modifier.height(8.dp))
        DatePicker(label = "End Date", onDateSelected = { endDate = it })
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(onClick = { earthquakeViewModel.fetchAndSaveEarthquakes(startDate, endDate) }) {
                Text("Search")
            }
            Button(onClick = { navController.navigate("history") }) {
                Text("History")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
        EarthquakeList(earthquakes)
    }
}

@Composable
fun DatePicker(label: String, onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val date = "$year-${month + 1}-$dayOfMonth"
            onDateSelected(date)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    OutlinedButton(onClick = { datePickerDialog.show() }) {
        Text(label)
    }
}

@Composable
fun EarthquakeList(earthquakes: List<EarthquakeProperties>) {
    LazyColumn {
        items(earthquakes) { earthquake ->
            EarthquakeItem(earthquake)
        }
    }
}

@Composable
fun EarthquakeItem(earthquake: EarthquakeProperties) {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = Date(earthquake.time)
    val formattedDate = sdf.format(date)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)

    ) {

        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Place: ${earthquake.place}")
            Text(text = "Date: $formattedDate")
        }
    }
}
