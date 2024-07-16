package com.maxdobeck;

import static org.junit.Assert.assertTrue;
// import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.evinced.dto.results.Report;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.evinced.EvincedWebDriver;
import com.evinced.EvincedReporter;
import com.evinced.EvincedSDK;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EvincedSetupTest
{
    ChromeDriver driver;
    EvincedWebDriver evincedDriver;

    @Before
    public void setupAll() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @After
    public void teardownDrivers() {
        if (driver != null) {
            driver.quit();
        }
        if (evincedDriver != null) {
            evincedDriver.quit();
        }
    }

    // test for https://developer.evinced.com/sdks-for-web-apps/selenium-java-sdk#addevincedaccessibilitychecks(singlerunmode)
    @Test
    public void ShouldStartChrome() throws MalformedURLException {
        try {
            EvincedWebDriver evincedDriver = new EvincedWebDriver(driver);
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.get("https://demo.evinced.com");
            Report report = evincedDriver.evAnalyze();
            // Assert that there are SOME accessibility issues
            assertTrue(report.getIssues().size() != 0);
        } catch (Exception ignore) {
            // ignore exception
            System.out.println(ignore);
        }
    }

    @Test
    public void SaveReports() throws MalformedURLException {
        try {
            EvincedWebDriver evincedDriver = new EvincedWebDriver(driver);
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.get("https://demo.evinced.com");
            Report landing_page_report = evincedDriver.evAnalyze();
            EvincedReporter.evSaveFile("landing_page",landing_page_report, EvincedReporter.FileFormat.HTML);
            assertTrue(landing_page_report.getIssues().size() != 0);

            evincedDriver.get("https://demo.evinced.com/results/?what=Tiny%20House&where=Canada&date=Tue%20Jul%2009%202024%2011:21:39%20GMT-0400%20(Eastern%20Daylight%20Time");
            takeSnapshot("results_page", evincedDriver);

        } catch (Exception ignore) {
            // ignore exception
            System.out.println(ignore);
        }
    }

    @Test
    public void ChromeSauceTest() throws MalformedURLException {
        try {

            ChromeOptions browserOptions = new ChromeOptions();
            browserOptions.setCapability("platformName", "Windows 11");
            browserOptions.setCapability("browserVersion", "latest");
            Map<String, Object> sauceOptions = new HashMap<>();
            sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
            sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
            sauceOptions.put("build", "<your build id>");
            sauceOptions.put("name", "<your test name>");
            browserOptions.setCapability("sauce:options", sauceOptions);

            URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(url, browserOptions);

            EvincedWebDriver evincedDriver = new EvincedWebDriver(driver);
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.get("https://demo.evinced.com");
            Report report = evincedDriver.evAnalyze();
            // Assert that there are SOME accessibility issues
            assertTrue(report.getIssues().size() != 0);
        } catch (Exception ignore) {
            // ignore exception
            System.out.println(ignore);
        }
    }


    public static void takeSnapshot(String reportName, EvincedWebDriver evDriver) {
        Report report = evDriver.evAnalyze();
        EvincedReporter.evSaveFile(reportName, report, EvincedReporter.FileFormat.HTML);
    }

}