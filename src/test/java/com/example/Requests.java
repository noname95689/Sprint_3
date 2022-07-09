package com.example;

import com.example.model.Courier;
import com.example.model.CourierId;
import com.example.model.Order;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class Requests {

    @Step
    public static void checkIfCourierCreatedAndDelete(String login, String password) {
        if (sendPostCourierLoginAnswerCode(login, password) == 200) {
            deleteCreatedCourier(login, password);
        }
    }
    @Step("Send Post request with correct data to /api/v1/courier")
    public static Response sendPostRequestCourier(String login, String password, String firstName) {
        Courier courier = new Courier(login, password, firstName);
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
        return response;
    }

    @Step("Send Post request with login and password to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(String login, String password) {
        Courier courier = new Courier(login, password);
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Send Post request with login and password to /api/v1/courier/login")
    public static int sendPostCourierLogin(String login, String password) {
        Courier courier = new Courier(login, password);
        CourierId courierId = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login")
                .body()
                .as(CourierId.class);
        System.out.println("Id курьера: " + courierId.getId());
        return courierId.getId();
    }

    @Step("Send Post request with correct name and password to /api/v1/courier/login")
    public static int sendPostCourierLoginAnswerCode(String login, String password) {
        Courier courier = new Courier(login, password);
        int statusCode = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login")
                .getStatusCode();
        return statusCode;
    }

    @Step("Send Delete request to /api/v1/courier/:id ")
    public static void sendDeleteCourier(int id) {
        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/" + id);
        System.out.println("Курьер №" + id + " удален");
    }

    @Step("Delete creater model.Courier")
    public static void deleteCreatedCourier(String login, String password) {
        int id = sendPostCourierLogin(login, password);
        sendDeleteCourier(id);
    }

    @Step
    public static void createCourierBeforeTest(String login, String password, String firstName) {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Courier courier = new Courier(login, password, firstName);
        given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step
    public static Response sendPostOrders(String firstName, String lastName, String address,
                                   String metroStation, String phone, int rentTime,
                                   String deliveryDate, String comment, String[] color) {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Order order = new Order(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        Response response =
                given()
                        .log()
                        .all()
                        .header("Content-type", "application/json")
                        .body(order)
                        .post("/api/v1/orders");
        return response;
    }
}
