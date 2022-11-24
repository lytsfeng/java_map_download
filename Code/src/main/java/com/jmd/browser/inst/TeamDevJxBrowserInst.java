package com.jmd.browser.inst;

import javax.swing.SwingUtilities;

import com.jmd.browser.inst.base.AbstractBrowser;
import com.jmd.callback.CommonAsyncCallback;
import com.jmd.callback.JavaScriptExecutionCallback;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserException;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;

public class TeamDevJxBrowserInst implements AbstractBrowser {

	private static volatile TeamDevJxBrowserInst instance;

	private static String BROWSER_CONTEXT = System.getProperty("user.dir") + "/context/jxbrowser/data";
	private Browser browser = null;
	private BrowserView container;
	private int i = 0;

	@Override
	public void create(String url, CommonAsyncCallback callback) {
		try {
			BrowserContext contex = new BrowserContext(new BrowserContextParams(BROWSER_CONTEXT));
			this.browser = new Browser(contex);
			this.browser.loadURL(url);
			this.browser.addConsoleListener((e) -> {
				System.out.println(e.getMessage() + " " + e.getLineNumber() + " " + e.getSource());
			});
		} catch (BrowserException e) {
			i = i + 1;
			BROWSER_CONTEXT = BROWSER_CONTEXT + "_" + i;
			this.create(url, callback);
		}
		container = new BrowserView(browser);
		callback.execute();
	}

	@Override
	public Browser getBrowser() {
		return this.browser;
	}

	@Override
	public BrowserView getBrowserContainer() {
		return this.container;
	}

	@Override
	public Object getDevTools() {
		return null;
	}

	@Override
	public Component getDevToolsContainer() {
		return null;
	}

	@Override
	public String getVersion() {
		return "TeamDev JxBrowser 6.24.3, Chromium Core, " + this.browser.getUserAgent();
	}

	@Override
	public void loadURL(String url) {
		this.browser.loadURL(url);
	}

	@Override
	public void reload() {
		this.browser.reload();
	}

	@Override
	public void dispose(int a) {
		this.browser.dispose();
		System.gc();
	}

	@Override
	public void clearLocalStorage() {
		SwingUtilities.invokeLater(() -> {
			this.execJS("localStorage.removeItem(\"jmd-config\")");
		});
	}

	@Override
	public void execJS(String javaScript) {
		SwingUtilities.invokeLater(() -> {
			this.browser.executeJavaScript(javaScript);
		});
	}

	@Override
	public void execJSWithStringBack(String javaScript, JavaScriptExecutionCallback callback) {
		SwingUtilities.invokeLater(() -> {
			JSValue back = this.browser.executeJavaScriptAndReturnValue(javaScript);
			callback.execute(back.getStringValue());
		});
	}

	public static TeamDevJxBrowserInst getIstance() {
		if (instance == null) {
			synchronized (TeamDevJxBrowserInst.class) {
				if (instance == null) {
					instance = new TeamDevJxBrowserInst();
				}
			}
		}
		return instance;
	}

}
