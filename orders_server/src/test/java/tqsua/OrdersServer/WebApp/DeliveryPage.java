package tqsua.OrdersServer.WebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class DeliveryPage {
    private WebDriver driver;

    @FindBy(id = "pickup_address")
    private WebElement pick_up;

    @FindBy(id = "delivery_address")
    private WebElement destiny;

    @FindBy(id = "confirm_delivery")
    private WebElement confirm_delivery;
    
    @FindBy(id = "status_delivered")
    private WebElement status_delivered;

    @FindBy(id = "confirm_order_delivery_button")
    private WebElement confirm_order_delivery_button;

    //Constructor
    public DeliveryPage(WebDriver driver){
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public String getPickUpText(){
        return this.pick_up.getText();
    }

    public String getDestinyText(){
        return this.destiny.getText();
    }

    public void acceptOrder(){
        this.confirm_delivery.click();
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.elementToBeClickable(By.id("confirm_order_delivery_button")));
        }
        this.confirm_order_delivery_button.click();
    }

    public String getDeliveredMessage(){
        return this.status_delivered.getText();
    }

}
