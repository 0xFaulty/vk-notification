package com.defaulty.notivk.gui.components;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrowserTest {

    private Browser browser;

    @Before
    public void setUp() throws Exception {
        browser = new Browser();
    }

    @Test
    public void mainBrowserTest() throws Exception {
        browser.setListenerClass(() -> System.out.println("Listener executed"));
        browser.loadUrl("https://yandex.ru");
        browser.hideBrowser();
        browser.loadUrl("https://www.google.ru/");
        assertEquals(browser.getLocation(), "https://www.google.ru/");
    }

}