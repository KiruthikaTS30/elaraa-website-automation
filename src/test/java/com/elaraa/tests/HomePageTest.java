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
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(
            driver, Duration.ofSeconds(20)
        );
    }

    // TEST 1: Homepage Validation
    @Test
    public void verifyHomePage() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.getTitle() != null &&
            !d.getTitle().trim().isEmpty()
        );

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

    // TEST 2: Inspect Search Elements
    @Test
    public void inspectSearchElements() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        wait.until(d ->
            d.findElements(
                By.cssSelector("input, textarea")
            ).size() > 0
        );

        List<WebElement> elements = driver.findElements(
            By.cssSelector("input, textarea, button")
        );

        for (WebElement element : elements) {

            System.out.println("Tag: " + element.getTagName());

            System.out.println(
                "Type: " + element.getAttribute("type")
            );

            System.out.println(
                "Placeholder: " +
                element.getAttribute("placeholder")
            );

            System.out.println(
                "ARIA label: " +
                element.getAttribute("aria-label")
            );

            System.out.println(
                "Visible: " + element.isDisplayed()
            );

            System.out.println("--------------------");
        }

        Assert.assertTrue(
            elements.size() > 0,
            "Search page should contain elements"
        );
    }

    // TEST 3: Investigate Account Elements
    @Test
    public void inspectAccountElements() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.getTitle() != null &&
            !d.getTitle().trim().isEmpty()
        );

        List<WebElement> elements = driver.findElements(
            By.cssSelector("button, a, [role='button']")
        );

        System.out.println("Homepage elements:");

        for (WebElement element : elements) {

            if (element.isDisplayed()) {

                System.out.println(
                    "Tag: " + element.getTagName()
                );

                System.out.println(
                    "Text: " + element.getText()
                );

                System.out.println(
                    "ARIA label: " +
                    element.getAttribute("aria-label")
                );

                System.out.println(
                    "Title: " +
                    element.getAttribute("title")
                );

                System.out.println(
                    "Class: " +
                    element.getAttribute("class")
                );

                System.out.println("--------------------");
            }
        }

        System.out.println(
            "Total buttons and links: " + elements.size()
        );

        Assert.assertTrue(
            elements.size() > 0,
            "Homepage should contain buttons or links"
        );
    }

    // CLOSE BROWSER
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
