package com.udacity.jwdnd.course1.cloudstorage.page;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {
    private WebDriver driver;

    @FindBy(id = "log-out-button")
    private WebElement logoutButton;

    @FindBy(xpath = "//a[@id='nav-notes-tab']")
    private WebElement notesTab;

    @FindBy(id = "add-note-modal-button")
    private WebElement addNoteModalButton;
    @FindBy(id = "edit-note-button")
    private WebElement editNoteModalButton;
    @FindBy(id = "delete-note-button")
    private WebElement deleteNoteModalButton;
    @FindBy(id = "note-id")
    private WebElement noteId;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;


    @FindBy(id = "note-title-row")
    private WebElement noteTitle;

    @FindBy(id = "note-description-row")
    private WebElement noteDescription;


    @FindBy(id = "note-submit-button")
    private WebElement noteSubmitButton;

    @FindBy(id = "note-delete-button")
    private WebElement noteDeleteButton;

    //Credential
    @FindBy(xpath = "//a[@id='nav-credentials-tab']")
    private WebElement credentialsTab;

    @FindBy(id = "add-credential-modal-button")
    private WebElement addCredentialModalButton;

    @FindBy(id = "edit-credential-button")
    private WebElement editCredentialModalButton;

    @FindBy(id = "delete-credential-button")
    private WebElement deleteCredentialModalButton;

    @FindBy(id = "credential-id")
    private WebElement credentialId;

    @FindBy(id = "credential-url")
    private WebElement urlInput;

    @FindBy(id = "credential-username")
    private WebElement usernameInput;

    @FindBy(id = "credential-password")
    private WebElement passwordInput;

    @FindBy(id = "credential-url-row")
    private WebElement url;

    @FindBy(id = "credential-username-row")
    private WebElement username;

    @FindBy(id = "credential-password-row")
    private WebElement password;

    @FindBy(id = "credential-submit-button")
    private WebElement credentialSubmitButton;

    @FindBy(id = "credential-delete-button")
    private WebElement credentialDeleteButton;

    @FindBy(id = "credentialModal")
    private WebElement credentialModal;

    public HomePage(WebDriver webDriver) {
        driver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    private void waitForVisibility(WebElement element) throws Error {
        new WebDriverWait(driver, 60)
                .until(ExpectedConditions.visibilityOf(element));
    }


    public void logout() {
        logoutButton.click();
    }


    public void goToNotesTab() {
        waitForVisibility(notesTab);
        notesTab.click();
    }

    public void goToCredentialsTab() {
        waitForVisibility(credentialsTab);
        credentialsTab.click();
    }


    public void addNewNote(String noteTitle, String noteDescription) {
        goToNotesTab();
        waitForVisibility(addNoteModalButton);
        addNoteModalButton.click();
        saveNote(noteTitle, noteDescription);
    }

    private void saveNote(String noteTitle, String noteDescription) {
        waitForVisibility(noteTitleInput);
        this.noteTitleInput.sendKeys(noteTitle);
        this.noteDescriptionInput.sendKeys(noteDescription);
        waitForVisibility(noteSubmitButton);
        this.noteSubmitButton.click();
    }


    public Note getFirstNote() {
        waitForVisibility(noteTitle);
        Note result = new Note();
        result.setNoteTitle(noteTitle.getText());
        result.setNoteDescription(noteDescription.getText());
        return result;
    }

    public void editFirstNote(String noteTitle, String noteDescription) {
        goToNotesTab();
        waitForVisibility(editNoteModalButton);
        editNoteModalButton.click();
        clearNoteInputs();
        saveNote(noteTitle, noteDescription);
    }

    public void clearNoteInputs() {
        waitForVisibility(noteTitleInput);
        this.noteTitleInput.clear();
        this.noteDescriptionInput.clear();
    }

    public boolean isNoteTitleDisplayed() {
        return isElementPresent(By.id("note-title-row"));
    }

    public boolean isNoteDescriptionDisplayed() {
        return isElementPresent(By.id("note-description-row"));
    }

    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    public void deleteFirstNote() {
        goToNotesTab();
        waitForVisibility(deleteNoteModalButton);
        deleteNoteModalButton.click();
        waitForVisibility(noteDeleteButton);
        noteDeleteButton.click();
    }

    public void addNewCredential(String url, String username, String password) {
        goToCredentialsTab();
        waitForVisibility(addCredentialModalButton);
        addCredentialModalButton.click();
        saveCredential(url, username, password);
    }


    public void saveCredential(String url, String username, String password) {
        waitForVisibility(urlInput);
        this.urlInput.sendKeys(url);
        this.usernameInput.sendKeys(username);
        this.passwordInput.sendKeys(password);
        waitForVisibility(credentialSubmitButton);
        this.credentialSubmitButton.click();
    }

    public Credential getFirstCredential() {
        waitForVisibility(url);
        Credential result = new Credential();
        result.setUrl(url.getText());
        result.setUsername(username.getText());
        result.setPassword(password.getText());
        return result;
    }

    public void goToEditButton() {
        waitForVisibility(editCredentialModalButton);
        editCredentialModalButton.click();
    }

    public void editFirstCredential(String url, String username, String password) {
        clearCredentialInput();
        saveCredential(url, username, password);
    }

    public void clearCredentialInput() {
        waitForVisibility(urlInput);
        this.urlInput.clear();
        this.usernameInput.clear();
        this.passwordInput.clear();
    }

    public Credential getCredentialModalInput() {
        goToEditButton();
        waitForVisibility(credentialModal);
        Credential result = new Credential();
        result.setUrl(this.urlInput.getAttribute("value"));
        result.setUsername(this.usernameInput.getAttribute("value"));
        result.setPassword(this.passwordInput.getAttribute("value"));
        return result;
    }


    public void deleteFirstCredential() {
        goToCredentialsTab();
        waitForVisibility(deleteCredentialModalButton);
        deleteCredentialModalButton.click();
        waitForVisibility(credentialDeleteButton);
        credentialDeleteButton.click();
    }

    public boolean isCredentialUrlDisplayed() {
        return isElementPresent(By.id("credential-url-row"));
    }

    public boolean isCredentialUsernameDisplayed() {
        return isElementPresent(By.id("credential-username-row"));
    }


}
