package com.n9mtq4.exmcl.cleaner;

import com.n9mtq4.exmcl.api.cleaner.AddToDelete;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.logwindow.annotation.ListensFor;
import com.n9mtq4.logwindow.events.RemovalEvent;
import com.n9mtq4.logwindow.listener.GenericListener;
import com.n9mtq4.logwindow.listener.RemovalListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by will on 2/14/16 at 6:39 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
@Deprecated
public final class CleanerOld implements RemovalListener, GenericListener {
	
//	TODO: port to kotlin
	
	private final List<File> toDelete = new ArrayList<File>();
	
	@SuppressWarnings("unused")
	@ListensFor(AddToDelete.class)
	public final void listenForAddToDelete(AddToDelete event, BaseConsole baseConsole) {
		
		baseConsole.println("Added " + event.getFile().getAbsolutePath() + " to be deleted");
		addToDelete(event.getFile());
		
	}
	
	//	TODO: should this be Async?
	@Override
	public final void onRemoval(RemovalEvent e) {
		
		clean(e.getBaseConsole());
		
	}
	
	private void addToDelete(File file) {
		
		if (file == null) return;
		if (!toDelete.contains(file)) toDelete.add(file);
		
	}
	
	/**
	 * Removes every file in toDelete
	 * */
	private void clean(BaseConsole baseConsole) {
		
		deleteFiles(buildToDeleteTree());
		
	}
	
	private ArrayList<File> buildToDeleteTree() {
//		change directories into children files
		ArrayList<File> files = new ArrayList<File>();
		for (File file : toDelete) {
			if (file != null) {
				files.add(file);
				if (file.isDirectory()) {
					files.addAll(buildFileTree(file));
				}
			}
		}
//		directories have to be last
		files.sort(new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				if (o1.isDirectory()) return 1;
				if (o2.isDirectory()) return -1;
				return 0;
			}
		});
		return files;
	}
	
	private void deleteFiles(ArrayList<File> files) {
		
		for (File file : files) {
			
			try {
				
				if (file == null) continue;
				if (!file.exists()) continue;
				
				boolean success = file.delete();
				System.out.println((success ? "Deleted" : "Failed to delete") + " the file: " + file.getAbsolutePath());
				
				if (!success) {
					file.deleteOnExit();
					System.out.println("Set the file to delete when the program exits.");
				}
				
			}catch (Exception e) {
//				just in case
//				when dealing loops, it is good to prevent one bad apple from
//				spoiling the rest
				e.printStackTrace();
			}
		}
		
	}
	
	private static ArrayList<File> buildFileTree(File file) {
		
		ArrayList<File> files = new ArrayList<File>();
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			if (children == null) return files;
			for (File child : children) {
				files.addAll(buildFileTree(child));
			}
		}else {
			files.add(file);
		}
		return files;
		
	}
	
}
