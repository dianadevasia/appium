import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class AppiumActions {

    private AndroidDriver driver;

    AppiumActions( AndroidDriver driver ){
        this.driver = driver;
    }

    public void swipeUpElement( final int y2, final int y1 ) {
        try {
            int centerXofScreen = driver.manage().window().getSize().getWidth()/2;
            int centerYofScreen = driver.manage().window().getSize().getHeight()/2;
            new TouchAction(driver).press(centerXofScreen, centerYofScreen+200).waitAction( Duration.ofMillis( 2000))
                    .moveTo(centerXofScreen, y1).release().perform();
            Thread.sleep( 2000 );
        }
        catch ( NoSuchElementException ex ){
            ex.printStackTrace();
            System.out.println( "element not found -- inside swipeUpElement " );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
    }

    public void toggleFilterButton(){
        try {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 2)
                    .pollingEvery(20, TimeUnit.MILLISECONDS)
                    .ignoring(StaleElementReferenceException.class);

            if ( wait.until( ExpectedConditions.presenceOfElementLocated( By.xpath( "//android.widget.TextView[contains(@resource-id,'rs_filter_header_label') and @text='Filter']" ) ) ).isDisplayed() ) {
                final WebElement closedFilterDropdown = driver.findElement( By.xpath( "//android.widget.TextView[contains(@resource-id,'rs_filter_header_label') and @text='Filter']" ) );
                closedFilterDropdown.click();
                Thread.sleep( 2000 );
                final WebElement openedFilterDropdown = driver.findElement( By.xpath( "//android.widget.Button[contains(@resource-id,'refinements_menu_accessibility_dismiss_button')]" ) );
                openedFilterDropdown.click();
                Thread.sleep( 1000 );
            }
        } catch ( NoSuchElementException | InterruptedException e ) {
            e.printStackTrace();
        }
    }

    public void toggleElementAction( String type, String id) {
        try {
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 2)
                    .pollingEvery(20, TimeUnit.MILLISECONDS)
                    .ignoring(StaleElementReferenceException.class);

            if(wait.until(
                    ExpectedConditions.presenceOfElementLocated( By.xpath( "//" + type + "[contains(@resource-id,'" + id + "')]" ))
            ).isDisplayed())
            {
            final WebElement primeToggle = driver.findElement(
                    By.xpath( "//" + type + "[contains(@resource-id,'" + id + "')]" ) );
                primeToggle.click();
                Thread.sleep( 2000 );
                primeToggle.click();
                Thread.sleep( 2000 );
            }
        } catch ( NoSuchElementException | InterruptedException e ) {
            e.printStackTrace();
        }
    }
}
