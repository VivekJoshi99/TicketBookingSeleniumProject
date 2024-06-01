package org.testing;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class DataDrivenPageObjects {
    WebDriver driver;

    public DataDrivenPageObjects(WebDriver driver) {
        PageFactory.initElements(driver,this);
        this.driver = driver;
    }

    @FindBy(name="username")
    private WebElement Username;

    @FindBy(name="password")
    private WebElement Password;

    @FindBy(xpath="//button[contains(.,'Login')]")
    private WebElement Login;

    @FindBy(xpath="//div[@class='orangehrm-login-error']/div/p")
    private List<WebElement> Credentials;
}
