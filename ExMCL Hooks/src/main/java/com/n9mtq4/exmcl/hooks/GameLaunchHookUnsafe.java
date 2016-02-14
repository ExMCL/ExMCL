package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.events.AdditionEvent;
import com.n9mtq4.logwindow.events.ObjectEvent;
import com.n9mtq4.logwindow.listener.AdditionListener;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.logwindow.listener.ObjectListener;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by will on 7/28/15 at 3:53 PM.<br>
 * This class does some advanced and sneaky things to let
 * us process the play button action BEFORE the minecraft launcher
 * can. Because swing randomizes the order of ActionListeners we 
 * can't just add us the first one in the list. We have to
 * remove all other listeners, while keeping a copy of the
 * listeners for us. When our ActionListener gets sent the event,
 * we send it to all the BaseConsole listener and then if none of them
 * set the canceled flag to true, then we will send it to the listeners
 * we removed from before.
 */
public final class GameLaunchHookUnsafe implements ActionListener, AdditionListener, GenericListener {
	
	private ActionListener[] listeners;
	private BaseConsole baseConsole;
	protected ObjectEvent sentObjectEvent;
	
	@Override
	public void onAddition(AdditionEvent e) {
		e.getBaseConsole().addListenerAttribute(new DefaultGameLaunchEventCapture(this));
	}
	
	/**
	 * Gets the play button and adds the GameLaunchHook onto it.
	 * */
	@SuppressWarnings("unused")
	@ListensFor(PreDefinedSwingHookEvent.class)
	public final void ListensForPlayButton(PreDefinedSwingHookEvent e, BaseConsole baseConsole) {
		
//		makes sure it is the playbutton
		if (e.getType() != PreDefinedSwingComponent.PLAY_BUTTON) return;
		
//		set the baseConsole
		this.baseConsole = baseConsole;
		
//		get the button and listeners
		JButton playButton = (JButton) e.getComponent();
		this.listeners = playButton.getActionListeners();
//		remove all the action listeners on the button
//		we will handle them
		for (ActionListener listener : listeners) {
			playButton.removeActionListener(listener);
		}
		
//		now add us as the only action listener
		playButton.addActionListener(this);
		
	}
/*	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
//		makes sure it is the playbutton
		if (!e.getMessage().equalsIgnoreCase("playbutton")) return;
		if (!(e.getObj() instanceof JButton)) return;
		
//		set the baseConsole
		this.baseConsole = e.getInitiatingBaseConsole();
		
//		get the button and listeners
		JButton playButton = (JButton) e.getObj();
		this.listeners = playButton.getActionListeners();
//		remove all the action listeners on the button.
//		we will handle them
		for (ActionListener listener : listeners) {
			playButton.removeActionListener(listener);
		}
		
//		now add us as the only action listener
		playButton.addActionListener(this);
		
	}*/
	
	/**
	 * ActionListener actionPerformed - push the ActionEvent to the baseConsole,
	 * then maybe send it to the listeners
	 * */
	@Override
	public final void actionPerformed(ActionEvent e) {
		
//		send the event to people listening with the BaseConsole first.
		baseConsole.push(e, "gamelaunch");
		
		if (sentObjectEvent == null) {
			baseConsole.println("ERROR. SOMETHING HAPPENED WITH RECAPTURING THE SENTOBJECTEVENT.\n" +
					"WE WILL CONTINUE WITH SENDING THE EVENT TO THE ACTIONLISTENERS");
		}
		
//		makes sure that we send the button to mojang's listeners only if it hasn't canceled,
//		but we also have to take into account the EventCapture failing.
		if ((sentObjectEvent == null) || /*we know ? != null*/ (!sentObjectEvent.isCanceled())) {
//			then we can let mojang's launcher handle it if necessary.
			for (ActionListener listener : listeners) {
				listener.actionPerformed(e);
			}
		}
		
	}
	
	/**
	 * This class captures the ObjectEvent and gives it to the parent.
	 * This is so we can test if a listener has canceled the game launch event
	 * */
	private final static class DefaultGameLaunchEventCapture implements ObjectListener {
		
		private final GameLaunchHookUnsafe parent;
		
		public DefaultGameLaunchEventCapture(GameLaunchHookUnsafe parent) {
			this.parent = parent;
			parent.sentObjectEvent = null;
		}
		
		@Override
		public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
			
			if (!e.getMessage().equalsIgnoreCase("gamelaunch")) return;
			if (!(e.getObj() instanceof ActionEvent)) return;
			
			parent.sentObjectEvent = e;
			
		}
		
	}
	
}
