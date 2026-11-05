package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
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

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );

        wait.until(d -> d.getTitle() != null &&
            !d.getTitle().trim().isEmpty());

        String title = driver.getTitle();
        String url = driver.getCurrentUrl();

        System.out.println("Page Title: " + title);
        System.out.println("URL: " + url);

        Assert.assertTrue(
            url.startsWith("https://"),
            "Website should use HTTPS"
        );

        Assert.assertFalse(
            title.trim().isEmpty(),
            "Page title should not be empty"
        );
    }

    @Test
    public void inspectSearchElements() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(15)
        );

        wait.until(d -> !d.findElements(
            By.tagName("input")).isEmpty());

        List<WebElement> inputs =
            driver.findElements(By.tagName("input"));

        System.out.println(
            "Total input fields: " + inputs.size()
        );

        for (WebElement input : inputs) {

            System.out.println("Type: " +
                input.getAttribute("type"));

            System.out.println("Placeholder: " +
                input.getAttribute("placeholder"));

            System.out.println("Name: " +
                input.getAttribute("name"));

            System.out.println("ID: " +
                input.getAttribute("id"));

            System.out.println("--------------------");
        }

        Assert.assertTrue(
            inputs.size() > 0,
            "Search page should contain an input field"
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
