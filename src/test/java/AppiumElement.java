import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import io.appium.java_client.android.AndroidDriver;

public class AppiumElement {

    private final AppiumActions appiumActions;
    private final AndroidDriver driver;

    public AppiumElement(AndroidDriver driver) {
        this.driver = driver;
        appiumActions = new AppiumActions();
    }

    public boolean isPresentByText( String text ) {
        try {
            if( driver.findElements( By.xpath("//*[contains(@text,'"+text+"')]")).size()>0)
                return true;
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver, 700, 600);
            return false;
        }
        return false;
    }


    public boolean isPresentById( String id) {
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

}
