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

import java.awt.Component
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.channels.FileChannel
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.SwingUtilities
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


/**
 * Invoke Later.
 * Shorthand for SwingUtilities.invokeLater
 * */
inline fun il(crossinline body: () -> Unit) = SwingUtilities.invokeLater { body() }

fun msg(parent: Component? = null, msg: String, title: String, msgType: Int = JOptionPane.INFORMATION_MESSAGE) = JOptionPane.showMessageDialog(parent, msg, title, msgType)
