package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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

    By searchBox = By.cssSelector(
        "input[type='search']"
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

    // Open homepage

    public void openHomePage() {

        driver.get("https://elaraastudio.com/home");

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                loginButton
            )
        );
    }

    // Wait until Login button is not blocked

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

                WebElement button =
                    d.findElement(loginButton);

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

    // Open Login form

    public void openLoginForm() {

        openHomePage();

        waitUntilLoginIsUnblocked();

        driver.findElement(loginButton).click();

        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                mobileNumber
            )
        );
    }

    // TEST 1: Verify homepage title and HTTPS

    @Test
    public void verifyHomePage() {

        openHomePage();

        Assert.assertEquals(
            driver.getTitle(),
            "Elaraa Studio",
            "Homepage title is incorrect"
        );

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://"),
            "Website is not using HTTPS"
        );
    }

    // TEST 2: Verify homepage Login content

    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login =
            driver.findElement(loginButton);

        Assert.assertTrue(
            login.isDisplayed(),
            "Login button is not displayed"
        );

        Assert.assertTrue(
            login.getText().contains("Login"),
            "Login button text is incorrect"
        );

        System.out.println(
            "Homepage body: " +
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 3: Verify Login button functionality

    @Test
    public void verifyLoginButtonClick() {

        openLoginForm();

        WebElement mobile =
            driver.findElement(mobileNumber);

        Assert.assertTrue(
            mobile.isDisplayed(),
            "Mobile number field is not displayed"
        );

        System.out.println(
            "Login form opened successfully."
        );
    }

    // TEST 4: Investigate Campaign page

    @Test
    public void verifyCampaignPageLoads() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        WebDriverWait campaignWait =
            new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
            );

        WebElement root = campaignWait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("root")
            )
        );

        try {

            campaignWait.until(d ->
                !d.findElement(By.id("root"))
                    .getText().trim().isEmpty()
            );

        } catch (TimeoutException e) {

            System.out.println(
                "Campaign content did not appear within 20 seconds."
            );
        }

        String rootText =
            driver.findElement(By.id("root"))
                .getText().trim();

        System.out.println(
            "Campaign URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Campaign root content: " + rootText
        );

        System.out.println(
            "Campaign root HTML: " +
            root.getAttribute("innerHTML")
        );

        Assert.assertTrue(
            driver.getCurrentUrl().contains("filterdeal"),
            "Campaign URL is incorrect"
        );

        Assert.assertNotNull(
            root,
            "Campaign root element is missing"
        );
    }

    // TEST 5: Verify empty mobile number validation

    @Test
    public void verifyEmptyMobileNumberValidation() {

        openLoginForm();

        WebElement mobile =
            driver.findElement(mobileNumber);

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

        System.out.println("Input valid: " + valid);

        System.out.println(
            "Browser validation message: " + message
        );

        Assert.assertFalse(
            valid,
            "Empty mobile number should be rejected"
        );
    }

    // TEST 6: Investigate invalid mobile number

    @Test
    public void verifyInvalidMobileNumberValidation() {

        openLoginForm();

        WebElement mobile =
            driver.findElement(mobileNumber);

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
            "Browser validation valid: " + valid
        );

        System.out.println(
            "Browser validation message: " + message
        );

        System.out.println(
            "Final mobile number: " +
            mobile.getAttribute("value")
        );

        System.out.println(
            "Page response: " +
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 7: Verify search field visibility

    @Test
    public void verifySearchFieldVisibility() {

        openHomePage();

        WebElement search =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    searchBox
                )
            );

        Assert.assertTrue(
            search.isDisplayed(),
            "Search field is not displayed"
        );

        Assert.assertTrue(
            search.isEnabled(),
            "Search field is disabled"
        );

        System.out.println(
            "Search placeholder: " +
            search.getAttribute("placeholder")
        );
    }

    // TEST 8: Verify product search

    @Test
    public void verifyProductSearch() {

        openHomePage();

        WebElement search =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    searchBox
                )
            );

        search.sendKeys("shirt");

        System.out.println(
            "Entered search text: " +
            search.getAttribute("value")
        );

        search.sendKeys(Keys.ENTER);

        try {

            wait.until(d ->
                !d.getCurrentUrl().equals(
                    "https://elaraastudio.com/home"
                )
                || !d.findElement(By.tagName("body"))
                    .getText().trim().isEmpty()
            );

        } catch (TimeoutException e) {

            System.out.println(
                "No visible search response detected within timeout."
            );
        }

        System.out.println(
            "Search result URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Search result page: " +
            driver.findElement(By.tagName("body")).getText()
        );

        Assert.assertTrue(
            search.getAttribute("value").equals("shirt")
            || driver.getCurrentUrl().toLowerCase().contains("search"),
            "Search input or search navigation was not detected"
        );
    }

    // TEST 9: Verify empty search behaviour

    @Test
    public void verifyEmptySearch() {

        openHomePage();

        WebElement search =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    searchBox
                )
            );

        Assert.assertEquals(
            search.getAttribute("value"),
            "",
            "Search field is not empty initially"
        );

        search.sendKeys(Keys.ENTER);

        System.out.println(
            "URL after empty search: " +
            driver.getCurrentUrl()
        );

        System.out.println(
            "Page content after empty search: " +
            driver.findElement(By.tagName("body")).getText()
        );

        Assert.assertTrue(
            search.isDisplayed(),
            "Search field disappeared after empty search"
        );
    }

    // Close browser after every test

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
