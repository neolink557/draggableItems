package com.example.myapplication

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myapplication.ui.DragableScreen
import com.example.myapplication.ui.MainScreen
import com.example.myapplication.ui.MainViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {

    private var viewModel = MainViewModel()

    /* put this into your activity class */
    private var mSensorManager: SensorManager? = null
    private var mAccel // acceleration apart from gravity
            = 0f
    private var mAccelCurrent // current acceleration including gravity
            = 0f
    private var mAccelLast // last acceleration including gravity
            = 0f
    private val mSensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(se: SensorEvent) {
            val x = se.values[0]
            val y = se.values[1]
            val z = se.values[2]
            mAccelLast = mAccelCurrent
            mAccelCurrent = Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = mAccelCurrent - mAccelLast
            mAccel = mAccel * 0.9f + delta // perform low-cut filter
            if (mAccel > 12) {
                recreate()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensorManager!!.registerListener(mSensorListener, mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        mAccel = 0.00f
        mAccelCurrent = SensorManager.GRAVITY_EARTH
        mAccelLast = SensorManager.GRAVITY_EARTH
        setContent {
            MyApplicationTheme() {
                DragableScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(0.8f))
                ) {
                    if (mAccel > 12) {
                        viewModel = MainViewModel()
                        MainScreen(viewModel)
                    }
                    MainScreen(viewModel)
                }
            }
        }
    }

}