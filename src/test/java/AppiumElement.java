import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import io.appium.java_client.android.AndroidDriver;

public class AppiumElement {

    private final AppiumActions appiumActions;
    private final AndroidDriver driver;

    public AppiumElement(AndroidDriver driver) {
        this.driver = driver;
        appiumActions = new AppiumActions();
    }

    public int getIndexByText( String text ) {
        try {
            final List<WebElement> itemTitlesElement = driver.findElements( By.id( "item_title" ) );
            int counter = 0;
            for( WebElement itemTitleElement : itemTitlesElement){
                final String titleText = itemTitleElement.getText();
                String s = titleText.replaceAll( " ", "" ).replaceAll("\u00A0","");
                if( s.equals( text ))
                    return counter;
                counter++;
            }
            return -1;
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( driver, 700, 600);
            return -1;
        }
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
