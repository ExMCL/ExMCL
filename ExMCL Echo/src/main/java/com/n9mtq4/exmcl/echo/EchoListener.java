package com.n9mtq4.exmcl.echo;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.events.EnableEvent;
import com.n9mtq4.logwindow.events.GenericEvent;
import com.n9mtq4.logwindow.events.ObjectEvent;
import com.n9mtq4.logwindow.listener.EnableListener;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.logwindow.listener.ListenerContainer;
import com.n9mtq4.logwindow.listener.ObjectListener;
import com.n9mtq4.reflection.ReflectionHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by will on 3/31/15.<br>
 * This listener is for debugging purposes.
 * It takes any String or Object that is pushed to the
 * BaseConsole and prints it out so I can see it.
 */
public class EchoListener implements EnableListener, ObjectListener, GenericListener {
	
	/**
	 * It is important that Echoer is the first listener, so use ReflectionHelper to 
	 * accomplish that goal.
	 * */
	@Override
	public final void onEnable(EnableEvent e) {
//		gets the listeners
		ArrayList<ListenerContainer> l = ReflectionHelper.getObject("listenerContainers", e.getBaseConsole());
//		gets the listener container that is handling the methods for this listener
		ListenerContainer container = e.getBaseConsole().getContainerFromAttribute(this);
//		removes the container from the list
		l.remove(container);
//		re-adds it first
		l.add(0, container);
		e.getBaseConsole().println("Used hack to make Echoer the first listener");
	}
	
	/**
	 * Prints the object sent to the BaseConsole
	 * */
	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
		if (e.getObj() == null) {
			baseConsole.println("Object: " + e.getMessage() + " | (null)");
			return;
		}
		
		if (e.getObj() instanceof Object[]) {
			baseConsole.println("Object: " + e.getMessage() + " | (" + Arrays.toString((Object[]) e.getObj()) + ")");
		}else if (e.getObj() instanceof Collection) {
			baseConsole.println("Object: " + e.getMessage() + " | (" + Arrays.toString(((Collection) e.getObj()).toArray()) + ")");
		}else {
			baseConsole.println("Object: " + e.getMessage() + " | (" + e.getObj().toString() + ")");
		}
		
	}
	
	@ListensFor(GenericEvent.class)
	public final void listenForAllGenericEvents(GenericEvent event, BaseConsole baseConsole) {
		baseConsole.println(event.toString());
	}
	
}
