package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {
    @FindBy(css = "#inputFirstName")
    private WebElement firstNameField;
    @FindBy(css = "#inputLastName")
    private WebElement lastNameField;
    @FindBy(css = "#inputUsername")
    private WebElement usernameField;
    @FindBy(css = "#inputPassword")
    private WebElement passwordField;
    @FindBy(css = "#login-link")
    private WebElement loginLink;
    @FindBy(css = "#submit-button")
    private WebElement submitButton;


    public SignUpPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void signUp(String firstName, String lastName, String username, String password) {
        firstNameField.sendKeys(firstName);
        lastNameField.sendKeys(lastName);
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);
        submitButton.click();
    }


    public void goToLogin(){
        loginLink.click();
    }

}
