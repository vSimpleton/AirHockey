package com.imiyar.airhockey.program

import android.content.Context
import android.opengl.GLES20.*
import com.imiyar.airhockey.R

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC:
 */

class ColorShaderProgram(context: Context) : ShaderProgram(context, R.raw.vertex_shader, R.raw.fragment_shader) {

    private var uMatrixLocation = 0
    private var uColorLocation = 0

    private var aPositionLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        uColorLocation = glGetUniformLocation(mProgram, U_COLOR)
    }

    fun setUniforms(matrix: FloatArray, r: Float, g: Float, b: Float) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        glUniform4f(uColorLocation, r, g, b, 1f)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

}