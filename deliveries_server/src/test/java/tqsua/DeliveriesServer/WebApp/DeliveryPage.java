package tqsua.DeliveriesServer.WebApp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class DeliveryPage {
    private WebDriver driver;

    @FindBy(id = "total_weight")
    private WebElement total_weight;

    @FindBy(id = "rating")
    private WebElement rating;
    
    //Constructor
    public DeliveryPage(WebDriver driver){
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public String getTotalWeightText(){
        return this.total_weight.getText();
    }

    public String getRatingText(){
        return this.rating.getText();
    }

}
