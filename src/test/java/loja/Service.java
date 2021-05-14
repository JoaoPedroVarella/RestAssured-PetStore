package loja;

import org.junit.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class Service {
    public String tokenGeral;

    public String readJson(String searchJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(searchJson)));

    }

    //create / incluir / post
    @Test
    public void order() throws IOException{
        getToken("user", "password");
        PetPost();
        getPet();
        putPet();
        deletePet();
    }

    @Test
    public void PetPost() throws IOException {
        //ler conteúdo do arquivo
        String tokenLocal = getToken("user", "password");
        String jsonBody = readJson("db/pet.json");

        given()
                .contentType("application/json")    //  tipo de conteúdo da requisição
                                                    // "text/xml" para web services comuns
                                                    // "aplication/json" para APIs Rest
                .log().all()                        // Gerar log completo da requisiçã0
                .body(jsonBody)                     // Conteúdo do corpo da requisição
        .when()                                     // Quando
                .post("https://petstore.swagger.io/v2/pet") //endpoint
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(4258))
                .body("name", is("Mimo"))
                .body("tags.name", contains("adoption"));

        System.out.println("Executou o servico");
    }

    //Reach / Consultar / Get
    @Test
    public void getPet(){
        String petId = "4258";
        given()
                .contentType("application/json")
                .log().all()
        .when()
                .get("https://petstore.swagger.io/v2/pet/" + petId)
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Mimo"))
                .body("category.name", is("cat"));

    }

    //Update / Modificar / PUT
    @Test
    public void putPet() throws IOException {
        String jsonBody = readJson("db/petput.json");

        given()
                .contentType("application/json")    //  tipo de conteúdo da requisição
                                                    // "text/xml" para web services comuns
                                                    // "aplication/json" para APIs Rest
                .log().all()                        // Gerar log completo da requisiçã0
                .body(jsonBody)                     // Conteúdo do corpo da requisição
        .when()                                     // Quando
                .put("https://petstore.swagger.io/v2/pet") //endpoint
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(4258))
                .body("name", is("Mimo"))
                .body("status", is("adopted"));
    }

    //Delete / deletar / Delete
    @Test
    public void deletePet() throws IOException {
        given()
                .contentType("application/json")    //  tipo de conteúdo da requisição
                // "text/xml" para web services comuns
                // "aplication/json" para APIs Rest
                .log().all()                        // Gerar log completo da requisiçã0
        .when()                                     // Quando
                .delete("https://petstore.swagger.io/v2/pet/4258") //endpoint
        .then()
                .log().all()
                .statusCode(200)
                ;
        System.out.println(tokenGeral);
    }

    @Test
    public void chama(){
        getToken("user", "password");
    }

    //Login - token
    public String getToken(String user, String password){

        String token =
        given()
                .contentType("application/json")    //  tipo de conteúdo da requisição
                // "text/xml" para web services comuns
                // "aplication/json" para APIs Rest
                .log().all()                        // Gerar log completo da requisiçã0
        .when()
                .get("https://petstore.swagger.io/v2/user/login?username=" + user + "&password=" + password)
        .then()
                .log().all()
                .statusCode(200)
                .body("message", containsString("logged in user session:"))
                .extract()
                .path("message")
                ;
    tokenGeral = token.substring(23);
    return tokenGeral;
    }

}
