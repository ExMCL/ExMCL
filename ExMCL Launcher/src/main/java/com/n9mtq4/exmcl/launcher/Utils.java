package com.n9mtq4.exmcl.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by will on 1/6/16 at 4:46 PM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
public final class Utils {
	
	public static String getMinecraftJarLocation() throws IOException {
		InputStream in = Utils.class.getResourceAsStream("/mclauncher.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String firstLine = reader.readLine();
		reader.close();
		in.close();
		return firstLine;
	}
	
	public static <E> boolean arrayContains(E[] array, E obj) {
		for (E o : array) {
			if (o.equals(obj)) return true;
		}
		return false;
	}
	
}
