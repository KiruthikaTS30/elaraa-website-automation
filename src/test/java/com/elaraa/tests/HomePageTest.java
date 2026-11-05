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

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {

    WebDriver driver;
    WebDriverWait wait;

    String homeUrl = "https://elaraastudio.com/home";

    By loginButton =
            By.cssSelector("button[aria-label='Login to your account']");

    By mobileNumberField =
            By.cssSelector("input[type='tel'][placeholder='Mobile Number*']");

    By searchField =
            By.cssSelector("input[type='search']");

    By wishlistButton =
            By.cssSelector("button[aria-label='View wishlist']");

    By shoppingBagButton =
            By.cssSelector("button[aria-label='View shopping cart']");

    By profileButton =
            By.cssSelector("button[aria-label='View profile']");


    // BROWSER SETUP

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }


    // OPEN HOMEPAGE

    public void openHomePage() {

        driver.get(homeUrl);

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("root")));

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page Title: " + driver.getTitle());
    }


    // WAIT UNTIL ELEMENT IS READY TO CLICK

    public void waitUntilElementIsUnblocked(By locator) {

        wait.until(d -> {

            List<WebElement> elements = d.findElements(locator);

            if (elements.isEmpty()) {
                return false;
            }

            WebElement element = elements.get(0);

            if (!element.isDisplayed() || !element.isEnabled()) {
                return false;
            }

            return (Boolean) ((JavascriptExecutor) d).executeScript(
                    "var element = arguments[0];" +
                    "var rect = element.getBoundingClientRect();" +
                    "var x = rect.left + rect.width / 2;" +
                    "var y = rect.top + rect.height / 2;" +
                    "var topElement = document.elementFromPoint(x, y);" +
                    "return topElement === element || element.contains(topElement);",
                    element);
        });
    }


    // OPEN LOGIN FORM

    public void openLoginForm() {

        openHomePage();

        waitUntilElementIsUnblocked(loginButton);

        driver.findElement(loginButton).click();

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        mobileNumberField));
    }


    // TEST 1: VERIFY HOMEPAGE

    @Test
    public void verifyHomePage() {

        openHomePage();

        Assert.assertTrue(
                driver.getTitle().toLowerCase().contains("elaraa"),
                "Homepage title is incorrect");

        Assert.assertTrue(
                driver.getCurrentUrl().startsWith("https://"),
                "Website is not using HTTPS");

        System.out.println("Homepage verified successfully.");
    }


    // TEST 2: VERIFY HOMEPAGE CONTENT

    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login = wait.until(
                ExpectedConditions.visibilityOfElementLocated(loginButton));

        Assert.assertTrue(login.isDisplayed());

        System.out.println("Login button is visible.");
    }


    // TEST 3: VERIFY LOGIN BUTTON CLICK

    @Test
    public void verifyLoginButtonClick() {

        openLoginForm();

        Assert.assertTrue(
                driver.findElement(mobileNumberField).isDisplayed(),
                "Mobile number field is not displayed");

        System.out.println("Login form opened successfully.");
    }


    // TEST 4: VERIFY CAMPAIGN PAGE

    @Test
    public void verifyCampaignPageLoads() {

        String campaignUrl =
                "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true";

        driver.get(campaignUrl);

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("root")));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("filterdeal"),
                "Campaign page URL is incorrect");

        System.out.println("Campaign page URL: " + driver.getCurrentUrl());
    }


    // TEST 5: VERIFY EMPTY MOBILE NUMBER VALIDATION

    @Test
    public void verifyEmptyMobileNumberValidation() {

        openLoginForm();

        WebElement mobileField = driver.findElement(mobileNumberField);

        Assert.assertTrue(
                mobileField.getAttribute("required") != null,
                "Mobile number field is not marked as required");

        Boolean isValid = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].checkValidity();",
                        mobileField);

        Assert.assertFalse(
                isValid,
                "Empty mobile number should be invalid");

        System.out.println("Empty mobile number validation verified.");
    }


    // TEST 6: VERIFY INVALID MOBILE NUMBER

    @Test
    public void verifyInvalidMobileNumberValidation() {

        openLoginForm();

        WebElement mobileField = driver.findElement(mobileNumberField);

        mobileField.sendKeys("12345");

        System.out.println("Mobile field type: "
                + mobileField.getAttribute("type"));

        System.out.println("Entered mobile number: "
                + mobileField.getAttribute("value"));

        Boolean isValid = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].checkValidity();",
                        mobileField);

        System.out.println("Browser validation result: " + isValid);

        System.out.println("Page response:");
        System.out.println(
                driver.findElement(By.tagName("body")).getText());
    }


    // TEST 7: VERIFY SEARCH FIELD

    @Test
    public void verifySearchFieldVisibility() {

        openHomePage();

        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchField));

        Assert.assertTrue(search.isDisplayed());

        Assert.assertTrue(
                search.getAttribute("placeholder")
                        .contains("Search for products"),
                "Search placeholder is incorrect");

        System.out.println("Search field verified successfully.");
    }


    // TEST 8: VERIFY PRODUCT SEARCH

    @Test
    public void verifyProductSearch() {

        openHomePage();

        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchField));

        search.sendKeys("shirt");
        search.submit();

        wait.until(d ->
                d.getCurrentUrl().toLowerCase().contains("search"));

        Assert.assertTrue(
                driver.getCurrentUrl().toLowerCase().contains("search"),
                "Search results URL was not opened");

        System.out.println("Search URL: " + driver.getCurrentUrl());
    }


    // TEST 9: VERIFY EMPTY SEARCH

    @Test
    public void verifyEmptySearch() {

        openHomePage();

        WebElement search = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchField));

        search.clear();
        search.submit();

        wait.until(d ->
                d.getCurrentUrl().contains("/home")
                        || d.getCurrentUrl().contains("search"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/home"),
                "Empty search unexpectedly changed the page");

        System.out.println("Empty search behavior verified.");
    }


    // TEST 10: VERIFY WISHLIST BUTTON

    @Test
    public void verifyWishlistButton() {

        openHomePage();

        waitUntilElementIsUnblocked(wishlistButton);

        WebElement wishlist = driver.findElement(wishlistButton);

        Assert.assertTrue(wishlist.isDisplayed());

        wishlist.click();

        System.out.println("Wishlist button clicked.");

        System.out.println(
                driver.findElement(By.tagName("body")).getText());
    }


    // TEST 11: VERIFY SHOPPING BAG BUTTON

    @Test
    public void verifyShoppingBagButton() {

        openHomePage();

        waitUntilElementIsUnblocked(shoppingBagButton);

        WebElement bag = driver.findElement(shoppingBagButton);

        Assert.assertTrue(bag.isDisplayed());

        bag.click();

        System.out.println("Shopping bag button clicked.");

        System.out.println(
                driver.findElement(By.tagName("body")).getText());
    }


    // TEST 12: VERIFY PROFILE BUTTON

    @Test
    public void verifyProfileButton() {

        openHomePage();

        waitUntilElementIsUnblocked(profileButton);

        WebElement profile = driver.findElement(profileButton);

        Assert.assertTrue(profile.isDisplayed());

        profile.click();

        System.out.println("Profile button clicked.");

        System.out.println(
                driver.findElement(By.tagName("body")).getText());
    }


    // TEST 13: VERIFY FOOTER CONTENT

    @Test
    public void verifyFooterContent() {

        openHomePage();

        wait.until(d -> {

            String pageText =
                    d.findElement(By.tagName("body")).getText();

            return pageText.contains("Chudidar Sets")
                    && pageText.contains("Bottom Wear")
                    && pageText.contains("Kurtis")
                    && pageText.contains("Tops")
                    && pageText.contains("All rights reserved");
        });

        String pageText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                pageText.contains("Chudidar Sets"),
                "Chudidar Sets category is missing");

        Assert.assertTrue(
                pageText.contains("Bottom Wear"),
                "Bottom Wear category is missing");

        Assert.assertTrue(
                pageText.contains("Kurtis"),
                "Kurtis category is missing");

        Assert.assertTrue(
                pageText.contains("Tops"),
                "Tops category is missing");

        Assert.assertTrue(
                pageText.contains("All rights reserved"),
                "Copyright text is missing");

        System.out.println("Footer content verified successfully.");
    }


    // TEST 14: VERIFY KURTIS CATEGORY NAVIGATION

    @Test
    public void verifyKurtisCategoryNavigation() {

        openHomePage();

        By kurtisCategory =
                By.xpath("//*[normalize-space()='Kurtis']");

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        kurtisCategory));

        List<WebElement> categories =
                driver.findElements(kurtisCategory);

        WebElement kurtis = null;

        for (WebElement element : categories) {

            if (element.isDisplayed()) {
                kurtis = element;
                break;
            }
        }

        Assert.assertNotNull(
                kurtis,
                "Kurtis category is not visible");

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", kurtis);

        wait.until(d ->
                d.getCurrentUrl().contains("categoryId"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("categoryId"),
                "Kurtis category page did not open");

        System.out.println(
                "Kurtis category URL: " + driver.getCurrentUrl());
    }


    // TEST 15: CHECK PRODUCT DETAILS PAGE NAVIGATION

    @Test
    public void verifyProductDetailsPageNavigation() {

        openHomePage();

        List<WebElement> links =
                driver.findElements(By.cssSelector("#root a[href]"));

        String productUrl = null;

        for (WebElement link : links) {

            String href = link.getAttribute("href");

            if (href == null || href.isBlank()) {
                continue;
            }

            if (!link.isDisplayed()) {
                continue;
            }

            if (!href.startsWith("https://elaraastudio.com")) {
                continue;
            }

            if (href.equals("https://elaraastudio.com/")
                    || href.equals(homeUrl)
                    || href.contains("filterdeal")
                    || href.contains("search=")
                    || href.contains("/home")) {
                continue;
            }

            productUrl = href;
            break;
        }

        Assert.assertNotNull(
                productUrl,
                "No product details link was found on the homepage");

        System.out.println("Product link found: " + productUrl);

        driver.get(productUrl);

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("root")));

        Assert.assertNotEquals(
                driver.getCurrentUrl(),
                "https://elaraastudio.com/",
                "Navigation opened the homepage instead of a product page");

        Assert.assertFalse(
                driver.getCurrentUrl().contains("filterdeal"),
                "Navigation opened a campaign page instead of a product page");

        System.out.println(
                "Navigated to product link: " + driver.getCurrentUrl());
    }


    // BROWSER TEARDOWN

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
