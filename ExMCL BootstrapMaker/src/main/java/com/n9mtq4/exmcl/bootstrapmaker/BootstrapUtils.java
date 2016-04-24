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
import com.n9mtq4.reflection.ReflectionHelper;
import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import net.minecraft.bootstrap.Bootstrap;
import net.minecraft.bootstrap.FatalBootstrapError;
import net.minecraft.bootstrap.Util;
import net.minecraft.hopper.HopperService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.List;

/**
 * Created by will on 1/6/16 at 5:22 PM.
 * 
 * NOTE: MUCH OF THIS CODE WAS WRITTEN BY MOJANG.
 * IGNORE THE AUTO-GENERATED COPYRIGHT ABOVE
 * 
 * @author Will "n9Mtq4" Bresnahan
 */
public final class BootstrapUtils {
	
	public static Bootstrap makeABootstrap(String[] args, BaseConsole baseConsole) throws IOException {
		
		System.setProperty("java.net.preferIPv4Stack", "true");
		
		OptionParser optionParser = new OptionParser();
		optionParser.allowsUnrecognizedOptions();
		
		optionParser.accepts("help", "Show help").forHelp();
		optionParser.accepts("force", "Force updating");
		
		OptionSpec proxyHostOption = optionParser.accepts("proxyHost", "Optional").withRequiredArg();
		OptionSpec proxyPortOption = optionParser.accepts("proxyPort", "Optional").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
		OptionSpec proxyUserOption = optionParser.accepts("proxyUser", "Optional").withRequiredArg();
		OptionSpec proxyPassOption = optionParser.accepts("proxyPass", "Optional").withRequiredArg();
		OptionSpec workingDirectoryOption = optionParser.accepts("workDir", "Optional").withRequiredArg().ofType(File.class).defaultsTo(Util.getWorkingDirectory(), new File[0]);
		OptionSpec nonOptions = optionParser.nonOptions();
		OptionSet optionSet;
		try {
			optionSet = optionParser.parse(args);
		}catch (OptionException e) {
			optionParser.printHelpOn(System.out);
			System.out.println("(to pass in arguments to minecraft directly use: '--' followed by your arguments");
			return null;
		}
		
		if (optionSet.has("help")) {
			optionParser.printHelpOn(System.out);
			return null;
		}
		
		String hostName = (String) optionSet.valueOf(proxyHostOption);
		Proxy proxy = Proxy.NO_PROXY;
		if (hostName != null) {
			try {
				proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(hostName, ((Integer) optionSet.valueOf(proxyPortOption)).intValue()));
			}catch (Exception ignored) {
			}
		}
		String proxyUser = (String) optionSet.valueOf(proxyUserOption);
		String proxyPass = (String) optionSet.valueOf(proxyPassOption);
		PasswordAuthentication passwordAuthentication = null;
		if(!proxy.equals(Proxy.NO_PROXY) && stringHasValue(proxyUser) && stringHasValue(proxyPass)) {
			passwordAuthentication = new PasswordAuthentication(proxyUser, proxyPass.toCharArray());
			final PasswordAuthentication finalPasswordAuthentication = passwordAuthentication;
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return finalPasswordAuthentication;
				}
			});
		}
		
		
		File workingDirectory = (File) optionSet.valueOf(workingDirectoryOption);
		if ((workingDirectory.exists()) && (!workingDirectory.isDirectory()))
			throw new FatalBootstrapError("Invalid working directory: " + workingDirectory);
		if ((!workingDirectory.exists()) &&
				(!workingDirectory.mkdirs())) {
			throw new FatalBootstrapError("Unable to create directory: " + workingDirectory);
		}
		
		List strings = optionSet.valuesOf(nonOptions);
		String[] remainderArgs = (String[]) strings.toArray(new String[strings.size()]);
		
		boolean force = optionSet.has("force");
		
		ExMCLBootstrap frame = new ExMCLBootstrap(baseConsole, workingDirectory, proxy, passwordAuthentication, remainderArgs);
		try {
			frame.execute(force);
		}catch (Throwable t) {
			ByteArrayOutputStream stracktrace = new ByteArrayOutputStream();
			t.printStackTrace(new PrintStream(stracktrace));
			
			StringBuilder report = new StringBuilder();
			report.append(stracktrace).append("\n\n-- Head --\nStacktrace:\n").append(stracktrace).append("\n\n")
					.append((StringBuilder) ReflectionHelper.getObject("outputBuffer", frame, Bootstrap.class));
			report.append("\tMinecraft.Bootstrap Version: 5");
			try {
				HopperService.submitReport(proxy, report.toString(), "Minecraft.Bootstrap", "5");
			}catch (Throwable ignored) {
			}
			frame.println("FATAL ERROR: " + stracktrace.toString());
			frame.println("\nPlease fix the error and restart.");
		}
		
		return frame;
		
	}
	
	private static boolean stringHasValue(String string) {
		return string != null && !string.isEmpty();
	}
	
}
