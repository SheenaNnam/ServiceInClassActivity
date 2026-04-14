package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    lateinit var bindTimer : TimerService.TimerBinder
    var isConnected = false

    lateinit var textView : TextView

    val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(
            p0: ComponentName?,
            p1: IBinder?
        ) {
            bindTimer = p1 as TimerService.TimerBinder
            bindTimer.setHandler(timerHadler)
            isConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnected = false
        }

    }


    val timerHadler = Handler(Looper.getMainLooper()) {
        textView.text = it.what.toString()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val start_stopButton = findViewById<Button>(R.id.startButton)
        textView = findViewById<TextView>(R.id.textView)

        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE)

//        findViewById<Button>(R.id.startButton).setOnClickListener {
//            if (isConnected){
//
//                if(bindTimer.isRunning && !bindTimer.paused) {
//                    start_stopButton.text = "Unpause"
//                } else{
//                    start_stopButton.text = "Pause"
//                }
//                bindTimer.pause()
//
//                if (!bindTimer.isRunning && !bindTimer.paused){
//                    bindTimer.start(30)
//                }
//
//            }
//
//        }
//
//        findViewById<Button>(R.id.stopButton).setOnClickListener {
//            if (isConnected){
//                bindTimer.stop()
//                start_stopButton.text = "Start"
//            }
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId){

            R.id.play ->
                {
                    if (bindTimer.isRunning && !bindTimer.paused) {
                        item.setIcon(android.R.drawable.ic_media_play)
                    } else {
                        item.setIcon(android.R.drawable.ic_media_pause)
                    }
                    bindTimer.pause()

                    if (!bindTimer.isRunning && !bindTimer.paused){
                        bindTimer.start(30)
                    }
                }
            R.id.stop ->{
                if (isConnected) {
                    bindTimer.stop()
                    item.setIcon(android.R.drawable.ic_media_play)
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}