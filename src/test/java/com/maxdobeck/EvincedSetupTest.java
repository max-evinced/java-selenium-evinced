package com.maxdobeck;

import static org.junit.Assert.assertTrue;
// import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import com.evinced.dto.results.Report;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.evinced.EvincedWebDriver;
import com.evinced.EvincedReporter;
import com.evinced.EvincedSDK;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;

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
            Report results_page_report = evincedDriver.evAnalyze();
            EvincedReporter.evSaveFile("results_page",results_page_report, EvincedReporter.FileFormat.HTML);
            assertTrue(results_page_report.getIssues().size() == 0);

        } catch (Exception ignore) {
            // ignore exception
            System.out.println(ignore);
        }
    }

}