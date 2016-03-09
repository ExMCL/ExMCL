package com.n9mtq4.exmcl.modinstaller.ui

import java.awt.BorderLayout
import java.awt.Component
import javax.swing.JFrame
import javax.swing.JProgressBar
import javax.swing.WindowConstants

/**
 * Created by will on 3/8/16 at 2:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class PatchingWindow(val parent: Component) {
	
	private val frame: JFrame
	private val progress: JProgressBar
	
	init {
		this.frame = JFrame("Patching Minecraft")
		frame.defaultCloseOperation = WindowConstants.DO_NOTHING_ON_CLOSE
		
		this.progress = JProgressBar()
		progress.isIndeterminate = true
		
		frame.add(progress, BorderLayout.CENTER)
		
		frame.isAlwaysOnTop = true
		frame.pack()
		frame.isVisible = true
		frame.setSize(300, frame.size.height)
		frame.isResizable = false
		frame.setLocationRelativeTo(parent)
	}
	
	fun dispose() {
		frame.dispose()
	}
	
}
