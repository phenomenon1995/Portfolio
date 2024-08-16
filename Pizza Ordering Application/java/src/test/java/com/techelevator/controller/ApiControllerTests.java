package com.techelevator.controller;

import com.techelevator.controller.ApiController;
import com.techelevator.controller.ProductController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApiControllerTests {
    
    private RestTemplate http;
    private HttpHeaders header;
    private List<Map<String, String>> addresses;
    private Map<String, String> storeAddress;
    private Map<String, String> customerAddress;

    private final String BASE_URL = "http://localhost:9000";

    @Before
    public void setup() {
        http = new RestTemplate();
        header = new HttpHeaders();
        addresses = new ArrayList<>();
        storeAddress = new HashMap<>();
        //5140 Wilson Mills Rd, Richmond Heights, OH 44143
        storeAddress.put("street_address", "5140 Wilson Mills Rd");
        storeAddress.put("city", "Richmond Heights");
        storeAddress.put("state_abbreviation", "OH");
        storeAddress.put("zip_code", "44143");

        customerAddress = new HashMap<>();
    }

    @Test
    public void t01_valid_address_in_range_passes_validation(){
        customerAddress.put("street_address", "1349 SOM Center Road");
        customerAddress.put("city", "Mayfield Heights");
        customerAddress.put("state_abbreviation", "OH");
        customerAddress.put("zip_code", "44124");

        addresses.add(storeAddress);
        addresses.add(customerAddress);
        HttpEntity entity = new HttpEntity(addresses);

        String url = BASE_URL + "/validate";
        ResponseEntity response = http.exchange(url, HttpMethod.POST, entity, void.class);
        Assert.assertTrue(response.getStatusCode().is2xxSuccessful());

    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void t02_valid_address_out_of_range_fails_validation(){
        //484 Lowe Dr
        //Hague, Virginia(VA), 22469

        customerAddress.put("street_address", "484 Lowe Dr");
        customerAddress.put("city", "Hague");
        customerAddress.put("state_abbreviation", "VA");
        customerAddress.put("zip_code", "22469");

        addresses.add(storeAddress);
        addresses.add(customerAddress);
        HttpEntity entity = new HttpEntity(addresses);

        String url = BASE_URL + "/validate";
        ResponseEntity response = http.exchange(url, HttpMethod.POST, entity, void.class);
    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void t03_invalid_address_fails_validation(){
        //484 Lowe Dr
        //Hague, Virginia(VA), 22469

        customerAddress.put("street_address", "");
        customerAddress.put("city", "");
        customerAddress.put("state_abbreviation", "");
        customerAddress.put("zip_code", "");

        addresses.add(storeAddress);
        addresses.add(customerAddress);
        HttpEntity entity = new HttpEntity(addresses);

        String url = BASE_URL + "/validate";
        ResponseEntity response = http.exchange(url, HttpMethod.POST, entity, void.class);
    }
}

