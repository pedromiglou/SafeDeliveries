package tqsua.OrdersServer.WebApp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class RegisterPage {
    private WebDriver driver;
    
    @FindBy(css = "h2")
    private WebElement title;

    @FindBy(id= "register_firstname_input")
    private WebElement register_firstname_input;

    @FindBy(id= "register_lastname_input")
    private WebElement register_lastname_input;

    @FindBy(id= "register_email_input")
    private WebElement register_email_input;

    @FindBy(id= "register_password_input")
    private WebElement register_password_input;

    @FindBy(id= "register_confirmpass_input")
    private WebElement register_confirmpass_input;

    @FindBy(className = "button-entrar")
    private WebElement button_entrar;


    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public String getTitle() {
        return this.title.getText();
    }

    public void setInputs(String id, String value) {
        switch (id) {
            case "register_firstname_input":
                register_firstname_input.sendKeys(value);
                break;
            case "register_lastname_input":
                register_lastname_input.sendKeys(value);
                break;
            case "register_email_input":
                register_email_input.sendKeys(value);
                break;
            case "register_password_input":
                register_password_input.sendKeys(value);
                break;
            case "register_confirmpass_input":
                register_confirmpass_input.sendKeys(value);
                break;
        }

    }

    public void clickCreate() {
        this.button_entrar.click();;
    }
}
