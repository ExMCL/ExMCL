package com.n9mtq4.exmcl.api.tabs.ui

import com.n9mtq4.kotlin.extlib.pstAndUnit
import javax.swing.JTabbedPane
import javax.swing.UIManager

/**
 * Created by will on 2/13/16 at 12:35 AM.
 *
 * @author Will "n9Mtq4" Bresnahan
 */
class TabTab : JTabbedPane() {
	
	init {
		
		pstAndUnit {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		
		tabLayoutPolicy = JTabbedPane.WRAP_TAB_LAYOUT
		
	}
	
}
