package com.imiyar.airhockey.objects

import android.opengl.GLES20
import com.imiyar.airhockey.utils.*
import kotlin.math.cos
import kotlin.math.sin

/**
 * NAME: vSimpleton
 * DATE: 2022/1/26
 * DESC: 物体构建器
 */

class ObjectBuilder(sizeInVertices: Int) {

    // 保存顶点数据的数组
    private val mVertexData = FloatArray(sizeInVertices * FLOAT_PER_VERTEX)
    private val mDrawList = mutableListOf<DrawCommand>()
    // 数组的偏移量
    private var mOffset = 0

    companion object {
        private const val FLOAT_PER_VERTEX = 3 // 一个顶点有3个分量（x y z）

        /**
         * 创建冰球
         */
        fun createPuck(puck: Cylinder, numPoints: Int): GenerateData {
            val size = sizeOfCircleInVertices(numPoints) + sizeOfOpenCylinderInVertices(numPoints)
            val builder = ObjectBuilder(size)
            val puckTop = Circle(puck.center.translateY(puck.height / 2f), puck.radius)
            builder.appendCircle(puckTop, numPoints)
            builder.appendOpenCylinder(puck, numPoints)

            return builder.build()
        }

        /**
         * 创建木槌
         * 手柄高度占整体高度75%，基部高度占整体高度25%
         */
        fun createMallet(center: Point, radius: Float, height: Float, numPoints: Int): GenerateData {
            val size = sizeOfCircleInVertices(numPoints) * 2 + sizeOfOpenCylinderInVertices(numPoints) * 2
            val builder = ObjectBuilder(size)

            val baseHeight = height * 0.25f
            val baseCircle = Circle(center.translateY(-baseHeight), radius)
            val baseCylinder = Cylinder(baseCircle.center.translateY(-baseHeight / 2f), radius, baseHeight)

            builder.appendCircle(baseCircle, numPoints)
            builder.appendOpenCylinder(baseCylinder, numPoints)

            val handleHeight = height * 0.75f
            val handleRadius = radius / 3f
            val handleCircle = Circle(center.translateY(height * 0.5f), handleRadius)
            val handleCylinder = Cylinder(handleCircle.center.translateY(-handleHeight / 2f), handleRadius, handleHeight)

            builder.appendCircle(handleCircle, numPoints)
            builder.appendOpenCylinder(handleCylinder, numPoints)

            return builder.build()
        }

    }

    private fun build(): GenerateData {
        return GenerateData(mVertexData, mDrawList)
    }

    /**
     * 用三角形扇构建圆
     */
    private fun appendCircle(circle: Circle, numPoints: Int) {
        val startVertex = mOffset / FLOAT_PER_VERTEX
        val numVertices = sizeOfCircleInVertices(numPoints)

        mVertexData[mOffset++] = circle.center.x
        mVertexData[mOffset++] = circle.center.y
        mVertexData[mOffset++] = circle.center.z

        for (i in 0..numPoints) {
            val angleInRadians = ((i.toFloat() / numPoints.toFloat()) * (Math.PI * 2f)).toFloat()
            mVertexData[mOffset++] = (circle.center.x + circle.radius * cos(angleInRadians))
            mVertexData[mOffset++] = circle.center.y
            mVertexData[mOffset++] = (circle.center.z + circle.radius * sin(angleInRadians.toDouble())).toFloat()
        }

        mDrawList.add(object : DrawCommand {
            override fun draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices)
            }
        })
    }

    /**
     * 用三角形带构建圆柱体侧面
     */
    private fun appendOpenCylinder(cylinder: Cylinder, numPoints: Int) {
        val startVertex = mOffset / FLOAT_PER_VERTEX
        val numVertices = sizeOfOpenCylinderInVertices(numPoints)
        val yStart = cylinder.center.y - cylinder.height / 2f
        val yEnd = cylinder.center.y + cylinder.height / 2f

        for (i in 0..numPoints) {
            val angleInRadians = (i.toFloat() / numPoints.toFloat()) * (Math.PI * 2f)

            val xPosition = (cylinder.center.x + cylinder.radius * cos(angleInRadians)).toFloat()
            val zPosition = (cylinder.center.x + cylinder.radius * sin(angleInRadians)).toFloat()

            mVertexData[mOffset++] = xPosition
            mVertexData[mOffset++] = yStart
            mVertexData[mOffset++] = zPosition

            mVertexData[mOffset++] = xPosition
            mVertexData[mOffset++] = yEnd
            mVertexData[mOffset++] = zPosition
        }

        mDrawList.add(object : DrawCommand {
            override fun draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertices)
            }
        })
    }

}

interface DrawCommand {
    fun draw()
}

class GenerateData(val vertexData: FloatArray, val drawList: MutableList<DrawCommand>) {

}