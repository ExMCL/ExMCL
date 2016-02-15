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
	
	@Override
	public void objectReceived(ObjectEvent objectEvent, BaseConsole baseConsole) {
		
		if (!objectEvent.getMessage().equals("request")) return;
		if (!(objectEvent.getObj() instanceof String)) return;
		if (!objectEvent.getObj().equals("bootstrap")) return;
		
//		we should make a new Bootstrap
		try {
			BootstrapUtils.makeABootstrap(new String[] {}, baseConsole); // TODO: add arg support
			baseConsole.loadPlugins();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
