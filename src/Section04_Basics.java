import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.PayLoad;
import files.ReusableMethods;

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
		//use defined method to convert string to json
		JsonPath js = ReusableMethods.rawToJson(res);
		
		String placeId = js.getString("place_id");
		
		System.out.println(js.getString("place_id"));
		
		
		//PUT requst to update address for above place_id2
		
		String newAddress = "70 Summer walk, Ghansoli, Navi Mumbai";
		given().log().all().header("content-type","application/json").queryParam("key", "qaclick123")
		.body("{\r\n"
				+ "\"place_id\":\""+ placeId +"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		
		.when().put("maps/api/place/update/json")
		
		.then().assertThat().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		
		//GET request for getting above request details
		
		String getResp = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeId)
		
		.when().get("maps/api/place/get/json")
		
		.then().assertThat().statusCode(200).extract().response().body().asString();
		
		JsonPath js2 = ReusableMethods.rawToJson(getResp);
		
		System.out.println(js2.getString("address"));
		
		Assert.assertEquals(js2.getString("address"), newAddress);
	}

}
