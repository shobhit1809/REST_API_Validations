package Get_API;

import Listeners.CustomListeners;
import Listeners.REST;
import Listeners.RetryUsecases;
import Listeners.ReadPropertyFile;
import Listeners.ReadExcelFile;
import POJOs.Userdata;
import com.google.gson.JsonArray;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.json.*;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
@Listeners(CustomListeners.class)
public class Usecase1 {

//    @DataProvider(name="user_by_id")
//    public static Object[][] user_by_id(){
//        return new Object[][]{
//                {"2"},
//        };
//    }
//    @DataProvider(name="userdetails_by_id")
//    public static Object[][] userdetails_by_id(){
//        return new Object[][]{
//                {"4"},
//        };
//    }
//    @DataProvider(name="Update_Userdetail_By_Id_PUT")
//    public static Object[][] Update_Userdetail_By_Id_PUT(){
//        return new Object[][]{
//                {"9","Alberta"},
//        };
//    }
//    @DataProvider(name="Update_Userdetail_By_Id_PATCH")
//    public static Object[][] Update_Userdetail_By_Id_PATCH(){
//        return new Object[][]{
//                {"9","alberta.funke@reqres.in"},
//        };
//    }
//    @DataProvider(name="Delete_Userdetail_By_Id")
//    public static Object[][] Delete_Userdetail_By_Id(){
//        return new Object[][]{
//                {"10"},
//        };
//    }

    //    usernames
    @Test(priority =0,retryAnalyzer = RetryUsecases.class)
    public void validate_username_list_TC01() throws IOException {

        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        Reporter.log("Validating usernames list [“Emma Wong”, “Charles Morris” , “Tracey Ramos” ]",true );
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.given().log().all().
        when().get(obj2.getProperty("Usecase1_URL")).then().log().body().
                assertThat().statusCode(200).contentType(ContentType.JSON).body(obj.getProperty("JSON_PATH_UserList_firstnames"),hasItems("Emma","Charles","Tracey"));
        RestAssured.given().
                when().get(obj2.getProperty("Usecase1_URL")).then().
                assertThat().body(obj.getProperty("JSON_PATH_UserList_lastnames"),hasItems("Wong","Morris","Ramos"));
        Reporter.log("Verified usernames list [“Emma Wong”, “Charles Morris” , “Tracey Ramos” ]",true );


    }

    @Test(priority =1,retryAnalyzer = RetryUsecases.class)
    public void validate_emailIds_TC02() throws IOException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        Reporter.log("Validating emails for list [Tracey Ramos, Janet weaver ]",true );
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.given().log().all().
                when().get(obj2.getProperty("Usecase1_URL")).then().log().body().
                assertThat().body(obj.getProperty("JSON_PATH_User_Email_Janet"),equalTo("janet.weaver@reqres.in"));
        RestAssured.given().
                when().get(obj2.getProperty("Usecase1_URL")).then().
                assertThat().body(obj.getProperty("JSON_PATH_User_Email_Tracey"),equalTo("tracey.ramos@reqres.in"));
        Reporter.log("Verified email ids for [Tracey Ramos ,Janet Weaver]",true );
    }

    @Test(priority =2,retryAnalyzer = RetryUsecases.class)
    public void validate_usernameById_TC03() throws IOException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
//        Properties obj3 = ReadPropertyFile.fetch();
        baseURI = obj2.getProperty("Usecase1_URL");
