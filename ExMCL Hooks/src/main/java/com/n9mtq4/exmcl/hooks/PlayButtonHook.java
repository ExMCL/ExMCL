package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.ui.bottombar.PlayButtonPanel;

import javax.swing.JButton;

/**
 * Created by will on 7/27/15 at 6:15 PM.
 */
public class PlayButtonHook implements GenericListener {
	
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForSwingEvent(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
		if (e.getType() != PreDefinedSwingComponent.PLAY_BUTTON_PANEL) return;
		
		PlayButtonPanel playButtonPanel = (PlayButtonPanel) e.getComponent();
		JButton playButton = ReflectionHelper.getObject("playButton", playButtonPanel);
		
		baseConsole.pushEvent(new PreDefinedSwingHookEvent(playButton, PreDefinedSwingComponent.PLAY_BUTTON, baseConsole));
		
	}
	
}
