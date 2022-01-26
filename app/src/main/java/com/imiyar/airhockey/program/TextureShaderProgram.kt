package com.imiyar.airhockey.program

import android.content.Context
import android.opengl.GLES20.*
import com.imiyar.airhockey.R

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC:
 */

class TextureShaderProgram(context: Context) :
    ShaderProgram(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader) {

    private var uMatrixLocation = 0
    private var uTextureUnitLocation = 0

    private var aPositionLocation = 0
    private var aTextureCoordinatesLocation = 0

    init {
        uMatrixLocation = glGetUniformLocation(mProgram, U_MATRIX)
        uTextureUnitLocation = glGetUniformLocation(mProgram, U_TEXTURE_UNIT)
        aPositionLocation = glGetAttribLocation(mProgram, A_POSITION)
        aTextureCoordinatesLocation = glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES)
    }

    fun setUniforms(matrix: FloatArray, textureId: Int) {
        glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0)
        // 把活动的纹理单元设置为纹理单元0
        glActiveTexture(GL_TEXTURE0)
        // 把纹理绑定到这个纹理单元
        glBindTexture(GL_TEXTURE_2D, textureId)
        // 把被选定的纹理单元传递给片元着色器中的uTextureUnitLocation
        glUniform1i(uTextureUnitLocation, 0)
    }

    fun getPositionAttributeLocation(): Int {
        return aPositionLocation
    }

    fun getTextureCoordinatesAttributeLocation(): Int {
        return aTextureCoordinatesLocation
    }

}