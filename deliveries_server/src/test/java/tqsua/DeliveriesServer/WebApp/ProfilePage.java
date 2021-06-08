package tqsua.DeliveriesServer.WebApp;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProfilePage {
    private WebDriver driver;

    /*
    @FindBy(id = "button-add")
    private WebElement button_add;
     */

    @FindBy(id= "n_registration")
    private WebElement n_registration;

    @FindBy(id= "n_brand")
    private WebElement n_brand;

    @FindBy(id= "n_model")
    private WebElement n_model;

    @FindBy(id= "n_category")
    private WebElement n_category;

    @FindBy(id= "n_capacity")
    private WebElement n_capacity;

    @FindBy(id= "button_nconfirm")
    private WebElement button_nconfirm;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickAdd() {
        //this.button_add.click();
        driver.findElement(By.id("button-add")).click();
    }

    public void setInputs(String id, String value){
        switch (id) {
            case "registration":
                this.n_registration.sendKeys(value);
                break;
            case "brand":
                this.n_brand.sendKeys(value);
                break;
            case "model":
                this.n_model.sendKeys(value);
                break;
            case "category":
                this.n_category.sendKeys(value);
                break;
            case "capacity":
                this.n_capacity.sendKeys(value);
                break;
        }
    }

    public void clickConfirm() {
        this.button_nconfirm.click();
    }

    public String checkAdded(String registration) {
        return driver.findElement(By.name("registration_" + registration)).getAttribute("value");
    }
}
