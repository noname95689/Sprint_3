package com.example;

import com.example.model.OrdersList;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.MatcherAssert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {

    @Test
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
