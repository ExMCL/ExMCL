package com.n9mtq4.exmcl.api.hooks.events;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.events.DefaultGenericEvent;

/**
 * Created by will on 2/13/16 at 1:03 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public class PreDefinedSwingHookEvent extends DefaultGenericEvent {
	
	private final Object component;
	private final PreDefinedSwingComponent type;
	
	public PreDefinedSwingHookEvent(Object component, PreDefinedSwingComponent type, BaseConsole initiatingBaseConsole) {
		super(initiatingBaseConsole);
		this.component = component;
		this.type = type;
	}
	
	public Object getComponent() {
		return component;
	}
	
	public PreDefinedSwingComponent getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return getClass().getName() + " carrying type: " + type.name() + " of value " + component.toString();
	}
	
}
