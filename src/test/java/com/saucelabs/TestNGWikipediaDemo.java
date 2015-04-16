package com.saucelabs;

/**
 * @author Neil Manvar
 */

import org.testng.annotations.Test;

import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;

public class TestNGWikipediaDemo {

    private WebDriver driver;

    @BeforeMethod
    public void setUp(Method method) throws Exception {
    	this.driver = new FirefoxDriver();
    }

    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void verifyLaunch() throws Exception {
    	driver.get("http://wikipedia.org");

        // Make the browser get the page and check its title
        Assert.assertEquals("Wikipedia", driver.getTitle());

        // Check if the launch page elements are there
        Assert.assertTrue(driver.findElement(By.cssSelector(".central-featured")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector("#searchInput")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector("#searchLanguage")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector(".search-form .formBtn")).isDisplayed());
    }

    @Test
    public void verifySearchForUFC() throws Exception {
    	driver.get("http://wikipedia.org");

    	// Fill out search box with UFC and click search
    	driver.findElement(By.cssSelector("#searchInput")).sendKeys("UFC");
    	driver.findElement(By.cssSelector(".search-form .formBtn")).click();

    	// verify UFC page is there
    	Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship", driver.getCurrentUrl());
    	Assert.assertEquals("Ultimate Fighting Championship - Wikipedia, the free encyclopedia", driver.getTitle());
    }

    @Test
    public void goToHistorySection() throws Exception {
    	driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

    	// click History
    	driver.findElement(By.cssSelector("li [href='#History']")).click();
    	Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship#History", driver.getCurrentUrl());

    	// make sure we have scrolled to history
    	JavascriptExecutor executor = (JavascriptExecutor) driver;
    	Long value = (Long) executor.executeScript("return window.scrollY;");
    	Assert.assertNotEquals(value, 0, 0);
    }

    @Test
    public void verifyEditPageUI() throws Exception {
    	driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

    	// click edit page
    	driver.findElement(By.cssSelector("#ca-edit a")).click();

    	// verify edit page UI
    	Assert.assertEquals("http://en.wikipedia.org/w/index.php?title=Ultimate_Fighting_Championship&action=edit", driver.getCurrentUrl());
    	Assert.assertTrue(driver.findElement(By.cssSelector(".wikiEditor-ui")).isDisplayed());
    }
}
