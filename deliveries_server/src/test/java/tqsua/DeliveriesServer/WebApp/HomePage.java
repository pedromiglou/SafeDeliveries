package tqsua.DeliveriesServer.WebApp;

import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class HomePage {
    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(id = "logo")
    private WebElement logo;

    @FindBy(id = "home-tab")
    private WebElement home_tab;

    @FindBy(id = "search-tab")
    private WebElement search_tab;

    @FindBy(id = "history-tab")
    private WebElement history_tab;

    @FindBy(id = "aboutus-tab")
    private WebElement aboutus_tab;

    @FindBy(id = "login")
    private WebElement login;

    //Constructor
    public HomePage(WebDriver driver, String page_url){
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        //Initialise Elements
        driver.get(page_url);
        PageFactory.initElements(driver, this);
        driver.manage().window().maximize();
    }

    public boolean pageLoaded() {
        return driver.getTitle().equals("tqs");
    }

    public String getSearchDeliveryTab(){
        return this.search_tab.getText();
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
    public boolean searchTabExists(){
        if (driver.findElements(By.id("search-tab")).isEmpty()) {
            return false;
        }
        return true;
    }

    public void clickLogin(){
        this.login.click();
    }

    public boolean isLogin() {
        return driver.findElement(By.cssSelector("h2")).getText().equals("Login");
    }


    public void login_auto() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("email", "rafael2@gmail.com");
        json.put("token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyYWZhZWwyQGdtYWlsLmNvbSIsImV4cCI6MTYyMzA3NjM3Mn0.PVkhEWNd_RtM1BBbklZzfdfE3YpBH_cBEIR2fD4eiOSDm-OO61HWDUP7BqcDKDziAlMbrNNI4hIdVBk-HjVjuQ");
        js.executeScript(String.format(
        "window.sessionStorage.setItem('%s','%s');", "user", json));
        driver.navigate().refresh();
    }

    public Boolean check_home_page() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logo")));
        }
        return this.pageLoaded();
    }

}
