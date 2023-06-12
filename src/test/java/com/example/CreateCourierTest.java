package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {

    private String login ="testuser8451";
    private String password = "1234";
    private String firstName ="jeka";
    public static int testCount = 0;

    @Before
    //Перед тестом проверяем, присутствует ли курьер с таким логином. Если да - удаляем его.
    public void beforeTests() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Requests.checkIfCourierCreatedAndDelete(login, password);
    }

    //Проверяем создан ли курьер после теста. Если да - удаляем его.
    @After
    public void afterTests() {
        Requests.checkIfCourierCreatedAndDelete(login, password);
        testCount = testCount++;
        System.out.println("Тестов пройдено:"+testCount);
    }

    //Запрос с корректными данными возвращает 201 created.
    @Test
    @DisplayName("model.Courier returns 201 created")
    @Description("Send POST request with correct login/password/firstname to /api/v1/courier")
    public void successfulCourierCreatingReturnsCorrectCode() {
        Response response = Requests.sendPostRequestCourier(login, password, firstName);
        response.then().assertThat().statusCode(201);
        System.out.println("Запрос на ручку /api/v1/courier с корректными данными возвращает 201 created");
    }

    @Test
    @DisplayName("model.Courier returns correct body")
    @Description("Send POST request with correct login/password/firstname to /api/v1/courier")
    public void successfulCourierCreatingReturnsCorrectBody() {
        Response response = Requests.sendPostRequestCourier(login, password, firstName);
        response.then().assertThat().body("ok", equalTo(true));
        System.out.println("Запрос на ручку /api/v1/courier с корректными данными возвращает true");
    }

    @Test
    @DisplayName("model.Courier return 400 bad request")
    @Description("Send POST request without login to /api/v1/courier")
    //Запрос без логина возвращает 400 Bad Request.
    public void  requestWithoutLoginReturnsCorrectCodeCourier() {
        Response response = Requests.sendPostRequestCourier(null, password, firstName);
        response.then().assertThat().statusCode(400);
        System.out.println("Запрос на ручку /api/v1/courier без логина возвращает 400 bad request");
    }

    //Запрос без логина возвращает корректное тело.
    @Test
    @DisplayName("model.Courier return correct body")
    @Description("Send POST request without login to /api/v1/courier")
    public void  requestWithoutLoginReturnsCorrectBodyCourier() {
        Response response = Requests.sendPostRequestCourier(null, password, firstName);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        System.out.println("Запрос на ручку /api/v1/courier без логина возвращает корректное тело");
    }

    //Запрос без пароля возвращает 400 Bad Request.
    @Test
    @DisplayName("model.Courier return 400 bad request")
    @Description("Send POST request without password to /api/v1/courier")
    public void  requestWithoutPasswordReturnsCorrectCodeCourier() {
        Response response = Requests.sendPostRequestCourier(login, null, firstName);
        response.then().assertThat().statusCode(400);
        System.out.println("Запрос на ручку /api/v1/courier без пароля возвращает 400 bad request");
    }

    //Запрос без пароля возвращает корректное тело.
    @Test
    @DisplayName("model.Courier return correct body")
    @Description("Send POST request without password to /api/v1/courier")
    public void  requestWithoutPasswordReturnsCorrectBodyCourier() {
        Response response = Requests.sendPostRequestCourier(login, null, firstName);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        System.out.println("Запрос на ручку /api/v1/courier без пароля возвращает 400 bad request");
    }

    //Запрос с повторяющимся логином возвращает 409 Сonflict.
    @Test
    @DisplayName("model.Courier return 409 Сonflict")
    @Description("Send POST with to /api/v1/courier")
    public void sendPostRequestWithExistingLoginCourierReturnsConflict() {
        Response response = Requests.sendPostRequestCourier(login, password, firstName);
        response.then().assertThat().statusCode(201);
        Response secondResponse = Requests.sendPostRequestCourier(login, password, firstName);
        secondResponse.then().assertThat().statusCode(409);
        System.out.println("Запрос с повторяющимся логином возвращает 409 Сonflict");
    }

    //Запрос с повторяющимся логином возвращает корректное тело.
    @Test
    @DisplayName("model.Courier return correct body")
    @Description("Send POST with to /api/v1/courier")
    public void sendPostRequestWithExistingLoginCourierReturnsCorrectBody() {
        Response response = Requests.sendPostRequestCourier(login, password, firstName);
        response.then().assertThat().statusCode(201);
        Response secondResponse = Requests.sendPostRequestCourier(login, password, firstName);
        secondResponse.then().assertThat().body("message", equalTo("Этот логин уже используется"));
        System.out.println("Запрос с повторяющимся логином возвращает корректное тело");
    }
}
