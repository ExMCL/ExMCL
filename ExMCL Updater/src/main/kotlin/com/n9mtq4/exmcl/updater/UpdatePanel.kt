package com.n9mtq4.exmcl.updater

import com.mojang.launcher.OperatingSystem
import com.n9mtq4.exmcl.api.card.LauncherCard
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import net.minecraft.launcher.ui.LauncherPanel
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.GridLayout
import java.net.URI
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea

/**
 * Created by will on 2/27/16 at 4:56 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class UpdatePanel(launcherPanel: LauncherPanel, val updateAvailable: UpdateAvailable) : LauncherCard(launcherPanel, BorderLayout()) {
	
	private val update: JButton
	private val ignore: JButton
	private val buttonPanel: JPanel
	
	init {
		
		this.update = JButton("Update")
		this.ignore = JButton("Ignore")
		this.buttonPanel = JPanel(GridLayout(1, 2))
		buttonPanel.add(update)
		buttonPanel.add(ignore)
		
		val body = JTextArea()
		body.lineWrap = true
		body.wrapStyleWord = true
		body.preferredSize = Dimension(400, 200)
		body.isEditable = false
		body.text = (updateAvailable.updateInfo["body"] ?: "Error getting changelog!") as String
		val scroll = JScrollPane(body)
		scroll.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
		
		add(JLabel("<html><h2>There is an update for ExMCL available!</h2></html>"), BorderLayout.NORTH)
		add(scroll, BorderLayout.CENTER)
		add(buttonPanel, BorderLayout.SOUTH)
		
		update.addActionListener { OperatingSystem.openLink(URI.create(UPDATE_URL)) }
		ignore.addActionListener { removeThisCard() }
		
		val panelSize = launcherPanel.preferredSize
		val width = (panelSize.width * .75).toInt()
		val height = (panelSize.height * .75).toInt()
		this.preferredSize = Dimension(width, height)
		
	}
	
}
