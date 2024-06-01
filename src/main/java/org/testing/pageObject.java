package org.testing;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class pageObject {

    WebDriver driver;
    WebDriverWait wait;
    List<String> expectedStationEntries = new ArrayList<>();
    List<String> actualStationEntries = new ArrayList<>();
    Map<String, List<String>> expectedValues = new LinkedHashMap<>();
    Map<String, List<String>> actualValues = new LinkedHashMap<>();
    String bookingMonth;
    String bookingDay;
    Utils utils;

    public pageObject(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        utils = new Utils();
    }

    @FindBy(id="txtStationFrom")
    private WebElement StationFrom;

    @FindBy(id="txtStationTo")
    private WebElement StationTo;

    @FindBy(xpath="(//div[@class='autocomplete'])[1]/div")
    private List<WebElement> StationFromAutosuggestion;

    @FindBy(xpath="(//div[@class='autocomplete'])[2]/div")
    private List<WebElement> StationToAutosuggestion;

    @FindBy(xpath="//input[@title='Select Departure date for availability']")
    private WebElement calenderViewField;

    public void enterSource(String from){
        waitForElementVisibility(StationFrom);
        StationFrom.clear();
        StationFrom.sendKeys(from);
        waitForAllElementVisibility(StationFromAutosuggestion);
        StationFromAutosuggestion.get(4).click();
    }

    public WebElement setCalenderView(String month , String day) {
        String xpathExpression = String.format("//div[@id='divCalender']//td[contains(text(),'%s')]//parent::tr//following-sibling::tr/td[contains(text(),'%s')]",month,day);
        return driver.findElement(By.xpath(xpathExpression));
    }

    public void enterDestination(String to){
        waitForElementVisibility(StationTo);
        StationTo.clear();
        StationTo.sendKeys(to);
        waitForAllElementVisibility(StationToAutosuggestion);
        StationToAutosuggestion.get(3).click();
    }

    public void selectBookingDate(){
        getBookingDateAndMonth();
        waitForElementVisibility(calenderViewField);
        calenderViewField.click();
        waitForElementVisibility(setCalenderView(bookingMonth, bookingDay));
        setCalenderView(bookingMonth, bookingDay).click();
    }

    public void waitForElementClickable(WebElement element){
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementVisibility(WebElement element){
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForAllElementVisibility(List<WebElement> element){
        wait.until(ExpectedConditions.visibilityOfAllElements(element));
    }

    public void readExcelSheet() throws IOException {
        String fileName = "./stationList.xlsx";
        FileInputStream fis  = new FileInputStream(fileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("ExpectedStation");
        int rowCount = sheet.getLastRowNum();
        for (int i=1 ; i<=rowCount ; i++){
            expectedStationEntries.add(sheet.getRow(i).getCell(0).toString());
            expectedValues.put("StationValues",expectedStationEntries);
        }
        fis.close();
    }

    public void writeOnExcelSheet() throws IOException {
        String fileName = "./stationList.xlsx";
        FileInputStream fis  = new FileInputStream(fileName);
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheet("ExpectedStation");
        for(int i=1 ; i<=StationFromAutosuggestion.size() ; i++){
            XSSFRow row = sheet.getRow(i);
            if(row==null){
                row = sheet.createRow(i);
            }
            if(row!=null){
                XSSFCell cell = row.createCell(2);
                cell.setCellValue(StationFromAutosuggestion.get(i-1).getAttribute("title"));
            }
        }
        FileOutputStream fos = new FileOutputStream(fileName);
        wb.write(fos);
        int rowCount = sheet.getLastRowNum();
        for (int i=1 ; i<=rowCount ; i++){
            actualStationEntries.add(sheet.getRow(i).getCell(2).toString());
            actualValues.put("StationValues",actualStationEntries);
        }
        fis.close();
        wb.close();
        fos.close();
    }
    public void compareStations(){
        utils.compare(expectedStationEntries, actualStationEntries);
    }
    public void getBookingDateAndMonth(){
        LocalDate currentDate = LocalDate.now();
        LocalDate bookingDate = currentDate.plusDays(30);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");
        String formattedBookingDate = bookingDate.format(formatter);
        int firstIndex = formattedBookingDate.indexOf("-");
        int lastIndex = formattedBookingDate.lastIndexOf("-");

        bookingDay = formattedBookingDate.substring(0,firstIndex);
        bookingMonth = formattedBookingDate.substring(firstIndex+1 , lastIndex);
    }
}
