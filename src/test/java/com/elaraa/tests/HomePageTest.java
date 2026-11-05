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

    By loginButton = By.cssSelector(
            "button[aria-label='Login to your account']");

    By mobileNumberField = By.cssSelector(
            "input[type='tel'][placeholder='Mobile Number*']");

    By searchField = By.cssSelector("input[type='search']");

    By wishlistButton = By.cssSelector(
            "button[aria-label='View wishlist']");

    By shoppingBagButton = By.cssSelector(
            "button[aria-label='View shopping cart']");

    By profileButton = By.cssSelector(
            "button[aria-label='View profile']");


    // BROWSER SETUP

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.manage().timeouts()
                .implicitlyWait(Duration.ZERO);

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }


    // COMMON HOMEPAGE METHOD

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


    // WAIT FOR ELEMENT TO BE VISIBLE

    public WebElement waitForVisible(By locator, int seconds) {

        WebDriverWait customWait =
                new WebDriverWait(driver, Duration.ofSeconds(seconds));

        return customWait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }


    // WAIT UNTIL ELEMENT IS NOT BLOCKED

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
                    "var e = arguments[0];" +
                    "var r = e.getBoundingClientRect();" +
                    "var x = r.left + r.width / 2;" +
                    "var y = r.top + r.height / 2;" +
                    "var top = document.elementFromPoint(x, y);" +
                    "return top === e || e.contains(top);",
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


    // TEST 1: VERIFY HOMEPAGE LOADS

    @Test(priority = 1)
    public void verifyHomePage() {

        openHomePage();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("elaraastudio.com"),
                "Homepage URL is incorrect");

        System.out.println("Homepage loaded successfully.");
    }


    // TEST 2: VERIFY HOMEPAGE TITLE

    @Test(priority = 2)
    public void verifyHomePageTitle() {

        openHomePage();

        Assert.assertTrue(
                driver.getTitle().contains("Elaraa Studio"),
                "Homepage title is incorrect");

        System.out.println("Homepage title verified.");
    }


    // TEST 3: VERIFY HOMEPAGE URL

    @Test(priority = 3)
    public void verifyHomePageURL() {

        openHomePage();

        Assert.assertEquals(
                driver.getCurrentUrl(),
                homeUrl,
                "Homepage URL does not match");

        System.out.println("Homepage URL verified.");
    }


    // TEST 4: VERIFY ROOT ELEMENT

    @Test(priority = 4)
    public void verifyRootElement() {

        openHomePage();

        WebElement root = waitForVisible(
                By.id("root"), 20);

        Assert.assertTrue(
                root.isDisplayed(),
                "Root element is not displayed");

        System.out.println("Root element verified.");
    }


    // TEST 5: VERIFY LOGIN BUTTON

    @Test(priority = 5)
    public void verifyLoginButton() {

        openHomePage();

        WebElement login = waitForVisible(
                loginButton, 20);

        Assert.assertTrue(
                login.isDisplayed(),
                "Login button is not displayed");

        System.out.println("Login button displayed successfully.");
    }


    // TEST 6: VERIFY LOGIN FORM OPENS

    @Test(priority = 6)
    public void verifyLoginFormOpens() {

        openLoginForm();

        Assert.assertTrue(
                driver.findElement(mobileNumberField).isDisplayed(),
                "Login form did not open");

        System.out.println("Login form opened successfully.");
    }


    // TEST 7: VERIFY MOBILE NUMBER FIELD

    @Test(priority = 7)
    public void verifyMobileNumberField() {

        openLoginForm();

        WebElement mobileField =
                driver.findElement(mobileNumberField);

        Assert.assertTrue(
                mobileField.isDisplayed(),
                "Mobile number field is not displayed");

        Assert.assertTrue(
                mobileField.isEnabled(),
                "Mobile number field is disabled");

        System.out.println("Mobile number field verified.");
    }


    // TEST 8: VERIFY MOBILE NUMBER FIELD ACCEPTS INPUT

    @Test(priority = 8)
    public void verifyMobileNumberInput() {

        openLoginForm();

        WebElement mobileField =
                driver.findElement(mobileNumberField);

        mobileField.sendKeys("9876543210");

        Assert.assertEquals(
                mobileField.getAttribute("value"),
                "9876543210",
                "Mobile number input was not entered correctly");

        System.out.println("Mobile number input verified.");
    }


    // TEST 9: VERIFY MOBILE NUMBER FIELD CAN BE CLEARED

    @Test(priority = 9)
    public void verifyMobileNumberFieldClear() {

        openLoginForm();

        WebElement mobileField =
                driver.findElement(mobileNumberField);

        mobileField.sendKeys("9876543210");
        mobileField.clear();

        Assert.assertEquals(
                mobileField.getAttribute("value"),
                "",
                "Mobile number field was not cleared");

        System.out.println("Mobile number field clear verified.");
    }


    // TEST 10: VERIFY WISHLIST BUTTON

    @Test(priority = 10)
    public void verifyWishlistButton() {

        openHomePage();

        waitUntilElementIsUnblocked(wishlistButton);

        WebElement wishlist =
                driver.findElement(wishlistButton);

        Assert.assertTrue(
                wishlist.isDisplayed(),
                "Wishlist button is not displayed");

        wishlist.click();

        WebElement mobileField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        mobileNumberField));

        Assert.assertTrue(
                mobileField.isDisplayed(),
                "Login form did not open after clicking Wishlist");

        System.out.println(
                "Wishlist button verified: Login form opened successfully.");
    }


    // TEST 11: VERIFY SHOPPING BAG BUTTON

    @Test(priority = 11)
    public void verifyShoppingBagButton() {

        openHomePage();

        waitUntilElementIsUnblocked(shoppingBagButton);

        WebElement bag =
                driver.findElement(shoppingBagButton);

        Assert.assertTrue(
                bag.isDisplayed(),
                "Shopping bag button is not displayed");

        String previousUrl = driver.getCurrentUrl();

        String previousPageText =
                driver.findElement(By.tagName("body")).getText();

        bag.click();

        wait.until(d -> {

            String currentUrl = d.getCurrentUrl();

            String currentPageText =
                    d.findElement(By.tagName("body")).getText();

            return !currentUrl.equals(previousUrl)
                    || !currentPageText.equals(previousPageText);
        });

        String currentUrl = driver.getCurrentUrl();

        String currentPageText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                !currentUrl.equals(previousUrl)
                        || !currentPageText.equals(previousPageText),
                "Shopping bag click produced no observable page change");

        System.out.println("Shopping bag button response verified.");
        System.out.println("Current URL: " + currentUrl);
        System.out.println("Updated page content:");
        System.out.println(currentPageText);
    }


    // TEST 12: VERIFY PROFILE BUTTON

    @Test(priority = 12)
    public void verifyProfileButton() {

        openHomePage();

        waitUntilElementIsUnblocked(profileButton);

        WebElement profile =
                driver.findElement(profileButton);

        Assert.assertTrue(
                profile.isDisplayed(),
                "Profile button is not displayed");

        String previousPageText =
                driver.findElement(By.tagName("body")).getText();

        profile.click();

        wait.until(d -> {

            String currentPageText =
                    d.findElement(By.tagName("body")).getText();

            return !currentPageText.equals(previousPageText)
                    && currentPageText.contains("View Profile");
        });

        String profileMenuText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                profileMenuText.contains("View Profile"),
                "View Profile option is not displayed");

        Assert.assertTrue(
                profileMenuText.contains("Orders"),
                "Orders option is not displayed");

        Assert.assertTrue(
                profileMenuText.contains("Wishlist"),
                "Wishlist option is not displayed");

        System.out.println("Profile menu verified successfully.");
        System.out.println(
                "View Profile, Orders and Wishlist are displayed.");
    }


    // TEST 13: VERIFY HOMEPAGE FOOTER

    @Test(priority = 13)
    public void verifyHomePageFooter() {

        openHomePage();

        String pageText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                pageText.contains("Chudidar Sets"),
                "Chudidar Sets is missing in footer");

        Assert.assertTrue(
                pageText.contains("Bottom Wear"),
                "Bottom Wear is missing in footer");

        Assert.assertTrue(
                pageText.contains("Kurtis"),
                "Kurtis is missing in footer");

        Assert.assertTrue(
                pageText.contains("Tops"),
                "Tops is missing in footer");

        Assert.assertTrue(
                pageText.contains("All rights reserved"),
                "Copyright text is missing");

        System.out.println("Homepage footer verified successfully.");
    }


    // TEST 14: VERIFY KURTIS CATEGORY NAVIGATION

    @Test(priority = 14)
    public void verifyKurtisCategoryNavigation() {

        openHomePage();

        Boolean clicked = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var elements = Array.from(" +
                "document.querySelectorAll('#root a, #root button, " +
                "#root [role=button]'));" +

                "var element = elements.find(e => " +
                "e.innerText.trim() === 'Kurtis' && " +
                "e.getClientRects().length > 0);" +

                "if (!element) return false;" +

                "element.click();" +

                "return true;"
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(clicked),
                "Kurtis category was not found");

        wait.until(d ->
                d.getCurrentUrl().contains("categoryId=2"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("categoryId=2"),
                "Kurtis category navigation failed");

        System.out.println("Kurtis category navigation verified.");
        System.out.println("Current URL: " + driver.getCurrentUrl());
    }


    // TEST 15: VERIFY HOMEPAGE IMAGES

    @Test(priority = 15)
    public void verifyHomepageImagesDisplayed() {

        openHomePage();

        wait.until(d -> {

            List<WebElement> images =
                    d.findElements(By.cssSelector("#root img"));

            for (WebElement image : images) {

                Boolean loaded = (Boolean)
                        ((JavascriptExecutor) d).executeScript(
                                "return arguments[0].complete && " +
                                "arguments[0].naturalWidth > 0;",
                                image);

                if (Boolean.TRUE.equals(loaded)) {
                    return true;
                }
            }

            return false;
        });

        List<WebElement> images =
                driver.findElements(By.cssSelector("#root img"));

        int loadedImages = 0;

        for (WebElement image : images) {

            Boolean loaded = (Boolean)
                    ((JavascriptExecutor) driver).executeScript(
                            "return arguments[0].complete && " +
                            "arguments[0].naturalWidth > 0;",
                            image);

            if (Boolean.TRUE.equals(loaded)) {
                loadedImages++;
            }
        }

        Assert.assertTrue(
                loadedImages > 0,
                "No homepage images loaded successfully");

        System.out.println(
                "Loaded homepage images: " + loadedImages);
    }


    // COMMON CATEGORY NAVIGATION METHOD

    public void verifyCategoryNavigation(String categoryName) {

        openHomePage();

        Boolean clicked = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var name = arguments[0];" +

                "var elements = Array.from(" +
                "document.querySelectorAll(" +
                "#root a, #root button, #root [role='button'], " +
                "#root [onclick]'));" +

                "var element = elements.find(e => " +
                "e.innerText.trim() === name && " +
                "e.getClientRects().length > 0);" +

                "if (!element) return false;" +

                "element.scrollIntoView({block:'center'});" +

                "element.click();" +

                "return true;",
                categoryName
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(clicked),
                categoryName + " category was not found");

        wait.until(d ->
                d.getCurrentUrl().contains("categoryId="));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("categoryId="),
                categoryName + " navigation failed");

        System.out.println(
                categoryName + " category navigation verified.");

        System.out.println(
                "Current URL: " + driver.getCurrentUrl());
    }


    // TEST 16: VERIFY BOTTOM WEAR CATEGORY

    @Test(priority = 16)
    public void verifyBottomWearCategoryNavigation() {

        verifyCategoryNavigation("Bottom Wear");
    }


    // TEST 17: VERIFY CHUDIDAR SETS CATEGORY

    @Test(priority = 17)
    public void verifyChudidarSetsCategoryNavigation() {

        verifyCategoryNavigation("Chudidar Sets");
    }


    // TEST 18: VERIFY TOPS CATEGORY

    @Test(priority = 18)
    public void verifyTopsCategoryNavigation() {

        verifyCategoryNavigation("Tops");
    }


    // TEST 19: VERIFY PRODUCT DETAILS PAGE

    @Test(priority = 19)
    public void verifyProductDetailsPage() {

        openHomePage();

        String homepageUrl = driver.getCurrentUrl();

        Boolean productClicked = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var links = Array.from(" +
                "document.querySelectorAll('#root a'));" +

                "var productLink = links.find(a => {" +

                "var img = a.querySelector('img');" +

                "if (!img || !img.getClientRects().length) " +
                "return false;" +

                "if (!a.href || a.href === window.location.href) " +
                "return false;" +

                "if (a.href.includes('filterdeal')) return false;" +

                "return img.complete && img.naturalWidth > 0;" +

                "});" +

                "if (!productLink) return false;" +

                "productLink.scrollIntoView({block:'center'});" +

                "productLink.click();" +

                "return true;"
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(productClicked),
                "No clickable product image was found");

        // WAIT FOR NAVIGATION

        wait.until(d ->
                !d.getCurrentUrl().equals(homepageUrl));

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        By.id("root")));

        // VERIFY PRODUCT IMAGE

        wait.until(d -> {

            List<WebElement> images =
                    d.findElements(By.cssSelector("#root img"));

            for (WebElement image : images) {

                if (image.isDisplayed()) {

                    Boolean loaded = (Boolean)
                            ((JavascriptExecutor) d).executeScript(
                                    "return arguments[0].complete && " +
                                    "arguments[0].naturalWidth > 0;",
                                    image);

                    if (Boolean.TRUE.equals(loaded)) {
                        return true;
                    }
                }
            }

            return false;
        });

        Assert.assertNotEquals(
                driver.getCurrentUrl(),
                homepageUrl,
                "Product details page did not open");

        System.out.println(
                "Product details page opened successfully.");

        System.out.println(
                "Product URL: " + driver.getCurrentUrl());

        System.out.println(
                "Product image loaded successfully.");
    }


    // BROWSER TEARDOWN

    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
