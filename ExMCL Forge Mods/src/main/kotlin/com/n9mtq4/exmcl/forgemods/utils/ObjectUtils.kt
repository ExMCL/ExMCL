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

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable

/**
 * Created by will on 2/14/16 at 10:18 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Read a saved serializable object.
 *
 * @param <E>  the type parameter
 * @param file the file
 * @return the object that has been saved
 * @throws Exception any errors that may have occurred.
 */
@Throws(Exception::class)
fun <E> readSerializable(file: File): E {
	val fileInputStream = FileInputStream(file)
	val objectInputStream = ObjectInputStream(fileInputStream)
	val o = objectInputStream.readObject() as E
	objectInputStream.close()
	return o
}

/**
 * Read serializable object.
 *
 * @param file the file
 * @return the object
 * @throws Exception any errors that may have occurred.
 */
@Throws(Exception::class)
fun readSerializableAny(file: File): Any? {
	val fileInputStream = FileInputStream(file)
	val objectInputStream = ObjectInputStream(fileInputStream)
	val o = objectInputStream.readObject()
	objectInputStream.close()
	return o
}

/**
 * Write a serializable to a file.
 *
 * @param object the object
 * @param file   the file
 * @throws Exception any errors that may have occur.
 */
@Throws(IOException::class)
fun writeSerializable(obj: Serializable, file: File) {
	val fileOutputStream = FileOutputStream(file)
	val objectOutputStream = ObjectOutputStream(fileOutputStream)
	objectOutputStream.writeObject(obj)
	objectOutputStream.close()
}
