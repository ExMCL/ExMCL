package com.n9mtq4.exmcl.hooks.override;

import com.mojang.launcher.events.GameOutputLogProcessor;
import com.mojang.launcher.updater.DownloadProgress;
import com.mojang.launcher.versions.CompleteVersion;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGameLaunchFailure;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGetFrame;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIGetTitle;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIHideDownloadProgress;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIInitializeFrame;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUISetDownloadProgress;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUISetVisible;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowCrashReport;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowGameOutputTabEvent;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowLoginPrompt;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowLoginPromptCallback;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowOutdatedNotice;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShutdownLauncher;
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIUpdatePlayState;
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
 * Wont necessarily be forwards compatible, so this is dangerous.
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
//		baseConsole.push(new Object[]{this, minecraftLauncher, callback}, "swinguserinterface showLoginPrompt");
		baseConsole.pushEvent(new SUIShowLoginPromptCallback(baseConsole, this, minecraftLauncher, callback));
	}
	
	@Override
	public void initializeFrame() {
		super.initializeFrame();
//		baseConsole.push(new Object[] {this}, "swinguserinterface initializeFrame");
		baseConsole.pushEvent(new SUIInitializeFrame(baseConsole, this));
	}
	
	@Override
	public void showOutdatedNotice() {
		super.showOutdatedNotice();
//		baseConsole.push(new Object[] {this}, "swinguserinterface showOutdatedNotice");
		baseConsole.pushEvent(new SUIShowOutdatedNotice(baseConsole, this));
	}
	
	@Override
	public void showLoginPrompt() {
		super.showLoginPrompt();
//		baseConsole.push(new Object[] {this}, "swinguserinterface showLoginPrompt");
		baseConsole.pushEvent(new SUIShowLoginPrompt(baseConsole, this));
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
//		baseConsole.push(new Object[] {this, visible}, "swinguserinterface setVisible");
		baseConsole.pushEvent(new SUISetVisible(baseConsole, this, visible));
	}
	
	@Override
	public void shutdownLauncher() {
		super.shutdownLauncher();
//		baseConsole.push(new Object[] {this}, "swinguserinterface shutdownLauncher");
		baseConsole.pushEvent(new SUIShutdownLauncher(baseConsole, this));
	}
	
	@Override
	public void setDownloadProgress(DownloadProgress downloadProgress) {
		super.setDownloadProgress(downloadProgress);
//		baseConsole.push(new Object[] {this, downloadProgress}, "swinguserinterface setDownloadProgress");
		baseConsole.pushEvent(new SUISetDownloadProgress(baseConsole, this, downloadProgress));
	}
	
	@Override
	public void hideDownloadProgress() {
		super.hideDownloadProgress();
//		baseConsole.push(new Object[] {this}, "swinguserinterface hideDownloadProgress");
		baseConsole.pushEvent(new SUIHideDownloadProgress(baseConsole, this));
	}
	
	@Override
	public void showCrashReport(CompleteVersion version, File crashReportFile, String crashReport) {
		super.showCrashReport(version, crashReportFile, crashReport);
//		baseConsole.push(new Object[]{this, version, crashReportFile, crashReport}, "swinguserinterface showCrashReport");
		baseConsole.pushEvent(new SUIShowCrashReport(baseConsole, this, version, crashReportFile, crashReport));
	}
	
	@Override
	public void gameLaunchFailure(String reason) {
		super.gameLaunchFailure(reason);
//		baseConsole.push(reason, "swinguserinterface gameLaunchFailure");
		baseConsole.pushEvent(new SUIGameLaunchFailure(baseConsole, this, reason));
	}
	
	@Override
	public void updatePlayState() {
		super.updatePlayState();
//		baseConsole.push(new Object[] {this}, "swinguserinterface updatePlayState");
		baseConsole.pushEvent(new SUIUpdatePlayState(baseConsole, this));
	}
	
	@Override
	public GameOutputLogProcessor showGameOutputTab(MinecraftGameRunner gameRunner) {
		GameOutputLogProcessor gameOutputLogProcessor = super.showGameOutputTab(gameRunner);
//		baseConsole.push(new Object[] {this, gameRunner}, "swinguserinterface showGameOutputTab");
		baseConsole.pushEvent(new SUIShowGameOutputTabEvent(baseConsole, this, gameRunner));
		return gameOutputLogProcessor;
	}
	
	@Override
	public String getTitle() {
		String title = super.getTitle();
//		baseConsole.push(new Object[] {this}, "swinguserinterface getTitle");
		baseConsole.pushEvent(new SUIGetTitle(baseConsole, this));
		return title;
	}
	
	@Override
	public JFrame getFrame() {
		JFrame frame =  super.getFrame();
//		baseConsole.push(new Object[] {this}, "swinguserinterface getFrame");
		baseConsole.pushEvent(new SUIGetFrame(baseConsole, this));
		return frame;
	}
	
}
