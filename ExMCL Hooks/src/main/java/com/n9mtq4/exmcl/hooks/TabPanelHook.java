package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.ui.LauncherPanel;
import net.minecraft.launcher.ui.tabs.LauncherTabPanel;

/**
 * Created by will on 7/27/15 at 5:56 PM.
 */
public final class TabPanelHook implements GenericListener {
	
	/**
	 * This listens for the minecraft launcher to be sent,
	 * and then sends a LauncherTabPanel back
	 * */
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForSwingEvent(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
		if (e.getType() != PreDefinedSwingComponent.LAUNCHER_PANEL) return;
		
		LauncherPanel launcherPanel = (LauncherPanel) e.getComponent();
		LauncherTabPanel launcherTabPanel = ReflectionHelper.getObject("tabPanel", launcherPanel);
		
		baseConsole.pushEvent(new PreDefinedSwingHookEvent(launcherTabPanel, PreDefinedSwingComponent.LAUNCHER_TAB_PANEL, baseConsole));
		
	}
/*	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
		if (!e.getMessage().equalsIgnoreCase("launcherpanel")) return;
		if (!(e.getObj() instanceof LauncherPanel)) return;
		
		LauncherPanel launcherPanel = (LauncherPanel) e.getObj();
		LauncherTabPanel launcherTabPanel = ReflectionHelper.getObject("tabPanel", launcherPanel);
		
		baseConsole.push(launcherTabPanel, "launchertabpanel");
		
	}*/
	
}
