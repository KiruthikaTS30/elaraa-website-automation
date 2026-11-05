
package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HomePageTest {

    WebDriver driver;

    private final String homeUrl = "https://elaraastudio.com/home";

    private final By root = By.id("root");

    private final By loginButton =
            By.cssSelector("button[aria-label='Login to your account']");

    private final By mobileNumberField =
            By.cssSelector("input[type='tel'][placeholder='Mobile Number*']");

    private final By searchField =
            By.cssSelector("input[type='search']");

    private final By wishlistButton =
            By.cssSelector("button[aria-label='View wishlist']");

    private final By shoppingBagButton =
            By.cssSelector("button[aria-label='View shopping cart']");

    private final By profileButton =
            By.cssSelector("button[aria-label='View profile']");

    @BeforeMethod
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(0));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }

    // Open homepage
    private void openHomePage() {

        driver.get(homeUrl);

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(40));

        wait.until(d ->
                "complete".equals(
                        ((JavascriptExecutor) d).executeScript(
                                "return document.readyState")));

        wait.until(d -> {
            try {
                return d.findElement(root).isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        });

        System.out.println("Current URL: " + driver.getCurrentUrl());
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Homepage opened.");
    }

    // Wait for visible element
    private WebElement waitForVisible(By locator, int seconds) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(seconds));

        return wait.until(d -> {
            try {
                WebElement element = d.findElement(locator);
                return element.isDisplayed() ? element : null;
            } catch (NoSuchElementException |
                     StaleElementReferenceException e) {
                return null;
            }
        });
    }

    // Wait until element is not covered by loading overlay
    private void waitUntilElementIsUnblocked(By locator) {

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(25));

        wait.ignoring(StaleElementReferenceException.class);
        wait.ignoring(NoSuchElementException.class);

        wait.until(d -> {

            WebElement element = d.findElement(locator);

            return (Boolean) ((JavascriptExecutor) d).executeScript(

                    "const target = arguments[0];" +
                    "if (!target.isConnected) return false;" +
                    "if (target.getClientRects().length === 0) return false;" +
                    "if (target.disabled) return false;" +

                    "const overlay = document.querySelector('.global-loading-overlay');" +

                    "if (overlay) {" +
                    " const style = window.getComputedStyle(overlay);" +
                    " if (style.display !== 'none' &&" +
                    "     style.visibility !== 'hidden' &&" +
                    "     Number(style.opacity) > 0) return false;" +
                    "}" +

                    "const rect = target.getBoundingClientRect();" +
                    "const x = rect.left + rect.width / 2;" +
                    "const y = rect.top + rect.height / 2;" +
                    "const top = document.elementFromPoint(x, y);" +

                    "return top === target || target.contains(top);",

                    element
            );
        });
    }

    // Open login form
    private void openLoginForm() {

        openHomePage();

        waitUntilElementIsUnblocked(loginButton);

        driver.findElement(loginButton).click();

        waitForVisible(mobileNumberField, 20);
    }

    // TEST 1: Homepage title and HTTPS
    @Test
    public void verifyHomePage() {

        openHomePage();

        Assert.assertEquals(
                driver.getTitle(),
                "Elaraa Studio",
                "Homepage title is incorrect");

        Assert.assertTrue(
                driver.getCurrentUrl().startsWith("https://"),
                "Website should use HTTPS");

        System.out.println("Homepage title and HTTPS verified.");
    }

    // TEST 2: Homepage Login button
    @Test
    public void verifyHomePageContent() {

        openHomePage();

        WebElement login = waitForVisible(loginButton, 20);

        Assert.assertTrue(login.isDisplayed(),
                "Login button is not visible");

        Assert.assertTrue(login.getText().contains("Login"),
                "Login text is not displayed");

        System.out.println("Homepage Login button verified.");
    }

    // TEST 3: Login button click
    @Test
    public void verifyLoginButtonClick() {

        openLoginForm();

        Assert.assertTrue(
                driver.findElement(mobileNumberField).isDisplayed(),
                "Mobile number field is not visible");

        System.out.println("Login form opened successfully.");
    }

    // TEST 4: Campaign page
    @Test
    public void verifyCampaignPageLoads() {

        driver.get(
                "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true");

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(40));

        wait.until(d ->
                "complete".equals(
                        ((JavascriptExecutor) d).executeScript(
                                "return document.readyState")));

        WebElement pageRoot = waitForVisible(root, 20);

        Assert.assertTrue(
                driver.getCurrentUrl().contains("filterdeal"),
                "Campaign page URL is incorrect");

        System.out.println("Campaign URL: " + driver.getCurrentUrl());
        System.out.println("Campaign content: " + pageRoot.getText());
    }

    // TEST 5: Empty mobile number validation
    @Test
    public void verifyEmptyMobileNumberValidation() {

        openLoginForm();

        WebElement mobileField =
                driver.findElement(mobileNumberField);

        boolean required =
                mobileField.getAttribute("required") != null;

        boolean valid = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].checkValidity();",
                        mobileField);

        String message = (String) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].validationMessage;",
                        mobileField);

        System.out.println("Required: " + required);
        System.out.println("Input valid: " + valid);
        System.out.println("Validation message: " + message);

        Assert.assertTrue(required,
                "Mobile number field should be required");

        Assert.assertFalse(valid,
                "Empty mobile number should be rejected");
    }

    // TEST 6: Invalid mobile number observation
    @Test
    public void verifyInvalidMobileNumberValidation() {

        openLoginForm();

        WebElement mobileField =
                driver.findElement(mobileNumberField);

        mobileField.sendKeys("12345");

        boolean valid = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].checkValidity();",
                        mobileField);

        System.out.println("Entered number: " +
                mobileField.getAttribute("value"));

        System.out.println("Browser validity: " + valid);

        System.out.println("Page response: " +
                driver.findElement(root).getText());
    }

    // TEST 7: Search field visibility
    @Test
    public void verifySearchFieldVisibility() {

        openHomePage();

        WebElement search = waitForVisible(searchField, 20);

        Assert.assertTrue(search.isDisplayed(),
                "Search field is not visible");

        Assert.assertEquals(
                search.getAttribute("placeholder"),
                "Search for products,brands and more",
                "Search placeholder is incorrect");

        System.out.println("Search field verified.");
    }

    // TEST 8: Product search navigation
    @Test
    public void verifyProductSearch() {

        openHomePage();

        WebElement search = waitForVisible(searchField, 20);

        search.sendKeys("shirt");
        search.submit();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(25));

        wait.until(d ->
                d.getCurrentUrl().contains("search=shirt"));

        Assert.assertTrue(
                driver.getCurrentUrl().contains("search=shirt"),
                "Search URL was not updated");

        System.out.println("Search URL: " +
                driver.getCurrentUrl());

        System.out.println("Search page: " +
                driver.findElement(root).getText());
    }

    // TEST 9: Empty search behavior
    @Test
    public void verifyEmptySearch() {

        openHomePage();

        WebElement search = waitForVisible(searchField, 20);

        search.submit();

        Assert.assertTrue(
                driver.getCurrentUrl().contains("/home"),
                "Empty search should not navigate to a search results URL");

        System.out.println("URL after empty search: " +
                driver.getCurrentUrl());
    }

    // TEST 10: Wishlist button
    @Test
    public void verifyWishlistButton() {

        openHomePage();

        waitUntilElementIsUnblocked(wishlistButton);

        driver.findElement(wishlistButton).click();

        System.out.println("Wishlist URL: " +
                driver.getCurrentUrl());

        System.out.println("Wishlist response: " +
                driver.findElement(root).getText());
    }

    // TEST 11: Shopping Bag button
    @Test
    public void verifyShoppingBagButton() {

        openHomePage();

        waitUntilElementIsUnblocked(shoppingBagButton);

        driver.findElement(shoppingBagButton).click();

        System.out.println("Shopping Bag URL: " +
                driver.getCurrentUrl());

        System.out.println("Shopping Bag response: " +
                driver.findElement(root).getText());
    }

    // TEST 12: Profile button
    @Test
    public void verifyProfileButton() {

        openHomePage();

        waitUntilElementIsUnblocked(profileButton);

        driver.findElement(profileButton).click();

        System.out.println("Profile URL: " +
                driver.getCurrentUrl());

        System.out.println("Profile response: " +
                driver.findElement(root).getText());
    }

    // TEST 13: Footer content validation
    @Test
    public void verifyFooterContent() {

        openHomePage();

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

        System.out.println("Footer content verified.");
    }

    // TEST 14: Kurtis category navigation
    @Test
    public void verifyKurtisCategoryNavigation() {

        openHomePage();

        By kurtisLink = By.xpath(
                "//*[self::a or self::button]" +
                "[normalize-space()='Kurtis']");

        WebElement kurtis = waitForVisible(kurtisLink, 20);

        String oldUrl = driver.getCurrentUrl();

        kurtis.click();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                !d.getCurrentUrl().equals(oldUrl));

        Assert.assertNotEquals(
                driver.getCurrentUrl(),
                oldUrl,
                "Kurtis category navigation did not change the URL");

        System.out.println("Kurtis category URL: " +
                driver.getCurrentUrl());
    }

    // TEST 15: Product details page navigation
    @Test
    public void verifyProductDetailsPageNavigation() {

        openHomePage();

        WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(20));

        wait.until(d ->
                d.findElements(By.cssSelector("#root a[href]"))
                        .stream()
                        .anyMatch(a -> {
                            try {
                                return a.isDisplayed()
                                        && !a.findElements(By.tagName("img")).isEmpty()
                                        && a.getAttribute("href") != null
                                        && a.getAttribute("href")
                                                .contains("elaraastudio.com");
                            } catch (StaleElementReferenceException e) {
                                return false;
                            }
                        }));

        List<WebElement> links =
                driver.findElements(By.cssSelector("#root a[href]"));

        WebElement productLink = null;

        for (WebElement link : links) {

            try {

                String href = link.getAttribute("href");

                boolean hasImage =
                        !link.findElements(By.tagName("img")).isEmpty();

                if (link.isDisplayed()
                        && hasImage
                        && href != null
                        && href.contains("elaraastudio.com")
                        && !href.endsWith("/home")
                        && !href.contains("filterdeal")
                        && !href.contains("search=")) {

                    productLink = link;
                    break;
                }

            } catch (StaleElementReferenceException e) {
                // Continue checking other links
            }
        }

        Assert.assertNotNull(
                productLink,
                "No visible product image link was found");

        String oldUrl = driver.getCurrentUrl();

        productLink.click();

        wait.until(d ->
                !d.getCurrentUrl().equals(oldUrl));

        Assert.assertNotEquals(
                driver.getCurrentUrl(),
                oldUrl,
                "Product details navigation did not change the URL");

        System.out.println("Product details URL: " +
                driver.getCurrentUrl());

        System.out.println("Product details page opened.");
    }
}
