package com.imiyar.airhockey.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.opengl.GLES20
import android.opengl.GLES20.*
import android.opengl.GLUtils
import android.util.Log

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC:
 */

object TextureHelper {

    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureIds = IntArray(1)
        glGenTextures(1, textureIds, 0)

        if (textureIds[0] == 0) {
            Log.d("texture", "Could not generate a new OpenGL texture object...")
            return 0
        }

        // OpenGL不能直接读取PNG或JPEG文件的数据，因为这些文件被编码为特定的压缩格式
        // 使用Android内置的位图解码器把图像文件解压缩为OpenGL能理解的形式
        val options = BitmapFactory.Options()
        // 需要原始的图像数据，不需要压缩
        options.inScaled = false
        val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

        if (bitmap == null) {
            Log.d("texture", "Resource ID $resourceId could not be decoded.")
            glDeleteTextures(1, textureIds, 0)
            return 0
        }

        // 绑定textureId
        glBindTexture(GL_TEXTURE_2D, textureIds[0])

        // 设置默认的纹理过滤参数
        // GL_TEXTURE_MIN_FILTER 缩小过滤（使用MIP贴图级别之间插值的最近邻过滤）
        // GL_TEXTURE_MAG_FILTER 放大过滤（双线性过滤）
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // 加载位图数据到OpenGL
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap.recycle()

        // 生成MIP贴图
        glGenerateMipmap(GL_TEXTURE_2D)

        // 解绑
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureIds[0]

    }

}