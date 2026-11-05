package com.elaraa.tests;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @Test
    public void verifyHomePage() {
        driver.get("https://elaraastudio.com/home");

        wait.until(d -> d.findElement(By.id("root")).isDisplayed());

        Assert.assertTrue(driver.getCurrentUrl().startsWith("https://"));
        Assert.assertEquals(driver.getTitle(), "Elaraa Studio");

        System.out.println("PASS: Homepage loaded");
        System.out.println("Title: " + driver.getTitle());
    }

    @Test
    public void verifyHomePageContent() {
        driver.get("https://elaraastudio.com/home");

        wait.until(d -> d.findElement(By.id("root"))
                         .getText().contains("Login"));

        String pageText = driver.findElement(By.id("root")).getText();

        Assert.assertTrue(pageText.contains("Profile"),
                "Profile text should be present");
        Assert.assertTrue(pageText.contains("Wishlist"),
                "Wishlist text should be present");
        Assert.assertTrue(pageText.contains("Bags"),
                "Bags text should be present");
        Assert.assertTrue(pageText.contains("Login"),
                "Login text should be present");

        System.out.println("PASS: Homepage content verified");
    }

    @Test
    public void inspectLoginDOM() {
        driver.get("https://elaraastudio.com/home");

        wait.until(d -> d.findElement(By.id("root"))
                         .getText().contains("Login"));

        JavascriptExecutor js = (JavascriptExecutor) driver;

        List<WebElement> loginElements = driver.findElements(
            By.xpath("//*[contains(normalize-space(.), 'Login')]")
        );

        System.out.println("LOGIN DOM INSPECTION");

        for (WebElement element : loginElements) {
            if (element.getText().trim().equals("Login")) {
                System.out.println("Tag: " + element.getTagName());
                System.out.println("HTML: " +
                        element.getAttribute("outerHTML"));
                break;
            }
        }

        String clickableInfo = (String) js.executeScript(
            "const nodes = [...document.querySelectorAll('#root *')];" +
            "const el = nodes.find(e => e.textContent.trim() === 'Login');" +
            "if (!el) return 'LOGIN ELEMENT NOT FOUND';" +
            "return 'Tag: ' + el.tagName + " +
            "' | Parent: ' + el.parentElement.outerHTML.substring(0, 1500);"
        );

        System.out.println(clickableInfo);

        Assert.assertTrue(
            driver.findElement(By.id("root")).isDisplayed(),
            "Homepage should be visible"
        );
    }

    @Test
    public void verifySearchPageLoads() {
        driver.get(
            "https://elaraastudio.com/filterdeal?campaign=4&campaignstatus=true"
        );

        wait.until(d -> d.findElement(By.tagName("body")).isDisplayed());

        Assert.assertTrue(
            driver.getTitle() != null && !driver.getTitle().isEmpty(),
            "Search page should have a title"
        );

        System.out.println("PASS: Search page loaded");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
