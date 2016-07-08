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

package com.n9mtq4.exmcl.updater

import com.mojang.launcher.OperatingSystem
import com.n9mtq4.exmcl.api.card.LauncherCard
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import com.n9mtq4.kotlin.extlib.io.open
import com.n9mtq4.kotlin.extlib.pst
import com.n9mtq4.kotlin.extlib.pstAndGiven
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
	private val later: JButton
	private val ignore: JButton
	private val buttonPanel: JPanel
	
	init {
		
//		the build number is guarantied to succeed, while for some reason the tag_name might not
		val updateVersion = pstAndGiven(updateAvailable.targetBuildNumber.toString()) {
			updateAvailable.updateInfo["tag_name"]
		}
		
		this.update = JButton("Update")
		this.later = JButton("Later")
		this.ignore = JButton("Skip version $updateVersion")
		this.buttonPanel = JPanel(GridLayout(1, 3))
		buttonPanel.add(update)
		buttonPanel.add(later)
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
		later.addActionListener { removeThisCard() }
		ignore.addActionListener { 
			pst {
				val f = open(IGNORE_UPDATE_FILE, "w")
				f.writeln(updateAvailable.targetBuildNumber.toString())
				f.close()
			}
			removeThisCard()
		}
		
		val panelSize = launcherPanel.preferredSize
		val width = (panelSize.width * .75).toInt()
		val height = (panelSize.height * .75).toInt()
		this.preferredSize = Dimension(width, height)
		
	}
	
}
