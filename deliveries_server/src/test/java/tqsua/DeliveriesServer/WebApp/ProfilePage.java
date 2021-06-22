package tqsua.DeliveriesServer.WebApp;

import java.util.concurrent.TimeUnit;

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

    @FindBy(id= "edit-icon")
    private WebElement edit_icon;

    @FindBy(id= "email")
    private WebElement email;

    @FindBy(id= "fname")
    private WebElement fname;

    @FindBy(id= "lname")
    private WebElement lname;

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

    /*
    @FindBy(id= "button_nconfirm")
    private WebElement button_nconfirm;
    */

    private String registration;

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void setRegistration(String registration){
        this.registration = registration;
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

    public void setInputsEditable(String id, String value){
        switch (id) {
            case "registration":
                driver.findElement(By.name("registration_" + this.registration)).sendKeys(value);
                break;
            case "brand":
                driver.findElement(By.name("brand_" + this.registration)).sendKeys(value);
                break;
            case "model":
                driver.findElement(By.name("model_" + this.registration)).sendKeys(value);
                break;
            case "category":
                driver.findElement(By.name("category_" + this.registration)).sendKeys(value);
                break;
            case "capacity":
                driver.findElement(By.name("capacity_" + this.registration)).sendKeys(value);
                break;
        }
    }

    public void clickConfirm() {
        driver.findElement(By.id("button-nconfirm")).click();
    }

    public void clickConfirmEdit(){
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("edit_c" + this.registration)));
        }
        driver.findElement(By.id("edit_c" + this.registration)).click();
    }

    public String checkAdded() {
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.name("registration_" + this.registration)));
        }
        return driver.findElement(By.name("registration_" + this.registration)).getAttribute("placeholder");
    }

    public String checkEdited(String id) throws InterruptedException{
        TimeUnit.SECONDS.sleep(2);
        String value;
        switch (id) {
            case "registration":
                value = driver.findElement(By.name("registration_" + this.registration)).getAttribute("placeholder");
                break;
            case "brand":
                value = driver.findElement(By.name("brand_" + this.registration)).getAttribute("placeholder");
                break;
            case "model":
                value = driver.findElement(By.name("model_" + this.registration)).getAttribute("placeholder");
                break;
            case "category":
                value = driver.findElement(By.name("category_" + this.registration)).getAttribute("placeholder");
                break;
            case "capacity":
                value = driver.findElement(By.name("capacity_" + this.registration)).getAttribute("placeholder");
                break;
            default:
                value="";
        }
        return value;
    }

    public boolean checkDeleted(){
        if ( driver.findElements(By.name("registration_" + this.registration)).isEmpty() ) {
            return true;
        }
        return false;
    }

    public void clickAdd() {
        driver.findElement(By.id("button-add")).click();
    }

    public void clickEdit() {
        driver.findElement(By.id("edit_" + this.registration)).click();
    }

    public void clickDelete(){
        driver.findElement(By.id("del_" + this.registration)).click();
        {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("registration_" + this.registration)));
        }
    }

    public void clickEditDetails() {
        this.edit_icon.click();
    }

    public void changeDetail(String id, String value) {
        switch (id) {
            case "email":
                this.email.sendKeys(value);
                break;
            case "first name":
                this.fname.sendKeys(value);
                break;
            case "last name":
                this.lname.sendKeys(value);
                break;
        }
    }

    public void confirmDetail() {
        driver.findElement(By.id("confirm-detail")).click();
    }

    public boolean checkDetail(String id, String value) {
        switch (id) {
            case "email":
                if (this.email.getAttribute("placeholder").equals(value)){
                    return true;
                }
                return false;
            case "first name":
                if (this.fname.getAttribute("placeholder").equals(value)){
                    return true;
                }
                return false;
            case "last name":
                if (this.lname.getAttribute("placeholder").equals(value)){
                    return true;
                }
                return false;
        }
        return false;
    }


}
