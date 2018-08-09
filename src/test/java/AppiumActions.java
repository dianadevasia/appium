import org.openqa.selenium.NoSuchElementException;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;

public class AppiumActions {
    public void swipeUpElement( AndroidDriver driver, final int y2, final int y1 ) {
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

}
