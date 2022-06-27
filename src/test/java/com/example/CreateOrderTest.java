import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private String firstName = "Семен";
    private String lastName = "Семенович";
    private String address = "ул. Тверская, д 14";
    private int metroStation = 7;
    private String phone = "79001234567";
    private int rentTime = 3;
    private String deliveryDate = "2022-07-07";
    private String comment = "Завернуть в подарочную упаковку";
    private final String[] color;

    public CreateOrderTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] setColor() {
        return new Object[][] {
                {"BLACK"},
                {"BLACK", "GRAY"},
                {"GRAY"},
                {},
        };
    }

    @Test
    @Description("Send POST request with order data  to /api/v1/order")
    public void sendPostOrdersAnswerCode() {
        Response response = Requests.sendPostOrders(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        response.then().assertThat().statusCode(201);
        System.out.println("Запрос на /api/v1/orders с данными заказа и выбраными цветами: "
                    + " возвращает 201 created");
    }

    @Test
    @Description("Send POST request with order data  to /api/v1/order")
    public void sendPostOrdersAnswerBody() {
        Response response = Requests.sendPostOrders(firstName, lastName, address, metroStation,
                phone, rentTime, deliveryDate, comment, color);
        response.then().assertThat().body("track", notNullValue());
        System.out.println("Запрос на /api/v1/orders с данными заказа и выбраными цветами: "
                +  " возвращает 201 created");
    }
}
