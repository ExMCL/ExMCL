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

package com.n9mtq4.exmcl.bootstrapmaker;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.events.ObjectEvent;
import com.n9mtq4.logwindow.listener.ObjectListener;

import java.io.IOException;

/**
 * Created by will on 1/6/16 at 5:18 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class BootstrapRequestListener implements ObjectListener {
	
	private String[] args = new String[] {};
	
	@Override
	public void objectReceived(ObjectEvent objectEvent, BaseConsole baseConsole) {
		
		tryForArgs(objectEvent, baseConsole);
		tryForBootstrap(objectEvent, baseConsole);
		
	}
	
	private void tryForArgs(ObjectEvent objectEvent, BaseConsole baseConsole) {
		
		if (!objectEvent.getMessage().equals("args")) return;
		if (!(objectEvent.getObj() instanceof String[])) return;
		
		args = (String[]) objectEvent.getObj();
		
	}
	
	private void tryForBootstrap(ObjectEvent objectEvent, BaseConsole baseConsole) {
		
		if (!objectEvent.getMessage().equals("request")) return;
		if (!(objectEvent.getObj() instanceof String)) return;
		if (!objectEvent.getObj().equals("bootstrap")) return;
		
//		we should make a new Bootstrap
		try {
			BootstrapUtils.makeABootstrap(args, baseConsole);
			baseConsole.loadPlugins();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
