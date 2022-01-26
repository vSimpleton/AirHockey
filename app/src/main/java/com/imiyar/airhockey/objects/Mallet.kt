package com.imiyar.airhockey.objects

import com.imiyar.airhockey.data.VertexArray
import com.imiyar.airhockey.program.ColorShaderProgram
import com.imiyar.airhockey.utils.Point

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC: 存储木槌的位置数据
 */

class Mallet(radius: Float, val height: Float, numPointsAroundPuck: Int) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3 // 每个顶点有2个分量
    }

    private val mGenerateData by lazy {
        ObjectBuilder.createMallet(
            Point(0f, 0f, 0f),
            radius, height, numPointsAroundPuck
        )
    }
    private val mVertexArray by lazy { VertexArray(mGenerateData.vertexData) }
    private val mDrawList by lazy { mGenerateData.drawList }

    fun bindData(program: ColorShaderProgram) {
        mVertexArray.setVertexAttribPointer(
            0,
            program.getPositionAttributeLocation(),
            POSITION_COMPONENT_COUNT,
            0
        )
    }

    fun draw() {
        mDrawList.forEach {
            it.draw()
        }
    }

}