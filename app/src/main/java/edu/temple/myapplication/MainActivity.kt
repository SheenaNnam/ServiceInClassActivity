package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button

class MainActivity : AppCompatActivity() {


    lateinit var bindTimer : TimerService.TimerBinder
    var isConnected = false

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            p0: ComponentName?,
            p1: IBinder?
        ) {
            bindTimer = p1 as TimerService.TimerBinder
            isConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val start_stopButton = findViewById<Button>(R.id.startButton)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (isConnected){

                if(bindTimer.isRunning && !bindTimer.paused) {
                    start_stopButton.text = "Unpause"
                } else{
                    start_stopButton.text = "Pause"
                }
                bindTimer.pause()

                if (!bindTimer.isRunning && !bindTimer.paused){
                    bindTimer.start(30)
                }

            }

        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (isConnected){
                bindTimer.stop()
                start_stopButton.text = "Start"
            }
        }
    }
}