package com.n9mtq4.exmcl.hooks.override;

import com.mojang.launcher.events.GameOutputLogProcessor;
import com.mojang.launcher.updater.DownloadProgress;
import com.mojang.launcher.versions.CompleteVersion;
import com.n9mtq4.logwindow.BaseConsole;
import com.n9mtq4.reflection.ReflectionHelper;
import com.n9mtq4.reflection.ReflectionWrapper;
import net.minecraft.launcher.Launcher;
import net.minecraft.launcher.SwingUserInterface;
import net.minecraft.launcher.game.MinecraftGameRunner;
import net.minecraft.launcher.ui.popups.login.LogInPopup;

import javax.swing.JFrame;
import java.io.File;

/**
 * Created by will on 8/31/15 at 5:06 PM.<br>
 * A class for better hooks into the SwingUserInterface
 * Wont be forwards compatible and not necessary, so NYI.
 */
public class HookedSwingUserInterface extends SwingUserInterface {
	
	private BaseConsole baseConsole;
	private ReflectionWrapper thisWrapper;
	private ReflectionWrapper parentWrapper;
	
	public HookedSwingUserInterface(SwingUserInterface parent, BaseConsole baseConsole) {
		super((Launcher) ReflectionHelper.getObject("minecraftLauncher", parent), parent.getFrame());
		this.baseConsole = baseConsole;
		thisWrapper = ReflectionWrapper.attachToObject(this);
		parentWrapper = ReflectionWrapper.attachToObject(parent);
		thisWrapper.setField("launcherPanel", parentWrapper.getField("launcherPanel"));
	}
	
	@Override
	public void showLoginPrompt(Launcher minecraftLauncher, LogInPopup.Callback callback) {
		super.showLoginPrompt(minecraftLauncher, callback);
		baseConsole.push(new Object[]{this, minecraftLauncher, callback}, "swinguserinterface showLoginPrompt");
	}
	
	@Override
	public void initializeFrame() {
		super.initializeFrame();
		baseConsole.push(new Object[] {this}, "swinguserinterface initializeFrame");
	}
	
	@Override
	public void showOutdatedNotice() {
		super.showOutdatedNotice();
		baseConsole.push(new Object[] {this}, "swinguserinterface showOutdatedNotice");
	}
	
	@Override
	public void showLoginPrompt() {
		super.showLoginPrompt();
		baseConsole.push(new Object[] {this}, "swinguserinterface showLoginPrompt");
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		baseConsole.push(new Object[] {this, visible}, "swinguserinterface setVisible");
	}
	
	@Override
	public void shutdownLauncher() {
		super.shutdownLauncher();
		baseConsole.push(new Object[] {this}, "swinguserinterface shutdownLauncher");
	}
	
	@Override
	public void setDownloadProgress(DownloadProgress downloadProgress) {
		super.setDownloadProgress(downloadProgress);
		baseConsole.push(new Object[] {this, downloadProgress}, "swinguserinterface setDownloadProgress");
	}
	
	@Override
	public void hideDownloadProgress() {
		super.hideDownloadProgress();
		baseConsole.push(new Object[] {this}, "swinguserinterface hideDownloadProgress");
	}
	
	@Override
	public void showCrashReport(CompleteVersion version, File crashReportFile, String crashReport) {
		super.showCrashReport(version, crashReportFile, crashReport);
		baseConsole.push(new Object[]{this, version, crashReportFile, crashReport}, "swinguserinterface showCrashReport");
	}
	
	@Override
	public void gameLaunchFailure(String reason) {
		super.gameLaunchFailure(reason);
		baseConsole.push(reason, "swinguserinterface gameLaunchFailure");
	}
	
	@Override
	public void updatePlayState() {
		super.updatePlayState();
		baseConsole.push(new Object[] {this}, "swinguserinterface updatePlayState");
	}
	
	@Override
	public GameOutputLogProcessor showGameOutputTab(MinecraftGameRunner gameRunner) {
		GameOutputLogProcessor gameOutputLogProcessor = super.showGameOutputTab(gameRunner);
		baseConsole.push(new Object[] {this, gameRunner}, "swinguserinterface showGameOutputTab");
		return gameOutputLogProcessor;
	}
	
	@Override
	public String getTitle() {
		String title = super.getTitle();
		baseConsole.push(new Object[] {this}, "swinguserinterface getTitle");
		return title;
	}
	
	@Override
	public JFrame getFrame() {
		JFrame frame =  super.getFrame();
		baseConsole.push(new Object[] {this}, "swinguserinterface getFrame");
		return frame;
	}
	
}
