package com.n9mtq4.exmcl.launcher;

import javax.swing.JOptionPane;
import java.io.File;
import java.io.IOException;

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
	
	private void initStartUp() {
		
		try {
			verifyBootstrap();
			addBootstrap();
			System.out.println("Successfully added the minecraft launcher to the class path");
		}catch (RuntimeException e) {
			JOptionPane.showMessageDialog(null, Strings.NO_MINECRAFT, "Error", JOptionPane.ERROR_MESSAGE);
		}catch (IOException e) {
			JOptionPane.showMessageDialog(null, Strings.NO_MINECRAFT, "Error", JOptionPane.ERROR_MESSAGE);
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
	* getters that don't really serve a purpose
	* */
	
	@Deprecated
	public String[] getArgs() {
		return args;
	}
	
	@Deprecated
	public File getMcLauncherFile() {
		return mcLauncherFile;
	}
	
	@Deprecated
	public BootstrapLauncher getBootstrapLauncher() {
		return bootstrapLauncher;
	}
	
}
