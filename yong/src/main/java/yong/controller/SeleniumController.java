package yong.controller;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SeleniumController {

    //WebDriver
    private WebDriver driver;
    
    //private WebElement webElement;
    
    private ChromeOptions option;
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    // http://chromedriver.chromium.org/downloads 크롬 드라이버 버전에 맞게 설치
    public static final String WEB_DRIVER_PATH = "C:\\Users\\yong\\Downloads\\chromedriver_win32\\chromedriver.exe";
    
    //크롤링 할 URL
    private String url;
    
    @RequestMapping("/maskInfo")
    public String maskInfo() {
        
        return "/mask/maskInfo";
    }
    
    @RequestMapping("/mask")
    public String mask() {
        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        
        option = new ChromeOptions();
        option.addArguments("user-agent= Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//            option.addArguments("headless");
//        option.addArguments("disable-gpu");
        
        //Driver SetUp
        driver = new ChromeDriver(option);
        
        //url = "https://www.naver.com";
        url ="http://localhost:8080/maskInfo";
        
        try {
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            driver.get(url);
            
            int count = 0;
            WebElement webElement = null; 
            log.debug("webElement => {}", webElement);
            while((webElement = driver.findElement(By.linkText("구매하기"))) == null) {
                driver.navigate().refresh();
            }
            log.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            log.debug("webElement => {}", webElement);
            log.debug("_"+driver.findElement(By.linkText("구매하기")).getSize()+"_");
            log.debug("_"+driver.findElement(By.linkText("구매하기"))+"_");
            log.debug("_"+driver.findElement(By.linkText("구매하기")).getTagName()+"_");
            log.debug(ExpectedConditions.visibilityOfElementLocated(By.linkText("구매하기"))+"");
            
            
//            while (ExpectedConditions.visibilityOfElementLocated(By.linkText("구매하기")) {
//                driver.navigate().refresh();
//                count++;
//                log.debug(count+"");
//                if (count == 500) {
//                    break;
//                }
//            }
            
            driver.navigate().refresh();
            //driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, 20);
            
//                      wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("공동운항")));
//            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main-container > div > div > div.js-results-main-panel > div > div.wide-wrapper.container.result.js-results-container > section.container.tickets > div.js-paginator.paginator-box > div > div > div.results-count")));
//           By.
            String html = driver.getPageSource();
            System.out.println(html);
        } catch (Exception e) {
            
        }
            
        
        return "redirect:/";


    }
}
