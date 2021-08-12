import com.google.gson.Gson;
import io.restassured.response.ExtractableResponse;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class RestAssuredSimpleTests {

    @Test
    void getOneUser(){
        given()
                .get("https://reqres.in/api/users/2")
                .then()
                .body("data.first_name", is("Janet"));
    }

    @Test
    void negativeGetOneUser(){

        given()
                .get("https://reqres.in/api/users/200")
                .then()
                .statusCode(404)
                .assertThat();
    }

    @Test
    void postCreateNewUser(){
        String name = "Spartacus";
        ExtractableResponse response =  given()
                .contentType(JSON)
                .body("{\"name\": \""+name+"\"," +
                        "\"job\": \"Warlord\"}")
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .extract();
        assertThat(response.path("name").toString()).isEqualTo(name);
    }

    @Test
    void positiveLogin(){
        Map credentials =  new LinkedHashMap();
        credentials.put("email", "eve.holt@reqres.in");
        credentials.put("password", "pistol");
        String credentialsJson = new Gson().toJson(credentials);
        ExtractableResponse response = given()
                .contentType(JSON)
                .body(credentialsJson)
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .extract();
        assertThat(response.path("token").toString()).isEqualTo("QpwL5tke4Pnpja7X4");
    }
    @Test
    void negativeLogin(){
        given()
                .contentType(JSON)
                .body("")
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .assertThat();
    }
}
