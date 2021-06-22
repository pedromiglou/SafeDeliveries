package tqsua.OrdersServer.WebApp;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestOrderPage {
    private WebDriver driver;
    private JavascriptExecutor js;

    @FindBy(id = "paddress")
    private WebElement paddress;

    @FindBy(id = "pcountry")
    private WebElement pcountry;

    @FindBy(id = "pcity")
    private WebElement pcity;

    @FindBy(id = "pzip")
    private WebElement pzip;

    @FindBy(id = "daddress")
    private WebElement daddress;

    @FindBy(id = "dcountry")
    private WebElement dcountry;

    @FindBy(id = "dcity")
    private WebElement dcity;

    @FindBy(id = "dzip")
    private WebElement dzip;

    @FindBy(id = "checkMap")
    private WebElement button_check_map;

    @FindBy(id = "button-add")
    private WebElement button_add_item;

    @FindBy(id = "place-order")
    private WebElement button_place_order;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    //Constructor
    public RequestOrderPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.js = (JavascriptExecutor) driver;
    }

    public void fillAddress(String id, ArrayList<String> values){
        if (id.equals("pickup")){
            paddress.sendKeys(values.get(0));
            pcountry.sendKeys(values.get(1));
            pcity.sendKeys(values.get(2));
            pzip.sendKeys(values.get(3));
        } else if (id.equals("delivery")){
            daddress.sendKeys(values.get(0));
            dcountry.sendKeys(values.get(1));
            dcity.sendKeys(values.get(2));
            dzip.sendKeys(values.get(3));
        }
    }

    public void checkMap() {
        button_check_map.click();
    }

    public void clickAddItem(){
        button_add_item.click();
    }

    public void fillItemDetails(String name, String category, String weight){
        WebElement inp_name = driver.findElement(By.id("n_name"));
        WebElement inp_category = driver.findElement(By.id("n_category"));
        WebElement inp_weight = driver.findElement(By.id("n_weight"));
        WebElement item_confirm = driver.findElement(By.id("confirm_item"));

        inp_name.sendKeys(name);
        inp_category.sendKeys(category);
        inp_weight.sendKeys(weight);
        item_confirm.click();
    }

    public void confirmOrder(){
        button_place_order.click();
    }

    public String getOrderConfirmationMessage() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order-success")));
        }
        WebElement message_div = driver.findElement(By.id("order-success"));
        return message_div.getText();
    }

    public boolean waiting_rider() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("waiting_rider")));
        }
        String waiting_text = driver.findElement(By.id("waiting_rider")).getText();
        return waiting_text.equals("Waiting for a rider");
    }

    public void simulateRiderAcceptsOrder() throws IOException, InterruptedException {
        // form parameters
        Long deliver_id = Long.parseLong(driver.findElement(By.id("track_id")).getText());
        Map<Object, Object> data = new HashMap<>();
        data.put("order_id", deliver_id);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValueAsString(data);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create("http://localhost:8081/api/orders/notificate"))
                .header("Content-type", "application/json")
                .build();

        httpClient.send(request, HttpResponse.BodyHandlers.ofString());


    }

    public boolean redirected_confirmDetails() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order_details")));
        }
        String order_details_text = driver.findElement(By.id("order_details")).getText();
        return order_details_text.equals("Order details");
    }

    public String confirmAddress(String id){
        if (id.equals("pickup")){
            String pickup_address = driver.findElement(By.id("pickup_address")).getText();
            return pickup_address;
        } else if (id.equals("delivery")){
            String delivery_address = driver.findElement(By.id("delivery_address")).getText();
            return delivery_address;
        }
        return "";
    }

    public boolean verifyItem(int key, String name1, String category1, String weight1, String name2, String category2, String weight2) {
        String table_name = driver.findElement(By.id("name_" + key)).getAttribute("placeholder");
        String table_category_ = driver.findElement(By.id("category_" + key)).getAttribute("placeholder");
        String table_weight = driver.findElement(By.id("weight_" + key)).getAttribute("placeholder");

        return ( table_name.equals(name1) || table_name.equals(name2) )
                && ( table_category_.equals(category1) || table_category_.equals(category2) )
                && ( table_weight.equals(weight1) || table_weight.equals(weight2) );
    }

    public String getErrorMsg() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("order-error")));
        }
        WebElement message_div = driver.findElement(By.id("order-error"));
        return message_div.getText();
    }
}
