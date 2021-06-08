package tqsua.OrdersServer.WebApp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;
import org.json.JSONException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;
    private JavascriptExecutor js;

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

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    //Constructor
    public HomePage(WebDriver driver, String page_url){
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
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

    public Boolean check_home_page() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("logo")));
        }
        return this.pageLoaded();
    }

    public boolean isLogin() {
        return driver.findElement(By.cssSelector("h2")).getText().equals("Login");
    }

    public void login() throws IOException, InterruptedException, JSONException {
        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("email", "rafael2@gmail.com");
        data.put("password", "rafael123");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8081/api/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        System.out.println(json);
        System.out.println(js);
        js.executeScript(String.format(
        "window.sessionStorage.setItem('%s','%s');", "user_orders", json));
        driver.navigate().refresh();
    }
}
