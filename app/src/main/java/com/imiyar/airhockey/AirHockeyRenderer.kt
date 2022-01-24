package com.imiyar.airhockey

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.imiyar.airhockey.utils.BufferHelper
import com.imiyar.airhockey.utils.ResourceReaderHelper
import com.imiyar.airhockey.utils.ShaderHelper
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC: 自定义渲染器
 */

class AirHockeyRenderer(private val glSurfaceView: MyGLSurfaceView) : GLSurfaceView.Renderer {

    private lateinit var mVertexBuffer: FloatBuffer // 顶点坐标数据缓冲区
    private val mContext = glSurfaceView.context
    private var mProgram = 0 // 着色器程序

    private var aColorLocation = 0
    private var aPositionLocation = 0
    private var uMatrixLocation = 0
    private var mProjectionMatrix = FloatArray(16)

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2 // 每个顶点有2个分量
        private const val COLOR_COMPONENT_COUNT = 3 // 每个颜色有3个分量
        private const val BYTES_PER_FLOAT = 4
        private const val A_COLOR = "a_Color"
        private const val A_POSITION = "a_Position"
        private const val U_MATRIX = "u_Matrix"
        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    init {
        initBuffer()
    }

    /**
     * 定义顶点坐标，并使顶点数据被OpenGL存取
     * FloatBuffer：把内存从Java堆复制到本地堆，从而不受垃圾回收器的管理
     */
    private fun initBuffer() {
        // 前两个为顶点数据，后三个为颜色RGB数据
        val vertex = floatArrayOf(
            // Triangle Fan
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            // Line 1
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            // Mallets
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f,
        )
        mVertexBuffer = BufferHelper.putFloatBuffer(vertex)
    }

    /**
     * 当Surface被创建的时候，会调用此方法。
     *
     * 这发生在应用程序第一次运行的时候，并且当设备被唤醒或者用户从其他activity切换回来的时候，此方法也可能会被调用。
     */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 清屏时使用黑色
        glClearColor(0f, 0f, 0f, 0f)

        // 初始化着色器与链接程序
        val vertexStr = ResourceReaderHelper.readTextFileFromResource(mContext, R.raw.vertex_shader)
        val fragmentStr = ResourceReaderHelper.readTextFileFromResource(mContext, R.raw.fragment_shader)
        val vShaderId = ShaderHelper.compileVertexShader(vertexStr)
        val fShaderId = ShaderHelper.compileFragmentShader(fragmentStr)
        mProgram = ShaderHelper.linkProgram(vShaderId, fShaderId)

        // 使用程序
        glUseProgram(mProgram)

        // 获取uniform与attribute变量的索引位置
        aColorLocation = glGetAttribLocation(mProgram, A_COLOR)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX)

        // 关联aPositionLocation与顶点数据的数组
        mVertexBuffer.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, mVertexBuffer)
        // 使能顶点数据
        glEnableVertexAttribArray(aPositionLocation)

        // 关联aColorLocation与顶点颜色数据的数组
        mVertexBuffer.position(POSITION_COMPONENT_COUNT)
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, mVertexBuffer)
        // 使能顶点颜色数据
        glEnableVertexAttribArray(aColorLocation)

    }

    /**
     * 当Surface尺寸改变时，此方法会被调用
     *
     * 如横屏、竖屏来回切换
     */
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置视窗的尺寸大小
        glViewport(0, 0, width, height)

        // 创建正交投影矩阵，适配横竖屏切换
        val aspectRatio = (if (width > height) width / height else height / width).toFloat()
        if (width > height) {
            Matrix.orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            Matrix.orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    /**
     * 每绘制一帧时，此方法都会被调用
     *
     * 在此方法中，我们一定要绘制一些东西，即使只是清空屏幕。因为在这个方法返回后，渲染缓冲区会被交换并显示到屏幕上，
     * 如果什么都没画，可能会看到屏幕闪烁
     */
    override fun onDrawFrame(gl: GL10?) {
        // 清屏
        glClear(GL_COLOR_BUFFER_BIT)
        glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0)

        // 绘制长方形（由两个三角形构成）
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6)

        // 绘制分割线
        glDrawArrays(GL_LINES, 6, 2)

        // 绘制两个点(一个蓝色，一个红色)
        glDrawArrays(GL_POINTS, 8, 1)
        glDrawArrays(GL_POINTS, 9, 1)

    }
}