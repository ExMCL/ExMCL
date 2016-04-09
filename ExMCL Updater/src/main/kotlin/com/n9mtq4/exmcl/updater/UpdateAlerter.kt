@file:Suppress("unused", "UNUSED_PARAMETER")
package com.n9mtq4.exmcl.updater

import com.n9mtq4.exmcl.api.hooks.events.SwingUserInterfaceEvent
import com.n9mtq4.exmcl.api.updater.UpdateAvailable
import com.n9mtq4.logwindow.BaseConsole
import com.n9mtq4.logwindow.annotation.ListensFor
import com.n9mtq4.logwindow.listener.GenericListener
import com.n9mtq4.reflection.ReflectionWrapper
import net.minecraft.launcher.SwingUserInterface
import net.minecraft.launcher.ui.LauncherPanel
import javax.swing.SwingUtilities

/**
 * Created by will on 2/27/16 at 4:20 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Suppress("unused", "UNUSED_PARAMETER")
class UpdateAlerter : GenericListener {
	
	private lateinit var swingUserInterface: SwingUserInterface
	private lateinit var swingUserInterfaceRR: ReflectionWrapper<SwingUserInterface>
	private lateinit var launcherPanel: LauncherPanel
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForSwingUserInterface(e: SwingUserInterfaceEvent, baseConsole: BaseConsole) {
		this.swingUserInterface = e.swingUserInterface
		this.swingUserInterfaceRR = ReflectionWrapper.attachToObject(swingUserInterface)
		this.launcherPanel = swingUserInterfaceRR["launcherPanel"]
	}
	
	@Suppress("unused", "UNUSED_PARAMETER")
	@ListensFor
	fun listenForUpdateEvent(e: UpdateAvailable, baseConsole: BaseConsole) = SwingUtilities.invokeLater {
		try {
			val updatePanel = UpdatePanel(launcherPanel, e)
			updatePanel.addThisCard()
		}catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
}
