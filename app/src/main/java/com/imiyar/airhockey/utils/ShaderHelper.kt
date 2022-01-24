package com.imiyar.airhockey.utils

import android.opengl.GLES20
import android.opengl.GLES20.*
import android.util.Log

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC: 着色器工具类  编译、链接
 */

object ShaderHelper {

    /**
     * 加载并编译着色器代码
     * @param type 着色器类型（GL_VERTEX_SHADER, GL_FRAGMENT_SHADER）
     * @param shaderStr 着色器代码
     *
     * @return 着色器id(返回0表示失败）
     */
    private fun compileShader(type: Int, shaderStr: String): Int {
        // 创建着色器
        val shaderId = glCreateShader(type)
        // 加载着色器
        glShaderSource(shaderId, shaderStr)
        // 编译着色器代码，检验是否字符串拼写错误
        glCompileShader(shaderId)
        val status = IntArray(1)
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0)
        if (status[0] != GL_TRUE) {
            // 若编译失败，则删除着色器对象
            glDeleteShader(shaderId)
            Log.d("shader", "Results of compile source: \n $shaderStr \n ${glGetShaderInfoLog(shaderId)}")
        }

        return shaderId
    }

    /**
     * 加载并编译顶点着色器代码
     */
    fun compileVertexShader(vertexStr: String): Int {
        return compileShader(GL_VERTEX_SHADER, vertexStr)
    }

    /**
     * 加载并编译顶点着色器代码
     */
    fun compileFragmentShader(fragmentStr: String): Int {
        return compileShader(GL_FRAGMENT_SHADER, fragmentStr)
    }

    /**
     * 将顶点着色器和片元着色器一起链接到 OpenGL 程序中
     */
    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programId = glCreateProgram()
        // 将顶点和片元着色器附加到程序中
        glAttachShader(programId, vertexShaderId)
        glAttachShader(programId, fragmentShaderId)
        // 链接着色器
        glLinkProgram(programId)
        val status = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0)
        if (status[0] != GL_TRUE) {
            glDeleteProgram(programId)
            Log.d("program", "Results of link program: \n ${glGetProgramInfoLog(programId)}")
        }

        // 释放着色器
        glDeleteShader(vertexShaderId)
        glDeleteShader(fragmentShaderId)

        return programId
    }

    /**
     * 验证程序是否可用
     */
    fun validateProgram(programId: Int): Boolean {
        glValidateProgram(programId)
        val status = IntArray(1)
        glGetProgramiv(programId, GL_VALIDATE_STATUS, status, 0)

        return status[0] == GL_TRUE
    }

}