import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.By;

public class TestNGExample {

    private WebDriver driver;

    @BeforeMethod
    public void setUp(Method method) throws Exception {
    	this.driver = new FirefoxDriver();
    	this.driver.get("http://localhost:3000/");
    }
    
    @AfterMethod
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void verifyTitle() throws Exception {
        // Make the browser get the page and check its title
        Assert.assertEquals("Todo Redis", driver.getTitle());
    }
    
    @Test
    public void verifyLaunchUI() throws Exception {
        // Make the browser get the page and check its title
        Assert.assertTrue(driver.findElement(By.cssSelector("input[placeholder='new todo']")).isDisplayed());
    }
    
}
