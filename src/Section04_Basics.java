import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Section04_Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Restassured.BaseURI --> to give baseURI
		//given --> to provide query parameters, headers, Payload --> all input details
		//when --> to mention requet type and provide resources --> Submit API
		//then --> to get and assert response
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String res = given().log().all().header("content-type","application/json").queryParam("key", "qaclick123")
		.body(PayLoad.addPlace())
		
		.when().post("maps/api/place/add/json")
		
		.then().assertThat().statusCode(200).header("Server", "Apache/2.4.18 (Ubuntu)").body("scope", equalTo("APP"))
		.extract().response().asString();

		//extracting data from response bodyyy
		System.out.println(res);
		
		JsonPath js = new JsonPath(res);
		
		System.out.println(js.getString("place_id"));
	}

}
