import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class appiumTest {

    AndroidDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeTest
    public void setUp() throws MalformedURLException {

        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("deviceName", "ZY2239T773");
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");
        capabilities.setCapability(CapabilityType.VERSION, "7.0.1");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.amazon.mShop.android.shopping");
        capabilities.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");
        capabilities.setCapability( MobileCapabilityType.FULL_RESET, false);
        capabilities.setCapability( MobileCapabilityType.NO_RESET, true);

//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities) {};
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
//        driver.resetApp();
    }


    @Test
    public void Sum() throws Exception {

        Properties prop = new Properties();
        FileInputStream fileInput = null;
        try {
            fileInput = new FileInputStream(new File("datafile.properties"));
            prop.load(fileInput);
        } catch ( IOException e ) {
            System.out.print( "file not found" );
            e.printStackTrace();
        }

        final String search_keywords_0 = prop.getProperty( "search_keywords_0" );

//        WebElement loginAsButton = driver.findElement( By.xpath( "//android.widget.Button[@text='Continue']" ) );
//        loginAsButton.click();

        WebElement searchTextView = driver.findElement( By.xpath( "//android.widget.EditText" ) );
        searchTextView.sendKeys( search_keywords_0 + " \n" );

        driver.findElement( By.id( "com.amazon.mShop.android.shopping:id/rs_results_image" ) ).click();

        final WebElement closeButton = driver.findElement( By.id("tutorial_tool_tip_close_button") );
        closeButton.click();
//
//        while ( !isElementPresent() ){
        waitForElementToBePresent();
//        }

        final WebElement addToCart1 = driver.findElement( By.id("add-to-cart-button") );
        addToCart1.click();






//        final WebElement addToCart = driver.findElement(By.xpath( "//android.widget.Button[@text='Add to Basket']" ) );
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//        js.executeScript( "arguments[0].scrollIntoView(true);", addToCart );

    }

    private void waitForElementToBePresent() {
        try {
            boolean a = isElementPresent();
            System.out.print( "element found -- inside waitforElement" );
//            scrollWithTouchAction("up", 0, 0, 280, 1700, 1);
        } catch (NoSuchElementException ex) {
            System.out.print( "element not found -- inside catch waitForElement" );
            scrollWithTouchAction("up", 0, 0, 280, 1700, 1);
        }
    }

    public void scrollWithTouchAction(String direction, int x1, int x2, int y1, int y2, int time) {
        TouchAction touchAction = new TouchAction(driver);

        for (int i = 0; i < time; i++) {
            if (direction.equals("down")) {
                touchAction.press(x1, y1).moveTo(0, -y2).perform();
            } else if (direction.equals("up")) {
                touchAction.press(x2, y2).moveTo(x1, y1).release().perform();
            }
            try {
                Thread.sleep(200); //sleep between scroll
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    private boolean isElementPresent( ) {
        try {
//            return driver.findElements( By.id( "//add-to-cart-button" ) ).size() > 0;
            final WebElement addToCart1 = driver.findElement(By.id("add-to-cart-button"));
            scrollWithTouchAction("up", 0, 0, 280, 1500, 1);
            scrollWithTouchAction("up", 0, 0, 280, 1500, 1);
//            scrollWithTouchAction("up", 0, 0, 280, 1500, 1);
            final WebElement addToCart2 = driver.findElement(By.id("add-to-cart-button"));
            addToCart2.click();
            return true;
//            return driver.findElements(By.xpath( "//android.widget.Button[@text='Add to Basket']" ) ).size() > 0;
        }catch ( NoSuchElementException ex) {
            System.out.print( "element not found -- inside catch" );
            return false;
        }
    }

    @AfterTest
    public void End() {
        driver.quit();
    }
}
