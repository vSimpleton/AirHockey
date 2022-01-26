package com.imiyar.airhockey.data

import android.opengl.GLES20.*
import com.imiyar.airhockey.utils.BufferHelper

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC: 封装存储顶点矩阵的FloatBuffer
 */

class VertexArray(vertexArray: FloatArray) {

    private val mFloatBuffer by lazy { BufferHelper.putFloatBuffer(vertexArray) }  // 顶点坐标数据缓冲区

    /**
     * 关联attribute属性与顶点数据
     *
     * @param offset 需要读取的数据偏移量
     * @param attributeLocation 属性位置
     * @param componentCount 顶点数据占用的分量
     * @param stride 每读取一顶点时需要跳过的数据数量
     */
    fun setVertexAttribPointer(offset: Int, attributeLocation: Int, componentCount: Int, stride: Int) {
        mFloatBuffer.position(offset)
        glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, mFloatBuffer)
        glEnableVertexAttribArray(attributeLocation)
        mFloatBuffer.position(0)
    }

}