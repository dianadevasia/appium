import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class AppiumActions {

    private AndroidDriver driver;

    AppiumActions( AndroidDriver driver ){
        this.driver = driver;
    }

    public void swipeUpElement( final int y2, final int y1 ) {
        try {
            TouchAction touchAction = new TouchAction( driver );
            touchAction.press( 0, y2 ).moveTo( 0, y1 ).release().perform();
            Thread.sleep( 3000 );
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
            final WebElement closedFilterDropdown = driver.findElement( By.xpath( "//android.widget.TextView[contains(@resource-id,'rs_filter_header_label') and @text='Filter']" ) );
            if ( closedFilterDropdown.isDisplayed() ) {
                closedFilterDropdown.click();
                Thread.sleep( 5000 );
                final WebElement openedFilterDropdown = driver.findElement( By.xpath( "//android.widget.Button[contains(@resource-id,'refinements_menu_accessibility_dismiss_button')]" ) );
                openedFilterDropdown.click();
                Thread.sleep( 2000 );
            }
        } catch ( NoSuchElementException | InterruptedException e ) {
            e.printStackTrace();
        }
    }

    public void toggleElementAction( String type, String id) {
        try {
            final WebElement primeToggle = driver.findElement(
                    By.xpath( "//" + type + "[contains(@resource-id,'" + id + "')]" ) );
            if ( primeToggle.isDisplayed() ) {
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
