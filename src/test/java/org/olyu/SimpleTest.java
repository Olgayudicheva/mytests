package org.olyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class SimpleTest {
    static final Logger LOGGER = LogManager.getLogger(SimpleTest.class);
    WebDriver driver;
    @BeforeAll
    static void before() {
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void after() throws InterruptedException {
        if (driver!=null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    @Test
    void testFirst() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://ya.ru");
    }
}
