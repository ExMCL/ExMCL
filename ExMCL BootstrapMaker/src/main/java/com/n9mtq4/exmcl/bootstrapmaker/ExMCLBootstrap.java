package com.n9mtq4.exmcl.bootstrapmaker;

import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.utils.JarLoader;
import net.minecraft.bootstrap.Bootstrap;
import net.minecraft.bootstrap.FatalBootstrapError;
import net.minecraft.launcher.Launcher;

import javax.swing.JFrame;
import java.io.File;
import java.lang.reflect.Constructor;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLClassLoader;

import static com.n9mtq4.reflection.ReflectionHelper.getObject;

/**
 * Created by will on 1/6/16 at 5:26 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class ExMCLBootstrap extends Bootstrap {
	
	public static final int EMULATED_BOOTSTRAP_VERSION = 30;
	
	private final BaseConsole baseConsole;
	private transient Launcher launcher;
	
	public ExMCLBootstrap(BaseConsole baseConsole, File workDir, Proxy proxy, PasswordAuthentication proxyAuth, String[] remainderArgs) {
		super(workDir, proxy, proxyAuth, remainderArgs);
		this.baseConsole = baseConsole;
	}
	
	@Override
	public void startLauncher(File launcherJar) {
		println("Starting launcher.");
		println("The Extendable Minecraft Launcher | http://exmcl.github.io");
		try {
			
			JarLoader.addFile(launcherJar);
			File workDir = getObject("workDir", this, Bootstrap.class);
			Proxy proxy = getObject("proxy", this, Bootstrap.class);
			PasswordAuthentication proxyAuth = getObject("proxyAuth", this, Bootstrap.class);
			String[] remainderArgs = getObject("remainderArgs", this, Bootstrap.class);
			
			Class aClass = new URLClassLoader(new URL[] { launcherJar.toURI().toURL() }).loadClass("net.minecraft.launcher.Launcher");
			Constructor constructor = aClass.getConstructor(JFrame.class, File.class, Proxy.class, PasswordAuthentication.class, String[].class, Integer.class);
//			last int is the bootstrap version. were spoofing it so we aren't prompted to update
			launcher = (Launcher) constructor.newInstance(this, workDir, proxy, proxyAuth, remainderArgs, EMULATED_BOOTSTRAP_VERSION);
//			launcher = ReflectionHelper.callConstructor(aClass, this, workDir, proxy, proxyAuth, remainderArgs, 30);
//			BootstrapEvent.fireMinecraftLauncherCreated(launcher);
//			send the launcher to the base console
			baseConsole.push(this, "jframe");
			baseConsole.push(launcher, "minecraftlauncher");
		}catch (Exception e) {
			throw new FatalBootstrapError("Unable to start: " + e);
		}
	}
	
}
