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

    private WebDriver webDriver;

    @Given("I navigate to {string}")
    public void i_navigate_to(String url) {
        WebDriverManager.firefoxdriver().setup();
        webDriver = new FirefoxDriver();
        webDriver.get(url);
    }

    @When("I am not logged in my account")
    public void i_am_not_logged_in_my_account() {

    }

    @And("I click in {string} button")
    public void i_click_button(String button) {
        webDriver.findElement(By.xpath("//input[@value=\'" + button + "\']")).click();
    }

    @And("I submit the register form")
    public void i_submit_form() {
        webDriver.findElement(By.cssSelector("i")).click();
        assertThat(webDriver.findElement(By.cssSelector("h2")).getText(), is("Register"));
        webDriver.findElement(By.id("register_firstname_input")).click();
        webDriver.findElement(By.id("register_firstname_input")).sendKeys("rafael");
        webDriver.findElement(By.id("register_lastname_input")).sendKeys("baptista");
        webDriver.findElement(By.id("register_email_input")).sendKeys("rafael@gmail.com");
        webDriver.findElement(By.id("register_password_input")).sendKeys("rafael123");
        webDriver.findElement(By.id("register_confirmpass_input")).sendKeys("rafael123");
        webDriver.findElement(By.cssSelector(".button-entrar")).click();
    }

    @Then("It should have the message {string}")
    public void it_should_not_have_the_functionalities_and(String message) {
        {
            WebDriverWait wait = new WebDriverWait(webDriver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert")));
          }
          assertThat(webDriver.findElement(By.cssSelector(".alert")).getText(), is(message));
    }

}
