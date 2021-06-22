package tqsua.OrdersServer.WebApp.RequestOrder;

import io.cucumber.java.en.*;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.json.JSONException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import tqsua.OrdersServer.WebApp.DeliveryPage;
import tqsua.OrdersServer.WebApp.HistoryPage;
import tqsua.OrdersServer.WebApp.HomePage;
import tqsua.OrdersServer.WebApp.RequestOrderPage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@ExtendWith(SeleniumJupiter.class)
public class RequestOrderSteps {
    private WebDriver driver = new FirefoxDriver();

    private HomePage home;
    private RequestOrderPage requestPage;
    private HistoryPage historyPage;
    private DeliveryPage deliveryPage;


    //Background
    @Given("I go to {string}")
    public void iGoTo(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @And("I am logged in")
    public void iAmLoggedIn() throws IOException, InterruptedException, JSONException{
        home.login();
    }

    @And("I click on Request Delivery Tab")
    public void iClickOnRequestDeliveryTab() {
        home.clickRequest();
        requestPage = new RequestOrderPage(driver);
    }

    // Request Order
    @Given("I fill pick up address \\(Addres {string}, Country {string}, City {string}, Postal Code {string})")
    public void iFillPickUpAddressAddresCountryCityPostalCode(String address, String country, String city, String postal_code) {
        ArrayList<String> paddress = constructAddress(address, country, city, postal_code);
        requestPage.fillAddress("pickup", paddress);
    }

    @And("I fill delivery address \\(Addres {string}, Country {string}, City {string}, Postal Code {string})")
    public void iFillDeliveryAddressAddresCountryCityPostalCode(String address, String country, String city, String postal_code) {
        ArrayList<String> daddress = constructAddress(address, country, city, postal_code);
        requestPage.fillAddress("delivery", daddress);
        requestPage.checkMap();
    }

    @And("I add item\\(s) by clicking on button Add")
    public void iAddItemSByClickingOnButtonAdd() {
        requestPage.clickAddItem();
    }

    @And("filling the fields with name {string}, category {string} and weight {string}")
    public void fillingTheFieldsWithNameCategoryAndWeight(String name, String category, String weight) {
        requestPage.fillItemDetails(name, category, weight);
    }

    @And("adding another item with name {string}, category {string} and weight {string}")
    public void addingAnotherItemWithNameCategoryAndWeight(String name, String category, String weight) {
        requestPage.fillItemDetails(name, category, weight);
    }

    @When("I click on button Place Order")
    public void iClickOnButtonPlaceOrder() {
        requestPage.confirmOrder();
    }

    @Then("I should see the message {string}")
    public void iShouldSeeTheMessage(String message) {
        assertThat(requestPage.getOrderConfirmationMessage(), is(message));
    }

    @And("I should be redirected to the Waiting For a Rider Page")
    public void iShouldBeRedirectedToTheWaitingForARiderPage() {
        assertThat(requestPage.waiting_rider(), is(true));
    }

    @When("a rider accepts my order, I should be redirected to the Order Details Page")
    public void aRiderAcceptsMyOrderIShouldBeRedirectedToTheOrderDetailsPage() throws IOException, InterruptedException {
        requestPage.simulateRiderAcceptsOrder();
        assertThat(requestPage.redirected_confirmDetails(), is(true));
    }

    @And("I can see the order that I requested has pick up address {string}")
    public void iCanSeeTheOrderThatIRequestedHasPickUpAddress(String address) {
        assertThat(requestPage.confirmAddress("pickup"), is(address));
    }

    @And("delivery address {string}")
    public void deliveryAddress(String address) {
        assertThat(requestPage.confirmAddress("delivery"), is(address));
    }

    @And("the Items with names {string} and {string}, categories {string} and {string} and weights {string} and {string}")
    public void theItemsWithNamesAndCategoriesAndAndWeightsAnd(String name1, String name2, String category1, String category2, String weight1, String weight2) {
        assertThat(requestPage.verifyItem(0, name1, category1, weight1, name2, category2, weight2), is(true) );
        assertThat(requestPage.verifyItem(1, name1, category1, weight1, name2, category2, weight2), is(true) );
        driver.quit();
    }

    public ArrayList<String> constructAddress(String address, String country, String city, String postal_code){
        ArrayList<String> new_address = new ArrayList<>();
        new_address.add(address);
        new_address.add(country);
        new_address.add(city);
        new_address.add(postal_code);
        return new_address;
    }

    // Request Order with no items
    @Given("I want to do a order but i forget to add items")
    public void iWantToDoAOrderButIForgetToAddItems() {

    }

    @And("I click to Place Order")
    public void iClickToPlaceOrder() {
        requestPage.confirmOrder();
    }

    @Then("I get the warning {string}")
    public void iGetTheWarning(String error_msg) {
        assertThat(requestPage.getErrorMsg(), is(error_msg));
        driver.quit();
    }

    @And("I click on History Tab")
    public void iClickOnHistoryTab() {
        home.clickHistoryTab();
        historyPage = new HistoryPage(driver);
    }

    @Then("It should appear a order")
    public void should_appear_order() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        assertThat(historyPage.checkOrderExists(), is(true));
    }

    @When("I click in the order")
    public void clickOrder() throws URISyntaxException, JSONException {
        historyPage.clickOrder();
        deliveryPage = new DeliveryPage(driver);
    }

    @When("It sould appear the same information")
    public void checkInfo() throws URISyntaxException, JSONException {
        assertThat(deliveryPage.getPickUpText(), is("R. Circular 3, 3140 Pereira, Portugal"));
        assertThat(deliveryPage.getDestinyText(), is("Rua De Pedride 223, 4635-616 São Lourenço, Portugal"));
    }

    @When("I accept Order")
    public void acceptOrder() throws URISyntaxException, JSONException {
        deliveryPage.acceptOrder();
    }

    @Then("It should appear status Delivered")
    public void checkMessage() throws URISyntaxException, JSONException {
        assertThat(deliveryPage.getDeliveredMessage() , is("Status: Delivered") );
        driver.quit();
    }
}
