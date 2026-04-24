// File ini ada di: androidMain/kotlin/org/example/project/MainActivity.kt
package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.example.project.UI.App
import org.example.project.db.DatabaseDriverFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Kirim DatabaseDriverFactory ke App()
            // DatabaseDriverFactory butuh Context untuk buat SQLite driver di Android
            App(driverFactory = DatabaseDriverFactory(applicationContext))
        }
    }
}