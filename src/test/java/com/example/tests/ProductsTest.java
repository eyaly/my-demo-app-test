package com.example.tests;

import com.saucelabs.visual.CheckOptions;
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
        Object platformNameObj = getDriver().getCapabilities().getCapability("platformName");
        String platformName = String.valueOf(platformNameObj);

//        CheckOptions options = new CheckOptions();
//        options.enableFullPageScreenshots();
//        getVisual().sauceVisualCheck("Products Screen (full page)", options);
        // CheckOptions options = new CheckOptions();
        // options.setCaptureDom(true);
    //    getVisual().sauceVisualCheck("Products Screen");

        if (platformName.equalsIgnoreCase("Android")) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

            // Click on the first product
            By firstProduct = By.id("com.saucelabs.mydemoapp.android:id/productIV");
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstProduct));
            getDriver().findElement(firstProduct).click();

       //     getVisual().sauceVisualCheck("Click on the first product");

            // Click on "Tap to add product to cart"
            By addToCartBtn = AppiumBy.accessibilityId("Tap to add product to cart");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
            getDriver().findElement(addToCartBtn).click();

       //     getVisual().sauceVisualCheck("Click on - Tap to add product to cart");

            // Verify cart badge text is 1
            By cartBadge = By.id("com.saucelabs.mydemoapp.android:id/cartTV");
            wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge));
            WebElement cartElement = getDriver().findElement(cartBadge);
            Assert.assertEquals(cartElement.getText(), "1", "Cart badge text should be 1");
        } else if (platformName.equalsIgnoreCase("iOS")) {

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            By firstProduct = By.xpath("(//XCUIElementTypeImage[@name='Product Image'])[1]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(firstProduct));
            getDriver().findElement(firstProduct).click();

            // Click on "Tap to add product to cart"
            By addToCartBtn = AppiumBy.accessibilityId("Add To Cart");
            wait.until(ExpectedConditions.visibilityOfElementLocated(addToCartBtn));
            getDriver().findElement(addToCartBtn).click();

            // Intentional failure for iOS: Click on non-existent locator
            //By nonExistentElement = AppiumBy.accessibilityId("this-element-does-not-exist");

        }
    }

    @Test
    public void testProductsScreenLoaded() {

     //   getVisual().sauceVisualCheck("Products Screen");
        Object platformNameObj = getDriver().getCapabilities().getCapability("platformName");
        String platformName = String.valueOf(platformNameObj);

        if (platformName.equalsIgnoreCase("Android")) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));

            // Check for the presence of a product item to verify screen is loaded
            By productItem = By.id("com.saucelabs.mydemoapp.android:id/productIV");
            wait.until(ExpectedConditions.visibilityOfElementLocated(productItem));
            Assert.assertTrue(getDriver().findElement(productItem).isDisplayed(), "Product list should be visible");
        // CheckOptions options = new CheckOptions();
//        options.enableFullPageScreenshots();
        // options.setCaptureDom(true);
   //     getVisual().sauceVisualCheck("Products Screen");
        }
//        else {
//            getVisual().sauceVisualCheck("Products Screen iOS");
//        }
    }
}
