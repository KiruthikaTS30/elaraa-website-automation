
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

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(3));

        driver.manage().window().maximize();
    }

    // Open homepage
    public void openHomePage() {

        driver.get(homeUrl);

        wait.until(d ->
                ((JavascriptExecutor) d)
                        .executeScript("return document.readyState")
                        .equals("complete"));

        wait.until(ExpectedConditions
                .visibilityOfElementLocated(By.id("root")));
    }

    // Wait for visible element
    public WebElement waitForVisible(By locator) {

        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait until element is not blocked
    public void waitUntilElementIsUnblocked(By locator) {

        wait.until(d -> {

            List<WebElement> elements = d.findElements(locator);

            if (elements.isEmpty()) {
                return false;
            }

            WebElement element = elements.get(0);

            return element.isDisplayed() && element.isEnabled();
        });
    }

    // Open login form
    public void openLoginForm() {

        openHomePage();

        By loginButton =
                By.cssSelector("button[aria-label='Login to your account']");

        waitUntilElementIsUnblocked(loginButton);

        driver.findElement(loginButton).click();

        wait.until(d ->
                d.findElements(
                        By.cssSelector("input[type='tel']")
                ).size() > 0);
    }

    // Click category from the website menu
    public void verifyCategoryNavigation(String categoryName) {

        openHomePage();

        // Open hamburger menu
        Boolean menuOpened = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var elements = Array.from(document.querySelectorAll(" +
                "'button, a, [role=\"button\"]'));" +

                "var menu = elements.find(e => {" +
                "var label = ((e.getAttribute('aria-label') || '') + ' ' +" +
                "(e.getAttribute('title') || '') + ' ' +" +
                "(e.innerText || '')).toLowerCase();" +
                "return e.getClientRects().length > 0 && " +
                "(label.includes('menu') || label.includes('navigation'));" +
                "});" +

                "if (!menu) {" +
                "menu = elements.find(e => {" +
                "var r = e.getBoundingClientRect();" +
                "return e.getClientRects().length > 0 && " +
                "r.right > window.innerWidth - 100 && " +
                "r.width > 20 && r.width < 100;" +
                "});" +
                "}" +

                "if (!menu) return false;" +
                "menu.click();" +
                "return true;"
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(menuOpened),
                "Hamburger menu was not found"
        );

        // Find and click category text
        Boolean categoryClicked = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var name = arguments[0];" +

                "var elements = Array.from(document.querySelectorAll('#root *'));" +

                "var element = elements.find(e => " +
                "e.children.length === 0 && " +
                "e.innerText && " +
                "e.innerText.trim().toLowerCase() === name.toLowerCase() && " +
                "e.getClientRects().length > 0);" +

                "if (!element) return false;" +

                "var target = element.closest(" +
                "'a, button, [role=\"button\"], [onclick]') || element;" +

                "target.scrollIntoView({block:'center'});" +
                "target.click();" +

                "return true;",

                categoryName
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(categoryClicked),
                categoryName + " category was not found"
        );

        wait.until(d -> {

            String url = d.getCurrentUrl();

            String body = d.findElement(By.tagName("body")).getText();

            return url.contains("category") ||
                   url.contains("filter") ||
                   body.toLowerCase().contains(categoryName.toLowerCase());
        });

        Assert.assertTrue(
                driver.findElement(By.tagName("body"))
                        .getText()
                        .toLowerCase()
                        .contains(categoryName.toLowerCase())
                || driver.getCurrentUrl().toLowerCase().contains("category"),
                categoryName + " category navigation failed"
        );
    }

    // 1. Verify homepage loads
    @Test(priority = 1)
    public void verifyHomePageLoads() {

        openHomePage();

        Assert.assertTrue(
                driver.findElement(By.id("root")).isDisplayed(),
                "Homepage is not displayed"
        );

        System.out.println("Homepage loaded successfully");
    }

    // 2. Verify page title
    @Test(priority = 2)
    public void verifyHomePageTitle() {

        openHomePage();

        Assert.assertTrue(
                driver.getTitle().toLowerCase().contains("elaraa"),
                "Homepage title is incorrect"
        );

        System.out.println("Homepage title verified");
    }

    // 3. Verify homepage URL
    @Test(priority = 3)
    public void verifyHomePageURL() {

        openHomePage();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("elaraastudio.com"),
                "Incorrect homepage URL"
        );

        System.out.println("Homepage URL verified");
    }

    // 4. Verify root element
    @Test(priority = 4)
    public void verifyRootElement() {

        openHomePage();

        Assert.assertTrue(
                driver.findElement(By.id("root")).isDisplayed(),
                "Root element is missing"
        );

        System.out.println("Root element verified");
    }

    // 5. Verify login button
    @Test(priority = 5)
    public void verifyLoginButton() {

        openHomePage();

        By loginButton =
                By.cssSelector("button[aria-label='Login to your account']");

        Assert.assertTrue(
                driver.findElements(loginButton).size() > 0,
                "Login button was not found"
        );

        System.out.println("Login button verified");
    }

    // 6. Verify login form
    @Test(priority = 6)
    public void verifyLoginForm() {

        openLoginForm();

        Assert.assertTrue(
                driver.findElement(
                        By.cssSelector("input[type='tel']")
                ).isDisplayed(),
                "Login form was not displayed"
        );

        System.out.println("Login form verified");
    }

    // 7. Verify mobile number field
    @Test(priority = 7)
    public void verifyMobileNumberField() {

        openLoginForm();

        WebElement mobile = waitForVisible(
                By.cssSelector("input[type='tel']")
        );

        Assert.assertTrue(mobile.isDisplayed());

        System.out.println("Mobile number field verified");
    }

    // 8. Verify mobile number input
    @Test(priority = 8)
    public void verifyMobileNumberInput() {

        openLoginForm();

        WebElement mobile = waitForVisible(
                By.cssSelector("input[type='tel']")
        );

        mobile.sendKeys("9876543210");

        Assert.assertEquals(
                mobile.getAttribute("value"),
                "9876543210"
        );

        System.out.println("Mobile number input verified");
    }

    // 9. Verify mobile number field clear
    @Test(priority = 9)
    public void verifyMobileNumberFieldClear() {

        openLoginForm();

        WebElement mobile = waitForVisible(
                By.cssSelector("input[type='tel']")
        );

        mobile.sendKeys("9876543210");
        mobile.clear();

        Assert.assertEquals(
                mobile.getAttribute("value"),
                ""
        );

        System.out.println("Mobile number field clear verified");
    }

    // 10. Verify wishlist button
    @Test(priority = 10)
    public void verifyWishlistButton() {

        openHomePage();

        By wishlist =
                By.cssSelector("button[aria-label='View wishlist']");

        waitUntilElementIsUnblocked(wishlist);

        driver.findElement(wishlist).click();

        wait.until(d ->
                d.findElements(
                        By.cssSelector("input[type='tel']")
                ).size() > 0
        );

        Assert.assertTrue(
                driver.findElement(
                        By.cssSelector("input[type='tel']")
                ).isDisplayed(),
                "Login prompt was not displayed after wishlist click"
        );

        System.out.println("Wishlist login prompt verified");
    }

    // 11. Verify shopping bag button
    @Test(priority = 11)
    public void verifyShoppingBagButton() {

        openHomePage();

        By cart =
                By.cssSelector("button[aria-label='View shopping cart']");

        waitUntilElementIsUnblocked(cart);

        String oldUrl = driver.getCurrentUrl();

        driver.findElement(cart).click();

        wait.until(d ->
                !d.getCurrentUrl().equals(oldUrl)
                || d.findElements(
                        By.cssSelector("input[type='tel']")
                ).size() > 0
        );

        Assert.assertTrue(
                !driver.getCurrentUrl().equals(oldUrl)
                || driver.findElements(
                        By.cssSelector("input[type='tel']")
                ).size() > 0,
                "Shopping bag did not respond"
        );

        System.out.println("Shopping bag response verified");
    }

    // 12. Verify profile button
    @Test(priority = 12)
    public void verifyProfileButton() {

        openHomePage();

        By profile =
                By.cssSelector("button[aria-label='View profile']");

        waitUntilElementIsUnblocked(profile);

        driver.findElement(profile).click();

        wait.until(d ->
                d.findElement(By.tagName("body"))
                        .getText()
                        .contains("View Profile")
        );

        String bodyText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(bodyText.contains("View Profile"));
        Assert.assertTrue(bodyText.contains("Orders"));
        Assert.assertTrue(bodyText.contains("Wishlist"));

        System.out.println("Profile menu verified");
    }

    // 13. Verify homepage footer
    @Test(priority = 13)
    public void verifyHomePageFooter() {

        openHomePage();

        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, document.body.scrollHeight);"
        );

        wait.until(d ->
                d.findElement(By.tagName("body"))
                        .getText()
                        .contains("POPULAR CATEGORIES")
        );

        String footerText =
                driver.findElement(By.tagName("body")).getText();

        Assert.assertTrue(
                footerText.contains("POPULAR CATEGORIES"),
                "Popular categories heading missing"
        );

        Assert.assertTrue(
                footerText.contains("Chudidar Sets"),
                "Chudidar Sets is missing in footer"
        );

        Assert.assertTrue(
                footerText.contains("Bottom Wear"),
                "Bottom Wear is missing in footer"
        );

        Assert.assertTrue(
                footerText.contains("Kurtis"),
                "Kurtis is missing in footer"
        );

        Assert.assertTrue(
                footerText.contains("Tops"),
                "Tops is missing in footer"
        );

        System.out.println("Homepage footer verified");
    }

    // 14. Verify Kurtis category
    @Test(priority = 14)
    public void verifyKurtisCategoryNavigation() {

        verifyCategoryNavigation("Kurtis");

        System.out.println("Kurtis category verified");
    }

    // 15. Verify homepage images
    @Test(priority = 15)
    public void verifyHomepageImagesDisplayed() {

        openHomePage();

        wait.until(d ->
                d.findElements(By.cssSelector("#root img")).size() > 0
        );

        List<WebElement> images =
                driver.findElements(By.cssSelector("#root img"));

        int loadedImages = 0;

        for (WebElement image : images) {

            Boolean loaded = (Boolean)
                    ((JavascriptExecutor) driver).executeScript(
                    "return arguments[0].complete && " +
                    "arguments[0].naturalWidth > 0;",
                    image
            );

            if (Boolean.TRUE.equals(loaded)) {
                loadedImages++;
            }
        }

        Assert.assertTrue(
                loadedImages > 0,
                "No homepage images loaded"
        );

        System.out.println(
                "Homepage images loaded: " + loadedImages
        );
    }

    // 16. Verify Bottom Wear category
    @Test(priority = 16)
    public void verifyBottomWearCategoryNavigation() {

        verifyCategoryNavigation("Bottom Wear");

        System.out.println("Bottom Wear category verified");
    }

    // 17. Verify Chudidar Sets category
    @Test(priority = 17)
    public void verifyChudidarSetsCategoryNavigation() {

        verifyCategoryNavigation("Chudidar Sets");

        System.out.println("Chudidar Sets category verified");
    }

    // 18. Verify Tops category
    @Test(priority = 18)
    public void verifyTopsCategoryNavigation() {

        verifyCategoryNavigation("Tops");

        System.out.println("Tops category verified");
    }

    // 19. Verify product details page
    @Test(priority = 19)
    public void verifyProductDetailsPage() {

        openHomePage();

        // Scroll down to product section
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollTo(0, 650);"
        );

        wait.until(d ->
                d.findElements(By.cssSelector("#root img")).size() > 0
        );

        // Find a visible product image and click its card
        Boolean productClicked = (Boolean)
                ((JavascriptExecutor) driver).executeScript(

                "var images = Array.from(document.querySelectorAll('#root img'));" +

                "images = images.filter(img => {" +
                "var r = img.getBoundingClientRect();" +
                "return img.complete && img.naturalWidth > 100 && " +
                "r.width > 100 && r.height > 100 && " +
                "r.top > 80 && r.bottom < window.innerHeight + 500;" +
                "});" +

                "if (images.length === 0) return false;" +

                "images.sort((a,b) => " +
                "a.getBoundingClientRect().top - b.getBoundingClientRect().top);" +

                "var img = images[0];" +

                "var target = img.closest(" +
                "'a, button, [role=\"button\"], [onclick]');" +

                "if (!target) target = img;" +

                "target.scrollIntoView({block:'center'});" +
                "target.click();" +

                "return true;"
        );

        Assert.assertTrue(
                Boolean.TRUE.equals(productClicked),
                "No visible product image was found"
        );

        // Verify product details
        wait.until(d -> {

            String body = d.findElement(By.tagName("body")).getText()
                    .toUpperCase();

            return body.contains("SELECT SIZE")
                    || body.contains("BUY NOW");
        });

        String pageText =
                driver.findElement(By.tagName("body"))
                        .getText()
                        .toUpperCase();

        Assert.assertTrue(
                pageText.contains("SELECT SIZE"),
                "Product size selection was not displayed"
        );

        Assert.assertTrue(
                pageText.contains("BUY NOW"),
                "Buy Now button was not displayed"
        );

        System.out.println("Product details page verified");
    }

    // Close browser after every test
    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
