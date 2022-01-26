package com.imiyar.airhockey.data

import android.opengl.GLES20
import com.imiyar.airhockey.program.ColorShaderProgram
import com.imiyar.airhockey.utils.BYTES_PER_FLOAT

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC: 存储木槌的位置数据
 */

class Mallet {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2 // 每个顶点有2个分量
        private const val COLOR_COMPONENT_COUNT = 3 // 每个颜色有3个分量
        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    private val vertexData = floatArrayOf(
        // Orders of coordinates: X, Y, R, G, B
        // Mallets
        0f, -0.4f, 0f, 0f, 1f,
        0f,  0.4f, 1f, 0f, 0f,
    )
    private val mVertexArray by lazy { VertexArray(vertexData) }

    fun bindData(program: ColorShaderProgram) {
        mVertexArray.setVertexAttribPointer(
            0,
            program.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        mVertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            program.getColorAttributeLocation(),
            COLOR_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 2)
    }

}