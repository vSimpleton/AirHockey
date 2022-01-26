package com.imiyar.airhockey.objects

import android.opengl.GLES20
import com.imiyar.airhockey.data.VertexArray
import com.imiyar.airhockey.program.TextureShaderProgram
import com.imiyar.airhockey.utils.BYTES_PER_FLOAT

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC: 存储桌子的位置数据
 */

class Table {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2 // 每个顶点有2个分量
        private const val TEXTURE_COORDINATES_COMPONENT_COUNT = 2 // 每个纹理坐标有2个分量
        private const val STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    private val vertexData = floatArrayOf(
        // Orders of coordinates: X, Y, S, T
        // Triangles Fan
           0f,    0f, 0.5f, 0.5f,
        -0.5f, -0.8f,   0f, 0.9f,
         0.5f, -0.8f,   1f, 0.9f,
         0.5f,  0.8f,   1f, 0.1f,
        -0.5f,  0.8f,   0f, 0.1f,
        -0.5f, -0.8f,   0f, 0.9f,
    )
    private val mVertexArray by lazy { VertexArray(vertexData) }

    fun bindData(program: TextureShaderProgram) {
        mVertexArray.setVertexAttribPointer(
            0,
            program.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            STRIDE
        )

        mVertexArray.setVertexAttribPointer(
            POSITION_COMPONENT_COUNT,
            program.getTextureCoordinatesAttributeLocation(),
            TEXTURE_COORDINATES_COMPONENT_COUNT,
            STRIDE
        )
    }

    fun draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
    }

}