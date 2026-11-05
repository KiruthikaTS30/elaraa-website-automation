
package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
    }

    // TEST CASE 1: Verify Homepage
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

    // TEST CASE 2: Inspect Search Elements
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
            By.cssSelector("input, textarea, button")
        );

        for (WebElement element : elements) {

            System.out.println("Tag: " + element.getTagName());

            System.out.println("Type: " +
                element.getAttribute("type"));

            System.out.println("Placeholder: " +
                element.getAttribute("placeholder"));

            System.out.println("ARIA label: " +
                element.getAttribute("aria-label"));

            System.out.println("Visible: " +
                element.isDisplayed());

            System.out.println("--------------------");
        }

        Assert.assertTrue(
            elements.size() > 0,
            "Search page should contain elements"
        );
    }

    // TEST CASE 3: Verify View Account Button
    @Test
    public void verifyViewAccountButton() {

        driver.get("https://elaraastudio.com/home");

        WebDriverWait wait = new WebDriverWait(
            driver, Duration.ofSeconds(20)
        );

        WebElement accountButton = wait.until(
            ExpectedConditions.elementToBeClickable(
                By.cssSelector(
                    "button[aria-label='View account']"
                )
            )
        );

        Assert.assertTrue(
            accountButton.isDisplayed(),
            "View Account button should be visible"
        );

        System.out.println(
            "View Account button is visible"
        );

        accountButton.click();

        // Wait for navigation or account panel
        wait.until(d ->
            !d.getCurrentUrl().equals(
                "https://elaraastudio.com/home"
            )
            ||
            d.findElements(
                By.cssSelector(
                    "[role='dialog'], [role='menu']"
                )
            ).stream().anyMatch(WebElement::isDisplayed)
        );

        System.out.println(
            "View Account button clicked successfully"
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
