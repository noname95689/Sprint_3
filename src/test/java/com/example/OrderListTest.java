import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest {


    @Test
    public void getOrderList() {
        RestAssured.baseURI= "https://qa-scooter.praktikum-services.ru/";
        Response response =
        given()
                .header("Content-type", "application/json")
                .get("/api/v1/orders");
        response.then().assertThat().body("orders",  notNullValue());
    }
}
