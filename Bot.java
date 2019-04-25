package com.company;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.company.Main.*;

class Bot {
    WebDriver driver;
    boolean initDone = false;
    private static String yudongpw="1234";
    void initialise() {
        mainWindow.delaySettingPanel();
        readSetting();
        mainWindow.appendInfoText("설정 읽어들임");
        if (useScript) {
            readScript();
        }
        mainWindow.appendInfoText("브라우져 시동중");
        try {
            initBrowser();
        }catch (Exception e){
            mainWindow.setText("크롬 드라이버 문제 발생. 프로그램 종료후 다시 시도하세요. 5초후 자동종료합니다.");
            try {
                Thread.sleep(5500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }
        mainWindow.appendInfoText("사이트 연결중");
        connectTo(myURL);
    }

    void writeComment() {
        WebElement id = driver.findElement(By.id("comment_nick"));
        WebElement pw = driver.findElement(By.id("comment_pw"));
        String ids = id.getText();
        if (ids.length() <= 0) {
            id.clear();
            pw.clear();
            id.sendKeys("ㅇㅇ");
            pw.sendKeys(yudongpw);
        }
        WebElement memo = driver.findElement(By.id("comment_memo"));
        String content = replies.poll();
        memo.sendKeys(content);
        mainWindow.appendInfoText("댓글 작성: " + content);
        WebElement element = driver.findElement(By.cssSelector("button[class=btn-comment-write]"));
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", element);
        System.out.println("clicked");
        mainWindow.updateReplyPanel();
    }

    void recommend() {
        WebElement button = driver.findElement(By.id("recommend_join"));
        if (needCode) {
            String code = mainWindow.codeField.getText();
            if (code.length() < 1) {
                mainWindow.setText("코드를 먼저 입력해주세요.");
                return;
            }
            mainWindow.appendInfoText("코드: "+code);
            WebElement codesection=driver.findElement(By.id("captcha_codeR"));
            codesection.clear();
            codesection.sendKeys(code);
        }
        if (useScript) {
            Actions actions = new Actions(driver);
            WebElement element = driver.findElement(By.id("recommend_join"));
            actions.moveToElement(element).perform();
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript(script);
            try {
                Thread.sleep(500);
                while (true) {
                    try {
                        driver.switchTo().alert().accept();
                    } catch (NoAlertPresentException e) {
                        break;
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            button.click();
        }
        mainWindow.appendInfoText("추천 전송");
    }


    void clear() {
//        connectTo("chrome://settings/clearBrowserData");
//        WebElement clearButton = driver.findElement(By.cssSelector("* /deep/ #clearBrowsingDataConfirm"));
//        clearButton.click();
        driver.manage().deleteAllCookies();
    }

    void connectTo(String URL) throws TimeoutException {
        initDone = false;
        System.out.println("Selenium connected to " + URL);
        try {
            driver.get(URL);  //접속할 사이트
        } catch (UnhandledAlertException e) {
            driver.switchTo().alert().accept();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            driver.get(URL);
        }
        initDone = true;
    }

    private void readSetting() {
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("settings.txt"), StandardCharsets.UTF_8));
            reader.readLine();
            String temp = reader.readLine();
            myURL = temp;
            while (reader.ready()) {
                temp = reader.readLine();
                if (temp.charAt(0) == '#') continue;
                System.out.println(temp);
                String[] token = temp.split(",");
                String head = token[0];
                String in = token[1];
                switch (head) {
                    case "로그인":
                        login = Boolean.parseBoolean(in);
                        break;
                    case "코드필요":
                        needCode = Boolean.parseBoolean(in);
                        break;
                    case "스크립트사용":
                        useScript = Boolean.parseBoolean(in);
                        break;
                    case "유동비밀번호":
                        yudongpw = in;
                        break;
                }
            }
            reader.close();
            BufferedReader rr = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("replies.txt"), StandardCharsets.UTF_8));
            rr.readLine();
            while (rr.ready()) {
                temp = rr.readLine();
                replies.add(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mainWindow.appendInfoText("settings.txt 혹은 relies.txt 파일이 존재하지 않습니다.");
        }
        System.out.println("Reading " + "settings.txt" + " DONE!");
    }

//    public void logIn() {
//        try {
//            driver.get("http://m.dcinside.com/auth/login?r_url=http://m.dcinside.com/");
//            System.out.println("로그인 대기중");
//            driver.findElement(By.id("user_id")).sendKeys("아이디");
//            driver.findElement(By.id("user_pw")).sendKeys("비밀번호");
//            Thread.sleep(500);
//            driver.findElement(By.id("user_pw")).sendKeys(Keys.RETURN);
//            //  driver.findElement(By.cssSelector("button[type=submit]")).click();
//            Thread.sleep(1000);
//            System.out.println("로그인 성공");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void initBrowser() {
        System.out.println("Initiating Chrome Driver");
        try{
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");}
        catch (Exception e){
            mainWindow.appendInfoText("chromedriver.exe 파일이 존재하지 않습니다.");
        }
        Map<String, String> mobileEmulation = new HashMap<>();
        mobileEmulation.put("deviceName", "Nexus 5");
        ChromeOptions options = new ChromeOptions();
        try {
            options.addExtensions(new File("Adguard.crx")); //AdGuard
            mainWindow.appendInfoText("Adguard 로드");
        } catch (Exception e) {
            mainWindow.appendInfoText("Adguard 사용 안함");
        }
        options.setExperimentalOption("mobileEmulation", mobileEmulation);
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); //응답시간 5초설정
    }

}
