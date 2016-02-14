package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.Async;
import com.n9mtq4.logwindow.events.ObjectEvent;
import com.n9mtq4.logwindow.listener.ObjectListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.SwingUserInterface;

import javax.swing.JFrame;
import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;

/**
 * Created by will on 7/28/15 at 2:18 PM.
 */
public final class SwingComponentHook implements ObjectListener {
	
	@Async
	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
		if (!e.getMessage().equalsIgnoreCase("swinguserinterface")) return;
		if (!(e.getObj() instanceof SwingUserInterface)) return;
		
		JFrame frame = ReflectionHelper.getObject("frame", (SwingUserInterface) e.getObj());
		
		ArrayList<Component> components = getAllComponents(frame);
		for (Component component : components) {
			e.getInitiatingBaseConsole().push(component, "allcomponents");
		}
		
		baseConsole.disableListenerAttribute(this);
		
	}
	
	public static ArrayList<Component> getAllComponents(final Container c) {
		Component[] comps = c.getComponents();
		ArrayList<Component> compList = new ArrayList<Component>();
		for (Component comp : comps) {
			compList.add(comp);
			if (comp instanceof Container)
				compList.addAll(getAllComponents((Container) comp));
		}
		return compList;
	}
	
}
