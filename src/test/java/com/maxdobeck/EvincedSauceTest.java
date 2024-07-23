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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class EvincedSauceTest
{
    ChromeDriver driver;
    EvincedWebDriver evincedDriver;

    @Test
    public void ChromeSauceTest() throws MalformedURLException {
        try {
            ChromeOptions browserOptions = new ChromeOptions();
            browserOptions.setCapability("platformName", "Windows 11");
            browserOptions.setCapability("browserVersion", "latest");
            Map<String, Object> sauceOptions = new HashMap<>();
            sauceOptions.put("username", System.getenv("SAUCE_USERNAME"));
            sauceOptions.put("accessKey", System.getenv("SAUCE_ACCESS_KEY"));
            sauceOptions.put("name", "Simple Evinced Chrome Test:Start/Stop");
            browserOptions.setCapability("sauce:options", sauceOptions);

            URL url = new URL("https://ondemand.us-west-1.saucelabs.com:443/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(url, browserOptions);

            EvincedConfiguration configuration = new EvincedConfiguration();
            configuration.setEnableScreenshots(true);
            EvincedWebDriver evincedDriver = new EvincedWebDriver(driver, configuration);
            EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
            evincedDriver.evStart();
            evincedDriver.get("https://demo.evinced.com");
            Report report = evincedDriver.evStop();
            // Assert that there are SOME accessibility issues
            assertTrue(report.getIssues().size() != 0);
            takeSnapshot("sauce_report", evincedDriver);
            driver.executeScript("sauce:job-result=" + true);
            driver.quit();
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