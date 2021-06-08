package tqsua.DeliveriesServer.WebApp.Profile;

import io.cucumber.java.en.*;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqsua.DeliveriesServer.WebApp.HomePage;
import tqsua.DeliveriesServer.WebApp.ProfilePage;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProfileFunctionalitiesSteps {
    private WebDriver driver = new FirefoxDriver();

    private ProfilePage profile;
    private HomePage home;

    // Background
    @Given("I go to {string}")
    public void i_access(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @And("I am logged into my account")
    public void i_logged_into_account() throws JSONException, ClientProtocolException, IOException, InterruptedException{
        home.login();
    }

    @When("I click on the user-logo")
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
        profile = new ProfilePage(driver);
        profile.clickAdd();
    }

    @Then("I have to fill the registration\\({string}), maker\\({string}), model\\({string}), capacity\\({string}) and type\\({string}) fields")
    public void i_fill_fields(String registration, String maker, String model, String capacity, String type) {
        profile.setInputs("registration", registration);
        profile.setInputs("maker", maker);
        profile.setInputs("model", model);
        profile.setInputs("capacity", capacity);
        profile.setInputs("type", type);
    }

    @Then("I have to click on button confirm")
    public void i_click_on_button_confirm() {
        profile.clickConfirm();
    }

    @And("Check that the vehicle with registration \\({string}) is in the table")
    public void check_vehicle_added_is_in_table(String registration){
        assertThat(profile.checkAdded(registration), is(registration));
    }

    //Scenario: Edit vehicle
    @When("I want to edit a vehicle, I Click on the pencil icon of the vehicle with registration \\({string})")
    public void i_click_on_pencil_icon(String registration){
        profile.clickEdit(registration);
    }

    @Then("Change the field referring to {string} to {string} and confirm")
    public void change_field(String id, String value){
        profile.setInputs(id, value);
    }

    @And("Check that the vehicle with registration \\({string}) was edited, and now has in the field {string} the value {string}")
    public void check_vehicle_edited(String registration, String id, String value){
        assertThat(profile.checkEdited(registration, id), is(value));
    }

    //Scenario: Delete vehicle
    @When("I want to delete a vehicle, I click on the delete icon of the vehicle with registration \\({string})")
    public void i_click_on_delete_icon(String registration) {
        profile.clickDelete(registration);
    }

    @Then("Check that the vehicle with registration \\({string}) is no longer in the table")
    public void checkThatItNoLongerExistsInTheTable(String registration) {
        assertThat(profile.checkDeleted(registration), is(false)) ;
    }
}
