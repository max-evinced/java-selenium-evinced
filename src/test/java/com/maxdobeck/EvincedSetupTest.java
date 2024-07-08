package com.maxdobeck;

import static org.junit.Assert.assertTrue;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import com.evinced.EvincedWebDriver;
import com.evinced.dto.results.Report;

import io.github.bonigarcia.wdm.WebDriverManager;

import com.evinced.EvincedSDK;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;

public class EvincedSetupTest
{ 
    @Before
    public void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    // test for https://developer.evinced.com/sdks-for-web-apps/selenium-java-sdk#addevincedaccessibilitychecks(singlerunmode)
    @Test
    public void ShouldStartChrome() throws MalformedURLException
    {
        try {
            EvincedWebDriver evincedDriver = new EvincedWebDriver(new ChromeDriver());
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.get("https://demo.evinced.com");
            Report report = evincedDriver.evAnalyze();
            // Assert that there are SOME accessibility issues
            assertTrue(report.getIssues().size() != 0);
            evincedDriver.quit();
        } catch (Exception ignore) {
            // ignore exception
            System.out.println(ignore);
        }
    }
}