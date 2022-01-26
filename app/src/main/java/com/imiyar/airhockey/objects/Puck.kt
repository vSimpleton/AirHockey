package com.imiyar.airhockey.objects

import com.imiyar.airhockey.data.VertexArray
import com.imiyar.airhockey.program.ColorShaderProgram
import com.imiyar.airhockey.utils.Cylinder
import com.imiyar.airhockey.utils.Point

/**
 * NAME: vSimpleton
 * DATE: 2022/1/26
 * DESC: 冰球
 */

class Puck(radius: Float, val height: Float, numPointsAroundPuck: Int) {

    companion object {
        private const val POSITION_COMPONENT_COUNT = 3
    }

    private val mGenerateData by lazy {
        ObjectBuilder.createPuck(
            Cylinder(
                Point(0f, 0f, 0f),
                radius, height
            ), numPointsAroundPuck
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