package tqsua.OrdersServer.WebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    private WebDriver driver;

    @FindBy(id = "logo")
    private WebElement logo;

    @FindBy(id = "home-tab")
    private WebElement home_tab;

    @FindBy(id = "request-tab")
    private WebElement request_tab;

    @FindBy(id = "history-tab")
    private WebElement history_tab;

    @FindBy(id = "aboutus-tab")
    private WebElement aboutus_tab;

    @FindBy(id = "login")
    private WebElement login;

    //Constructor
    public HomePage(WebDriver driver, String page_url){
        this.driver = driver;
        driver.get(page_url);
        //Initialise Elements
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public boolean pageLoaded() {
        return driver.getTitle().equals("tqs");
    }

    public String getSearchDeliveryTab(){
        return this.request_tab.getText();
    }

    public String getHistoryTab(){
        return this.history_tab.getText();
    }

    public String getLoginTab(){
        return this.login.getText();
    }

    public boolean historyTabExists(){
        if (driver.findElements(By.id("history-tab")).isEmpty()) {
            return false;
        }
        return true;
    }
    public boolean requestTabExists(){
        if (driver.findElements(By.id("request-tab")).isEmpty()) {
            return false;
        }
        return true;
    }

    public void clickLogin(){
        this.login.click();
    }
}
