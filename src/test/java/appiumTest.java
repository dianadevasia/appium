import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;

public class appiumTest {

    private AndroidDriver driver;
    private AppiumActions appiumActions;
    private AppiumElement element;

    @Test
    public void checkoutProductFlow() throws TimeoutException {
        setUpDriver();
        appiumActions = new AppiumActions(driver);
        element = new AppiumElement( driver );
        try {
            findForProductInTheList( System.getProperty( "searchKeywords"), System.getProperty( "productTitle" ) );
            addProductToCart();
            proceedToCheckout();
        }
        catch ( NoSuchElementException e ) {
            System.out.println( "Element not found" );
            e.printStackTrace();
        }
    }

    private void proceedToCheckout() {
        if(System.getProperty( "proceedToCheckout").equals( "true" )) {
            waitForElementToBePresentByIdAndThenClick( "action_bar_cart_image" );
            clickProceedToCheckout();
        }
    }

    private void setUpDriver() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability( CapabilityType.BROWSER_NAME, "Android" );
            capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "ANDROID");
            capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.ANDROID_UIAUTOMATOR2);

            capabilities.setCapability( MobileCapabilityType.PLATFORM_VERSION, System.getProperty("androidVersion" ) );
            capabilities.setCapability( MobileCapabilityType.DEVICE_NAME, System.getProperty("deviceName" ) );
            capabilities.setCapability(MobileCapabilityType.UDID, System.getProperty("deviceId" )  );
            capabilities.setCapability( AndroidMobileCapabilityType.SYSTEM_PORT, System.getProperty("port" ));
            capabilities.setCapability( "appPackage", "com.amazon.mShop.android.shopping" );
            capabilities.setCapability( "appActivity", "com.amazon.mShop.home.HomeActivity" );
            capabilities.setCapability( "newCommandTimeout", "30" );
            final String APPIUM_SERVER_URL = "http://0.0.0.0:4723/wd/hub";
            driver = new AndroidDriver<MobileElement>( new URL(APPIUM_SERVER_URL), capabilities);
            driver.manage().timeouts().implicitlyWait( 2, TimeUnit.MINUTES );
            if(System.getProperty("fullReset").equals( "true" ) )
                driver.resetApp();

        } catch ( MalformedURLException e ){
            System.out.println( "Could not create the android driver" );
            e.printStackTrace();
            end();
        }
    }

    private void findForProductInTheList( final String searchKeyword, final String productTitle ) throws TimeoutException {
        WebDriverWait wait = (WebDriverWait) new WebDriverWait( driver, 20 )
                .pollingEvery( 20, TimeUnit.MILLISECONDS );
        try {
            wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath( "//android.widget.Button[contains(@resource-id,'com.amazon.mShop.android.shopping:id/skip_sign_in_button') and @text='Skip sign in']" ) ) )
            .click();
            final String parsedSearchKeyword = searchKeyword.replaceAll( "_", " " );
            wait.until( ExpectedConditions.presenceOfElementLocated( ( By.id( "rs_search_src_text" ) ) ) ).click();
            Thread.sleep( 5000 );
            wait.until( ExpectedConditions.presenceOfElementLocated( ( By.id( "rs_search_src_text" ) ) ) ).sendKeys(
                    parsedSearchKeyword );
            driver.pressKeyCode( AndroidKeyCode.ENTER );
        } catch ( NoSuchElementException | InterruptedException ex ){
            ex.printStackTrace();
        }
        try {
            if ( wait.until( ExpectedConditions.presenceOfElementLocated( ( By.id( "tutorial_tool_tip_close_button" ) ) ) )
                    .isDisplayed() ) {
                driver.findElement( By.id( "tutorial_tool_tip_close_button" ) ).click();
                enableTouchActions();
            }
        } catch ( NoSuchElementException ex){
            System.out.println("tool tip not present");
        }
        final String parsedProductTitle = productTitle.replaceAll( "[^0-9a-zA-Z]", "" );
        waitForElementToBePresentByTextAndThenClick( parsedProductTitle );

    }

    private void enableTouchActions() {
        if(System.getProperty( "humanTouch" ).equals( "true" )){
            appiumActions.toggleElementAction("android.widget.Switch", "rs_promoted_filter_toggle");
            appiumActions.toggleFilterButton();
        }
    }

    private void addProductToCart() {
        try {
            Thread.sleep( 10000 );
            WebDriverWait wait = (WebDriverWait) new WebDriverWait( driver, 20 )
                    .pollingEvery( 20, TimeUnit.MILLISECONDS );
            final By byAddToCart = By.xpath( "//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']" );
            if ( wait.until( ExpectedConditions.presenceOfElementLocated( byAddToCart )).isDisplayed() ) {

                int bottomYofScreen = driver.manage().window().getSize().getHeight();
                int centerXofScreen = driver.manage().window().getSize().getWidth()/2;
                new TouchAction( driver ).press( 0, bottomYofScreen - 200 )
                        .waitAction( Duration.ofMillis( 1000 ) ).moveTo( 0, centerXofScreen+200 ).release().perform();
                new TouchAction( driver ).press( 0, bottomYofScreen - 200 )
                        .waitAction( Duration.ofMillis( 1000 ) ).moveTo( 0, centerXofScreen+200 ).release().perform();
                try {
                    Thread.sleep( 1000 );
                    WebElement webElement = driver.findElement( byAddToCart );
                    wait.until( ExpectedConditions.presenceOfElementLocated( byAddToCart )).isDisplayed();
                    webElement.click();
                } catch ( NoSuchElementException | InterruptedException e ) {
                    System.out.print( "Add to cart button cant be clicked" );
                    e.printStackTrace();
                }
            }
        }catch ( NoSuchElementException |InterruptedException e ){
            System.out.print( "Add to cart button not visible" );
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByIdAndThenClick( String id) {
        try {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 40)
                    .pollingEvery(40, TimeUnit.MILLISECONDS);
            while(!( wait.until( ExpectedConditions.presenceOfElementLocated( By.id( id ) )).isDisplayed())) {
                appiumActions.swipeUpElement( 700, 500);
            }
            final WebElement webElement = driver.findElement( By.id( id ));
            Thread.sleep( 5000 );
            webElement.click();
            System.out.print( "element found -- inside waitforElement" );
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( 700, 500 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void clickProceedToCheckout() {
        try {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 40)
                    .pollingEvery(40, TimeUnit.MILLISECONDS);
            final By byProceedToCheckout = By.xpath(
                    "//android.widget.Button[contains(@resource-id,'a-autoid-0-announce') and @text='Proceed to Checkout']" );
            while(!( wait.until( ExpectedConditions.presenceOfElementLocated(
                    byProceedToCheckout ))
                    .isDisplayed())) {
                appiumActions.swipeUpElement( 700, 500);
            }
            final WebElement webElement = driver.findElement( byProceedToCheckout );
            Thread.sleep( 5000 );
            webElement.click();
            System.out.print( "element found -- inside waitforElement" );
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( 700, 500 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByTextAndThenClick( String text ) throws TimeoutException {
        int timeOutValue = (Integer.parseInt( System.getProperty( "sessionTimeOutInterval" ) ))- 3;
        long startTime = System.currentTimeMillis();
        long elapsedTime;
        try {
            int indexOfText;
            Thread.sleep( 5 );
            while ( (indexOfText = element.getIndexByText( text ))  == -1){
                elapsedTime = (new Date()).getTime() - startTime;
                if (elapsedTime < timeOutValue*60*1000) {
                    appiumActions.swipeUpElement( 700, 600);
                }
                else{
                    System.out.println( "session has timed out. exiting the test with failure status" );
                    throw new TimeoutException( "session has timed out" );
                }
            }

            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 20)
                    .pollingEvery(20, TimeUnit.MILLISECONDS);
            if(wait.until(ExpectedConditions.presenceOfElementLocated( By.id( "item_title" ))).isDisplayed()) {
                final WebElement webElement = (WebElement) driver.findElements( By.id( "item_title" ) ).get( indexOfText );
                webElement.click();
            }
        } catch ( NoSuchElementException | InterruptedException ex ) {
            appiumActions.swipeUpElement( 700, 600);
        }
    }

    @AfterTest
    public void end() {
        driver.resetApp();
        driver.quit();
        System.out.println( "Stopped the driver" );
    }
}
