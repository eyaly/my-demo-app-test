package com.example.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class SortTest extends BaseTest {

    @Test
    public void testSortByOptionsAvailable() {
        // Convert the platformName capability to String safely
        Object platformNameObj = getDriver().getCapabilities().getCapability("platformName");
        String platformName = String.valueOf(platformNameObj);
        
        System.out.println("Running testSortByOptionsAvailable on " + platformName);

        if (platformName.equalsIgnoreCase("Android")) {
            // Android Locators
            By sortButton = By.id("com.saucelabs.mydemoapp.android:id/sortIV");
            
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(sortButton));
            getDriver().findElement(sortButton).click();

            // Verify Sort Options by Accessibility ID
            Assert.assertTrue(isElementVisible(AppiumBy.accessibilityId("Ascending order by name")), "Ascending order by name option not visible");
            Assert.assertTrue(isElementVisible(AppiumBy.accessibilityId("Descending order by name")), "Descending order by name option not visible");
            Assert.assertTrue(isElementVisible(AppiumBy.accessibilityId("Ascending order by price")), "Ascending order by price option not visible");
            Assert.assertTrue(isElementVisible(AppiumBy.accessibilityId("Descending order by price")), "Descending order by price option not visible");

        } else if (platformName.equalsIgnoreCase("iOS")) {
             // iOS Locators
             By sortButton = AppiumBy.iOSClassChain("**/XCUIElementTypeButton[`name == \"Button\"`]");

             WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
             wait.until(ExpectedConditions.visibilityOfElementLocated(sortButton));
             getDriver().findElement(sortButton).click();

             // Verify Sort Options by iOS Class Chain
             Assert.assertTrue(isElementVisible(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Name - Ascending\"`]")), "Name - Ascending option not visible");
             Assert.assertTrue(isElementVisible(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Name - Descending\"`]")), "Name - Descending option not visible");
             Assert.assertTrue(isElementVisible(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Price - Ascending\"`]")), "Price - Ascending option not visible");
             Assert.assertTrue(isElementVisible(AppiumBy.iOSClassChain("**/XCUIElementTypeStaticText[`name == \"Price - Descending\"`]")), "Price - Descending option not visible");
        }
    }

    private boolean isElementVisible(By locator) {
        try {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
