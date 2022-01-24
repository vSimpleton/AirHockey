package com.imiyar.airhockey.utils

import android.content.Context
import android.content.res.Resources.NotFoundException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.lang.StringBuilder

/**
 * NAME: vSimpleton
 * DATE: 2022/1/24
 * DESC:
 */

object ResourceReaderHelper {

    /**
     * 读取GLSL文件中着色器代码
     */
    fun readTextFileFromResource(context: Context, resourceId: Int): String {
        val body = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(resourceId)
            val inputStreamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String?
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open resource: $resourceId", e)
        } catch (nfe: NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId", nfe)
        }
        return body.toString()
    }

}