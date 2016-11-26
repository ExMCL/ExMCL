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

@file:JvmName("Cleaner") // just to make sure
package com.n9mtq4.exmcl.cleaner

import com.n9mtq4.exmcl.api.cleaner.AddToDelete
import com.n9mtq4.kotlin.extlib.pstAndUnit
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.events.EnableEvent
import com.n9mtq4.logwindow.events.RemovalEvent
import com.n9mtq4.logwindow.listener.EnableListener
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.logwindow.listener.RemovalListener
import java.io.File
import java.util.ArrayList

/**
 * Created by will on 2/14/16 at 6:51 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class Cleaner : GenericListener, RemovalListener, EnableListener {
	
	private typealias FileList = ArrayList<File>
	
	private val toDelete: FileList = ArrayList()
	
	override fun onEnable(e: EnableEvent) {
		val workingDir: File = File(System.getProperty("user.dir"))
		val children: Array<File> = workingDir.listFiles()
		children.filter { it.name.contains("hs", true) && it.name.endsWith(".log", true) }.forEach { addToDelete(it) }
	}
	
	@Suppress("unused")
	@ListensFor(AddToDelete::class)
	fun listenForAddToDelete(event: AddToDelete, baseConsole: BaseConsole) {
		
		baseConsole.println("Added ${event.file.absolutePath} to be deleted")
		addToDelete(event.file)
		
	}
	
	override fun onRemoval(e: RemovalEvent) = clean()
	
	private fun addToDelete(file: File) {
		if (!toDelete.contains(file)) toDelete.add(file)
	}
	
	private fun clean() = deleteFiles(toDelete)
	
	private fun deleteFiles(files: FileList) {
		
		files.filter(File::exists).forEach {
			
//			just in case
//			when dealing loops, it is good to prevent one bad apple from
//			spoiling the rest
			pstAndUnit {
				
				val success = it.deleteRecursively()
				println("${if (success) "Deleted" else "Failed to delete"} the file: ${it.absolutePath}")
				
				if (!success) {
					it.walkBottomUp().forEach(File::deleteOnExit)
					println("Set the ${it.absolutePath} to delete when the program exits.")
				}
				
			}
			
		}
		
	}
	
}
