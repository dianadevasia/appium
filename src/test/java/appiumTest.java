import java.io.File;
import java.io.IOException;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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

        capabilities.setCapability( "deviceName", "ZY2239T773" );
        capabilities.setCapability( CapabilityType.BROWSER_NAME, "Android" );
        capabilities.setCapability( CapabilityType.VERSION, "7.0.1" );
        capabilities.setCapability( "platformName", "Android" );
        capabilities.setCapability( "appPackage", "com.amazon.mShop.android.shopping" );
        capabilities.setCapability( "appActivity", "com.amazon.mShop.home.HomeActivity" );
        capabilities.setCapability( MobileCapabilityType.FULL_RESET, false );
        capabilities.setCapability( MobileCapabilityType.NO_RESET, true );

        //        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities) {};
        driver = new AndroidDriver<>( new URL( "http://127.0.0.1:4723/wd/hub" ), capabilities );
        driver.manage().timeouts().implicitlyWait( 25, TimeUnit.SECONDS );
    }

    @Test
    public void checkoutProductFlow() {
        PropertyFile prop;
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            prop = mapper.readValue( new File( "datafile.yaml" ), PropertyFile.class );
            final String product_title_0 = prop.getProductTitle0();
            if ( prop.getClearApkDataOnStart().equals( "true" ) )
                driver.resetApp();
            final String search_keywords_0 = prop.getSearchKeywords0();
            WebElement searchTextView = driver.findElement( By.xpath( "//android.widget.EditText" ) );
            searchTextView.sendKeys( search_keywords_0 + "  \n" );


            waitForElementToBePresentByTextAndThenClick( product_title_0 );

            //        driver.findElement( By.id( "com.amazon.mShop.android.shopping:id/rs_results_image" ) ).click();
            waitForElementToBePresentByIdAndThenClick( "add-to-cart-button" );

        } catch ( NoSuchElementException| IOException e ) {
            System.out.println( "Element not found for tooltip or edit text to enter keyword" );
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByIdAndThenClick( String id) {
        try {
            while (!isElementPresentById( id )){
                swipeUpElement( 700, 500);
            }
            final WebElement webElement =
                    driver.findElement( By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"));

            swipeUpElement( 700, 530 );
            Thread.sleep( 5000 );
            webElement.click();
            Thread.sleep( 600000 );
            System.out.print( "element found -- inside waitforElement" );
        } catch ( NoSuchElementException ex ) {
            swipeUpElement( 700, 500 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByTextAndThenClick( String text ) {
        try {
            while (!isElementPresentByText( text )){
                swipeUpElement( 700, 600);
            }
            final WebElement webElement =
                    (WebElement) driver.findElements(By.xpath("//*[contains(@text,'"+text+"')]")).get( 0 );
            webElement.click();
        } catch ( NoSuchElementException ex ) {
            swipeUpElement( 700, 600);
        }
    }

    public void swipeUpElement( final int y2, final int y1 ) {
        try {
            TouchAction touchAction = new TouchAction( driver );
            touchAction.press( 0, y2 ).moveTo( 0, y1 ).release().perform();
            Thread.sleep( 5000 );
        }
        catch ( NoSuchElementException ex ){
            ex.printStackTrace();
            System.out.println( "element not found -- inside swipeUpElement " );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }


    private boolean isElementPresentById( String id) {
        try {
            if( driver.findElement(
                    By.xpath("//android.view.View[contains(@resource-id,'add-to-wishlist-button-submit') and @text='ADD TO LIST']"))
                    .isDisplayed())
                return true;
        } catch ( NoSuchElementException ex ) {
            swipeUpElement( 700, 500);
            return false;
        }
        return false;
    }

    private boolean isElementPresentByText( String text ) {
        try {
            if( driver.findElements(By.xpath("//*[contains(@text,'"+text+"')]")).size()>0)
                return true;
        } catch ( NoSuchElementException ex ) {
            swipeUpElement(700, 600);
            return false;
        }
        return false;
    }


    @AfterTest
    public void End() {
        driver.quit();
    }
}
