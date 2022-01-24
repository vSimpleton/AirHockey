package com.imiyar.airhockey

import android.app.ActivityManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private val mGLSurfaceView by lazy { MyGLSurfaceView(this) }
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 检查系统是否支持OpenGL ES 2.0
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportEs2 = configurationInfo.reqGlEsVersion >= 0x20000

        if (supportEs2) {
            mGLSurfaceView.initEGL()
            rendererSet = true
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.", Toast.LENGTH_LONG).show()
            return
        }

        setContentView(mGLSurfaceView)
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            mGLSurfaceView.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            mGLSurfaceView.onResume()
        }
    }

}