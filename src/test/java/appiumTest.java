import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class appiumTest {

    private AndroidDriver driver;
    private AppiumActions appiumActions;

    @Test
    public void checkoutProductFlow() {
        PropertyFile propertyFile = readFromFile();
        setUpDriver( propertyFile );
        appiumActions = new AppiumActions();
        try {
            final String searchKeyword = propertyFile.getSearchKeywords0();
            final String productTitle = propertyFile.getProductTitle0();
            findForProductInTheList( searchKeyword, productTitle );
            addProductToCart();
        }
        catch ( NoSuchElementException e ) {
            System.out.println( "Element not found" );
            e.printStackTrace();
        }
    }

    private void addProductToCart() {
        switchContext("WEBVIEW");
        waitForElementToBePresentByIdAndThenClick( "add-to-cart-button" );
        WebElement webElement =
                driver.findElement( By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"));
        webElement.click();
    }

    private PropertyFile readFromFile() {
        PropertyFile prop = null;
        ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
        try {
            prop = mapper.readValue( new File( "datafile.yaml" ), PropertyFile.class );
        } catch ( NoSuchElementException | IOException e ) {
            System.out.println( "Could not read from the file" );
            e.printStackTrace();
            end();
        }
        return prop;
    }

    private void setUpDriver( final PropertyFile prop ) {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            // Parameterize this device name
            capabilities.setCapability( "deviceName", prop.getDeviceName() );
            capabilities.setCapability( CapabilityType.BROWSER_NAME, "Android" );
            capabilities.setCapability( CapabilityType.VERSION, "7.0.1" );
            capabilities.setCapability( "platformName", "Android" );
            capabilities.setCapability( MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM );

            capabilities.setCapability( MobileCapabilityType.FULL_RESET, false );
            capabilities.setCapability( MobileCapabilityType.NO_RESET, false );
            capabilities.setCapability( "appPackage", "com.amazon.mShop.android.shopping" );
            capabilities.setCapability( "appActivity", "com.amazon.mShop.home.HomeActivity" );
            //        capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);

            //        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities) {};
            driver = new AndroidDriver<>( new URL( "http://127.0.0.1:4723/wd/hub" ), capabilities );
            driver.manage().timeouts().implicitlyWait( 25, TimeUnit.SECONDS );
        } catch ( MalformedURLException e ){
            System.out.println( "Could not create the android driver" );
            e.printStackTrace();
            end();
        }
    }

    private void findForProductInTheList( final String searchKeyword, final String productTitle ) {
        WebElement searchTextView = driver.findElement( By.xpath( "//android.widget.EditText" ) );
        searchTextView.sendKeys( searchKeyword + "  \n" );
        waitForElementToBePresentByTextAndThenClick( productTitle );
    }

    private void switchContext( final String context ) {
        Set<String> allContextHandles = driver.getContextHandles();
        for(String contextHandle : allContextHandles)
        {
            System.out.println("Available Context : "+contextHandle);
            if(contextHandle.contains(context))
            {
                driver.context(contextHandle);
                break;
            }
        }
        System.out.println("After Switching : "+driver.getContext());
    }

    private void waitForElementToBePresentByIdAndThenClick( String id) {
        try {
            while (!isElementPresentById( id )){
                appiumActions.swipeUpElement( driver,700, 500);
            }
            final WebElement webElement =
                    driver.findElement( By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"));

            appiumActions.swipeUpElement( driver,700, 530 );
            Thread.sleep( 5000 );
            webElement.click();
            Thread.sleep( 10000 );
            System.out.print( "element found -- inside waitforElement" );
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver, 700, 500 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByTextAndThenClick( String text ) {
        try {
            while (!isElementPresentByText( text )){
                appiumActions.swipeUpElement( driver, 700, 600);
            }
            final WebElement webElement =
                    (WebElement) driver.findElements(By.xpath("//*[contains(@text,'"+text+"')]")).get( 0 );
            webElement.click();
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver,700, 600);
        }
    }

    private boolean isElementPresentById( String id) {
        try {
            if( driver.findElement(
                     By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"))
                    .isDisplayed())
                return true;
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver,700, 500);
            return false;
        }
        return false;
    }

    private boolean isElementPresentByText( String text ) {
        try {
            if( driver.findElements(By.xpath("//*[contains(@text,'"+text+"')]")).size()>0)
                return true;
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver, 700, 600);
            return false;
        }
        return false;
    }

    @AfterTest
    public void end() {
        driver.quit();
    }
}
