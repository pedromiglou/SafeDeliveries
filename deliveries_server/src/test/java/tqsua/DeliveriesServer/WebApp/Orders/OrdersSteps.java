package tqsua.DeliveriesServer.WebApp.Orders;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tqsua.DeliveriesServer.WebApp.HomePage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

public class OrdersSteps {
    
    private WebDriver driver = new FirefoxDriver();

    private JavascriptExecutor js = (JavascriptExecutor) driver;

    private HomePage home;

    private String token;

    // Background
    @Given("I access the url {string}")
    public void i_access(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @And("I am logged into normal account")
    public void i_logged_into_account() throws JSONException, ClientProtocolException, IOException, InterruptedException{
        token = home.login();
    }

    @And("I have a car")
    public void i_have_a_car() throws JSONException, ClientProtocolException, IOException, InterruptedException, URISyntaxException{
        token = home.login();
        Map<String, Object> data = new HashMap<>();
        data.put("brand", "Tesla");
        data.put("model", "Model 3");
        data.put("category", "Carro");
        data.put("capacity", 320.0);
        data.put("registration", "AABBCC");
        String info = String.valueOf(js.executeScript("return window.sessionStorage.getItem('user');"));
        System.out.println(js.executeScript("return window.sessionStorage.getItem('user');"));
        JSONObject json = new JSONObject(info);
        data.put("rider", json.getInt("id"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", token);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        
        String baseUrl = "http://localhost:8080/api/private/vehicle";
        URI uri = new URI(baseUrl);
        
        new RestTemplate().postForEntity(uri, entity, String.class);
        
    }

    @When("I receive a notification of a order")
    public void receive_notification() throws URISyntaxException, JSONException {
        home.clickLogo();
        home.changeStatus("Online");

        Map<String, Object> data = new HashMap<>();
        data.put("pick_up_lat", 40.6405);
        data.put("pick_up_lng", 8.6538);
        data.put("deliver_lat", 40.23);
        data.put("deliver_lng", 23.24);
        data.put("weight", 12.0);
        data.put("app_name", "SafeDeliveries");

        String baseUrl = "http://localhost:8080/api/orders";
        URI uri = new URI(baseUrl);
        
        new RestTemplate().postForEntity(uri, data, String.class);

        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("accept_order_button")));
        }
    }

    @And("I click Accept")
    public void i_accept_order() throws JSONException, ClientProtocolException, IOException, InterruptedException{
        home.clickAccept();
    }

    @Then("My status changed to Delivering")
    public void status_delivering() {
        assertThat(home.checkStatus("Delivering"), is(true));
        driver.close();
    }


    @And("I click Decline")
    public void i_decline_order() throws JSONException, ClientProtocolException, IOException, InterruptedException{
        home.clickDecline();
    }

    @Then("My status stays Online")
    public void status_online() {
        assertThat(home.checkStatus("Online"), is(true));
        driver.close();
    }

}
