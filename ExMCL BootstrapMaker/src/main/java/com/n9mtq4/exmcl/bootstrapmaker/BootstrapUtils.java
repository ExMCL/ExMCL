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
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;

/**
 * Created by will on 1/6/16 at 5:22 PM.
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
			frame.println(new StringBuilder().append("FATAL ERROR: ").append(stracktrace.toString()).toString());
			frame.println("\nPlease fix the error and restart.");
		}
		
		return frame;
		
	}
	
}
