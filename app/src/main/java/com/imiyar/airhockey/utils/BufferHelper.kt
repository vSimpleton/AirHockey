package com.imiyar.airhockey.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC: NIO Buffer的工具类 ，创建ByteBuffer
 */

object BufferHelper {

    /**
     * 获取浮点形的缓冲数据
     */
    fun putFloatBuffer(array: FloatArray): FloatBuffer {
        // 分配一块本地内存，并把Java的数据复制到本地内存（不受Java垃圾回收器的管理）
        // 顶点坐标个数 * 坐标数据类型（float占4字节）
        val buffer = ByteBuffer.allocateDirect(array.size * 4)
            .order(ByteOrder.nativeOrder()) // 设置使用设备硬件的本地字节序（保证数据排序一致）
            .asFloatBuffer()

        return buffer.put(array)
    }

}