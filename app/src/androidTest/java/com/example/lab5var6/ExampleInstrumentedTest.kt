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

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class EarthquakeDatabaseTest {
    private lateinit var dbHelper: EarthquakeDatabaseHelper

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dbHelper = EarthquakeDatabaseHelper(context)
    }

    @After
    fun tearDown() {
        dbHelper.clearAllEarthquakes()
    }

    @Test
    fun insertBook_success() {
        val earthquakeProperties =EarthquakeProperties(
            "test",10)



        dbHelper.insertEarthquake(earthquakeProperties)
        val earthquakes = dbHelper.getAllEarthquakes()



        assertEquals(earthquakeProperties.place, earthquakes[0].place)
        assertEquals(earthquakeProperties.time, earthquakes[0].time)


    }

    @Test(expected = Exception::class)
    fun insertBook_fail() {
        val earthquakeProperties =EarthquakeProperties(
            "test",10)

        dbHelper.insertEarthquake(earthquakeProperties)
        dbHelper.insertEarthquake(earthquakeProperties)

    }


    @Test
    fun clearAllBooks_success() {
        val earthquakeProperties =EarthquakeProperties(
            "test",10)

        val earthquakeProperties1 =EarthquakeProperties(
            "test2",15)

        dbHelper.insertEarthquake(earthquakeProperties)
        dbHelper.insertEarthquake(earthquakeProperties1)
        dbHelper.clearAllEarthquakes()
        val earthquakes = dbHelper.getAllEarthquakes()
        Assert.assertTrue(earthquakes.isEmpty())
    }



    @Test
    fun testGetAllBooks_emptyDatabase() {
        // Ensure the database is empty
        dbHelper.clearAllEarthquakes()
        val earthquakes = dbHelper.getAllEarthquakes()
        Assert.assertTrue(earthquakes.isEmpty())
    }

}
