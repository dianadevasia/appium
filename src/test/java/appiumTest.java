import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class appiumTest {

    private AndroidDriver driver;
    private AppiumActions appiumActions;
    private AppiumElement element;

    @Test
    public void checkoutProductFlow() {
//        propertyFile = readFromFile();
        startAppiumServer( System.getProperty( "port" ) );
        setUpDriver();
        appiumActions = new AppiumActions();
        element = new AppiumElement( driver );
        try {
            findForProductInTheList( System.getProperty( "searchKeywords"), System.getProperty( "productTitle" ) );
            addProductToCart();
        }
        catch ( NoSuchElementException e ) {
            System.out.println( "Element not found" );
            e.printStackTrace();
        }
    }

    private void startAppiumServer( final String port ) {
        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(
                new AppiumServiceBuilder()
                        .withAppiumJS(new File("/usr/local/Cellar/node/7.4.0/lib/node_modules/appium/build/lib/main.js"))
                        .withIPAddress("127.0.0.1")
                        .usingPort(Integer.parseInt(port)));
        service.start();
    }

    private void setUpDriver() {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            // Parameterize this device name
            capabilities.setCapability( CapabilityType.BROWSER_NAME, "Android" );
            capabilities.setCapability( "platformName", "Android" );
            capabilities.setCapability( MobileCapabilityType.AUTOMATION_NAME, AutomationName.APPIUM );
            capabilities.setCapability( MobileCapabilityType.FULL_RESET, true );

            capabilities.setCapability( CapabilityType.VERSION, System.getProperty("androidVersion" ) );
            capabilities.setCapability( "deviceName", System.getProperty("deviceName" ) );
            capabilities.setCapability( "app", System.getProperty("apkPath" ) );
            capabilities.setCapability( "appPackage", "com.amazon.mShop.android.shopping" );
            capabilities.setCapability( "appActivity", "com.amazon.mShop.home.HomeActivity" );
            //        capabilities.setCapability(MobileCapabilityType.AUTO_WEBVIEW, true);

            //        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities) {};
            driver = new AndroidDriver<>( new URL( "http://127.0.0.1:" + System.getProperty("port" ) + "/wd/hub" ), capabilities );
            driver.manage().timeouts().implicitlyWait( 25, TimeUnit.SECONDS );
        } catch ( MalformedURLException e ){
            System.out.println( "Could not create the android driver" );
            e.printStackTrace();
            end();
        }
    }

    private void findForProductInTheList( final String searchKeyword, final String productTitle ) {
        try {
            WebElement skipSignInButton = driver.findElement(
                    By.xpath( "//android.widget.Button[contains(@resource-id,'com.amazon.mShop.android.shopping:id/skip_sign_in_button') and @text='Skip sign in']" ) );
            skipSignInButton.click();
        }
        catch ( NoSuchElementException ex ){
            System.out.println( "Sign in button not present" );
        }
        WebElement searchTextView = driver.findElement( By.xpath( "//android.widget.EditText" ) );
        final String parsedSearchKeyword = searchKeyword.replaceAll( "_", " " );
        final String parsedProductTitle = productTitle.replaceAll( "_", " " );
        searchTextView.sendKeys( parsedSearchKeyword + "  \n" );
        waitForElementToBePresentByTextAndThenClick( parsedProductTitle );
    }

    private void addProductToCart() {
        WebElement webElement =
                driver.findElement( By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"));
        int topYofWebElement = webElement.getLocation().getY();
        int bottomYofWebElement = topYofWebElement + webElement.getSize().getHeight();
        int centerXofWebElement = webElement.getLocation().getX() + (webElement.getSize().getWidth()/2);
        new TouchAction(driver).press(centerXofWebElement, bottomYofWebElement-200).waitAction(10000).moveTo(0, centerXofWebElement).release().perform();
        new TouchAction(driver).press(centerXofWebElement, bottomYofWebElement-200).waitAction(10000).moveTo(0, centerXofWebElement).release().perform();
        webElement.click();
        try {
            Thread.sleep( 5000 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByIdAndThenClick( String id) {
        try {
            while (!element.isPresentById( id )){

                appiumActions.swipeUpElement( driver,700, 500);
            }
            final WebElement webElement =
                    driver.findElement( By.xpath("//android.widget.Button[contains(@resource-id,'add-to-cart-button') and @text='Add to Basket']"));

            appiumActions.swipeUpElement( driver,700, 530 );
            Thread.sleep( 5000 );
            webElement.click();
            Thread.sleep( 600000 );
            System.out.print( "element found -- inside waitforElement" );
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver, 700, 500 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void waitForElementToBePresentByTextAndThenClick( String text ) {
        try {
            while (!element.isPresentByText( text )){
                appiumActions.swipeUpElement( driver, 700, 600);
            }
            final WebElement webElement =
                    (WebElement) driver.findElements(By.xpath("//*[contains(@text,'"+text+"')]")).get( 0 );
            webElement.click();
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver,700, 600);
        }
    }

    private static void runCommandAndWaitToComplete( String[] command ) {
        String completeCommand = String.join(" ", command);
        System.out.println("Executing command: " + completeCommand);
        String line;
        String returnValue = "";

        try {
            Process processCommand = Runtime.getRuntime().exec(command);
            BufferedReader response = new BufferedReader(new InputStreamReader(processCommand.getInputStream()));

            try {
                processCommand.waitFor();
            } catch (InterruptedException commandInterrupted) {
                System.out.println("Were waiting for process to end but something interrupted it" + commandInterrupted.getMessage());
            }

            while ((line = response.readLine()) != null) {
                returnValue = returnValue + line + "\n";
            }
            response.close();

        } catch (Exception e) {
            System.out.println("Unable to run command: " + completeCommand + ". Error: " + e.getMessage());
        }
        System.out.println("Response : runCMDAndWaitToComplete(" + completeCommand + ") : " + returnValue);
    }

    private static void stopAppiumServer(String port) {
        System.out.println(String.format("Stopping Appium server on port %s", port));

        // command to stop Appium service running on port --> sh -c lsof -P | grep ':4723' | awk '{print $2}' | xargs kill -9
        String stopCommand[] = new String[]{"sh", "-c", String.format("lsof -P | grep ':%s' | awk '{print $2}' | xargs kill -9", port)};
        runCommandAndWaitToComplete(stopCommand);
    }

    @AfterTest
    public void end() {
        driver.quit();
        if(System.getProperty( "port" ) != null)
            stopAppiumServer( System.getProperty( "port" ) );
    }
}
