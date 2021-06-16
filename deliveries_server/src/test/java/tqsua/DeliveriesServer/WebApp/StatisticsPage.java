package tqsua.DeliveriesServer.WebApp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class StatisticsPage {
    private WebDriver driver;

    @FindBy(id= "total_orders")
    private WebElement total_orders;

    public StatisticsPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTotalOrdersTitle() {
        return this.total_orders.getText();
    }
    
}
