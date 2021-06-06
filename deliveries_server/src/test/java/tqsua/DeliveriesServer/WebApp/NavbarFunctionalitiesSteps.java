package tqsua.DeliveriesServer.WebApp;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.bonigarcia.seljup.SeleniumJupiter;

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
    }

    @When("I am logged in my account")
    public void i_am_logged_in_my_account() {
        home.clickLogin();
    }

    @Then("It should have the functionalities {string} and {string}")
    public void it_should_have_the_functionality(String functionality1, String functionality2) {
        assertThat(home.getSearchDeliveryTab(), is(functionality1));
        assertThat(home.getHistoryTab(), is(functionality2));
    }
}