package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private WebDriver driver;

    @FindBy(css = "#inputUsername")
    private WebElement usernameField;
    @FindBy(css = "#inputPassword")
    private WebElement passwordField;
    @FindBy(css = "#signup-link")
    private WebElement signUpLink;
    @FindBy(css = "#submit-button")
    private WebElement submitButton;


    public LoginPage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibility(WebElement element) throws Error {
        new WebDriverWait(driver, 60)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void login(String username, String password){
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitButton.click();
    }

    public void goToSignUp(){
        waitForVisibility(signUpLink);
        signUpLink.click();
    }

}
