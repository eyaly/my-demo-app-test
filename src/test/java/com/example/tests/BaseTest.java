package com.example.tests;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import com.saucelabs.visual.VisualApi;
import com.saucelabs.visual.DataCenter;
import org.openqa.selenium.MutableCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import org.testng.ITestResult;
import org.openqa.selenium.JavascriptExecutor;

import com.saucelabs.visual.testng.TestMetaInfoListener;
import org.testng.annotations.Listeners;

@Listeners({TestMetaInfoListener.class})
public class BaseTest {

    private static final ThreadLocal<AppiumDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<VisualApi> visual = new ThreadLocal<>();

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driverInstance) {
        driver.set(driverInstance);
    }

    public VisualApi getVisual() {
        return visual.get();
    }

    public void setVisual(VisualApi visualInstance) {
        visual.set(visualInstance);
    }

    @Parameters({"platformName", "platformVersion", "deviceName", "app"})
    @BeforeMethod
    public void setUp(String platformName, 
                      @Optional String platformVersion, 
                      @Optional String deviceName,
                      @Optional String app,
                      Method method) throws MalformedURLException {
        
        URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
        
        String username = System.getenv("SAUCE_USERNAME");
        String accessKey = System.getenv("SAUCE_ACCESS_KEY");
        if (username == null || accessKey == null) {
             System.out.println("Warning: SAUCE_USERNAME or SAUCE_ACCESS_KEY environment variables not set.");
        }

        try {
            MutableCapabilities caps = new MutableCapabilities();
            caps.setCapability("appium:platformVersion", platformVersion);
            caps.setCapability("appium:deviceName", deviceName);
            caps.setCapability("appium:app", app);

            MutableCapabilities sauceOptions = new MutableCapabilities();
            sauceOptions.setCapability("username", username);
            sauceOptions.setCapability("accessKey", accessKey);
            sauceOptions.setCapability("build", "appium-demo-test-build_8");
            sauceOptions.setCapability("name", method.getName());
            sauceOptions.setCapability("appiumVersion", "latest");

            if (platformName.equalsIgnoreCase("Android")) {

                caps.setCapability("platformName", "Android");
                caps.setCapability("appium:automationName", "UiAutomator2");
                caps.setCapability("sauce:options", sauceOptions);
                
                setDriver(new AndroidDriver(url, caps));

            } else if (platformName.equalsIgnoreCase("iOS")) {

                caps.setCapability("platformName", "iOS");
                caps.setCapability("appium:automationName", "XCUITest");
                caps.setCapability("sauce:options", sauceOptions);

                setDriver(new IOSDriver(url, caps));
            } else {
                throw new IllegalArgumentException("Platform name must be 'Android' or 'iOS'");
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize driver for " + platformName);
            e.printStackTrace();
            throw new RuntimeException("Driver initialization failed", e);
        }
        
        if (getDriver() != null) {
            getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            // Initialize VisualApi
            setVisual(new VisualApi.Builder(getDriver(), username, accessKey, DataCenter.EU_CENTRAL_1)
                    .withBuild("Sauce My Demo App Test")
                    .withBranch("main")
                    .withProject("Appium examples")
                    .build());
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        AppiumDriver driverInstance = getDriver();
        if (driverInstance != null) {
            try {
                String status = result.isSuccess() ? "passed" : "failed";
                ((JavascriptExecutor) driverInstance).executeScript("sauce:job-result=" + status);
            } catch (Exception e) {
                System.err.println("Failed to report test status to Sauce Labs: " + e.getMessage());
            } finally {
                System.out.println("Sauce - release driver");
                driverInstance.quit();
                driver.remove();
                if (getVisual() != null) {
                    visual.remove();
                }
            }
        }
    }
}
