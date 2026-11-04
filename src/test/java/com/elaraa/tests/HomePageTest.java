
package com.elaraa.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    @Test
    public void verifyHomePage() {

        driver.get("https://elaraastudio.com/home");

        String title = driver.getTitle();
        String url = driver.getCurrentUrl();

        System.out.println("Page Title: " + title);
        System.out.println("URL: " + url);

        Assert.assertTrue(
            url.startsWith("https://"),
            "Website should use HTTPS"
        );

        Assert.assertFalse(
            title == null || title.trim().isEmpty(),
            "Page title should not be empty"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
