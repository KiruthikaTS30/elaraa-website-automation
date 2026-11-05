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

        wait.until(d -> d.getTitle() != null
            && !d.getTitle().trim().isEmpty());

        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("URL: " + driver.getCurrentUrl());

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://"),
            "Website should use HTTPS"
        );

        Assert.assertFalse(
            driver.getTitle().trim().isEmpty(),
            "Page title should not be empty"
        );
    }

    @Test
    public void inspectSearchElements() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(20)
        );

        wait.until(d -> d.findElements(
            By.cssSelector("input, textarea")
        ).size() > 0);

        List<WebElement> elements = driver.findElements(
            By.cssSelector("input, textarea")
        );

        System.out.println("Total search-related fields: "
            + elements.size());

        for (WebElement element : elements) {

            System.out.println("Tag: "
                + element.getTagName());

            System.out.println("Type: "
                + element.getAttribute("type"));

            System.out.println("Placeholder: "
                + element.getAttribute("placeholder"));

            System.out.println("Name: "
                + element.getAttribute("name"));

            System.out.println("ID: "
                + element.getAttribute("id"));

            System.out.println("Visible: "
                + element.isDisplayed());

            System.out.println("--------------------");
        }

        Assert.assertTrue(
            elements.size() > 0,
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
