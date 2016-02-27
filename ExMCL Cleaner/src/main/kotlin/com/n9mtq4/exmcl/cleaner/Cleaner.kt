@file:JvmName("Cleaner") // just to make sure
package com.n9mtq4.exmcl.cleaner

import com.n9mtq4.exmcl.api.cleaner.AddToDelete
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
		deleteFiles(buildToDeleteTree())
	}
	
	private fun deleteFiles(files: ArrayList<File>) {
		
		files.forEach { 
			
			try {
				
				if (!it.exists()) return@forEach
				
				val success = it.delete()
				println("${if (success) "Deleted" else "Failed to delete"} the file: ${it.absolutePath}")
				
				if (!success) {
					it.deleteOnExit()
					println("Set the file to delete when the program exits.")
				}
				
			}catch (e: Exception) {
//				just in case
//				when dealing loops, it is good to prevent one bad apple from
//				spoiling the rest
				e.printStackTrace()
			}
			
		}
		
	}
	
	private fun buildToDeleteTree(): ArrayList<File> {
//		change directories into children files
		val files = ArrayList<File>()
		for (file in toDelete) {
			files.add(file)
			if (file.isDirectory) {
				files.addAll(buildFileTree(file))
			}
		}
//		directories have to be last
		files.sort { o1, o2 ->
			if (o1.isDirectory) return@sort 1
			if (o2.isDirectory) return@sort -1
			return@sort 0
		}
		return files
	}
	
	private fun buildFileTree(file: File): ArrayList<File> {
		
		val files = ArrayList<File>()
		if (file.isDirectory) {
			val children = file.listFiles() ?: return files
			for (child in children) {
				files.addAll(buildFileTree(child))
			}
		}else {
			files.add(file)
		}
		return files
		
	}
	
}
