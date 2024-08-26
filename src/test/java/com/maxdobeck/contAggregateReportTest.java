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
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;


public class contAggregateReportTest
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
        // Save an aggregate report
        EvincedReporter.evSaveFile("aggregated_report", EvincedReporter.FileFormat.HTML);
        if (driver != null) {
            driver.quit();
        }
        if (evincedDriver != null) {
            evincedDriver.quit();
        }
    }

    @Test
    public void ShouldStartChrome() throws MalformedURLException {
        EvincedWebDriver evincedDriver = new EvincedWebDriver(driver);
        EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
        evincedDriver.evStart();
        evincedDriver.get("https://bbc.com");
        evincedDriver.evStop();
    }

    @Test
    public void SaveReports() throws MalformedURLException {
        EvincedConfiguration configuration = new EvincedConfiguration();
        configuration.setEnableScreenshots(true);
        EvincedWebDriver evincedDriver = new EvincedWebDriver(driver, configuration);
        EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
        evincedDriver.evStart();
        evincedDriver.get("https://demo.evinced.com");
        evincedDriver.get("https://demo.evinced.com/results/?what=Tiny%20House&where=Canada&date=Tue%20Jul%2009%202024%2011:21:39%20GMT-0400%20(Eastern%20Daylight%20Time");
        evincedDriver.evStop();
        }

        @Test
        public void AnotherDomain() throws MalformedURLException {
            EvincedConfiguration configuration = new EvincedConfiguration();
            configuration.setEnableScreenshots(true);
            EvincedWebDriver evincedDriver = new EvincedWebDriver(driver, configuration);
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.evStart();
            evincedDriver.get("https://evinced.com");
            evincedDriver.get("https://pbs.org");
            evincedDriver.evStop();
            }
    
}