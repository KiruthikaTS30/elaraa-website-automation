package com.elaraa.tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void verifyHomePage() {

        driver.get("https://elaraastudio.com/home");

        String title = driver.getTitle();

        System.out.println("Page Title: " + title);
        System.out.println("URL: " + driver.getCurrentUrl());

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://"),
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
