package com.n9mtq4.exmcl.modinstaller.utils

import java.awt.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

/**
 * Created by will on 2/14/16 at 10:33 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */

/**
 * Opens a JFileChooser to search
 * for mods.
 * */
fun browseForMods(component: Component): Array<File> {
	val chooser = JFileChooser()
	chooser.isMultiSelectionEnabled = true
	val fnef = FileNameExtensionFilter("Mods (*.zip, *.jar)", "zip", "jar")
	chooser.fileFilter = fnef
	chooser.dialogTitle = "Choose Mod(s)"
	val returnVal = chooser.showOpenDialog(component)
	return when (returnVal) {
		JFileChooser.APPROVE_OPTION -> chooser.selectedFiles
		else -> arrayOf()
	}
}


/**
 * http://stackoverflow.com/a/115086
 * Auto-converted from java
 */
@SuppressWarnings("Duplicates")
@Throws(IOException::class)
internal fun copyFile(sourceFile: File, destFile: File) {
	if (!destFile.exists()) {
		destFile.parentFile.mkdirs()
		destFile.createNewFile()
	}
	
	var source: FileChannel? = null
	var destination: FileChannel? = null
	
	try {
		source = FileInputStream(sourceFile).channel
		destination = FileOutputStream(destFile).channel
		destination?.transferFrom(source, 0, source!!.size())
	} finally {
		source?.close()
		destination?.close()
	}
}
