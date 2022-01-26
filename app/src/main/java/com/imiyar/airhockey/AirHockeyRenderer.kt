package com.imiyar.airhockey

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.imiyar.airhockey.objects.Mallet
import com.imiyar.airhockey.objects.Puck
import com.imiyar.airhockey.objects.Table
import com.imiyar.airhockey.program.ColorShaderProgram
import com.imiyar.airhockey.program.TextureShaderProgram
import com.imiyar.airhockey.utils.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC: 自定义渲染器
 */

class AirHockeyRenderer(glSurfaceView: MyGLSurfaceView) : GLSurfaceView.Renderer {

    private val mContext = glSurfaceView.context
    private val mTextureProgram by lazy { TextureShaderProgram(mContext) }
    private val mColorProgram by lazy { ColorShaderProgram(mContext) }

    private var mProjectionMatrix = FloatArray(16) // 正交投影矩阵（归一化设备坐标）
    private var mModelMatrix = FloatArray(16) // 模型矩阵（移动物体）

    private var viewMatrix = FloatArray(16) // 视图矩阵
    private var viewProjectionMatrix = FloatArray(16)
    private var modelViewProjectionMatrix = FloatArray(16)

    private val mTable by lazy { Table() }
    private val mMallet by lazy { Mallet(0.08f, 0.15f, 32) }
    private val mPuck by lazy { Puck(0.06f, 0.02f, 32) }

    private var mTextureId = 0

    /**
     * 当Surface被创建的时候，会调用此方法。
     *
     * 这发生在应用程序第一次运行的时候，并且当设备被唤醒或者用户从其他activity切换回来的时候，此方法也可能会被调用。
     */
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 清屏时使用黑色
        glClearColor(0f, 0f, 0f, 0f)
        mTextureId = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface)
    }

    /**
     * 当Surface尺寸改变时，此方法会被调用
     *
     * 如横屏、竖屏来回切换
     */
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        // 设置视窗的尺寸大小
        glViewport(0, 0, width, height)

        // 使用透视投影矩阵
        Matrix.perspectiveM(mProjectionMatrix, 0, 45f, width.toFloat() / height.toFloat(), 1f, 10f)

        // 创建视图矩阵
        // eyeX、eyeY、eyeZ表示眼睛所在的位置
        // centerX、centerY、centerZ表示眼睛正在看的地方，这个位置出现在整个场景的中心
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.2f,
            0f, 0f, 0f, 0f, 1f, 0f)

        // 把模型矩阵设为单位矩阵，并沿着z轴负方向移动2个单位
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, 0f, 0f, -0.8f)

        // 正交投影矩阵与模型矩阵相乘，最后把相乘的结果copy到正交投影矩阵中
        val tempArray = FloatArray(16)
        Matrix.multiplyMM(tempArray, 0, mProjectionMatrix, 0, mModelMatrix, 0)
        System.arraycopy(tempArray, 0, mProjectionMatrix, 0, tempArray.size)

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

        Matrix.multiplyMM(viewProjectionMatrix, 0, mProjectionMatrix, 0, viewMatrix, 0)

        positionTableInScene()
        mTextureProgram.useProgram()
        mTextureProgram.setUniforms(modelViewProjectionMatrix, mTextureId)
        mTable.bindData(mTextureProgram)
        mTable.draw()

        positionObjectInScene(0f, mMallet.height / 2, -0.4f)
        mColorProgram.useProgram()
        mColorProgram.setUniforms(modelViewProjectionMatrix, 1f, 0f, 0f)
        mMallet.bindData(mColorProgram)
        mMallet.draw()

        positionObjectInScene(0f, mMallet.height / 2, 0.4f)
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0f, 0f, 1f)
        mMallet.draw()

        positionObjectInScene(0f, mPuck.height / 2, 0f)
        mColorProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f)
        mPuck.bindData(mColorProgram)
        mPuck.draw()
    }

    /**
     * 在绘制桌子前更新模型矩阵
     */
    private fun positionTableInScene() {
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.rotateM(mModelMatrix, 0, -90f, 1f, 0f, 0f)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, mModelMatrix, 0)
    }

    private fun positionObjectInScene(x: Float, y: Float, z: Float) {
        Matrix.setIdentityM(mModelMatrix, 0)
        Matrix.translateM(mModelMatrix, 0, x, y, z)
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, mModelMatrix, 0)
    }

}