package org.olyu;

import io.qameta.allure.Attachment;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;

public class CreditCalcTests {
    By summaCredita = By.cssSelector("input[name=credit_sum]"); //By.name("credit_sum")
    By srokCredita = By.cssSelector("input[name=period]");
    By ym = By.cssSelector("select[name=period_type]");
    By m = By.cssSelector("option[value=\"M\"]");
    By y = By.cssSelector("option[value=\"Y\"]");
    By percent = By.cssSelector("input[name=\"percent\"]");
    By click = By.cssSelector("input[class=\"calc-submit me-3\"]");


   @Attachment(type = "image/png")
   byte[] saveAllureScreenshot(){
       return screenshot(OutputType.BYTES);
   }
        /*тест кредитного калькулятора - позитивный
   * в тесте использую CSS селектор
   * использую selenid для удобства
   * к проекту подключен allure*/
    @Test
    void testFirst() throws InterruptedException {
        open("https://calcus.ru/kreditnyj-kalkulyator");
        Thread.sleep(1000);
        $(summaCredita).sendKeys("1000000");
        $(srokCredita).sendKeys("36");
        $(ym).click();
        $(m).shouldBe(visible).click();
        $(percent).sendKeys("4.87");
        $(click).click();
        saveAllureScreenshot();
        //если нет проверки на подсвечивание ошибок (пустых полей на экране), то тест проходит
        $("#credit_sum-error").shouldNotBe(visible);
        $("#period-error").shouldNotBe(visible);
        $("#percent-error").shouldNotBe(visible);
        $(".calc-result-split").shouldBe(visible).scrollTo();
        //выставляю паузу перед скриншотом т.к. не успевают прогрузиться анимации
        Thread.sleep(1000);
        saveAllureScreenshot();




    }
}
