package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.ExpectedConditions;
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

    // TEST 3: View Account Button
    @Test
    public void verifyViewAccountButton() {

        driver.get("https://elaraastudio.com/home");

        By accountLocator = By.cssSelector(
            "button[aria-label='View account']"
        );

        WebElement accountButton = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                accountLocator
            )
        );

        Assert.assertTrue(
            accountButton.isDisplayed(),
            "Account button should be visible"
        );

        System.out.println(
            "Account button is visible"
        );

        // Scroll button into view
        ((JavascriptExecutor) driver).executeScript(
            "arguments[0].scrollIntoView({block:'center'});",
            accountButton
        );

        // Wait until clickable
        accountButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                accountLocator
            )
        );

        // Perform normal Selenium click
        accountButton.click();

        System.out.println(
            "Account button clicked"
        );

        // Verify account page or panel
        boolean opened = wait.until(d ->
            !d.getCurrentUrl().equals(
                "https://elaraastudio.com/home"
            )
            ||
            d.findElements(
                By.cssSelector(
                    "[role='dialog'], [role='menu']"
                )
            ).stream().anyMatch(
                WebElement::isDisplayed
            )
        );

        Assert.assertTrue(
            opened,
            "Account page or panel should open"
        );

        System.out.println(
            "Current URL: " + driver.getCurrentUrl()
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
