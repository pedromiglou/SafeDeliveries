package tqsua.DeliveriesServer.WebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "register_icon")
    private WebElement create_button;

    @FindBy(id= "login_email_input")
    private WebElement login_email_input;

    @FindBy(id= "login_password_input")
    private WebElement login_password_input;

    @FindBy(className = "button-entrar")
    private WebElement button_entrar;

    public LoginPage(WebDriver driver2) {
        this.driver = driver2;
        PageFactory.initElements(driver2, this);
    }

    public void register_click() {
        this.create_button.click();
    }

    public String check_alert() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".alert")));
        }
        return driver.findElement(By.cssSelector(".alert")).getText();
    }

    public void setInputs(String id, String value) {
        switch (id) {
            case "login_email_input":
                login_email_input.sendKeys(value);
                break;
            case "register_password_input":
                login_password_input.sendKeys(value);
                break;
        }
    }

    public void clickCreate() {
        this.button_entrar.click();;
    }
    
}
