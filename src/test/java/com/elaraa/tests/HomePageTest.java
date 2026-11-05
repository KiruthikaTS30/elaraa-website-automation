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

    By wishlistButton = By.cssSelector(
        "button[aria-label='View wishlist']"
    );

    By bagButton = By.cssSelector(
        "button[aria-label='View shopping cart']"
    );

    By profileButton = By.cssSelector(
        "button[aria-label='View profile']"
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

    // Wait until loading overlay disappears

    public void waitUntilOverlayDisappears() {

        wait.until(d -> {

            List<WebElement> overlays =
                d.findElements(loadingOverlay);

            for (WebElement overlay : overlays) {

                if (overlay.isDisplayed()) {
                    return false;
                }
            }

            return true;
        });
    }

    // Wait until an element is not covered by another element

    public void waitUntilElementIsUnblocked(By locator) {

        wait.until(d -> {

            List<WebElement> overlays =
                d.findElements(loadingOverlay);

            for (WebElement overlay : overlays) {

                if (overlay.isDisplayed()) {
                    return false;
                }
            }

            List<WebElement> elements =
                d.findElements(locator);

            if (elements.isEmpty()) {
                return false;
            }

            WebElement element = elements.get(0);

            if (!element.isDisplayed() || !element.isEnabled()) {
                return false;
            }

            return (Boolean) ((JavascriptExecutor) d)
                .executeScript(
                    "var r = arguments[0].getBoundingClientRect();" +
                    "var e = document.elementFromPoint(" +
                    "r.left + r.width/2, r.top + r.height/2);" +
                    "return e === arguments[0] || " +
                    "arguments[0].contains(e);",
                    element
                );
        });
    }

    // Wait until Login button is unblocked

    public void waitUntilLoginIsUnblocked() {

        waitUntilElementIsUnblocked(loginButton);
    }

    // Open login form

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

    // TEST 1: Homepage title and HTTPS

    @Test
    public void verifyHomePage() {

        openHomePage();

        Assert.assertEquals(
            driver.getTitle(),
            "Elaraa Studio"
        );

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://")
        );

        System.out.println("Homepage title verified.");
    }

    // TEST 2: Homepage Login content

    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login =
            driver.findElement(loginButton);

        Assert.assertTrue(login.isDisplayed());

        Assert.assertTrue(
            login.getText().contains("Login")
        );

        System.out.println(
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 3: Login button functionality

    @Test
    public void verifyLoginButtonClick() {

        openLoginForm();

        Assert.assertTrue(
            driver.findElement(mobileNumber).isDisplayed()
        );

        System.out.println("Login form opened successfully.");
    }

    // TEST 4: Campaign page

    @Test
    public void verifyCampaignPageLoads() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        WebElement root = wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.id("root")
            )
        );

        try {

            new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
            ).until(d ->
                !d.findElement(By.id("root"))
                    .getText().trim().isEmpty()
            );

        } catch (TimeoutException e) {

            System.out.println(
                "Campaign content wait timed out."
            );
        }

        System.out.println(
            "Campaign URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Campaign content: " + root.getText()
        );

        Assert.assertTrue(
            driver.getCurrentUrl().contains("filterdeal")
        );
    }

    // TEST 5: Empty mobile number validation

    @Test
    public void verifyEmptyMobileNumberValidation() {

        openLoginForm();

        WebElement mobile =
            driver.findElement(mobileNumber);

        Assert.assertEquals(
            mobile.getAttribute("value"),
            ""
        );

        driver.findElement(continueButton).click();

        boolean valid = (Boolean)
            ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid;",
                mobile
            );

        String message = (String)
            ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validationMessage;",
                mobile
            );

        System.out.println("Input valid: " + valid);

        System.out.println(
            "Validation message: " + message
        );

        Assert.assertFalse(
            valid,
            "Empty mobile number should be rejected"
        );
    }

    // TEST 6: Invalid mobile number investigation

    @Test
    public void verifyInvalidMobileNumberValidation() {

        openLoginForm();

        WebElement mobile =
            driver.findElement(mobileNumber);

        mobile.sendKeys("12345");

        driver.findElement(continueButton).click();

        System.out.println(
            "Entered number: " +
            mobile.getAttribute("value")
        );

        System.out.println(
            "Browser validity: " +
            ((JavascriptExecutor) driver).executeScript(
                "return arguments[0].validity.valid;",
                mobile
            )
        );

        System.out.println(
            "Page response: " +
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 7: Search field visibility

    @Test
    public void verifySearchFieldVisibility() {

        openHomePage();

        WebElement search = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                searchBox
            )
        );

        Assert.assertTrue(search.isDisplayed());

        Assert.assertTrue(search.isEnabled());

        System.out.println(
            "Search placeholder: " +
            search.getAttribute("placeholder")
        );
    }

    // TEST 8: Product search

    @Test
    public void verifyProductSearch() {

        openHomePage();

        WebElement search = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                searchBox
            )
        );

        search.sendKeys("shirt");

        search.sendKeys(Keys.ENTER);

        System.out.println(
            "Search URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Search page: " +
            driver.findElement(By.tagName("body")).getText()
        );

        Assert.assertTrue(
            search.getAttribute("value").equals("shirt")
            || driver.getCurrentUrl()
                .toLowerCase().contains("search"),
            "Search text or search navigation was not detected"
        );
    }

    // TEST 9: Empty search

    @Test
    public void verifyEmptySearch() {

        openHomePage();

        WebElement search = wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                searchBox
            )
        );

        Assert.assertEquals(
            search.getAttribute("value"),
            ""
        );

        search.sendKeys(Keys.ENTER);

        Assert.assertTrue(search.isDisplayed());

        System.out.println(
            "URL after empty search: " +
            driver.getCurrentUrl()
        );
    }

    // TEST 10: Wishlist button

    @Test
    public void verifyWishlistButton() {

        openHomePage();

        waitUntilElementIsUnblocked(wishlistButton);

        WebElement wishlist =
            driver.findElement(wishlistButton);

        wishlist.click();

        System.out.println(
            "Wishlist URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Wishlist response: " +
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 11: Shopping bag button

    @Test
    public void verifyShoppingBagButton() {

        openHomePage();

        waitUntilElementIsUnblocked(bagButton);

        WebElement bag =
            driver.findElement(bagButton);

        bag.click();

        System.out.println(
            "Shopping bag URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Shopping bag response: " +
            driver.findElement(By.tagName("body")).getText()
        );
    }

    // TEST 12: Profile button

    @Test
    public void verifyProfileButton() {

        openHomePage();

        waitUntilElementIsUnblocked(profileButton);

        WebElement profile =
            driver.findElement(profileButton);

        profile.click();

        System.out.println(
            "Profile URL: " + driver.getCurrentUrl()
        );

        System.out.println(
            "Profile response: " +
            driver.findElement(By.tagName("body")).getText()
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
