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
import org.openqa.selenium.TimeoutException;

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void verifyHomePage() {
        driver.get("https://elaraastudio.com/home");

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("root")));

        Assert.assertEquals(driver.getTitle(), "Elaraa Studio");
        Assert.assertTrue(driver.getCurrentUrl().startsWith("https://"));

        System.out.println("PASS: Homepage validation");
    }

    @Test
    public void verifyHomePageContent() {
        driver.get("https://elaraastudio.com/home");

        wait.until(d -> d.findElement(By.id("root"))
                .getText().contains("Login"));

        String text = driver.findElement(By.id("root")).getText();

        Assert.assertTrue(text.contains("Profile"));
        Assert.assertTrue(text.contains("Wishlist"));
        Assert.assertTrue(text.contains("Bags"));
        Assert.assertTrue(text.contains("Login"));

        System.out.println("PASS: Homepage content validation");
    }

    // LOGIN FUNCTIONAL INSPECTION
    @Test
    public void inspectLoginAfterClick() {
        driver.get("https://elaraastudio.com/home");

        By loginLocator = By.cssSelector(
                "button[aria-label='Login to your account']");

        WebElement loginButton = wait.until(
                ExpectedConditions.elementToBeClickable(loginLocator));

        Assert.assertTrue(loginButton.isDisplayed());
        Assert.assertTrue(loginButton.isEnabled());

        loginButton.click();

        System.out.println("LOGIN BUTTON CLICKED");
        System.out.println("Current URL: " + driver.getCurrentUrl());

        try {
            new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> {
                List<WebElement> fields =
                        d.findElements(By.cssSelector(
                                "input, textarea, select"));

                for (WebElement field : fields) {
                    if (field.isDisplayed()) {
                        return true;
                    }
                }

                return !d.getCurrentUrl().equals(
                        "https://elaraastudio.com/home");
            });
        } catch (TimeoutException e) {
            System.out.println(
                    "No visible form or URL change detected within 5 seconds.");
        }

        System.out.println("FIELDS AFTER LOGIN CLICK:");

        List<WebElement> fields = driver.findElements(
                By.cssSelector("input, textarea, select"));

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

        System.out.println("VISIBLE BUTTONS AFTER LOGIN CLICK:");

        List<WebElement> buttons = driver.findElements(
                By.tagName("button"));

        for (WebElement button : buttons) {
            if (button.isDisplayed()) {
                System.out.println(
                        "Text: " + button.getText());
                System.out.println(
                        "ARIA: " + button.getAttribute("aria-label"));
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
