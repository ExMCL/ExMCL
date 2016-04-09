package com.n9mtq4.exmcl.api.card

import com.n9mtq4.reflection.ReflectionWrapper
import net.minecraft.launcher.ui.LauncherPanel
import java.awt.CardLayout
import java.awt.FlowLayout
import java.awt.LayoutManager
import javax.swing.JPanel

/**
 * Created by will on 2/28/16 at 1:31 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
open class LauncherCard(protected val launcherPanel: LauncherPanel, layout: LayoutManager, isDoubleBuffered: Boolean) : JPanel(layout, isDoubleBuffered) {
	
	private val launcherPanelRR = ReflectionWrapper.attachToObject(launcherPanel)
	private val cardLayout: CardLayout = launcherPanelRR["cardLayout"]
	
	constructor(launcherPanel: LauncherPanel) : this(launcherPanel, true)
	constructor(launcherPanel: LauncherPanel, isDoubleBuffered: Boolean) : this(launcherPanel, FlowLayout(), isDoubleBuffered)
	constructor(launcherPanel: LauncherPanel, layout: LayoutManager) : this(launcherPanel, layout, true)
	
	fun addThisCard() = launcherPanel.setCard("login", this)
	fun removeThisCard() = cardLayout.show(launcherPanel, "launcher")
	
}
