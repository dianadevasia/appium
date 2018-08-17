import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.android.AndroidDriver;

public class AppiumElement {

    private final AppiumActions appiumActions;
    private final AndroidDriver driver;

    AppiumElement( AndroidDriver driver ) {
        this.driver = driver;
        appiumActions = new AppiumActions(driver);
    }

    public int getIndexByText( String text ) {
        try {
            Thread.sleep( 10 );
            WebDriverWait wait = (WebDriverWait) new WebDriverWait(driver, 40)
                    .pollingEvery(40, TimeUnit.MILLISECONDS);
            if(wait.until( ExpectedConditions.presenceOfElementLocated( By.id( "item_title" ) )).isDisplayed()) {
                final List<WebElement> itemTitlesElement = driver.findElements( By.id( "item_title" ) );
                int counter = 0;
                for ( WebElement itemTitleElement : itemTitlesElement ) {
                    String titleText = itemTitleElement.getText();
                    titleText = titleText.replaceAll( "\\p{Pd}", "-" );
                    String s = titleText.replaceAll( "[^0-9a-zA-Z]", "" );
                    if ( s.equals( text ) )
                        return counter;
                    counter++;
                }
            }
            return -1;
        } catch ( NoSuchElementException |InterruptedException ex ) {
            appiumActions.swipeUpElement( 700, 600);
            return -1;
        }
    }

    public boolean isPresentById( String id) {
        try {
            if( driver.findElements( By.id(id)).size() > 0)
                return true;
        } catch ( NoSuchElementException ex ) {
            appiumActions.swipeUpElement( 700, 500);
            return false;
        }
        return false;
    }

}
