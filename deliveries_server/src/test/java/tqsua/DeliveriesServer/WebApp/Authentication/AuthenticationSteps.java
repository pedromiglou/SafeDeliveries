package tqsua.DeliveriesServer.WebApp.Authentication;

import io.cucumber.java.en.*;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.extension.ExtendWith;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import tqsua.DeliveriesServer.WebApp.HomePage;
import tqsua.DeliveriesServer.WebApp.LoginPage;
import tqsua.DeliveriesServer.WebApp.RegisterPage;

@ExtendWith(SeleniumJupiter.class)
public class AuthenticationSteps {

    private WebDriver driver = new FirefoxDriver();;

    private HomePage home;

    private LoginPage login_page;

    private RegisterPage register_page;

    //Background
    @Given("I access {string}")
    public void i_access(String url) {
        home = new HomePage(driver, url);
        assertThat(home.pageLoaded(), is(true));
    }

    @And("I click in login tab")
    public void i_click_login_button() {
        home.clickLogin();
        login_page = new LoginPage(driver);
    }

    //Create an account
    @When("I click on create account")
    public void iClickOnCreateAccount() {
        login_page.register_click();
        register_page = new RegisterPage(driver);
        assertThat(register_page.getTitle(), is("Register"));
    }

    @And("I fill all the details asked")
    public void iFillAllTheDetailsAsked() {
        register_page.setInputs("register_firstname_input", "Rafael");
        register_page.setInputs("register_lastname_input", "Baptista");
        register_page.setInputs("register_email_input", "rafael2@gmail.com");
        register_page.setInputs("register_password_input", "rafael123");
        register_page.setInputs("register_confirmpass_input", "rafael123");
        register_page.setInputs("register_city_input", "Aveiro");
    }

    @And("I click on create")
    public void iClickOnCreate() {
        register_page.clickCreate();
    }

    @Then("It should have the message {string}")
    public void it_should_not_have_the_functionalities_and(String message) {
        assertThat(login_page.check_alert(), is(message));
        driver.quit();
    }


    //Login
    @When("I fill my credentials")
    public void iFillMyCredentials() {
        login_page.setInputs("login_email_input", "rafael2@gmail.com");
        login_page.setInputs("login_password_input", "rafael123");
    }

    @And("I click in enter button")
    public void iClickInEnterButton() {
        login_page.clickCreate();
    }

    @Then("I should be redirected to the main page")
    public void iShouldBeRedirectedToTheMainPage() {
        assertThat(home.check_home_page(), is(true));
        driver.quit();
    }
}
