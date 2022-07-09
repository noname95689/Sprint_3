package com.example;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private static String firstName = "Семен";
    private static String lastName = "Семенович";
    private static String address = "ул. Тверская, д 14";
    private static String metroStation = "7";
    private static String phone = "89001234567";
    private static int rentTime = 3;
    private static String deliveryDate = "2022-07-07 21:00:00+00";
    private static String comment = "Завернуть в подарочную упаковку";

    public static String[] colors(int i) {
        List<String[]> colors= new ArrayList<>();
        colors.add(new String[]{"BLACK"});
        colors.add(new String[]{"GRAY"});
        colors.add(new String[]{"BLACK", "GRAY"});
        colors.add(new String[]{""});
        return colors.get(i);
    }

    @Test
    @Description("Send POST request with order data  to /api/v1/order")
    public void sendPostOrdersAnswerCode() {
        for (int i = 0; i <=3; i++) {
            Response response = Requests.sendPostOrders(firstName, lastName, address, metroStation,
                    phone, rentTime, deliveryDate, comment, colors(i));
            response.then().assertThat().statusCode(201);
            System.out.println("Запрос на /api/v1/orders с данными заказа и выбраными цветами: "
                    + " возвращает 201 created");
        }
    }

    @Test
    @Description("Send POST request with order data  to /api/v1/order")
    public void sendPostOrdersAnswerBody() {
         for (int i = 0; i <3; i++) {
             Response response = Requests.sendPostOrders(firstName, lastName, address, metroStation,
                     phone, rentTime, deliveryDate, comment, colors(i));
             response.then().assertThat().body("track", notNullValue());
             System.out.println("Запрос на /api/v1/orders с данными заказа и выбраными цветами: "
                     + " возвращает 201 created");
         }
    }
}




