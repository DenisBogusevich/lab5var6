package com.example.lab5var6

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Assert

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class EarthquakeDatabaseTest {
    private lateinit var dbHelper: EarthquakeDb

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = EarthquakeDb(context)
    }

    @After
    fun tearDown() {
        dbHelper.clearAllEarthquakes()
    }

    @Test
    fun insertBook_success() {
        val earthquake =Earthquake(
            "test",10)



        dbHelper.insertEarthquake(earthquake)
        val earthquakes = dbHelper.getAllEarthquakes()



        assertEquals(earthquake.place, earthquakes[0].place)
        assertEquals(earthquake.time, earthquakes[0].time)


    }

    @Test(expected = Exception::class)
    fun insertBook_fail() {
        val earthquake =Earthquake(
            "test",10)

        dbHelper.insertEarthquake(earthquake)
        dbHelper.insertEarthquake(earthquake)

    }


    @Test
    fun clearAllBooks_success() {
        val earthquake =Earthquake(
            "test",10)

        val earthquake1 =Earthquake(
            "test2",15)

        dbHelper.insertEarthquake(earthquake)
        dbHelper.insertEarthquake(earthquake1)
        dbHelper.clearAllEarthquakes()
        val earthquakes = dbHelper.getAllEarthquakes()
        assertTrue(earthquakes.isEmpty())
    }



    @Test
    fun testGetAllBooks_emptyDatabase() {

        dbHelper.clearAllEarthquakes()
        val earthquakes = dbHelper.getAllEarthquakes()
        assertTrue(earthquakes.isEmpty())
    }

}