package com.n9mtq4.exmcl.modinstaller.utils

import java.awt.Component
import java.io.File
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
