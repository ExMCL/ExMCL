/*
 * MIT License
 *
 * Copyright (c) 2016 Will (n9Mtq4) Bresnahan
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
	
}
