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
	
	private val toDelete: ArrayList<File> = ArrayList();
	
	override fun onEnable(e: EnableEvent) {
		val workingDir: File = File(System.getProperty("user.dir"))
		val children: Array<File> = workingDir.listFiles()
		children.filter { it.name.contains("hs", true) && it.name.endsWith(".log", true) }.forEach { addToDelete(it) }
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor(AddToDelete::class)
	fun listenForAddToDelete(event: AddToDelete, baseConsole: BaseConsole) {
		
		baseConsole.println("Added ${event.file.absolutePath} to be deleted")
		addToDelete(event.file)
		
	}
	
	override fun onRemoval(e: RemovalEvent) = clean()
	
	private fun addToDelete(file: File) {
		if (!toDelete.contains(file)) toDelete.add(file)
	}
	
	private fun clean() {
		deleteFiles(toDelete)
	}
	
	private fun deleteFiles(files: ArrayList<File>) {
		
		files.forEach {
			
//			just in case
//			when dealing loops, it is good to prevent one bad apple from
//			spoiling the rest
			pstAndUnit {
				
				if (!it.exists()) return@forEach
				
				val success = it.deleteRecursively()
				println("${if (success) "Deleted" else "Failed to delete"} the file: ${it.absolutePath}")
				
				if (!success) {
					it.walkBottomUp().forEach { it.deleteOnExit() }
					println("Set the ${it.absolutePath} to delete when the program exits.")
				}
				
			}
			
		}
		
	}
	
}
