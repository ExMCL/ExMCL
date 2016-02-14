package com.n9mtq4.exmcl.hooks;

import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingComponent;
import com.n9mtq4.exmcl.api.hooks.events.PreDefinedSwingHookEvent;
import com.n9mtq4.exmcl.hooks.override.HookedSwingUserInterface;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.events.ObjectEvent;
import com.n9mtq4.logwindow.listener.ObjectListener;
import com.n9mtq4.reflection.ReflectionHelper;
import net.minecraft.launcher.Launcher;
import net.minecraft.launcher.SwingUserInterface;

/**
 * Created by will on 7/27/15 at 5:58 PM.
 */
public final class SwingUserInterfaceHook implements ObjectListener {
	
	private static final boolean HOOK_INTERFACE = true;
	
	@Override
	public final void objectReceived(ObjectEvent e, BaseConsole baseConsole) {
		
		if (!e.getMessage().equalsIgnoreCase("minecraftlauncher")) return;
		if (!(e.getObj() instanceof Launcher)) return;
		
		SwingUserInterface ui = ReflectionHelper.getObject("userInterface", e.getObj());
		
		if (HOOK_INTERFACE) {
			try {
				Launcher launcher = (Launcher) e.getObj();
//			HookedSwingUserInterface hookedSwingUserInterface = new HookedSwingUserInterface(launcher, ((SwingUserInterface) launcher.getUserInterface()).getFrame(), baseConsole);
				HookedSwingUserInterface hookedSwingUserInterface = new HookedSwingUserInterface(ui, baseConsole);
				ReflectionHelper.setObject(hookedSwingUserInterface, "userInterface", launcher);
			}catch (Exception e1) {
				e1.printStackTrace();
				baseConsole.printStackTrace(e1);
				System.err.println("SwingUserInterface Hook not working...");
				baseConsole.println("SwingUserInterface Hook not working...");
			}
		}
		
//		e.getInitiatingBaseConsole().push(ui, "swinguserinterface");
		e.getInitiatingBaseConsole().pushEvent(new PreDefinedSwingHookEvent(ui, PreDefinedSwingComponent.SWING_USER_INTERFACE, baseConsole));
		
	}
	
}
