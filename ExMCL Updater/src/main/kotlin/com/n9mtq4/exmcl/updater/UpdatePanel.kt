package com.n9mtq4.exmcl.updater

import com.n9mtq4.reflection.ReflectionWrapper
import net.minecraft.launcher.ui.LauncherPanel
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.JButton
import javax.swing.JPanel
import javax.swing.SwingUtilities

/**
 * Created by will on 2/27/16 at 4:56 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class UpdatePanel(val launcherPanelRR: ReflectionWrapper<LauncherPanel>) : JPanel(), ActionListener {
	
	private val okButton: JButton
	
	init {
		
		okButton = JButton("Ok")
		add(okButton)
		
		okButton.addActionListener(this)
		
	}
	
	override fun actionPerformed(e: ActionEvent) {
		
		removeThisCard()
		repack()
		
	}
	
	internal fun removeThisCard() {
//		TODO: remove the card
	}
	
	internal fun repack() {
		val window = SwingUtilities.windowForComponent(this) ?: return
		window.pack()
	}
	
}
