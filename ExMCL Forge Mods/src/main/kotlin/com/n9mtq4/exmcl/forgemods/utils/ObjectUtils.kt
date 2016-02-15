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
