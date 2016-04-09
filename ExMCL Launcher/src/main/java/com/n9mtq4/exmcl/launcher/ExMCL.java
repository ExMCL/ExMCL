package com.n9mtq4.exmcl.launcher;

import javax.swing.JOptionPane;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by will on 2/12/16 at 11:37 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class ExMCL {
	
	public static void main(String[] args) {
		try {
			new ExMCL(args);
		}catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, Strings.UNKNOWN_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private final String[] args;
	private final File mcLauncherFile;
	private BootstrapLauncher bootstrapLauncher;
	
	private ExMCL(String[] args) throws IOException {
		this.args = args;
		this.mcLauncherFile = new File(Utils.getMinecraftJarLocation());
		initStartUp();
	}
	
	private void initStartUp() throws IOException {
		
		try {
			verifyBootstrap();
			addBootstrap();
			System.out.println("Successfully added the minecraft launcher to the class path");
		}catch (Exception e) {
			
			Object[] buttons = {"Auto", "Manual", "Quit"};
			int result = JOptionPane.showOptionDialog(null, Strings.NO_MINECRAFT, "Mnecraft.jar?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
			if (result == JOptionPane.YES_OPTION) {
//				download it
				URL url = new URL(Strings.DOWNLOAD_URL);
				ReadableByteChannel rbc = Channels.newChannel(url.openStream());
				FileOutputStream fos = new FileOutputStream(mcLauncherFile);
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				
				addBootstrap();
				
			}else if (result == JOptionPane.NO_OPTION) {
//				open minecraft.net
				openWebpage(new URL("http://minecraft.net"));
				System.exit(0);
			}else if (result == JOptionPane.CANCEL_OPTION) {
//				quit
				System.exit(1);
			}else {
//				Error
//				eh fuck it - just exit. no time for error messages
				System.exit(2);
			}
		}
		
		try {
			loadLibraries();
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, Strings.LIBRARY_ERROR, "Error", JOptionPane.ERROR_MESSAGE);
		}
		
		this.bootstrapLauncher = new BootstrapLauncher(args);
		
	}
	
	private void verifyBootstrap() throws RuntimeException {
		if (!mcLauncherFile.exists()) throw new RuntimeException("No Minecraft.jar");
	}
	
	private void addBootstrap() throws IOException {
		JarLoader.addFile(mcLauncherFile);
	}
	
	private void loadLibraries() throws IOException {
		File libDir = new File("libs/");
		File[] children = libDir.listFiles();
		if (children == null) return; // stupid null possibility
		for (File lib : children) {
//			make sure it is a library
			if (lib.isDirectory()) continue; // ignore directories
			if (!lib.getName().endsWith(".jar") &&
					!lib.getName().endsWith(".zip")) continue; // only load jars and zips
			if (lib.getName().startsWith(".")) continue; // only load if they aren't hidden
			JarLoader.addFile(lib);
			System.out.println("Added: " + lib.getAbsolutePath());
		}
	}
	
	/*
	* http://stackoverflow.com/a/10967469
	* */
	private static void openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void openWebpage(URL url) {
		try {
			openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
}
