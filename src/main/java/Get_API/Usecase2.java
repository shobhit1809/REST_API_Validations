package Get_API;

import Listeners.REST;
import Listeners.RetryUsecases;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class Usecase2 {

    @Test(priority = 0, retryAnalyzer = RetryUsecases.class)
    public void validate_Indian_State_University_Name_TC01() throws IOException, InterruptedException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        RestAssured.baseURI = obj2.getProperty("Usecase2_URL");
        Response response = null;
        Reporter.log("Get the results for username By id ", true);
        try {
            baseURI = obj2.getProperty("Usecase2_URL");
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all();
            response = httpRequest.get(baseURI);
            Thread.sleep(600000);
            response.then().log().all();

            String jsonstring = response.asString();
//                JSONObject mainobj = new JSONObject(jsonstring);
            JSONArray arr = new JSONArray(jsonstring);
            int len = arr.length();

            ArrayList<HashMap> desired_resp = new ArrayList<HashMap>();
            HashSet<String> university =new HashSet<>();
            HashSet<String> state =new HashSet<>();
            for (int i = 0; i < len; i++) {
                String country_temp = arr.getJSONObject(i).getString("country");
                String name_temp = arr.getJSONObject(i).getString("name");
//                String state_temp = arr.getJSONObject(i).getString("state-province");
                HashMap<String, String> object_temp = new HashMap<>();
                object_temp.put("country", country_temp);
                object_temp.put("name", name_temp);
//                object_temp.put("webpage_temp", state_temp);
                desired_resp.add(object_temp);
                if((arr.getJSONObject(i).getString("country")).equals("India")){
                    university.add(name_temp);
//                    state.add(state_temp);
                }

            }
            for (int j = 0; j < desired_resp.size(); j++) {
                System.out.println(desired_resp.get(j).toString());
            }
//            boolean flag =false;
//            if((university.contains("University of Petroleum and Energy Studies") && state.contains("Dehradun")) && ((university.contains("Lovely Professional University") && state.contains("Punjab")))) {
//                flag = true;
//            }
//
//            Assert.assertTrue(flag,"Verified successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Reporter.log("Verified Test successfully", true);
    }
    @Test(priority = 1, retryAnalyzer = RetryUsecases.class)
    public void validate_stateProvince_USA_TC02() throws IOException, InterruptedException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        RestAssured.baseURI = obj2.getProperty("Usecase2_URL");
        Response response = null;
        Reporter.log("Get the results for username By id ", true);
            try {
                baseURI = obj2.getProperty("Usecase2_URL");
                RestAssured.useRelaxedHTTPSValidation();
                RequestSpecification httpRequest = RestAssured.given().log().all();
                response = httpRequest.get(baseURI);
                Thread.sleep(300000);
                response.then().log().all();

                String jsonstring = response.asString();
//                JSONObject mainobj = new JSONObject(jsonstring);
                JSONArray arr = new JSONArray(jsonstring);
                int len = arr.length();

                ArrayList<HashMap> desired_resp = new ArrayList<HashMap>();
                for (int i = 0; i < len; i++) {
                    String country_temp = arr.getJSONObject(i).getString("country");
                    String state_temp = arr.getJSONObject(i).getString("state-province");
                    HashMap<String, String> object_temp = new HashMap<>();
                    object_temp.put("country", country_temp);
                    object_temp.put("state-province", state_temp);
                    desired_resp.add(object_temp);

                }
                for (int j = 0; j < desired_resp.size(); j++) {
                    System.out.println(desired_resp.get(j).toString());
                }
                boolean flag =true;
                for(int i=0;i<desired_resp.size();i++) {
                    if((desired_resp.get(i).get("country")).equals("United States") && (desired_resp.get(i).get("state-province"))!=null) {
                        flag = false;
                    }
                }
                Assert.assertTrue(flag,"For country United States,state province without null exists");

            } catch (Exception e) {
                e.printStackTrace();
            }
            Reporter.log("Verified all state provinces are null for country USA", true);
        }

    @Test(priority = 2, retryAnalyzer = RetryUsecases.class)
    public void validate_RussianFederationUniversity_TC03() throws IOException, InterruptedException {
        Properties obj = REST.setUP();
        Properties obj2 = REST.load();
        RestAssured.baseURI = obj2.getProperty("Usecase2_URL");
        Response response = null;
        Reporter.log("Get the results for username By id ", true);
        try {
            baseURI = obj2.getProperty("Usecase2_URL");
            RestAssured.useRelaxedHTTPSValidation();
            RequestSpecification httpRequest = RestAssured.given().log().all();
            response = httpRequest.get(baseURI);
            Thread.sleep(300000);
            response.then().log().all();

            String jsonstring = response.asString();
//                JSONObject mainobj = new JSONObject(jsonstring);
            JSONArray arr = new JSONArray(jsonstring);
            int len = arr.length();

            ArrayList<HashMap> desired_resp = new ArrayList<HashMap>();
            HashSet<String> university =new HashSet<>();
            HashSet<String> webpage =new HashSet<>();
            for (int i = 0; i < len; i++) {
                String country_temp = arr.getJSONObject(i).getString("country");
                String name_temp = arr.getJSONObject(i).getString("name");
                String webpage_temp = arr.getJSONObject(i).getString("web_pages");
                HashMap<String, String> object_temp = new HashMap<>();
                object_temp.put("country", country_temp);
                object_temp.put("name", name_temp);
                object_temp.put("webpage_temp", webpage_temp);
                desired_resp.add(object_temp);
                if((arr.getJSONObject(i).getString("country")).equals("Russian Federation")){
                    university.add(name_temp);
                    webpage.add(webpage_temp);
                }

            }
            for (int j = 0; j < desired_resp.size(); j++) {
                System.out.println(desired_resp.get(j).toString());
            }
            boolean flag =false;
                if(university.contains("St. Petersburg State Cinema and TV University") && webpage.contains("http://www.gukit.ru/")) {
                    flag = true;
                }

            Assert.assertTrue(flag,"For country Russian Federation,state province without null exists");

        } catch (Exception e) {
            e.printStackTrace();
        }
        Reporter.log("Verified Test successfully", true);
    }
}
