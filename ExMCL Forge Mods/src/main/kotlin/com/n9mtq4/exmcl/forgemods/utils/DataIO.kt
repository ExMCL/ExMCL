/*
 * MIT License
 *
 * Copyright (c) 2016 Will (n9Mtq4) Bresnahan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.n9mtq4.exmcl.forgemods.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.n9mtq4.exmcl.forgemods.data.ModData
import com.n9mtq4.jsonserialzation.CompressStream
import com.n9mtq4.kotlin.extlib.syntax.def
import java.io.File

/**
 * Created by will on 6/14/16 at 8:07 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

internal val gsonStructure: Gson by lazy { createJsonStructure().create() }

private fun createJsonStructure() = def {
	val gsonBuilder = GsonBuilder()
	gsonBuilder
}

fun ModData.toJson() = gsonStructure.toJson(this)
fun ModData.writeToFile(file: File) {
	val json = toJson()
	CompressStream.compressStringToFile(json, file)
}

fun readForgeDataFromFile(file: File): ModData {
	val json = CompressStream.readCompressedStringFromFile(file)
	return gsonStructure.fromJson(json, ModData::class.java)
}
