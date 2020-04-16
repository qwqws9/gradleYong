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

//    private WebDriver driver;

    private static ChromeOptions option;

    private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

    // http://chromedriver.chromium.org/downloads 크롬 드라이버 버전에 맞게 설치 후 경로 기입
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

    /**
     * 
     * 미마 마스크 졸업 // 엔에이웰
     *
     * @since 2020. 4. 9.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/mask")
    public String mask() {
        //엔에이웰
        this.maskShopUrl ="https://smartstore.naver.com/pyeongpyoen/products/4690028600";
        //미마
//        this.maskShopUrl ="https://smartstore.naver.com/aseado/products/4837257765";
        this.getMask("마스크","중형","품절");

        return "redirect:/";
    }
    
    /**
     * 
     * 아에르 마스크
     *
     * @since 2020. 4. 10.
     * @author yong
     *
     * @return
     */
    @RequestMapping("/maskAer")
    public String maskAer() {
        this.maskShopUrl ="https://smartstore.naver.com/aer-shop/products/4722827602";
        getMask("필수옵션","L","품절");

        return "redirect:/";
    }
    
    @RequestMapping("/maskDrpuri1")
    public String maskDrpuri1() {
        this.maskShopUrl ="https://smartstore.naver.com/mfbshop/products/4072573492";
        getMask("사이즈","대형","품절");
        
        return "redirect:/";
    }
    @RequestMapping("/maskDrpuri2")
    public String maskDrpuri2() {
        this.maskShopUrl ="https://smartstore.naver.com/mfbshop/products/4072435942";
        getMask("사이즈","대형","품절");
        
        return "redirect:/";
    }
    
    @RequestMapping("/mask3")
    public String mask3() {
//        this.maskShopUrl ="https://smartstore.naver.com/gonggami/products/4705579501";
        
        this.maskShopUrl ="https://smartstore.naver.com/soommask/products/4828127993";
        this.getMaskNoOption();

        return "redirect:/";
    }
    
    @RequestMapping("/mask4")
    public String mask4() {
        this.maskShopUrl ="https://smartstore.naver.com/gonggami/products/4705579501";
        
//        this.maskShopUrl ="https://smartstore.naver.com/soommask/products/4828127993";
        this.getMaskNoOption();
        
        return "redirect:/";
    }
    
    /** 샤인웰 10:00 */
    @RequestMapping("/mask5")
    public String mask5() {
        this.maskShopUrl ="https://smartstore.naver.com/shinewell_healthcare/products/4861603330";
        this.getMaskNoOption();
        
        return "redirect:/";
    }
    
    
    
    /**
     * 
     * 옵션 선택항목이 없는 사이트
     *
     * @since 2020. 4. 10.
     * @author yong
     *
     */
    private void getMaskNoOption() {
        WebDriver driver = new ChromeDriver(option);

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
            driver.navigate().to(this.maskShopUrl);
            driver.get(this.maskShopUrl);
            WebElement webElement = null;

            wait = new WebDriverWait(driver, 60000);
            // 새로고침 속도
            wait.pollingEvery(Duration.ofMillis(10));
            
            boolean soldout = true;

            while (soldout) {
                // 구매하기 버튼 찾을때까지 새로고침 진행
                webElement = wait.until(new Function<WebDriver, WebElement>() {
                    @Override
                    public WebElement apply(WebDriver webDriver) {
                        driver.navigate().refresh();
                        // 페이지 에러날때 네이버 홈 이동 후 다시 사이트로 이동 (테스트중)
                        if (driver.findElements(By.className("title_error")).size() != 0) {
                            driver.navigate().to(NAVER_URL);
                            driver.findElement(By.cssSelector("#search_btn")).click();
                            driver.navigate().to(maskShopUrl);
                            driver.get(maskShopUrl);
                        }

                        return driver.findElement(By.linkText("구매하기"));
                    }
                });
                log.debug("==============>>>>>>>>>>>>>>>>>>>>>>>>> {}",webElement.getAttribute("class"));
                if (webElement.getAttribute("class").length() > 12) {
                    soldout = false;
                    webElement.click();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    /**
     * 
     * 네이버 로그인 후 판매사이트에서 마스크 구매
     *
     * @since 2020. 4. 10.
     * @author yong
     *
     * @param selectText 셀렉트박스 기본 텍스트
     * @param containText 셀렉트박스에서 포함될 단어
     * @param exceptText 셀렉트박스에서 제외될 단어
     */
    private void getMask(String selectText, String containText,String exceptText) {
        WebDriver driver = new ChromeDriver(option);

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
            driver.navigate().to(this.maskShopUrl);
            driver.get(this.maskShopUrl);
            WebElement webElement = null;

            wait = new WebDriverWait(driver, 60000);
            // 새로고침 속도
            wait.pollingEvery(Duration.ofMillis(10));
            
            boolean soldout = true;

            while (soldout) {
                // 구매하기 버튼 찾을때까지 새로고침 진행
                webElement = wait.until(new Function<WebDriver, WebElement>() {
                    @Override
                    public WebElement apply(WebDriver webDriver) {
                        driver.navigate().refresh();
                        // 페이지 에러날때 네이버 홈 이동 후 다시 사이트로 이동 (테스트중)
                        if (driver.findElements(By.className("title_error")).size() != 0) {
                            driver.navigate().to(NAVER_URL);
                            driver.findElement(By.cssSelector("#search_btn")).click();
                            driver.navigate().to(maskShopUrl);
                            driver.get(maskShopUrl);
                        }
                        return driver.findElement(By.linkText("구매하기"));
                    }
                });
                
                // select box label
                List<WebElement> list = driver.findElements(By.cssSelector(".selectbox-label"));
                // 옵션 리스트
                List<WebElement> selectOpt;
                boolean click = false;
                for (WebElement w : list) {
                    // selectText가  포함된 셀렉트박스를 찾아 클릭
                    if (w.getText().contains("선택하세요") || w.getText().contains(selectText)) {
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
                                if (i.getText().contains(containText) && !i.getText().contains(exceptText)) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
