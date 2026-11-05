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
    WebDriverWait wait;

    By loadingOverlay = By.cssSelector(".global-loading-overlay");

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // Wait until website loading overlay disappears
    public void waitForPageReady() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("root")));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                loadingOverlay));
    }

    @Test
    public void verifyHomePage() {
        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        Assert.assertEquals(driver.getTitle(), "Elaraa Studio");
        Assert.assertTrue(driver.getCurrentUrl().startsWith("https://"));

        System.out.println("PASS: Homepage validation");
    }

    @Test
    public void verifyHomePageContent() {
        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        String text = driver.findElement(By.id("root")).getText();

        Assert.assertTrue(text.contains("Profile"));
        Assert.assertTrue(text.contains("Wishlist"));
        Assert.assertTrue(text.contains("Bags"));
        Assert.assertTrue(text.contains("Login"));

        System.out.println("PASS: Homepage content validation");
    }

    // LOGIN FUNCTIONAL TEST
    @Test
    public void inspectLoginAfterClick() {

        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        By loginLocator = By.cssSelector(
                "button[aria-label='Login to your account']");

        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(loginLocator));

        Assert.assertTrue(loginButton.isDisplayed());
        Assert.assertTrue(loginButton.isEnabled());

        loginButton.click();

        System.out.println("PASS: Login button clicked");

        System.out.println("URL after click: " +
                driver.getCurrentUrl());

        // Inspect fields after clicking Login
        List<WebElement> fields = driver.findElements(
                By.cssSelector("input, textarea, select"));

        System.out.println("FIELDS AFTER LOGIN CLICK:");

        for (WebElement field : fields) {
            if (field.isDisplayed()) {
                System.out.println("Tag: " + field.getTagName());
                System.out.println("Type: " +
                        field.getAttribute("type"));
                System.out.println("Placeholder: " +
                        field.getAttribute("placeholder"));
                System.out.println("Name: " +
                        field.getAttribute("name"));
                System.out.println("-------------------");
            }
        }

        System.out.println("VISIBLE BUTTONS:");

        List<WebElement> buttons = driver.findElements(
                By.tagName("button"));

        for (WebElement button : buttons) {
            if (button.isDisplayed()) {
                System.out.println("Text: " + button.getText());
                System.out.println("ARIA: " +
                        button.getAttribute("aria-label"));
            }
        }

        System.out.println("LOGIN INSPECTION COMPLETED");
    }

    @Test
    public void verifySearchPageLoads() {
        driver.get(
                "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.tagName("body")));

        Assert.assertFalse(driver.getTitle().trim().isEmpty());

        System.out.println("PASS: Campaign page loaded");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
