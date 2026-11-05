package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.support.ui.WebDriverWait;

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

        wait = new WebDriverWait(
            driver, Duration.ofSeconds(30)
        );
    }

    // TEST 1: Homepage Validation
    @Test
    public void verifyHomePage() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.getTitle() != null &&
            !d.getTitle().trim().isEmpty()
        );

        System.out.println("Page Title: " + driver.getTitle());
        System.out.println("URL: " + driver.getCurrentUrl());

        Assert.assertTrue(
            driver.getCurrentUrl().startsWith("https://"),
            "Website should use HTTPS"
        );

        Assert.assertFalse(
            driver.getTitle().trim().isEmpty(),
            "Page title should not be empty"
        );
    }

    // TEST 2: Inspect Search Elements
    @Test
    public void inspectSearchElements() {

        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        wait.until(d ->
            d.findElement(By.tagName("body")).isDisplayed()
        );

        List<WebElement> elements = driver.findElements(
            By.cssSelector("input, textarea, button")
        );

        System.out.println("Search Page Elements:");

        for (WebElement element : elements) {

            System.out.println("Tag: " + element.getTagName());

            System.out.println(
                "Type: " + element.getAttribute("type")
            );

            System.out.println(
                "Placeholder: " +
                element.getAttribute("placeholder")
            );

            System.out.println(
                "ARIA label: " +
                element.getAttribute("aria-label")
            );

            System.out.println(
                "Visible: " + element.isDisplayed()
            );

            System.out.println("--------------------");
        }

        System.out.println(
            "Total Search Elements: " + elements.size()
        );

        Assert.assertTrue(
            driver.findElement(By.tagName("body")).isDisplayed(),
            "Search page should load successfully"
        );
    }

    // TEST 3: Inspect Account Elements
    @Test
    public void inspectAccountElements() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.getTitle() != null &&
            !d.getTitle().trim().isEmpty()
        );

        wait.until(d ->
            d.findElement(By.tagName("body")).isDisplayed()
        );

        List<WebElement> elements = driver.findElements(
            By.xpath("//*")
        );

        int accountCount = 0;
        int interactiveCount = 0;

        System.out.println("ACCOUNT ELEMENT INSPECTION");

        for (WebElement element : elements) {

            if (!element.isDisplayed()) {
                continue;
            }

            String tag = element.getTagName();
            String text = element.getText();
            String aria = element.getAttribute("aria-label");
            String title = element.getAttribute("title");
            String className = element.getAttribute("class");
            String id = element.getAttribute("id");
            String href = element.getAttribute("href");
            String role = element.getAttribute("role");

            String combined = (
                tag + " " + text + " " + aria + " " +
                title + " " + className + " " +
                id + " " + href + " " + role
            ).toLowerCase();

            boolean isInteractive =
                tag.equalsIgnoreCase("button") ||
                tag.equalsIgnoreCase("a") ||
                tag.equalsIgnoreCase("input") ||
                "button".equalsIgnoreCase(role) ||
                element.getAttribute("onclick") != null;

            if (isInteractive) {
                interactiveCount++;
            }

            boolean isAccountElement =
                combined.contains("account") ||
                combined.contains("login") ||
                combined.contains("log in") ||
                combined.contains("profile") ||
                combined.contains("user") ||
                combined.contains("sign in");

            if (isAccountElement) {

                accountCount++;

                System.out.println("ACCOUNT ELEMENT FOUND");
                System.out.println("Tag: " + tag);
                System.out.println("Text: " + text);
                System.out.println("ARIA: " + aria);
                System.out.println("Title: " + title);
                System.out.println("Class: " + className);
                System.out.println("ID: " + id);
                System.out.println("Href: " + href);
                System.out.println("Role: " + role);

                System.out.println("--------------------");
            }
        }

        System.out.println(
            "Total Interactive Elements: " + interactiveCount
        );

        System.out.println(
            "Total Account Related Elements: " + accountCount
        );

        Assert.assertTrue(
            driver.findElement(By.tagName("body")).isDisplayed(),
            "Homepage should load successfully"
        );
    }

    // TEST 4: Inspect Login Element
    @Test
    public void inspectLoginElement() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.getTitle() != null &&
            !d.getTitle().trim().isEmpty()
        );

        wait.until(d ->
            d.findElement(By.tagName("body")).isDisplayed()
        );

        List<WebElement> loginElements = driver.findElements(
            By.xpath("//*[normalize-space(text())='Login']")
        );

        System.out.println("LOGIN ELEMENT INSPECTION");

        for (WebElement element : loginElements) {

            System.out.println(
                "Tag: " + element.getTagName()
            );

            System.out.println(
                "Text: " + element.getText()
            );

            System.out.println(
                "HTML: " + element.getAttribute("outerHTML")
            );

            try {

                System.out.println(
                    "Parent HTML: " +
                    element.findElement(By.xpath(".."))
                           .getAttribute("outerHTML")
                );

            } catch (Exception e) {

                System.out.println(
                    "Parent element could not be inspected."
                );
            }

            System.out.println("--------------------");
        }

        System.out.println(
            "Login elements found: " + loginElements.size()
        );

        Assert.assertTrue(
            driver.findElement(By.tagName("body")).isDisplayed(),
            "Homepage should load for Login inspection"
        );
    }

    // TEST 5: Deep DOM Inspection
    @Test
    public void inspectLoginDOM() {

        driver.get("https://elaraastudio.com/home");

        wait.until(d ->
            d.findElement(By.tagName("body")).isDisplayed()
        );

        wait.until(d ->
            d.findElement(By.id("root"))
             .getText()
             .contains("Login")
        );

        JavascriptExecutor js =
            (JavascriptExecutor) driver;

        String result = (String) js.executeScript(

            "const root = document.querySelector('#root');" +

            "if (!root) return 'ROOT NOT FOUND';" +

            "const walker = document.createTreeWalker(" +
            "root, NodeFilter.SHOW_TEXT);" +

            "let node;" +

            "while (node = walker.nextNode()) {" +

            "if (node.nodeValue.trim().toLowerCase() === 'login') {" +

            "let element = node.parentElement;" +

            "let output = 'LOGIN HTML STRUCTURE:\\n';" +

            "for (let i = 0; i < 5 && element; i++) {" +

            "output += element.outerHTML.substring(0, 2000) + '\\n';" +

            "element = element.parentElement;" +

            "}" +

            "return output;" +

            "}" +

            "}" +

            "return 'LOGIN TEXT NODE NOT FOUND';"
        );

        System.out.println(result);

        Assert.assertTrue(
            driver.findElement(By.id("root")).isDisplayed(),
            "Homepage root should be visible"
        );
    }

    // CLOSE BROWSER
    @AfterMethod(alwaysRun = true)
    public void tearDown() {

        if (driver != null) {
            driver.quit();
        }
    }
}
