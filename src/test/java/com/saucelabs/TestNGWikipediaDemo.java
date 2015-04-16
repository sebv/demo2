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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.By;

import java.net.URL;
import java.util.Map;

import com.saucelabs.saucerest.SauceREST;

public class TestNGWikipediaDemo {
    /*
    String username = "random";
    String accessKey = "iforgot";
    */

    Map<String, String> env = System.getenv();
    String username = env.get("SAUCE_USERNAME");
    String accessKey = env.get("SAUCE_ACCESS_KEY");

    private WebDriver driver;
    String jobId;
    boolean passed = true;
    private SauceREST sauceREST;

    @BeforeMethod
    public void setUp(Method method) throws Exception {
      DesiredCapabilities capabilities = DesiredCapabilities.firefox();
      capabilities.setCapability("name", "TestNGWikipediaDemo - " + method.getName());
      this.driver = new RemoteWebDriver(
            new URL("http://" + username + ":" + accessKey + "@ondemand.saucelabs.com:80/wd/hub"),
            capabilities);
      sauceREST = new SauceREST(username, accessKey);
      this.jobId = ((RemoteWebDriver) driver).getSessionId().toString();
    }

    @AfterMethod
    public void tearDown() throws Exception {
      if(passed) {
         sauceREST.jobPassed(jobId);
      } else {
          sauceREST.jobFailed(jobId);
      }
      driver.quit();
    }

    @Test
    public void verifyLaunch() throws Exception {
        try {
    	  driver.get("http://wikipedia.org");

        // Make the browser get the page and check its title
        Assert.assertEquals("Wikipedia", driver.getTitle());

        // Check if the launch page elements are there
        Assert.assertTrue(driver.findElement(By.cssSelector(".central-featured")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector("#searchInput")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector("#searchLanguage")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector(".search-form .formBtn")).isDisplayed());
      } catch(Exception e) {
        passed = false;
        throw e;
      }
    }

    @Test
    public void verifySearchForUFC() throws Exception {
      try {
        driver.get("http://wikipedia.org");

        // Fill out search box with UFC and click search
        driver.findElement(By.cssSelector("#searchInput")).sendKeys("UFC");
        driver.findElement(By.cssSelector(".search-form .formBtn")).click();

        // verify UFC page is there
        Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship", driver.getCurrentUrl());
        Assert.assertEquals("Ultimate Fighting Championship - Wikipedia, the free encyclopedia", driver.getTitle());
      } catch(Exception e) {
          passed = false;
          throw e;
      }
    }

    @Test
    public void goToHistorySection() throws Exception {
        try {
            driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

            // click History
            driver.findElement(By.cssSelector("li [href='#History']")).click();
            Assert.assertEquals("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship#History", driver.getCurrentUrl());

            // make sure we have scrolled to history
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            Long value = (Long) executor.executeScript("return window.scrollY;");
            Assert.assertNotEquals(value, 0, 0);
        } catch(Exception e) {
            passed = false;
            throw e;
        }
    }

    @Test
    public void verifyEditPageUI() throws Exception {
        try {
            driver.get("http://en.wikipedia.org/wiki/Ultimate_Fighting_Championship");

            // click edit page
            driver.findElement(By.cssSelector("#ca-edit a")).click();

            // verify edit page UI
            Assert.assertEquals("http://en.wikipedia.org/w/index.php?title=Ultimate_Fighting_Championship&action=edit", driver.getCurrentUrl());
            Assert.assertTrue(driver.findElement(By.cssSelector(".wikiEditor-ui")).isDisplayed());
        } catch(Exception e) {
            passed = false;
            throw e;
        }
    }
}
