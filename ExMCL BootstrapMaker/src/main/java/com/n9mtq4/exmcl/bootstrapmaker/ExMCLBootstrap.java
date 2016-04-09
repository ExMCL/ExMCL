package com.n9mtq4.exmcl.bootstrapmaker;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.utils.JarLoader;
import com.n9mtq4.reflection.ReflectionHelper;
import com.n9mtq4.reflection.ReflectionWrapper;
import net.minecraft.bootstrap.Bootstrap;
import net.minecraft.bootstrap.FatalBootstrapError;
import net.minecraft.launcher.Launcher;

import javax.swing.JFrame;
import java.io.File;
import java.net.PasswordAuthentication;
import java.net.Proxy;

/**
 * Created by will on 1/6/16 at 5:26 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class ExMCLBootstrap extends Bootstrap {
	
	public static final int EMULATED_BOOTSTRAP_VERSION = 5;
	
	private final BaseConsole baseConsole;
	
	public ExMCLBootstrap(BaseConsole baseConsole, File workDir, Proxy proxy, PasswordAuthentication proxyAuth, String[] remainderArgs) {
		super(workDir, proxy, proxyAuth, remainderArgs);
		this.baseConsole = baseConsole;
	}
	
	@Override
	public void startLauncher(File launcherJar) {
		println("Starting launcher.");
		println("The Extendable Minecraft Launcher | http://exmcl.github.io");
		try {
			
//			Load the jar file
			JarLoader.addFile(launcherJar);
			
//			get private fields
//			TODO: find some way to get the generic inference to work
			final ReflectionWrapper thiz = ReflectionWrapper.attachToObject(this);
			File workDir = (File) thiz.get("workDir");
			Proxy proxy = (Proxy) thiz.get("proxy");
			PasswordAuthentication proxyAuth = (PasswordAuthentication) thiz.get("proxyAuth");
			String[] remainderArgs = (String[]) thiz.get("remainderArgs");
			
//			create the launcher
			final Launcher launcher = ReflectionHelper.callConstructor(Launcher.class, 
					new Class[] {JFrame.class, File.class, Proxy.class, PasswordAuthentication.class, String[].class, Integer.class}, 
					new Object[] {this, workDir, proxy, proxyAuth, remainderArgs, EMULATED_BOOTSTRAP_VERSION});
			
//			send the launcher to the base console
			baseConsole.push(this, "jframe");
			baseConsole.push(launcher, "minecraftlauncher");
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new FatalBootstrapError("Unable to start: " + e);
		}
	}
	
}
