package com.example;

import com.example.model.OrdersList;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.testng.annotations.Test;

import static com.example.CreateCourierTest.testCount;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @After
    public void afterTests() {
        testCount = testCount++;
        System.out.println("Тестов пройдено:"+testCount);
    }

    @Test
    @DisplayName("getOrderList returns orderList")
    @Description("Send GET request to /api/v1/orders")
    public void getOrderList() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        OrdersList ordersList =
                given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders")
                        .body().as(OrdersList.class);
        MatcherAssert.assertThat(ordersList, notNullValue());
    }

    @Test
    public void getAnotherOrderList() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .get("/api/v1/orders");
        response.then().assertThat().body("orders.id", notNullValue());
    }
}
