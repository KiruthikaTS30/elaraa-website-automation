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

    By root = By.id("root");

    By overlay = By.cssSelector(
            ".global-loading-overlay");

    By loginButton = By.cssSelector(
            "button[aria-label='Login to your account']");

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(
                driver, Duration.ofSeconds(30));
    }

    public void waitForPageReady() {

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(root));

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        overlay));
    }

    @Test
    public void verifyHomePage() {

        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        Assert.assertEquals(
                driver.getTitle(), "Elaraa Studio");

        Assert.assertTrue(
                driver.getCurrentUrl().startsWith("https://"));

        System.out.println("PASS: Homepage validation");
    }

    @Test
    public void verifyHomePageContent() {

        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        String pageText =
                driver.findElement(root).getText();

        Assert.assertFalse(
                pageText.trim().isEmpty(),
                "Homepage content is empty");

        Assert.assertTrue(
                driver.findElement(loginButton).isDisplayed(),
                "Login button is not displayed");

        System.out.println("PASS: Homepage content validation");
    }

    @Test
    public void verifyLoginButtonClick() {

        driver.get("https://elaraastudio.com/home");

        waitForPageReady();

        wait.until(
                ExpectedConditions.invisibilityOfElementLocated(
                        overlay));

        WebElement login = wait.until(
                ExpectedConditions.elementToBeClickable(
                        loginButton));

        Assert.assertTrue(login.isDisplayed());
        Assert.assertTrue(login.isEnabled());

        login.click();

        System.out.println("Login button clicked");

        // Wait for page or login form changes
        wait.until(driver ->
                !driver.findElements(
                        By.cssSelector("input, textarea")).isEmpty()
                || !driver.getCurrentUrl().endsWith("/home"));

        System.out.println(
                "URL after login: " + driver.getCurrentUrl());

        List<WebElement> fields = driver.findElements(
                By.cssSelector("input, textarea, select"));

        System.out.println("LOGIN FORM FIELDS:");

        int visibleFields = 0;

        for (WebElement field : fields) {

            if (field.isDisplayed()) {

                visibleFields++;

                System.out.println(
                        "Type: " + field.getAttribute("type"));

                System.out.println(
                        "Placeholder: " +
                        field.getAttribute("placeholder"));

                System.out.println(
                        "Name: " + field.getAttribute("name"));
            }
        }

        Assert.assertTrue(
                visibleFields > 0,
                "No visible login form fields found after clicking Login");

        System.out.println("PASS: Login form fields displayed");
    }

    @Test
    public void verifyCampaignPageLoads() {

        driver.get(
                "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.tagName("body")));

        Assert.assertFalse(
                driver.getTitle().trim().isEmpty());

        System.out.println("PASS: Campaign page loaded");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
