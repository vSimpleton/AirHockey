package com.imiyar.airhockey.utils

import kotlin.math.tan

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC:
 */

object MatrixHelper {

    /**
     * Defines a projection matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes.
     *
     * @param m the float array that holds the perspective matrix
     * @param yFovInDegrees field of view in y direction, in degrees
     * @param aspect width to height aspect ratio of the viewport
     * @param zNear z轴最近的距离
     * @param zFar  z轴最远的距离
     */
    fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, zNear: Float, zFar: Float) {
        val angleInRadians = (yFovInDegrees * Math.PI / 180.0).toFloat()
        val a = (1.0 / tan(angleInRadians / 2.0)).toFloat()

        m[0] = a / aspect
        m[1] = 0f
        m[2] = 0f
        m[3] = 0f

        m[4] = 0f
        m[5] = a
        m[6] = 0f
        m[7] = 0f

        m[8] = 0f
        m[9] = 0f
        m[10] = -((zFar + zNear) / (zFar - zNear))
        m[11] = -1f

        m[12] = 0f
        m[13] = 0f
        m[14] = -(2f * zFar * zNear / (zFar - zNear))
        m[15] = 0f
    }

}