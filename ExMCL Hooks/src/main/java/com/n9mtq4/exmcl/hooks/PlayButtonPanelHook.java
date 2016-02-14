package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.ui.BottomBarPanel;
import net.minecraft.launcher.ui.bottombar.PlayButtonPanel;

/**
 * Created by will on 7/27/15 at 6:23 PM.
 */
public final class PlayButtonPanelHook implements GenericListener {
	
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForSwingEvent(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
		if (e.getType() != PreDefinedSwingComponent.BOTTOM_BAR_PANEL) return;
		
		BottomBarPanel bottomBarPanel = (BottomBarPanel) e.getComponent();
		PlayButtonPanel playButtonPanel = ReflectionHelper.getObject("playButtonPanel", bottomBarPanel);
		
		baseConsole.pushEvent(new PreDefinedSwingHookEvent(playButtonPanel, PreDefinedSwingComponent.PLAY_BUTTON_PANEL, baseConsole));
		
	}
	
/*	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
		if (!e.getMessage().equalsIgnoreCase("bottombarpanel")) return;
		if (!(e.getObj() instanceof BottomBarPanel)) return;
		
		BottomBarPanel bottomBarPanel = (BottomBarPanel) e.getObj();
		
		PlayButtonPanel playButtonPanel = ReflectionHelper.getObject("playButtonPanel", bottomBarPanel);
		
		e.getInitiatingBaseConsole().push(playButtonPanel, "playbuttonpanel");
		
	}
	*/
}
