package tqsua.DeliveriesServer.WebApp.Statistics;

import io.cucumber.java.en.*;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import tqsua.DeliveriesServer.WebApp.HomePage;
import tqsua.DeliveriesServer.WebApp.StatisticsPage;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class StatisticsSteps {
    private WebDriver driver = new FirefoxDriver();

    private HomePage home;
    private StatisticsPage statistics;

    // Background
    @Given("I go to the url {string}")
    public void i_access(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @And("I am logged into admin account")
    public void i_logged_into_admin_account() throws JSONException, ClientProtocolException, IOException, InterruptedException{
        home.loginWithAdmin();
    }

    @Then("It should have admin text")
    public void admin_text() {
        assertThat(home.getAdminText(), is("SafeDeliveries - Admin"));
    }

    @When("I click on the statistics page")
    public void click_on_statistics(){
        home.clickStatistics();
    }

    @Then("I have statistics page with total orders")
    public void check_total_orders_title(){
        statistics = new StatisticsPage(driver);
        assertThat(statistics.getTotalOrdersTitle(), is("Total Orders"));
        driver.quit();
    }

}