//        int id= sheet1.getRow(1).getNumericCellValue();
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("user_by_id");
        Response response = null;
        Reporter.log("Get the results for username By id ",true );
        try {
            RestAssured.useRelaxedHTTPSValidation();
            RestAssured.given().baseUri(obj2.getProperty("Usecase1_URL")).pathParam("id",id).when().get("/{id}").then().log().body().
                    assertThat().statusCode(200).contentType(ContentType.JSON).body(obj.getProperty("JSON_PATH_UserList_firstnames"),equalTo("Janet"));
            RestAssured.given().
                    when().baseUri(obj2.getProperty("Usecase1_URL")).pathParam("id",id).when().get("/{id}").then().
                    assertThat().body(obj.getProperty("JSON_PATH_UserList_lastnames"),equalTo("Weaver"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Verified username by id Janet Weaver",true );
    }

    @Test(priority =3,retryAnalyzer = RetryUsecases.class)
    public void validate_records_TC04() throws IOException {

        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        Response response = null;
        String page = "1";
        String per_page = "10";
        Reporter.log("Get the results for username By id ",true );
        try {
            baseURI = obj2.getProperty("Usecase1_URL");;
                        RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().queryParam("page",page).queryParam("per_page",per_page);
            response = httpRequest.get(baseURI);
            response.then().log().all();
            JsonPath jsonPathEval = response.jsonPath();

            int page_resp = jsonPathEval.get("page");
            int per_page_resp = jsonPathEval.get("per_page");

            System.out.println("page received from Response " + page);
            System.out.println("per_page received from Response " + per_page);

            // Validate the response
            Assert.assertEquals(String.valueOf(page_resp), "1", "Correct page received in the Response");
            Assert.assertEquals(String.valueOf(per_page_resp), "10", "Correct per_page received in the Response as"+per_page);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Verified username by id Janet Weaver",true );
    }

    @Test(priority =4,retryAnalyzer = RetryUsecases.class)
    public void validate_lastSixRecords_TC05() throws IOException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        Response response = null;
        String page = "1";
        String per_page = "10";
        Reporter.log("Get the results for username By id ", true);
        try {
            baseURI = obj2.getProperty("Usecase1_URL");
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().queryParam("page", page).queryParam("per_page", per_page);
            response = httpRequest.get(baseURI);
            response.then().log().all();

            String jsonstring = response.asString();
            JSONObject mainobj = new JSONObject(jsonstring);
            JSONArray arr = mainobj.getJSONArray("data");
            int len = arr.length();


            ArrayList<HashMap> desired_resp =new ArrayList<HashMap>();
            for(int i=len-6;i<len;i++){
                String first_name_temp = arr.getJSONObject(i).getString("first_name");
                String last_name_temp =  arr.getJSONObject(i).getString("last_name");
                HashMap<String,String> object_temp =new HashMap<>();
                object_temp.put("first_name",first_name_temp);
                object_temp.put("last_name",last_name_temp);
                desired_resp.add(object_temp);

            }
            for(int j=0;j< desired_resp.size();j++)
            {
                System.out.println(desired_resp.get(j).toString());
            }
            System.out.println(desired_resp);

//            JsonPath jsonPathEval = response.jsonPath();
//            JsonArray arr =new JsonArray();
//            arr.getJsonObject();
//            int len = arr.size();
//            List<Userdata> allUsers = jsonPathEval.getList("data", Userdata.class);
//            int len = allUsers.size();
//            HashMap<Integer,Object> objdata = new HashMap<>();
//            List<Userdata> allUsers = jsonPathEval.getList("data", Userdata.class);
//            int len =allUsers.size();
//            int page_resp = jsonPathEval.get("page");
//            int per_page_resp = jsonPathEval.get("per_page");
//            System.out.println("page received from Response " + page_resp);
//            System.out.println("per_page received from Response " + per_page_resp);

//            List<LastUsers> lastSixUsers = new ArrayList<LastUsers>();
//            HashMap<String,List> obj3 = new HashMap<>();
//            int i=0;
//            for(int j=len-1;j>=4;j--){
//                Userdata data =  allUsers.get(j);
//                String first_name_temp = data.first_name;
//                String last_name_temp = data.last_name;
//                LastUsers lsu= new LastUsers();
//                lsu.first_name = first_name_temp;
//                lsu.last_name = last_name_temp;
//                lastSixUsers.add(i,lsu);
//                i++;
//            }
//            int j=6;
//            for(int i=0;i<len;i++)
//            {
//                if(i<(len-j)){
////                    allUsers.remove(i);
//                }
//                else{
//                    lastSixUsers = (List<LastUsers>) allUsers.get(i);
//                }
//            }


            // Validate the response
            Assert.assertTrue(desired_resp.size()==6, "Correct length displayed");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Reporter.log("Last 6 Users are displayed", true);
    }

    @Test(priority =5,retryAnalyzer = RetryUsecases.class)
    public void Fetch_Userdetails_ById_TC06() throws IOException {

        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Get the results for username By id ",true );
        try {
            baseURI = obj2.getProperty("Usecase1_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id);
            response = httpRequest.get(baseURI+"/{id}");
            response.then().log().all();
            JsonPath jsonPathEval = response.jsonPath();
            String first_name = jsonPathEval.get("data.first_name");
            String last_name = jsonPathEval.get("data.last_name");

            // Validate the response
            Assert.assertEquals(first_name, "Eve", "Correct page received in the Response");
            Assert.assertEquals(last_name, "Holt", "Correct page received in the Response");


        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Verified username by id Janet Weaver",true );
    }

    @Test(priority =6,retryAnalyzer = RetryUsecases.class)
    public void Update_Userdetail_By_Id_PUT_TC07() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("Update_Userdetail_By_Id_PUT");
//        String first_name_value = obj3.getProperty("Update_Userdetail_By_Name_PUT");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("Update_Userdetail_By_Id_PUT");
        String first_name_value = excelData.get("Update_Userdetail_By_Name_PUT");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("first_name",first_name_value);
            baseURI = obj2.getProperty("Usecase1_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.put(baseURI+"/{id}");
            response.then().log().all();
            String jsonString =response.asString();

            // Validate the response
            Assert.assertTrue(jsonString.contains("updatedAt"), "Correct response recieved");


        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Name has been successfully updated for user with id=10",true );
    }

    @Test(priority =7,retryAnalyzer = RetryUsecases.class)
    public void Update_Userdetail_By_Id_PATCH_TC08() throws IOException {

        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("Update_Userdetail_By_Id_PATCH");
//        String email_value = obj3.getProperty("Update_Userdetail_By_Email_PATCH");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("Update_Userdetail_By_Id_PATCH");
        String email_value = excelData.get("Update_Userdetail_By_Email_PATCH");
        Reporter.log("Update the user details using the http method PATCH ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("email",email_value);
            baseURI = obj2.getProperty("Usecase1_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.patch(baseURI+"/{id}");
            response.then().log().all();
            String jsonString = response.asString();

            // Validate the response
            Assert.assertTrue(jsonString.contains("updatedAt"), "Correct response recieved");


        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Email has been successfully updated for user with id =9",true );
    }

    @Test(priority =8,retryAnalyzer = RetryUsecases.class)
    public void Delete_User_By_Id_TC09() throws IOException {

        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("Delete_Userdetail_By_Id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("Delete_Userdetail_By_Id");
        Reporter.log("Delete the userdetails By id ",true );
        try {
            baseURI = obj2.getProperty("Usecase1_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id);
            response = httpRequest.delete(baseURI+"/{id}");
            response.then().log().all();
            int status_code = response.statusCode();

            Assert.assertTrue(String.valueOf(status_code).equalsIgnoreCase("204"));


        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("User for id =10 has been successfully deleted",true );
    }

    @Test(priority =9,retryAnalyzer = RetryUsecases.class)
    public void Fetch_Unknown_resource_TC010() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            baseURI = obj2.getProperty("Usecase3_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id);
            response = httpRequest.get(baseURI+"/{id}");
            response.then().log().all();
            JsonPath jsonPathEval = response.jsonPath();
            String res_id = jsonPathEval.getString("data.id");

            // Validate the response
            Assert.assertTrue(res_id.equalsIgnoreCase(id), "Correct response recieved");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Name has been successfully updated for user with id=10",true );
    }

    @Test(priority =10,retryAnalyzer = RetryUsecases.class)
    public void Update_Unknown_resource_PUT_TC011() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("name","Tigerlily");
            baseURI = obj2.getProperty("Usecase3_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.put(baseURI+"/{id}");
            response.then().log().all();
            String jsonString =response.asString();

            // Validate the response
            Assert.assertTrue(jsonString.contains("updatedAt"), "Correct response recieved");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Resource has been successfully updated ",true );
    }

    @Test(priority =11,retryAnalyzer = RetryUsecases.class)
    public void Update_Unknown_resource_PATCH_TC012() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 = ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("name","Tigerlily");
            baseURI = obj2.getProperty("Usecase3_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.patch(baseURI+"/{id}");
            response.then().log().all();
            String jsonString =response.asString();

            // Validate the response
            Assert.assertTrue(jsonString.contains("updatedAt"), "Correct response recieved");

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Resource has been successfully updated ",true );
    }

    @Test(priority =12,retryAnalyzer = RetryUsecases.class)
    public void Delete_Unknown_resource_TC013() throws IOException {
        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("Delete_Userdetail_By_Id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("Delete_Userdetail_By_Id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            baseURI = obj2.getProperty("Usecase3_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id);
            response = httpRequest.delete(baseURI+"/{id}");
            response.then().log().all();
            int status_code = response.statusCode();
            Assert.assertTrue(String.valueOf(status_code).equalsIgnoreCase("204"));

        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("resource has been successfully deleted",true );
    }

    @Test(priority =13,retryAnalyzer = RetryUsecases.class)
    public void Create_New_Login_Session_TC014() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("username","abc");
            payload.put("email","abc@example.com");
            payload.put("password","abc123");
            baseURI = obj2.getProperty("Usecase4_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.post(baseURI+"/{id}");
            response.then().log().all();
            int status_code = response.statusCode();
            Assert.assertTrue(String.valueOf(status_code).equalsIgnoreCase("201"),"Invalid status code");
            String jsonString =response.asString();
            // Validate the response
            Assert.assertTrue(jsonString.contains("createdAt"), "Correct response recieved");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Resource has been successfully updated ",true );
    }

    @Test(priority =14,retryAnalyzer = RetryUsecases.class)
    public void Register_New_Login_Session_TC015() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("username","abc");
            payload.put("email","abc@example.com");
            payload.put("password","abc123");
            baseURI = obj2.getProperty("Usecase5_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.post(baseURI+"/{id}");
            response.then().log().all();
            int status_code = response.statusCode();
            Assert.assertTrue(String.valueOf(status_code).equalsIgnoreCase("201"),"Invalid status code");
            String jsonString =response.asString();
            // Validate the response
            Assert.assertTrue(jsonString.contains("createdAt"), "Correct response recieved");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Resource has been successfully updated ",true );
    }
    @Test(priority =15,retryAnalyzer = RetryUsecases.class)
    public void Logout_Session_TC016() throws IOException {

        Properties obj2 = REST.load();
//        Properties obj3 =ReadPropertyFile.fetch();
        Response response = null;
//        String id = obj3.getProperty("userdetails_by_id");
        HashMap<String,String> excelData = ReadExcelFile.fetchData();
        String id = excelData.get("userdetails_by_id");
        Reporter.log("Update the user detail using http method PUT ",true );
        try {
            HashMap<String,String> payload =new HashMap<>();
            payload.put("username","abc");
            payload.put("email","abc@example.com");
            payload.put("password","abc123");
            baseURI = obj2.getProperty("Usecase6_URL");;
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all().pathParam("id",id).body(payload);
            response = httpRequest.post(baseURI+"/{id}");
            response.then().log().all();
            int status_code = response.statusCode();
            Assert.assertTrue(String.valueOf(status_code).equalsIgnoreCase("201"),"Invalid status code");
            String jsonString =response.asString();
            // Validate the response
            Assert.assertTrue(jsonString.contains("createdAt"), "Correct response recieved");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Reporter.log("Resource has been successfully updated ",true );
    }

}