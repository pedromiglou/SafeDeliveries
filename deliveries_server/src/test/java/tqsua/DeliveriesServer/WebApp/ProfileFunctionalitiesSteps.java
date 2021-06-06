package tqsua.DeliveriesServer.WebApp;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProfileFunctionalitiesSteps {
    private WebDriver driver = new FirefoxDriver();

    private ProfilePage profile;
    private HomePage home;

    // Background
    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }
    @And("I am logged into my account")
    public void i_logged_into_account(){
        home.clickLogin();
    }
    @When("I click on the logo")
    public void click_on_logo(){
        home.clickLogo();
    }
    @And("I click on Profile")
    public void click_on_profile(){
        home.clickProfile();
    }

    //Scenario: Add vehicle
    @When("I want to add a new vehicle, I have to click on button add")
    public void i_click_on_button_add() {

    }

    @Then("I have to fill the maker\\({string}), model\\({string}), capacity\\({string}) and type\\({string}) fields")
    public void it_should_not_have_the_functionalities_and(String maker, String model, String capacity, String type) {

    }

    @Then("I have to click on button confirm")
    public void i_click_on_button_confirm(String functionality) {

    }

    @And("Check that the vehicle added is in the table")
    public void check_vehicle_added_is_in_table(){

    }

    //Scenario: Edit vehicle
    @When("I want to edit a vehicle, I Click on the pencil icon of the vehicle {string}")
    public void i_click_on_pencil_icon(String vehicle){

    }

    @Then("Change the field referring to {string} to {string} and confirm")
    public void change_field(String field, String new_field_value){

    }

    @And("Check that the vehicle {string} was edited")
    public void check_vehicle_edited(String vehicle){

    }

    //Scenario: Delete vehicle
    @When("I want to delete a vehicle, I click on the delete icon of the vehicle {string}")
    public void i_click_on_delete_icon(String vehicle) {

    }

    @Then("I click on button confirm")
    public void i_click_button_confirm() {

    }

    @And("Check that the vehicle {string} is no longer in the table")
    public void checkThatItNoLongerExistsInTheTable() {

    }
}
