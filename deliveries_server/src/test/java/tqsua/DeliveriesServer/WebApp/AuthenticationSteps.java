package tqsua.DeliveriesServer.WebApp;

import io.cucumber.java.en.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import org.junit.jupiter.api.extension.ExtendWith;
import io.github.bonigarcia.seljup.SeleniumJupiter;
import io.github.bonigarcia.wdm.WebDriverManager;

@ExtendWith(SeleniumJupiter.class)
public class AuthenticationSteps {

    private WebDriver driver = new FirefoxDriver();;

    private HomePage home;

    //Background
    @Given("I access {string}")
    public void i_access(String url) {
        home = new HomePage(driver, url);
        //assertThat(home.pageLoaded(), is(true));
    }

    @And("I click in login tab")
    public void i_click_login_button() {
        home.clickLogin();
    }

    //Create an account
    @When("I click on create account")
    public void iClickOnCreateAccount() {
        driver.findElement(By.cssSelector("i")).click();
        assertThat(driver.findElement(By.cssSelector("h2")).getText(), is("Register"));
    }

    @And("I fill all the details asked")
    public void iFillAllTheDetailsAsked() {
        driver.findElement(By.id("register_firstname_input")).click();
        driver.findElement(By.id("register_firstname_input")).sendKeys("rafael");
        driver.findElement(By.id("register_lastname_input")).sendKeys("baptista");
        driver.findElement(By.id("register_email_input")).sendKeys("rafael@gmail.com");
        driver.findElement(By.id("register_password_input")).sendKeys("rafael123");
        driver.findElement(By.id("register_confirmpass_input")).sendKeys("rafael123");
    }

    @And("I click on create")
    public void iClickOnCreate() {
        driver.findElement(By.cssSelector(".button-entrar")).click();
    }

    @Then("It should have the message {string}")
    public void it_should_not_have_the_functionalities_and(String message) {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert")));
          }
          assertThat(driver.findElement(By.cssSelector(".alert")).getText(), is(message));
    }


    //Login
    @When("I fill my credentials")
    public void iFillMyCredentials() {
    }

    @And("I click in enter button")
    public void iClickInEnterButton() {

    }

    @Then("I should be redirected to the main page")
    public void iShouldBeRedirectedToTheMainPage() {

    }
}
