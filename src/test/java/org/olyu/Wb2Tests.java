package org.olyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Stream;


public class Wb2Tests {
    static final Logger LOGGER = LogManager.getLogger(Wb2Tests.class);
    WebDriver driver;

    @BeforeAll
    static void before() {
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void after() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(500);
            driver.quit();
        }
    }

    private static Stream<Arguments> testFirstAutoCompleteElementData() {
        return Stream.of(
                Arguments.of("подод", "пододеяльник", true),
                Arguments.of("трус", "трусы женские", true),
                Arguments.of("игру", "игрушки для кошек", true),
                Arguments.of("туал", "туалетная бумага", true),
                Arguments.of("хы", "туалетная бумага", false)
        );
    }

    /**
     * Проверка первого элемента в списке автодополнения при поиске
     * @param enterText - вводимый в поисковую строку текст (первый элементв в testFirstAutoCompleteElementData)
     * @param expectedAutoComplete - ожидаемое содержимое в первом элементе автодополнения (второй элемент в testFirstAutoCompleteElementData)
     * @param positive - позитивный или негативный тест
     * @throws InterruptedException
     */
    @ParameterizedTest
    @MethodSource("testFirstAutoCompleteElementData")
    void testFirstAutoCompleteElement(String enterText, String expectedAutoComplete, boolean positive) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //идем на главную
        driver.get("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //ищем строку поиска
        WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
        //вводим первые символы для поиска
        searchInput.sendKeys(enterText);
        //ждем пока появится список автодополнений
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"searchBlock\"]/div[2]/div")));
        //выбираем первый в списке варивант
        WebElement firstAutoCompleteElement = driver.findElement(By.xpath("//*[@id=\"searchBlock\"]/div[2]/div/div/ul[1]/li[1]"));
        //проверяем ожидаемые результат с фактическим
        assert expectedAutoComplete.equals(firstAutoCompleteElement.getText())==positive : "expected: '"+expectedAutoComplete+"' actual: '"+firstAutoCompleteElement.getText()+"'" ;
    }

    /**
     * Проверка на присутствие текста в списке автодополнения при поиске
     * @param enterText - вводимый текст, берется из ValueSource
     * @throws InterruptedException
     */
    @ParameterizedTest
    @ValueSource(strings = {"кол", "игр", "день", "труба", "пластилин"})
    void testContatinsAutoCompleteElements(String enterText) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //идем на главную
        driver.get("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //ищем строку поиска
        WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
        //вводим первые символы для поиска
        searchInput.sendKeys(enterText);
        //ждем пока появится список автодополнений
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"searchBlock\"]/div[2]/div")));
        //выбираем первый в списке варивант
        List<WebElement> autoCompleteElements = driver.findElements(By.xpath("//*[@id=\"searchBlock\"]/div[2]/div/div/ul[1]/li"));
        for (WebElement autoCompleteElement : autoCompleteElements) {
            //проверяем ожидаемые результат с фактическим
            assert autoCompleteElement.getText().contains(enterText) : "'"+autoCompleteElement.getText()+"' not contains '"+enterText+"'";
        }
    }

    /**
     * Тест поиска по артиклу, в строку поиска вводится артикль, нажимаем enter, должны попасть на страницу товара с таким артиклем
     * @param article - берется из ValueSource
     * @throws InterruptedException
     */
    @ParameterizedTest
    @ValueSource(strings = {"13975826", "13975832", "158448987", "14397613"})
    void testFindByArticle(String article) throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        //идем на главную
        driver.get("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //ищем строку поиска
        WebElement searchInput = driver.findElement(By.xpath("//*[@id=\"searchInput\"]"));
        //вводим первые символы для поиска
        searchInput.sendKeys(article);
        searchInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.urlMatches("https://www.wildberries.ru/catalog/.*/detail.aspx"));
        String expected = "https://www.wildberries.ru/catalog/"+article;
        assert driver.getCurrentUrl().startsWith(expected) : "expected: '"+expected+"' actual: '"+driver.getCurrentUrl()+"'";
    }
}
