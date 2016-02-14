package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.ui.BottomBarPanel;
import net.minecraft.launcher.ui.LauncherPanel;

/**
 * Created by will on 7/28/15 at 2:25 PM.
 */
public final class BottomBarPanelHook implements GenericListener {
	
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForSwingEvent(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
		if (e.getType() != PreDefinedSwingComponent.LAUNCHER_PANEL) return;
		
		LauncherPanel launcherPanel = (LauncherPanel) e.getComponent();
		BottomBarPanel bottomBarPanel = ReflectionHelper.getObject("bottomBar", launcherPanel);
		
		baseConsole.pushEvent(new PreDefinedSwingHookEvent(bottomBarPanel, PreDefinedSwingComponent.BOTTOM_BAR_PANEL, baseConsole));
		
	}
	
}
