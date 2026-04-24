package org.example.project.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import orgexampleproject.db.NotesAppDatabase   // ✅ package baru

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = NotesAppDatabase.Schema,
            context = context,
            name = "notes.db"
        )
    }
}