package com.example.lab5var6

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_NAME = "earthquakes.db"
private const val DATABASE_VERSION = 1
private const val TABLE_EARTHQUAKES = "earthquakes"
private const val COLUMN_ID = "id"
private const val COLUMN_PLACE = "place"
private const val COLUMN_TIME = "time"

class EarthquakeDb(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_EARTHQUAKES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PLACE TEXT,
                $COLUMN_TIME INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EARTHQUAKES")
        onCreate(db)
    }

    fun insertEarthquake(earthquake: Earthquake) {
        if (isEarthquakeExists(earthquake)) {
            throw Exception("Item already exist")
        }
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PLACE, earthquake.place)
            put(COLUMN_TIME, earthquake.time)
        }
        db.insert(TABLE_EARTHQUAKES, null, values)
    }

    fun isEarthquakeExists(earthquake: Earthquake): Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_PLACE = ? AND $COLUMN_TIME = ?"
        val selectionArgs = arrayOf(earthquake.place, earthquake.time.toString())

        val cursor = db.query(
            TABLE_EARTHQUAKES,
            arrayOf(COLUMN_PLACE),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAllEarthquakes(): List<Earthquake> {
        val db = readableDatabase
        val earthquakes = mutableListOf<Earthquake>()
        val cursor = db.query(TABLE_EARTHQUAKES, null, null, null, null, null, null)
        with(cursor) {
            while (moveToNext()) {
                val place = getString(getColumnIndexOrThrow(COLUMN_PLACE))
                val time = getLong(getColumnIndexOrThrow(COLUMN_TIME))

                earthquakes.add(Earthquake(place, time))
            }
        }
        cursor.close()
        return earthquakes
    }

    fun clearAllEarthquakes() {
        val db = writableDatabase
        db.delete(TABLE_EARTHQUAKES, null, null)
    }
}