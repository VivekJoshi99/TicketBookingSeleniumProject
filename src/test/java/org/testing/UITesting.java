package org.testing;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;

public class UITesting {

    WebDriver driver;
    pageObject obj;
    DataDrivenPageObjects drivenPageObject;
    private static ExtentReports extent;
    private static ExtentHtmlReporter htmlReporter;
    private static ExtentTest test;

    @Before
    public void setup(){
        driver = new ChromeDriver();
        obj = new pageObject(driver);
        drivenPageObject = new DataDrivenPageObjects(driver);
        htmlReporter = new ExtentHtmlReporter("extent.html");
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
    }

    @After
    public void TearDown(){
        driver.quit();
        extent.flush();
    }

    @Test
    public void testing() throws IOException {
        test = extent.createTest("Test Name");
        driver.get("https://erail.in/");
        obj.enterSource("del");
        obj.enterDestination("Mum");
        obj.readExcelSheet();
        obj.writeOnExcelSheet();
        obj.compareStations();
        obj.selectBookingDate();
        test.pass("Step details");
    }

    @Test
    public void dataDrivenTesting(){
        test = extent.createTest("Data Driven Testing");
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        
    }
}
