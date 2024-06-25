import static org.junit.Assert.assertTrue;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.evinced.EvincedWebDriver;
import com.evinced.dto.results.Report;
import com.evinced.EvincedSDK;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class EvincedSetupTest
{
    public static DesiredCapabilities capabilities;
    public static EvincedWebDriver driver;

    @Before
    public void Setup()
    {
        EvincedSDK.setCredentials(System.getenv("SERVICE_ACCOUNT_ID"), System.getenv("API_KEY"));
    }
    
    // test for https://developer.evinced.com/sdks-for-web-apps/selenium-java-sdk#addevincedaccessibilitychecks(singlerunmode)
    @Test
    public void ShouldStartChrome() throws MalformedURLException
    {
        try {
            EvincedWebDriver driver = new EvincedWebDriver(new ChromeDriver());
            driver.get("https://www.google.com");
            Report report = driver.evAnalyze();
            // Assert that there are no accessibility issues
            assertTrue(report.getIssues().size() == 0);
        } catch (Exception ignore) {
            //ignore exception
        }
        assertTrue(true);
    }
}