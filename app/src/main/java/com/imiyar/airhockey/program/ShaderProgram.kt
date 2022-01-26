package com.imiyar.airhockey.program

import android.content.Context
import android.opengl.GLES20
import com.imiyar.airhockey.utils.ResourceReaderHelper
import com.imiyar.airhockey.utils.ShaderHelper

/**
 * NAME: vSimpleton
 * DATE: 2022/1/25
 * DESC:
 */

open class ShaderProgram(context: Context, vertexShaderResourceId: Int, fragmentShaderResourceId: Int) {

    companion object {
        const val A_COLOR = "a_Color"
        const val A_POSITION = "a_Position"
        const val A_TEXTURE_COORDINATES = "a_TextureCoordinates"

        const val U_MATRIX = "u_Matrix"
        const val U_TEXTURE_UNIT = "u_TextureUnit"
    }

    protected var mProgram = 0

    init {
        mProgram = ShaderHelper.buildProgram(ResourceReaderHelper.readTextFileFromResource(context, vertexShaderResourceId),
            ResourceReaderHelper.readTextFileFromResource(context, fragmentShaderResourceId))
    }

    fun useProgram() {
        GLES20.glUseProgram(mProgram)
    }

}