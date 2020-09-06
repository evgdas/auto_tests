package utils.drivers;

import com.codeborne.selenide.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.logging.Level;

import static utils.helpers.EnvironmentHelper.*;

public class CustomWebDriver implements WebDriverProvider {
    @Override
    public WebDriver createDriver(DesiredCapabilities capabilities) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        capabilities.setBrowserName(browser);
        capabilities.setVersion(version);
        capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);

        if ("firefox".equals(browser)) {
            WebDriverManager.firefoxdriver().setup();
            return getLocalFirefoxDriver(getFirefoxOptions().merge(capabilities));
        } else { //chrome
          //  WebDriverManager.chromedriver().setup();
            return getLocalChromeDriver(getChromeOptions().merge(capabilities));
        }
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions()
                .addArguments("--no-sandbox")
                .addArguments("--disable-notifications")
                .addArguments("--disable-infobars");
        if (isHeadless) chromeOptions.addArguments("headless");
        return chromeOptions;
    }

    private FirefoxOptions getFirefoxOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions()
                .addPreference("browser.link.open_newwindow", 3)
                .addPreference("browser.link.open_newwindow.restriction", 0);
        if (isHeadless) firefoxOptions.addArguments("headless");
        return firefoxOptions;
    }

    private WebDriver getLocalChromeDriver(ChromeOptions chromeOptions) {
        return new ChromeDriver(chromeOptions);
    }

    private WebDriver getLocalFirefoxDriver(FirefoxOptions firefoxOptions) {
        return new FirefoxDriver(firefoxOptions);
    }
}