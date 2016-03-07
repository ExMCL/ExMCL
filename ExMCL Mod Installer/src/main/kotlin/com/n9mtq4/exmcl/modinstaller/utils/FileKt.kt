@file:JvmName("KotlinFile")
package com.n9mtq4.exmcl.modinstaller.utils;

import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * Created by will on 12/30/15 at 9:53 PM.
 *
 * A python like implementation of files
 * this code is dense, ugly, and undocumented, but it works
 *
 * @author Will "n9Mtq4" Bresnahan
 */
private fun stopTheJavadoc() {}

fun open(f: File, t: String) = open(f, t.contains('r'), t.contains('w'), t.contains('+'))
fun open(fPath: String, t: String) = open(File(fPath), t)
fun open(f: File, canRead: Boolean = false, canWrite: Boolean = false, append: Boolean = false) = FileKt(f, canRead, canWrite, append)
fun open(fPath: String , canRead: Boolean = false, canWrite: Boolean = false, append: Boolean = false) = FileKt(File(fPath), canRead, canWrite, append)

open class FileKt(val file: File, val canRead: Boolean = false, val canWrite: Boolean = false, val append: Boolean = false) : AutoCloseable {
	
	private val bufferedReader: BufferedReader?
	private val bufferedWriter: BufferedWriter?
	
	init {
		if (canWrite) {
			this.bufferedWriter = BufferedWriter(FileWriter(file, append))
		}else this.bufferedWriter = null
		if (canRead) {
			this.bufferedReader = BufferedReader(FileReader(file))
		}else this.bufferedReader = null
	}
	
	fun read(): String? {
		assertCanRead()
		return bufferedReader!!.readLine()
	}
	
	fun readAll(): String {
		assertCanRead()
		var str = ""
		var line: String? = read()
		while (line != null) {
			str += line + "\n"
			line = read()
		}
//		TODO: reset the reader
//		bufferedReader?.reset()
		return str
	}
	
	fun write(msg: String): FileKt {
		assertCanWrite()
		bufferedWriter!!.append(msg)
		bufferedWriter.flush()
		return this
	}
	
	fun writeln(msg: String): FileKt {
		assertCanWrite()
		write(msg + "\n")
		return this
	}
	
	override fun close() {
		bufferedReader?.close()
		bufferedWriter?.close()
	}
	
	private fun assertCanWrite() {
		if (!canWrite) throw IllegalStateException("This can not write to a file!")
	}
	
	private fun assertCanRead() {
		if (!canRead) throw IllegalStateException("This can not read a file")
	}
	
}
