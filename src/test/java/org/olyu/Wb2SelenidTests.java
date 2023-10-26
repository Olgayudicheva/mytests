package org.olyu;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.*;

public class Wb2SelenidTests {

    //ищем элемент у которого id="searchInput" через Xpath
    By searchInputXpath = By.xpath("//*[@id=\"searchInput\"]");

    //ищем элемент у которого id="searchInput" через CssSelector
    String searchInputCss = "#searchInput";

    //Ищем элемент с классом autocomplete__phrase, рядом с которым есть элемент с классом autocomplete__icon и loupe, через Xpath
    By autoCompleteSearchElementXpath = By.xpath("//span[@class='autocomplete__icon loupe']/../span[@class='autocomplete__phrase']");

    //Ищем элемент с классом autocomplete__phrase, рядом с которым есть элемент с классом autocomplete__icon и loupe, через CssSelector
    String autoCompleteSearchElementCss = ".autocomplete__text:has(span.autocomplete__icon.loupe)>.autocomplete__phrase";

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
        //идем на главную
        open("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //ищем строку поиска
        //вводим первые символы для поиска
        $(searchInputCss).sendKeys(enterText);
        //ждем пока появится список автодополнений
        //выбираем первый в списке варивант
        SelenideElement firstAutoCompleteElement = $(autoCompleteSearchElementCss).shouldBe(visible);
        //проверяем ожидаемые результат с фактическим
        if (positive) {
            firstAutoCompleteElement.shouldBe(text(expectedAutoComplete));
        } else {
            firstAutoCompleteElement.shouldNotBe(text(expectedAutoComplete));
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
        //идем на главную
        open("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //ищем строку поиска
        //вводим первые символы для поиска + нажимаем ENTER
        $(searchInputCss)
                .sendKeys(article+Keys.ENTER);
        //ждем пока откроется страница карточки товара
        Selenide.Wait().until(ExpectedConditions.urlMatches("https://www.wildberries.ru/catalog/.*/detail.aspx"));
        //ожидаемый url
        String expected = "https://www.wildberries.ru/catalog/"+article;
        //проверка url-а
        assert url().startsWith(expected) : "expected: '"+expected+"' actual: '"+url()+"'";
    }

    /**
     * Проверка на присутствие текста в списке автодополнения при поиске
     * @param enterText - вводимый текст, берется из ValueSource
     * @throws InterruptedException
     */
    @ParameterizedTest
    @ValueSource(strings = {"кол", "игр", "день", "труба", "пластилин"})
    void testContatinsAutoCompleteElements(String enterText) throws InterruptedException {
        open("https://www.wildberries.ru");
        //ждем пока подгрузятся скрипты
        Thread.sleep(1000);
        //вводим первые символы для поиска
        $(searchInputCss).sendKeys(enterText);
        //ждем пока появится список автодополнений
        $(By.xpath("//*[@id=\"searchBlock\"]/div[2]/div")).shouldBe(visible);
        Thread.sleep(1000);
        //ждем пока появится первый элемент в списке
        $(autoCompleteSearchElementCss).shouldBe(visible);
        //Перебор элементов автодополнения на экране
        for (SelenideElement autoCompleteElement : $$(autoCompleteSearchElementCss)) {
            //проверяем ожидаемый результат с фактическим
            autoCompleteElement.shouldBe(matchText(".*"+enterText+".*"));

            //assert autoCompleteElement.getText().contains(enterText) : "'"+autoCompleteElement.getText()+"' not contains '"+enterText+"'";
        }
    }
}
