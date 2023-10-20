package org.olyu;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;


public class WbTest {
    static final Logger LOGGER = LogManager.getLogger(WbTest.class);
    WebDriver driver;

    @BeforeAll
    static void before() {
        WebDriverManager.chromedriver().setup();
    }

    @AfterEach
    void after() throws InterruptedException {
        if (driver != null) {
            Thread.sleep(3000);
            driver.quit();
        }
    }

    String tokenAut = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTc3MTQ3MTQsInZlcnNpb24iOjIsInVzZXIiOiIxNTI1NzcwNSIsInNoYXJkX2tleSI6IjI3IiwiY2xpZW50X2lkIjoid2IiLCJzZXNzaW9uX2lkIjoiZTNiNzdhY2RmMWIwNDgzNzg5OTQ2MWEwMjM4MGE0NjAiLCJ1c2VyX3JlZ2lzdHJhdGlvbl9kdCI6MTY3MDQ4OTg0MywidmFsaWRhdGlvbl9rZXkiOiJiMjE5NjBkNGE4NjUyYmEzOGMyM2JhNDYyMzMxNWEwOGNlZDZiOWYzOThiNjYyYTU0MzlhNDA2MmZlMjBlMDliIiwicGhvbmUiOiJ0dWVaT291L0JCSnRHbi9DM0I5RVVBPT0ifQ.KXTROp8PoI2qtWw3zD9Wq-CnsqZz_MXqmpTdQa9Yemvyj0OZmS_p0WOwNL33N78LP7RISRhq5uIJUI_Jyy3jMadAnyejxtjxSaBwBNjIy77C5-Bodfgu6gvwRDh3dFxCOMpHJcMKZYsltLw1iQYMIYuLcok4ys0__wt7ruKWdRBRdNa2xS8xoLGantlxDHAvHGFgtKki-_roqa8VC76Xl1y3nuU-2BuxymCgOrgpxuwqUV8jtXXk30QrFzUWuhhjvbmivhOGIkpArKkYgEL3kZAfqRVjwjcZCmzQIOinD1CYDnyhrxX3JVn90I5RZBqKjbzglA4khIxTkOVIw8pOuw";
    By.ByXPath searchXpath = new By.ByXPath("//*[@id=\"searchInput\"]");
    By.ByXPath addToCart1 = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[3]/div/div[3]/div[11]/div/div[1]/div[3]/div/button[2]");
    By.ByXPath clickOnPopularName = new By.ByXPath("//*[@id=\"catalog\"]/div/div[2]/div/section/ul/li[2]");
   //сортировка товаров
    By.ByXPath sorting = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[2]");
    By.ByXPath descPrice = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[2]/div/div/ul/li[4]/div");
    //границы цен
    By.ByXPath priceBox = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[7]/button");
    By.ByXPath priceFrom = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[7]/div/div[1]/div/div[1]/div/label/input");
    By.ByXPath priceUpTo = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[7]/div/div[1]/div/div[2]/div/label/input");
    By.ByXPath done = new By.ByXPath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[3]/div/div/div/div/div[1]/div[7]/div/div[2]/button[2]");
    //  XPath выбора доступного размера. [not(contains(@class,'disabled'))] - отсекает значения, у которых в классе есть disabled
    By.ByXPath availableSize = new By.ByXPath("//*[@class=\"product-page__sizes-wrap\"]/*/li[@class=\"sizes-list__item\"]/label[not(contains(@class,'disabled'))]/..");
    By.ByXPath allSize = new By.ByXPath("//*[@class=\"product-page__sizes-wrap\"]/*/li[@class=\"sizes-list__item\"]/label/..");
    @Test
    void testFirst() throws InterruptedException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-fullscreen");
        driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//1. ввод артикула товара в поисковую строку, открытие карточки товара (товар без размерного ряда) и добавление его в корзину
       driver.get("https://wildberries.ru");
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchXpath));
        wait.until(ExpectedConditions.elementToBeClickable(searchXpath)).click();
        driver.findElement(searchXpath).sendKeys("87286001");
        driver.findElement(searchXpath).sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.presenceOfElementLocated(addToCart1));
        wait.until(ExpectedConditions.elementToBeClickable(addToCart1)).click();

        //2. ввод наименования товара в поисковую строку
        wait.until(ExpectedConditions.presenceOfElementLocated(searchXpath));
        wait.until(ExpectedConditions.elementToBeClickable(searchXpath)).click();
        driver.findElement(searchXpath).sendKeys("футболка женская");
        driver.findElement(searchXpath).sendKeys(Keys.ENTER);
        //2.1 выбираем один из популярных запросов. Второй из предложенных
        wait.until(ExpectedConditions.presenceOfElementLocated(clickOnPopularName));
        wait.until(ExpectedConditions.elementToBeClickable(clickOnPopularName)).click();
        //2.2 сортируем по убыванию цены
         wait.until((ExpectedConditions.visibilityOfElementLocated(sorting)));
        wait.until(ExpectedConditions.elementToBeClickable(sorting)).click();
        //использую visibilityOf т.к. без него не выставляется сортировка
        Thread.sleep(500);
        wait.until(ExpectedConditions.visibilityOfElementLocated(descPrice));
        wait.until(ExpectedConditions.elementToBeClickable(descPrice)).click();

    //2.3 выставляем границы цен
        //находим и открываем фильтр цены
        wait.until((ExpectedConditions.presenceOfElementLocated(priceBox)));
        wait.until(ExpectedConditions.elementToBeClickable(priceBox)).click();
        //кликаем на Цена От
        wait.until(ExpectedConditions.elementToBeClickable(priceFrom)).click();
        //очищаем поле
        driver.findElement(priceFrom).clear();
        //вводим новое значение
        driver.findElement(priceFrom).sendKeys("100");
        wait.until(ExpectedConditions.elementToBeClickable(priceUpTo)).click();
        //очищаем поле
        driver.findElement(priceUpTo).clear();
        //вводим новое значение
        driver.findElement(priceUpTo).sendKeys("7777");
        wait.until(ExpectedConditions.presenceOfElementLocated(done));
        wait.until(ExpectedConditions.elementToBeClickable(done)).click();
        Thread.sleep(3000);
        //2.4 открываем рандомный товар
        //List<WebElement> list = driver.findElements(By.xpath("/html/body/div[1]/main/div[2]/div/div[2]/div/div/div[4]/div[1]/div[1]/div/article"));
        List<WebElement> list = driver.findElements(By.xpath("//*[@class=\"product-card-list\"]/article"));
        Random rand = new Random();
        WebElement randomElement = list.get(rand.nextInt(list.size()));
        new Actions(driver).scrollToElement(randomElement).moveToElement(randomElement).click().perform();//скролим страницу до элемента(карточки товара), двигаем мышь, кликаем на карточку товара
    //2.5 выбираем доступный размер товара и добавляем товар в корзину
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(allSize)); //ждем когда на экране появится хотя бы один размер
        List<WebElement> availableSizeList = driver.findElements(availableSize);
        //если отсутствует элемент выбора размера, то покидаем страницу и открываем другой товар
        /*if (availableSizeList.size()==0){
            driver.navigate().back();
        }*/
        WebElement randomSize = availableSizeList.get(rand.nextInt(availableSizeList.size()));
        System.out.println(randomSize.getText());
        Thread.sleep(500);
        new Actions(driver).moveToElement(randomSize).click().perform();//двигаем мышью к элементу(размер футболки), и кликаем на него
        Thread.sleep(500);
        wait.until(ExpectedConditions.presenceOfElementLocated(addToCart1));
        wait.until(ExpectedConditions.elementToBeClickable(addToCart1)).click();

        Thread.sleep(10000);



    }

}

