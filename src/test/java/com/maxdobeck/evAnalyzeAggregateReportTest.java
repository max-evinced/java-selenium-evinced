package com.maxdobeck;

import static org.junit.Assert.assertTrue;
// import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.evinced.dto.configuration.EvincedConfiguration;
import com.evinced.dto.results.Report;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.evinced.EvincedWebDriver;
import com.evinced.EvincedReporter;
import com.evinced.EvincedSDK;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;


// NOTE: Must use Evinced SDK 4.1.6+
public class evAnalyzeAggregateReportTest
{
    static ChromeDriver driver;
    static EvincedWebDriver evincedDriver;

    @Before
    public void startDriver() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @After
    public void teardownTest() {
        if (driver != null) {
            driver.quit();
        }
        if (evincedDriver != null) {
            evincedDriver.quit();
        }
    }

    @AfterClass
    public static void cleanupReport() {
        // Save an aggregate report
        EvincedReporter.evSaveFile("evAnalyze_agg_report", EvincedReporter.FileFormat.HTML);
    }

    @Test
    public void ShouldStartChrome() throws MalformedURLException {
        EvincedWebDriver evincedDriver = new EvincedWebDriver(driver);
        EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
        evincedDriver.get("https://bbc.com");
        evincedDriver.evAnalyze();
    }

    @Test
    public void SaveReports() throws MalformedURLException {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        EvincedConfiguration configuration = new EvincedConfiguration();
        configuration.setEnableScreenshots(true);
        EvincedWebDriver evincedDriver = new EvincedWebDriver(driver, configuration);
        EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
        evincedDriver.get("https://demo.evinced.com");
        evincedDriver.evAnalyze();
        evincedDriver.get("https://demo.evinced.com/results/?what=Tiny%20House&where=Canada&date=Tue%20Jul%2009%202024%2011:21:39%20GMT-0400%20(Eastern%20Daylight%20Time");
        evincedDriver.evAnalyze();
    }

    // @Test
    // public void ThirdTest() throws MalformedURLException {
    //     WebDriverManager.chromedriver().setup();
    //     driver = new ChromeDriver();

    //     EvincedConfiguration configuration = new EvincedConfiguration();
    //     configuration.setEnableScreenshots(true);
    //     EvincedWebDriver evincedDriver = new EvincedWebDriver(driver, configuration);
    //     EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
    //     evincedDriver.get("https://evinced.com");
    //     evincedDriver.evAnalyze();
    //     evincedDriver.get("https://pbs.org");
    //     evincedDriver.evAnalyze();
    // }
}