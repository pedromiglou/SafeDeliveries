package tqsua.DeliveriesServer.WebApp.Navbar;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import tqsua.DeliveriesServer.WebApp.HomePage;

@ExtendWith(SeleniumJupiter.class)
public class NavbarFunctionalitiesSteps {
    private WebDriver driver = new FirefoxDriver();

    private HomePage home;

    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @When("I am not logged in my account")
    public void i_am_not_logged_in_my_account() {

    }

    @Then("It should not have the functionalities {string} and {string}")
    public void it_should_not_have_the_functionalities_and(String functionality1, String functionality2) {
        assertThat(home.searchTabExists(), is(false));
        assertThat(home.historyTabExists(), is(false));
    }

    @But("It should have the functionality {string}")
    public void but_it_should_have_the_functionality(String functionality) {
        assertThat(home.getLoginTab(), is(functionality));
        driver.quit();
    }

    @When("I am logged in my account")
    public void i_am_logged_in_my_account() throws JSONException, ClientProtocolException, IOException, InterruptedException {
        home.login();
    }

    @Then("It should have the functionalities {string} and {string}")
    public void it_should_have_the_functionality(String functionality1, String functionality2) {
        assertThat(home.getSearchDeliveryTab(), is(functionality1));
        assertThat(home.getHistoryTab(), is(functionality2));
        driver.quit();
    }

    @Then("It should have status {string}")
    public void it_should_have_status(String status) {
        assertThat(home.checkStatus(status), is(true));
        if (status.equals("Offline")) {
            System.out.println("entrei aqui");
            driver.quit();
        }
    }

    
    @When("I change status to {string}")
    public void change_status(String status) {
        home.clickLogo();
        home.changeStatus(status);
    }
}
