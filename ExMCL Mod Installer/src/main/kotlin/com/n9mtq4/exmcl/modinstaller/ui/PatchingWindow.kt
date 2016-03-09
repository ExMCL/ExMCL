package com.n9mtq4.exmcl.modinstaller.ui

import java.awt.Component
import javax.swing.JDialog
import javax.swing.JProgressBar

/**
 * Created by will on 3/8/16 at 2:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class PatchingWindow(val parent: Component) : JDialog() {
	
	private val progress: JProgressBar
	
	init {
		
		title = "Patching Minecraft"
		defaultCloseOperation = DO_NOTHING_ON_CLOSE
		
		this.progress = JProgressBar(0, 100)
		progress.isIndeterminate = true
		
		add(progress)
		
		forceUpdate()
		
	}
	
	internal fun forceUpdate() {
		pack()
		isVisible = true
		setSize(300, size.height)
		isResizable = false
		setLocationRelativeTo(parent)
	}
	
}
