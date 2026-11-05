package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    By loginButton = By.cssSelector(
        "button[aria-label='Login to your account']");

    By loadingOverlay = By.cssSelector(
        ".global-loading-overlay");

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

    public void openHomePage() {

        driver.get("https://elaraastudio.com/home");

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                loginButton));
    }

    // Check that the loading overlay does not cover Login.
    public void waitUntilLoginIsUnblocked() {

        try {

            wait.until(currentDriver -> {

                List<WebElement> overlays =
                    currentDriver.findElements(loadingOverlay);

                for (WebElement overlay : overlays) {

                    if (overlay.isDisplayed()) {
                        return false;
                    }
                }

                WebElement button =
                    currentDriver.findElement(loginButton);

                if (!button.isDisplayed()
                        || !button.isEnabled()) {
                    return false;
                }

                JavascriptExecutor js =
                    (JavascriptExecutor) currentDriver;

                return Boolean.TRUE.equals(
                    js.executeScript(
                        "const el = arguments[0];" +
                        "const r = el.getBoundingClientRect();" +
                        "const x = r.left + r.width / 2;" +
                        "const y = r.top + r.height / 2;" +
                        "const top = document.elementFromPoint(x, y);" +
                        "return top === el || el.contains(top);",
                        button));
            });

        } catch (TimeoutException e) {

            System.out.println(
                "DEFECT: Login button remains blocked.");

            List<WebElement> overlays =
                driver.findElements(loadingOverlay);

            for (WebElement overlay : overlays) {

                System.out.println(
                    "Overlay displayed: " +
                    overlay.isDisplayed());

                System.out.println(
                    "Overlay HTML: " +
                    overlay.getAttribute("outerHTML"));
            }

            Assert.fail(
                "Login blocked by loading overlay. " +
                "Website did not become ready within 30 seconds.");
        }
    }

    @Test
    public void verifyHomePage() {

        openHomePage();

        Assert.assertEquals(
            driver.getTitle(),
            "Elaraa Studio");

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://"));

        System.out.println(
            "PASS: Homepage title and URL validation");
    }

    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login =
            driver.findElement(loginButton);

        Assert.assertTrue(
            login.isDisplayed(),
            "Login button is not visible");

        Assert.assertTrue(
            login.getText().contains("Login"),
            "Login button text is incorrect");

        System.out.println(
            "PASS: Login button visibility and text");

        System.out.println(
            "Homepage body text: " +
            driver.findElement(By.tagName("body")).getText());
    }

    @Test
    public void verifyLoginButtonClick() {

        openHomePage();

        waitUntilLoginIsUnblocked();

        WebElement login =
            driver.findElement(loginButton);

        login.click();

        System.out.println(
            "PASS: Login button clicked successfully");

        System.out.println(
            "URL after click: " +
            driver.getCurrentUrl());

        List<WebElement> fields =
            driver.findElements(
                By.cssSelector("input, textarea, select"));

        System.out.println("VISIBLE FORM FIELDS:");

        for (WebElement field : fields) {

            if (field.isDisplayed()) {

                System.out.println(
                    "Type: " +
                    field.getAttribute("type"));

                System.out.println(
                    "Placeholder: " +
                    field.getAttribute("placeholder"));
            }
        }
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

        System.out.println(
            "PASS: Campaign page loaded");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
