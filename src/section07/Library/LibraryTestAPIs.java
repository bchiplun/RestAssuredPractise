package section07.Library;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.Utilities.JsonSerializer;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LibraryTestAPIs {

	public String id;
    
	@DataProvider(name = "BooksData")
	public Object[][] getData() {
		return new Object[][] { { "aaaa", "0001" }, { "bbbb", "0002" }, { "cccc", "0003" } };
	}

	@Test(dataProvider="BooksData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";

		String resp = given().log().all().header("Content-type", "application/json")
				.body(LibraryPayload.AddBook(isbn, aisle))

				.when().post("Library/Addbook.php")

				.then().assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response()
				.asString();

		JsonPath js = JsonSerializer.rawToJson(resp);

		id = js.getString("ID");

		System.out.println(id);

	}

	@Test(dataProvider="BooksData")
	public void deleteBook(String isbn, String aisle) {
		System.out.println("Deleting book with ID: " + isbn + aisle);
		given().log().all().header("Content-type", "application/json")
				.body("{\r\n" + "\"ID\" : \"" + isbn + aisle + "\"\r\n" + "} \r\n" + "")

				.when().post("/Library/DeleteBook.php")

				.then().assertThat().statusCode(200).body("msg", equalTo("book is successfully deleted"));
	}

	@Test
	public void addBookusingStaicJson() throws IOException {
		
		//first read file to bytes --> then convert to string
		RestAssured.baseURI = "http://216.10.245.166";
		String resp = given().log().all().header("Content-type", "application/json")
				.body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"\\NewBook.json"))))
                      //reading file and converting to bytes --> converting to string 
				.when().post("Library/Addbook.php")

				.then().assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response()
				.asString();

		JsonPath js = JsonSerializer.rawToJson(resp);

		id = js.getString("ID");

		System.out.println(id);


	}

}
