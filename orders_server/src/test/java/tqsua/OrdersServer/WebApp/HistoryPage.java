package tqsua.OrdersServer.WebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HistoryPage {
    private WebDriver driver;

    @FindBy(id = "title")
    private WebElement title;

    @FindBy(id = "order-0")
    private WebElement order;

    //Constructor
    public HistoryPage(WebDriver driver){
        this.driver = driver;
        //Initialise Elements
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public String getTitleText(){
        return this.title.getText();
    }

    public Boolean checkOrderExists(){
        return !driver.findElements(By.id("order-0")).isEmpty();
    }

    public void clickOrder(){
        this.order.click();    
    }
}
