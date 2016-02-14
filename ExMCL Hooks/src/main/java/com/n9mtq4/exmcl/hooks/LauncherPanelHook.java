package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.SwingUserInterface;
import net.minecraft.launcher.ui.LauncherPanel;

/**
 * Created by will on 7/27/15 at 6:04 PM.
 */
public final class LauncherPanelHook implements GenericListener {
	
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForSwingEvent(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
		if (e.getType() != PreDefinedSwingComponent.SWING_USER_INTERFACE) return;
		
		SwingUserInterface ui = (SwingUserInterface) e.getComponent();
		LauncherPanel launcherPanel = ReflectionHelper.getObject("launcherPanel", ui);
		
		baseConsole.pushEvent(new PreDefinedSwingHookEvent(launcherPanel, PreDefinedSwingComponent.LAUNCHER_PANEL, baseConsole));
		
	}
	
}
