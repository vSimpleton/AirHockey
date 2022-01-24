package com.imiyar.airhockey

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC:
 */

class MyGLSurfaceView(context: Context?) : GLSurfaceView(context) {

    private val mGLRenderer by lazy { AirHockeyRenderer(this) }

    fun initEGL() {
        // 设置EGL版本为 2.0
        setEGLContextClientVersion(2)
        // 设置渲染器
        setRenderer(mGLRenderer)
    }

}