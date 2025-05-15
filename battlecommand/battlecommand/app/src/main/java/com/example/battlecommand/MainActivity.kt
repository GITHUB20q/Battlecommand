package com.example.battlecommand

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


// inside onCreate() of MainMenu activity or wherever buttons are defined:
                findViewById<ImageButton>(R.id.btnSettings).setOnClickListener {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }

        findViewById<ImageButton>(R.id.btnHelp).setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }


        val playButton: ImageButton = findViewById(R.id.btnPlay)
        playButton.setOnClickListener {
            // Go to GameActivity
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
        }

        // Optional: Implement Settings and Help later
    }
}
