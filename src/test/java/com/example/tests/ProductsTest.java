package com.example.tests;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class ProductsTest extends BaseTest {

    @Test
    public void testAddToCart() {
        Object platformNameObj = driver.getCapabilities().getCapability("platformName");
        String platformName = String.valueOf(platformNameObj);

        if (platformName.equalsIgnoreCase("Android")) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Click on the first product
            By firstProduct = By.id("com.saucelabs.mydemoapp.android:id/productIV");
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstProduct));
            driver.findElement(firstProduct).click();

            // Click on "Tap to add product to cart"
            By addToCartBtn = AppiumBy.accessibilityId("Tap to add product to cart");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
            driver.findElement(addToCartBtn).click();

            // Verify cart badge text is 1
            By cartBadge = By.id("com.saucelabs.mydemoapp.android:id/cartTV");
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            WebElement cartElement = driver.findElement(cartBadge);
            Assert.assertEquals(cartElement.getText(), "1", "Cart badge text should be 1");
        } else if (platformName.equalsIgnoreCase("iOS")) {
            // Intentional failure for iOS: Click on non-existent locator
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//            By nonExistentElement = AppiumBy.accessibilityId("this-element-does-not-exist");
//            wait.until(ExpectedConditions.visibilityOfElementLocated(nonExistentElement));
//            driver.findElement(nonExistentElement).click();
        }
    }

    @Test
    public void testProductsScreenLoaded() {
        Object platformNameObj = driver.getCapabilities().getCapability("platformName");
        String platformName = String.valueOf(platformNameObj);

        if (platformName.equalsIgnoreCase("Android")) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            
            // Check for the presence of a product item to verify screen is loaded
            By productItem = By.id("com.saucelabs.mydemoapp.android:id/productIV");
//            wait.until(ExpectedConditions.visibilityOfElementLocated(productItem));
            Assert.assertTrue(driver.findElement(productItem).isDisplayed(), "Product list should be visible");
        }
    }
}
