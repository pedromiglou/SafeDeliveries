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

import java.io.IOException;
import java.util.Arrays;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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

    @FindBy(id = "perfil-dropdown")
    private WebElement profile_logo;

    @FindBy(id = "profile-div")
    private WebElement profile;

    @FindBy(id = "admin_text")
    private WebElement admin_text;

    @FindBy(id = "statistics-tab")
    private WebElement statistics;

    @FindBy(id = "accept_order_button")
    private WebElement accept_order_button;

    @FindBy(id = "decline_order_button")
    private WebElement decline_order_button;

    @FindBy(id = "error_status_message")
    private WebElement error_status_message;

    @FindBy(id = "logout")
    private WebElement logout;
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

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

    public String getAdminText(){
        return this.admin_text.getText();
    }

    public String getErrorStatus(){
        return this.error_status_message.getText();
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

    public void clickAccept(){
        this.accept_order_button.click();
    }

    public void clickDecline(){
        this.decline_order_button.click();
    }

    public void clickStatistics(){
        this.statistics.click();
    }

    public void  clickProfile(){
        this.profile.click();
    }

    public String login() throws IOException, InterruptedException, JSONException {
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
                .uri(URI.create("http://localhost:8080/api/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());

        js.executeScript(String.format(
        "window.sessionStorage.setItem('%s','%s');", "user", json));
        driver.navigate().refresh();
        return "Bearer " + json.getString("token");
    }

    public void loginWithAdmin() throws IOException, InterruptedException, JSONException {
        // form parameters
        Map<Object, Object> data = new HashMap<>();
        data.put("email", "admin@gmail.com");
        data.put("password", "admin123");
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8080/api/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());

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

    public Boolean checkStatus(String status) {
        WebElement perfil = driver.findElement(By.id("perfil-dropdown"));
        switch (status) {
            case "Online":
                return Arrays.asList(perfil.getAttribute("class").split(" ")).contains("online");
            case "Delivering":
                return Arrays.asList(perfil.getAttribute("class").split(" ")).contains("delivering");
            case "Offline":
                return Arrays.asList(perfil.getAttribute("class").split(" ")).contains("offline");
        }
        return null;
    }

    public void clickLogo(){
        this.profile_logo.click();
    }

    public void changeStatus(String status) {
        switch (status) {
            case "Online":
                driver.findElement(By.id("state-online")).click();
                break;
            case "Delivering":
                driver.findElement(By.id("state-delivering")).click();
                break;
            case "Offline":
                driver.findElement(By.id("state-off")).click();
                break;
        }
        driver.findElement(By.id("perfil-dropdown")).click();
    }

    public void changeStatusToOnlineWhenDelivering() {
        driver.findElement(By.id("state-online")).click();
    }
}
