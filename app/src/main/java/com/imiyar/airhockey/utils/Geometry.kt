package com.imiyar.airhockey.utils

/**
 * 点的定义
 */
class Point(val x: Float, val y: Float, val z: Float) {

    fun translateY(distance: Float): Point {
        return Point(x, y + distance, z)
    }

}

/**
 * 圆的定义（圆的半径可缩放）
 * @param center 圆心
 * @param radius 半径
 */
class Circle(val center: Point, val radius: Float) {

    fun scale(scale: Float): Circle {
        return Circle(center, radius * scale)
    }

}

/**
 * 圆柱体的定义
 * @param center 圆心
 * @param radius 半径
 * @param height 圆柱的高度
 */
class Cylinder(val center: Point, val radius: Float, val height: Float) {

}

/**
 * 计算圆柱体顶部顶点数量
 */
fun sizeOfCircleInVertices(numPoints: Int): Int {
    return 1 + (numPoints + 1)
}

/**
 * 计算圆柱体侧面顶点的数量
 */
fun sizeOfOpenCylinderInVertices(numPoints: Int): Int {
    return (numPoints + 1) * 2
}