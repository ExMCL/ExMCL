package com.n9mtq4.exmcl.launcher;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.managers.StdOutRedirection;
import com.n9mtq4.logwindow.modules.ModuleExit;
import com.n9mtq4.logwindow.modules.ModuleJarLoader;
import com.n9mtq4.logwindow.modules.ModuleListener;
import com.n9mtq4.logwindow.ui.uis.GuiJFrameLightWeight;

/**
 * Created by will on 1/6/16 at 5:02 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class BootstrapLauncher {
	
	private final String[] args;
	private final BaseConsole baseConsole;
	
	public BootstrapLauncher(String[] args) {
		
		this.args = args;
		this.baseConsole = new BaseConsole();
		
		if (Utils.arrayContains(args, "DEBUGBUILD")) {
			baseConsole.addConsoleUi(new GuiJFrameLightWeight(baseConsole));
			StdOutRedirection.addToBaseConsole(baseConsole, true);
			baseConsole.addListenerAttribute(new ModuleListener());
			baseConsole.addListenerAttribute(new ModuleJarLoader());
			baseConsole.addListenerAttribute(new ModuleExit());
		}
		
//		baseConsole.loadPlugins();
		baseConsole.loadPlugins("pre-plugins");
		
		System.out.println("Success - calling for a bootstrap");
		
//		push the args
		baseConsole.push(args, "args");
//		request the bootstrap creation
		baseConsole.push("bootstrap", "request");
//		our job here is done
		
	}
	
	@Deprecated
	public String[] getArgs() {
		return args;
	}
	
	@Deprecated
	public BaseConsole getBaseConsole() {
		return baseConsole;
	}
	
}
