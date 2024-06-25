import static org.junit.Assert.assertTrue;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.evinced.EvincedWebDriver;
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
    
    @Test
    public void ShouldStartChrome() throws MalformedURLException
    {
        BaseOptions options = new BaseOptions()
            .setAutomationName("chromeDriver");
        try {
            EvincedWebDriver driver = new EvincedWebDriver(new ChromeDriver());
        } catch (Exception ignore) {
            //ignore exception
        }
        // Navigate to the site under test
        driver.get("https://www.google.com");

        // Run analysis and get the accessibility report
        Report report = driver.evAnalyze();

        // Assert that there are no accessibility issues
        assertTrue(report.getIssues().size() == 0);
    }
}