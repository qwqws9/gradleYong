package yong.controller;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

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

    private WebDriver driver;

    private static ChromeOptions option;

    private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

    // http://chromedriver.chromium.org/downloads 크롬 드라이버 버전에 맞게 설치
    private static final String WEB_DRIVER_PATH = "C:\\Users\\yong\\Downloads\\chromedriver_win32\\chromedriver.exe";

    // 로그인을 위한 네이버 페이지 URL
    private static final String NAVER_URL = "https://www.naver.com";

    // 마스크 판매하는 URL (네이버 스토어만 가능)
    private String maskShopUrl;

    private int count;

    static {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        option = new ChromeOptions();
        option.addArguments("user-agent= Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        option.addArguments("disable-gpu");
        // option.addArguments("headless");
    }

    @RequestMapping("/maskInfo")
    public String maskInfo() {
        // Math.floor(Math.random() * 10) + 1
        Random random = new Random();
        int result = random.nextInt(30) + 1;

        if (result > 1) { return "/mask/maskInfoView"; }

        return "/mask/maskInfo2";
    }

    @RequestMapping("/maskInfoView")
    public String maskInfoView() {
        return "/mask/maskInfoView";
    }

    /**
     * 
     * 미마 마스크
     *
     * @since 2020. 4. 9.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/mask")
    public String mask() {
//        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//
//        option = new ChromeOptions();
//        option.addArguments("user-agent= Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//        option.addArguments("disable-gpu");
        // option.addArguments("headless");
        driver = new ChromeDriver(option);
        
//        this.maskShopUrl = "http://localhost:8080/maskInfo";
         this.maskShopUrl ="https://smartstore.naver.com/aseado/products/4837257765";

        WebDriverWait wait = new WebDriverWait(driver, 18000);

        try {
            // 로그인을 위한 naver 이동
            driver.navigate().to(NAVER_URL);

            // 로그인 성공시까지 5초마다 DOM 탐색
            wait.pollingEvery(Duration.ofMillis(5000));
            wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    count++;
                    log.debug("로그인성공까지 대기중....");

                    // 브라우저 열리고 사용자가 조작시 DOM 탐색을 못하므로 15초마다 검색버튼 클릭
                    if (count % 3 == 0) {
                        driver.findElement(By.cssSelector("#search_btn")).click();
                    }
                    return driver.findElement(By.cssSelector(".gnb_my"));
                }
            });

            // 로그인 성공 후 마스크 판매처로 URL 이동
            driver.navigate().to(maskShopUrl);
            driver.get(maskShopUrl);
            WebElement webElement = null;

            wait = new WebDriverWait(driver, 60000);
            // 새로고침 속도
            wait.pollingEvery(Duration.ofMillis(100));
            
            boolean soldout = true;

            while (soldout) {
                // 구매하기 버튼 찾을때까지 새로고침 진행
                webElement = wait.until(new Function<WebDriver, WebElement>() {
                    @Override
                    public WebElement apply(WebDriver webDriver) {
                        driver.navigate().refresh();
                        return driver.findElement(By.linkText("구매하기"));
                    }
                });
                
                // select box label
                List<WebElement> list = driver.findElements(By.cssSelector(".selectbox-label"));
                // 옵션 리스트
                List<WebElement> selectOpt;
                boolean click = false;
                for (WebElement w : list) {
                    // 마스크가 포함된 셀렉트박스를 찾아 클릭
                    if (w.getText().contains("마스크")) {
                        w.click();
                        // selectbox-source 여러개가 있으나 제일 처음나오는것이 상품의 옵션임
                        WebElement w1 = driver.findElements(By.cssSelector(".selectbox-source")).get(0);
                        if (w1.getAttribute("title").equals("옵션 선택")) {
                            // 옵션 태그를 찾아 리스트 구성
                            selectOpt = w1.findElements(By.cssSelector("option"));
                            // 구매하기 버튼
                            click = false;

                            // 맘에드는 옵션을 contains에 포함시킨다. (옵션리스트 순회)
                            for (WebElement i : selectOpt) {
                                if (i.getText().contains("중형") && !i.getText().contains("품절")) {
                                    soldout = false;
                                    click = true;
                                    // 옵션 선택
                                    i.click();
                                }
                            }
                            if (click) {
                                webElement.click();
                            }
                        }
                    }
                }
//                if (!click) {
//                    driver.navigate().refresh();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";

    }
    
    
    @RequestMapping("/maskAer")
    public String maskAer() {
//        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//
//        option = new ChromeOptions();
//        option.addArguments("user-agent= Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//        option.addArguments("disable-gpu");
        // option.addArguments("headless");
        driver = new ChromeDriver(option);
        
//        this.maskShopUrl = "http://localhost:8080/maskInfo";
         this.maskShopUrl ="https://smartstore.naver.com/aer-shop/products/4722827602";

        WebDriverWait wait = new WebDriverWait(driver, 18000);

        try {
            // 로그인을 위한 naver 이동
            driver.navigate().to(NAVER_URL);

            // 로그인 성공시까지 5초마다 DOM 탐색
            wait.pollingEvery(Duration.ofMillis(5000));
            wait.until(new Function<WebDriver, WebElement>() {
                @Override
                public WebElement apply(WebDriver webDriver) {
                    count++;
                    log.debug("로그인성공까지 대기중....");

                    // 브라우저 열리고 사용자가 조작시 DOM 탐색을 못하므로 15초마다 검색버튼 클릭
                    if (count % 3 == 0) {
                        driver.findElement(By.cssSelector("#search_btn")).click();
                    }
                    return driver.findElement(By.cssSelector(".gnb_my"));
                }
            });

            // 로그인 성공 후 마스크 판매처로 URL 이동
            driver.navigate().to(maskShopUrl);
            driver.get(maskShopUrl);
            WebElement webElement = null;

            wait = new WebDriverWait(driver, 60000);
            // 새로고침 속도
            wait.pollingEvery(Duration.ofMillis(100));
            
            boolean soldout = true;

            while (soldout) {
                // 구매하기 버튼 찾을때까지 새로고침 진행
                webElement = wait.until(new Function<WebDriver, WebElement>() {
                    @Override
                    public WebElement apply(WebDriver webDriver) {
                        driver.navigate().refresh();
                        return driver.findElement(By.linkText("구매하기"));
                    }
                });
                
                // select box label
                List<WebElement> list = driver.findElements(By.cssSelector(".selectbox-label"));
                // 옵션 리스트
                List<WebElement> selectOpt;
                boolean click = false;
                for (WebElement w : list) {
                    // 마스크가 포함된 셀렉트박스를 찾아 클릭
                    if (w.getText().contains("마스크")) {
                        w.click();
                        // selectbox-source 여러개가 있으나 제일 처음나오는것이 상품의 옵션임
                        WebElement w1 = driver.findElements(By.cssSelector(".selectbox-source")).get(0);
                        if (w1.getAttribute("title").equals("옵션 선택")) {
                            // 옵션 태그를 찾아 리스트 구성
                            selectOpt = w1.findElements(By.cssSelector("option"));
                            // 구매하기 버튼
                            click = false;

                            // 맘에드는 옵션을 contains에 포함시킨다. (옵션리스트 순회)
                            for (WebElement i : selectOpt) {
                                if (i.getText().contains("L") && !i.getText().contains("품절")) {
                                    soldout = false;
                                    click = true;
                                    // 옵션 선택
                                    i.click();
                                }
                            }
                            if (click) {
                                webElement.click();
                            }
                        }
                    }
                }
//                if (!click) {
//                    driver.navigate().refresh();
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/";

    }
}
