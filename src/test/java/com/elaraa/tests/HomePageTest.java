
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
        "button[aria-label='Login to your account']"
    );

    By loadingOverlay = By.cssSelector(
        ".global-loading-overlay"
    );

    By mobileNumber = By.cssSelector(
        "input[type='tel'][placeholder='Mobile Number*']"
    );

    By continueButton = By.xpath(
        "//button[normalize-space()='CONTINUE']"
    );

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(
            driver,
            Duration.ofSeconds(30)
        );
    }

    public void openHomePage() {

        driver.get("https://elaraastudio.com/home");

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(loginButton)
        );
    }

    public void waitUntilLoginIsUnblocked() {

        try {

            wait.until(d -> {

                List<WebElement> overlays =
                    d.findElements(loadingOverlay);

                for (WebElement overlay : overlays) {
                    if (overlay.isDisplayed()) {
                        return false;
                    }
                }

                WebElement button = d.findElement(loginButton);

                return (Boolean) ((JavascriptExecutor) d)
                    .executeScript(
                        "var r = arguments[0].getBoundingClientRect();" +
                        "var e = document.elementFromPoint(" +
                        "r.left + r.width/2, r.top + r.height/2);" +
                        "return e === arguments[0] || " +
                        "arguments[0].contains(e);",
                        button
                    );
            });

        } catch (TimeoutException e) {

            Assert.fail(
                "Login button is blocked by another element."
            );
        }
    }

    public void openLoginForm() {

        openHomePage();

        waitUntilLoginIsUnblocked();

        driver.findElement(loginButton).click();

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(mobileNumber)
        );
    }

    // TEST CASE 1: Verify homepage title and HTTPS URL

    @Test
    public void verifyHomePage() {

        openHomePage();

        String title = driver.getTitle();
        String url = driver.getCurrentUrl();

        System.out.println("Page title: " + title);
        System.out.println("Current URL: " + url);

        Assert.assertEquals(
            title,
            "Elaraa Studio",
            "Homepage title is incorrect"
        );

        Assert.assertTrue(
            url.startsWith("https://"),
            "Website is not using HTTPS"
        );
    }

    // TEST CASE 2: Verify homepage Login content

    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login = driver.findElement(loginButton);

        Assert.assertTrue(
            login.isDisplayed(),
            "Login button is not displayed"
        );

        Assert.assertTrue(
            login.getText().contains("Login"),
            "Login button text is incorrect"
        );

        System.out.println("Homepage body text:");
        System.out.println(
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST CASE 3: Verify Login button functionality

    @Test
    public void verifyLoginButtonClick() {

        openLoginForm();

        WebElement mobile = driver.findElement(mobileNumber);

        Assert.assertTrue(
            mobile.isDisplayed(),
            "Mobile number field is not displayed"
        );

        System.out.println("Login form opened successfully.");
        System.out.println(
            "Mobile placeholder: " +
            mobile.getAttribute("placeholder")
        );
    }

    // TEST CASE 4: Verify Campaign page

    @Test
    public void verifyCampaignPageLoads() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.tagName("body")
            )
        );

        String title = driver.getTitle();

        System.out.println("Campaign page title: " + title);
        System.out.println(
            "Campaign URL: " + driver.getCurrentUrl()
        );

        Assert.assertTrue(
            driver.getCurrentUrl().contains("filterdeal"),
            "Campaign page URL is incorrect"
        );

        Assert.assertFalse(
            driver.findElement(By.tagName("body"))
                .getText().trim().isEmpty(),
            "Campaign page body is empty"
        );
    }

    // TEST CASE 5: Verify empty mobile number validation

    @Test
    public void verifyEmptyMobileNumberValidation() {

        openLoginForm();

        WebElement mobile = driver.findElement(mobileNumber);

        Assert.assertEquals(
            mobile.getAttribute("value"),
            "",
            "Mobile number field is not empty initially"
        );

        driver.findElement(continueButton).click();

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        boolean valid = (Boolean) js.executeScript(
            "return arguments[0].validity.valid;",
            mobile
        );

        String message = (String) js.executeScript(
            "return arguments[0].validationMessage;",
            mobile
        );

        System.out.println("Required: " +
            mobile.getAttribute("required"));

        System.out.println("Input valid: " + valid);

        System.out.println(
            "Browser validation message: " + message
        );

        Assert.assertFalse(
            valid,
            "Empty mobile number should be rejected"
        );
    }

    // TEST CASE 6: Check invalid mobile number behaviour

    @Test
    public void verifyInvalidMobileNumberValidation() {

        openLoginForm();

        WebElement mobile = driver.findElement(mobileNumber);

        mobile.sendKeys("12345");

        System.out.println(
            "Entered mobile number: " +
            mobile.getAttribute("value")
        );

        driver.findElement(continueButton).click();

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        boolean valid = (Boolean) js.executeScript(
            "return arguments[0].validity.valid;",
            mobile
        );

        String message = (String) js.executeScript(
            "return arguments[0].validationMessage;",
            mobile
        );

        System.out.println(
            "Invalid number input valid: " + valid
        );

        System.out.println(
            "Browser validation message: " + message
        );

        System.out.println(
            "Final mobile number: " +
            mobile.getAttribute("value")
        );

        System.out.println(
            "Invalid mobile number test completed."
        );
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
