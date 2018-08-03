import java.net.MalformedURLException;
import java.net.URL;
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
public class appiumTest {

    AndroidDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @BeforeTest
    public void setUp() throws MalformedURLException {

        // Created object of DesiredCapabilities class.
        DesiredCapabilities capabilities = new DesiredCapabilities();

        // Set android deviceName desired capability. Set your device name.
        capabilities.setCapability("deviceName", "ZY2239T773");

        // Set BROWSER_NAME desired capability. It's Android in our case here.
        capabilities.setCapability(CapabilityType.BROWSER_NAME, "Android");

        // Set android VERSION desired capability. Set your mobile device's OS version.
        capabilities.setCapability(CapabilityType.VERSION, "7.0.1");

        // Set android platformName desired capability. It's Android in our case here.
        capabilities.setCapability("platformName", "Android");

        // Set android appPackage desired capability. It is
        // com.google.android.calculator for calculator application.
        // Set your application's appPackage if you are using any other app.
        capabilities.setCapability("appPackage", "com.amazon.mShop.android.shopping");

        // Set android appActivity desired capability. It is
        // com.android.calculator2.Calculator for calculator application.
        // Set your application's appPackage if you are using any other app.
        capabilities.setCapability("appActivity", "com.amazon.mShop.home.HomeActivity");

        // Created object of RemoteWebDriver will all set capabilities.
        // Set appium server address and port number in URL string.
        // It will launch calculator app in android device.
//        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities) {};
        driver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
    }

    @Test
    public void Sum() throws Exception {

        WebElement loginAsButton = driver.findElement( By.xpath( "//android.widget.Button[@text='Continue']" ) );
        loginAsButton.click();

        WebElement searchTextView = driver.findElement( By.xpath( "//android.widget.EditText" ) );
        searchTextView.sendKeys( "samsung galaxy s8 \n" );

        driver.findElement( By.id( "com.amazon.mShop.android.shopping:id/rs_results_image" ) ).click();

        final WebElement closeButton = driver.findElement( By.id("tutorial_tool_tip_close_button") );
        closeButton.click();

//        final WebElement addToCart = driver.findElement( By.id("add-to-cart-button") );
//        addToCart.click();

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
//                LogUtils.INFO("*** SCROLLING ***");
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
