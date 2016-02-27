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
import com.n9mtq4.exmcl.api.hooks.events.swinguserinterface.SUIShowGameOutputTab;
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
	
	private final BaseConsole baseConsole;
	private final ReflectionWrapper thisWrapper;
	private final ReflectionWrapper parentWrapper;
	
	public HookedSwingUserInterface(SwingUserInterface parent, BaseConsole baseConsole) {
		super((Launcher) ReflectionHelper.getObject("minecraftLauncher", parent), parent.getFrame());
		this.baseConsole = baseConsole;
		thisWrapper = ReflectionWrapper.attachToObject(this);
		parentWrapper = ReflectionWrapper.attachToObject(parent);
		thisWrapper.setField("launcherPanel", parentWrapper.getField("launcherPanel"));
	}
	
	@Override
	public void showLoginPrompt(Launcher minecraftLauncher, LogInPopup.Callback callback) {
		SUIShowLoginPromptCallback event = new SUIShowLoginPromptCallback(baseConsole, this, minecraftLauncher, callback);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.showLoginPrompt(minecraftLauncher, callback);
	}
	
	@Override
	public void initializeFrame() {
		SUIInitializeFrame event = new SUIInitializeFrame(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.initializeFrame();
	}
	
	@Override
	public void showOutdatedNotice() {
		SUIShowOutdatedNotice event = new SUIShowOutdatedNotice(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.showOutdatedNotice();
	}
	
	@Override
	public void showLoginPrompt() {
		SUIShowLoginPrompt event = new SUIShowLoginPrompt(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.showLoginPrompt();
	}
	
	@Override
	public void setVisible(boolean visible) {
		SUISetVisible event = new SUISetVisible(baseConsole, this, visible);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.setVisible(visible);
	}
	
	@Override
	public void shutdownLauncher() {
		SUIShutdownLauncher event = new SUIShutdownLauncher(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.shutdownLauncher();
	}
	
	@Override
	public void setDownloadProgress(DownloadProgress downloadProgress) {
		SUISetDownloadProgress event = new SUISetDownloadProgress(baseConsole, this, downloadProgress);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.setDownloadProgress(downloadProgress);
	}
	
	@Override
	public void hideDownloadProgress() {
		SUIHideDownloadProgress event = new SUIHideDownloadProgress(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.hideDownloadProgress();
	}
	
	@Override
	public void showCrashReport(CompleteVersion version, File crashReportFile, String crashReport) {
		SUIShowCrashReport event = new SUIShowCrashReport(baseConsole, this, version, crashReportFile, crashReport);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.showCrashReport(version, crashReportFile, crashReport);
	}
	
	@Override
	public void gameLaunchFailure(String reason) {
		SUIGameLaunchFailure event = new SUIGameLaunchFailure(baseConsole, this, reason);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.gameLaunchFailure(reason);
	}
	
	@Override
	public void updatePlayState() {
		SUIUpdatePlayState event = new SUIUpdatePlayState(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) super.updatePlayState();
	}
	
	@Override
	public GameOutputLogProcessor showGameOutputTab(MinecraftGameRunner gameRunner) {
		SUIShowGameOutputTab event = new SUIShowGameOutputTab(baseConsole, this, gameRunner);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) return super.showGameOutputTab(gameRunner);
		else return event.getGameOutputLogProcessor();
	}
	
	@Override
	public String getTitle() {
		SUIGetTitle event = new SUIGetTitle(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) return super.getTitle();
		else return event.getTitle();
	}
	
	@Override
	public JFrame getFrame() {
		SUIGetFrame event = new SUIGetFrame(baseConsole, this);
		baseConsole.pushEvent(event);
		if (!event.getStopMojangsCode()) return super.getFrame();
		else return event.getFrame();
	}
	
}
