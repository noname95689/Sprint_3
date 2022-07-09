package com.example;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.example.model.Courier;
import com.example.model.CourierId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {

    private String login ="testuser8451";
    private String password = "1234";
    private String firstName ="jeka";

    @Before
    public void beforeTests() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Requests.createCourierBeforeTest(login, password, firstName);
    }

    @After
    public void afterTests() {
        Requests.checkIfCourierCreatedAndDelete(login, password);
    }

    @Step("Delete created Courier")
    public void deleteCreatedCourier(String login, String password) {
        int id = sendPostCourierLogin(login, password);
        Requests.sendDeleteCourier(id);
    }

    @Step("Send Post request with login and password to /api/v1/courier/login")
    public int sendPostCourierLogin(String login, String password) {
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

    //курьер может авторизоваться - код ответа
    @Step("Send Post request with correct name and password to /api/v1/courier/login")
    public Response sendPostCourierLoginResponse(String login, String password) {
        Courier courier = new Courier(login, password);
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login");
        return response;
    }

    @Step("Send Post request with correct name and password to /api/v1/courier/login")
    public int sendPostCourierLoginAnswerCode(String login, String password) {
        Courier courier = new Courier(login, password);
        int statusCode = given()
                .header("Content-type", "application/json")
                .body(courier)
                .post("/api/v1/courier/login")
                .getStatusCode();
        return statusCode;
    }

    @Test
    @DisplayName("Post courier/login with correct login/password returns 200 ok")
    @Description("Send Post request with login and password courier/login")
    public void postCourierLoginAnswerCode() {
        Response response = sendPostCourierLoginResponse(login, password);
        response.then().assertThat().statusCode(200);
        System.out.println("Запрос с корректным логином и паролем на courier/login возвращает 200 ok");
    }

    //курьер может авторизоваться - тело ответа
    @Test
    @DisplayName("Post courier/login with correct login/password returns id")
    @Description("Send Post request with login and password courier/login")
    public void postCourierLoginAnswerBody() {
        Response response = sendPostCourierLoginResponse(login, password);
        response.then().assertThat().body("id", notNullValue());
        System.out.println("Запрос с корректным логином и паролем на courier/login возвращает id");
    }

    //авторизации нужно передать все обязательные поля - логин - без поля ошибка - код ответа
    @Test
    @DisplayName("Post courier/login without login returns 400 bad request")
    @Description("")
    public void postCourierLoginWithoutLoginAnswerCode() {
        Response response = sendPostCourierLoginResponse(null, password);
        response.then().assertThat().statusCode(400);
        System.out.println("Запрос без логина на courier/login возвращает 400 bad request");

    }

    //авторизации нужно передать все обязательные поля - логин - без поля ошибка - тело ответа
    @Test
    @DisplayName("Post courier/login without login returns correct message")
    @Description("")
    public void postCourierLoginWithoutLoginAnswerBody() {
        Response response = sendPostCourierLoginResponse(null, password);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
        System.out.println("Запрос без логина на courier/login возвращает корректное тело");
    }

    //авторизации нужно передать все обязательные поля - пароль - без поля ошибка - код ответа
    @Test
    @DisplayName("Post courier/login without password returns 400 bad request")
    @Description("")
    public void postCourierLoginWithoutPasswordAnswerCode() {
        Response response = sendPostCourierLoginResponse(login, null);
        response.then().assertThat().statusCode(400);
        System.out.println("Запрос без пароля на courier/login возвращает 400 bad request");
    }

    //авторизации нужно передать все обязательные поля - пароль - без поля ошибка - тело ответа
    @Test
    @DisplayName("Post courier/login without password returns correct message")
    @Description("")
    public void postCourierLoginWithoutPasswordAnswerBody() {
        Response response = sendPostCourierLoginResponse(login, null);
        response.then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
        System.out.println("Запрос без логина на courier/login возвращает корректное тело");
    }

    //система вернёт ошибку, если неправиильно указать логин 404 Not Found
    @Test
    @DisplayName("Post courier/login with incorrect login returns 404 Not Found")
    @Description("")
    public void postCourierLoginIncorrectLoginAnswerCode() {
        deleteCreatedCourier(login, password);
        Response response = sendPostCourierLoginResponse(login, password);
        response.then().assertThat().statusCode(404);
        System.out.println("Запрос c несуществующим логином возвращает  404 Not Found");
    }

    //система вернёт ошибку, если неправильно указать логин  - "message": "Учетная запись не найдена"
    @Test
    @DisplayName("Post courier/login with incorrect login correct message")
    @Description("")
    public void postCourierLoginIncorrectLoginAnswerBody() {
        deleteCreatedCourier(login, password);
        Response response = sendPostCourierLoginResponse(login, password);
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
        System.out.println("Запрос c несуществующим логином возвращает корректное тело");
    }

    ////система вернёт ошибку, если неправильно указать пароль - 404 Not Found
    @Test
    @DisplayName("Post courier/login with incorrect password returns correct message")
    @Description("")
    public void postCourierLoginIncorrectPasswordAnswerBody() {
        Response response = sendPostCourierLoginResponse(login, "incorrectPassword");
        response.then().assertThat().statusCode(404);
        System.out.println("Запрос c несуществующим паролем возвращает корректное тело");
    }

    ////система вернёт ошибку, если неправильно указать пароль - "message": "Учетная запись не найдена"
    @Test
    @DisplayName("Post courier/login with incorrect password returns 404 Not Found")
    @Description("")
    public void postCourierLoginIncorrectPasswordAnswerCode() {
        Response response = sendPostCourierLoginResponse(login, "incorrectPassword");
        response.then().assertThat().body("message", equalTo("Учетная запись не найдена"));
        System.out.println("Запрос c несуществующим логином возвращает корректное тело");
    }

}
