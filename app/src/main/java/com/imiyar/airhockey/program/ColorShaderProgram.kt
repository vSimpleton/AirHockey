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

    private var aPositionLocation = 0
    private var aColorLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        aColorLocation = glGetAttribLocation(mProgram, A_COLOR)
    }

    fun setUniforms(matrix: FloatArray) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getColorAttributeLocation(): Int {
        return aColorLocation
    }

}