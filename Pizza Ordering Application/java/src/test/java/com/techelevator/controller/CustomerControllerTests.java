package com.techelevator.controller;

import com.techelevator.Application;
import com.techelevator.controller.CustomerController;
import com.techelevator.controller.ProductController;
import com.techelevator.dao.BaseDaoTests;
import com.techelevator.dao.CustomerDao;
import com.techelevator.dao.JdbcCustomerDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CustomerControllerTests extends BaseDaoTests {

    private CustomerController cc;
    private CustomerDao dao;
    private UserDao userDao;
    private JdbcTemplate jdbc;
    private NamedParameterJdbcTemplate namedJdbc;
    private RestTemplate http;
    private TestRestTemplate testHttp;
    private HttpHeaders header;
    private final String BASE_URL = "http://localhost:9000";

    @Before
    public void setup(){

        jdbc = new JdbcTemplate(dataSource);
        namedJdbc = new NamedParameterJdbcTemplate(dataSource);
        dao = new JdbcCustomerDao(jdbc, userDao);
        cc = new CustomerController(dao, userDao);
        http = new RestTemplate();
        testHttp = new TestRestTemplate();
    }

    public void loginAdmin(){
        // Create a request object
        LoginDto loginRequest = new LoginDto();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("password");
        String url = BASE_URL + "/login";

        // Send a POST request to the login endpoint
        Map response = http.postForObject(url, loginRequest, Map.class);
        String token = (String) response.get("token");
        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(token);


    }

    public void login(String username){
        // Create a request object
        LoginDto loginRequest = new LoginDto();
        loginRequest.setUsername(username);
        loginRequest.setPassword("password");
        String url = BASE_URL + "/login";

        // Send a POST request to the login endpoint
        Map response = http.postForObject(url, loginRequest, Map.class);
        String token = (String) response.get("token");
        header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(token);


    }

    @Test
    public void t01_API_returns_all_customers_with_no_email() {
        login("admin");
        String url = BASE_URL + "/customer";

        Customer customer = new Customer();

        HttpEntity entity = new HttpEntity( header );
        ResponseEntity response = http.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map> customers = (List<Map>) response.getBody();
        for(Map<String, Object> object : customers){
            System.out.println(object);
            customer.setUserId((int)object.get("user_id"));
            customer.setStreetAddress((String)object.get("street_address"));
            customer.setCustomerId((int)object.get("customer_id"));
            customer.setCity((String)object.get("city"));
            customer.setZipcode((int)object.get("zip_code"));
            customer.setStateAbbreviation((String)object.get("state_abbreviation"));
            customer.setPhoneNumber((String)object.get("phone_number"));
            customer.setEmail((String)object.get("email"));
            customer.setFirstName((String)object.get("first_name"));
            customer.setLastName((String)object.get("last_name"));

            System.out.println(customer.getUserId());
        }
        Assert.assertNotNull(customers);

    }

    @Test
    public void t02_API_returns_customer_with_an_email() {
        login("admin");
        String url = BASE_URL + "/customer";

        String requestParameter = "/sed.pharetra@hotmail.ca";


        HttpEntity entity = new HttpEntity( header );
        ResponseEntity response = http.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map> customers = (List<Map>) response.getBody();


        System.out.println(customers);
    }

    @Test
    public void t03_API_returns_single_customer_by_id_correctly(){
        login("admin");

        List<Integer> customerIds = new ArrayList<>();
        customerIds.add(3);
        customerIds.add(4);
        customerIds.add(5);
        customerIds.add(6);
        for (int id : customerIds) {
            String url = BASE_URL + "/customer/";
            url += id;
            HttpEntity entity = new HttpEntity(header);
            ResponseEntity<Map> response = http.exchange(url, HttpMethod.GET, entity, Map.class);
            Map customer = response.getBody();
            System.out.println(customer);
            Assert.assertNotNull(customer);
        }
    }

    @Test(expected =  Exception.class)
    public void t04_API_throws_exception_with_non_integer_customer_id(){
        login("admin");

        List<String> customerIds = new ArrayList<>();
        customerIds.add("A");
        for (String id : customerIds) {
            String url = BASE_URL + "/customer/";
            url += id;
            HttpEntity entity = new HttpEntity(header);
            ResponseEntity<Customer> response = http.exchange(url, HttpMethod.GET, entity, Customer.class);
            Customer customer = response.getBody();
            Assert.assertNull(customer);
        }
    }

    @Test
    public void t05_API_can_delete_customer(){
        login("admin");

        int testCustomerId = 2;
        String url = BASE_URL +  "/customer/" + testCustomerId;
        HttpEntity entity = new HttpEntity(header);

        http.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    @Test(expected = HttpClientErrorException.Forbidden.class)
    public void t06_non_admin_deleting_customer_throws_exception(){
        login("user");
        int testCustomerId = 1;
        String url = BASE_URL +  "/customer/" + testCustomerId;
        HttpEntity entity = new HttpEntity(header);

        http.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    @Test
    public void t07_API_can_update_customer(){
        login("admin");
        int testCustomerId = 3;
        Customer modifiedCustomer = dao.getCustomerById(testCustomerId);
        modifiedCustomer.setFirstName("John");
        modifiedCustomer.setLastName("Doe");
        modifiedCustomer.setCity("City");
        modifiedCustomer.setUserId(3);

        String url = BASE_URL + "/customer/" + testCustomerId;
        HttpEntity entity = new HttpEntity(modifiedCustomer, header);
        Customer updatedCustomer = http.exchange(url, HttpMethod.PUT, entity, Customer.class).getBody();

        Assert.assertNotNull(updatedCustomer);
    }

    @Test(expected = HttpClientErrorException.Forbidden.class)
    public void t08_non_admin_update_product_throws_exception(){
        login("user");
        int testCustomerId = 1;
        Customer modifiedCustomer = dao.getCustomerById(testCustomerId);
        modifiedCustomer.setFirstName("John");
        modifiedCustomer.setLastName("Doe");
        modifiedCustomer.setCity("City");

        String url = BASE_URL + "/customer/" + testCustomerId;
        HttpEntity entity = new HttpEntity(modifiedCustomer, header);
        Customer updatedCustomer = http.exchange(url, HttpMethod.PUT, entity, Customer.class).getBody();

    }

    @Test
    public void t09_API_can_create_a_customer(){
        login("admin");

        Customer newCustomer = new Customer();
        newCustomer.setFirstName("John");
        newCustomer.setLastName("Doe");
        newCustomer.setCity("City");
        newCustomer.setEmail("newemail@email.com");
        newCustomer.setPhoneNumber("1234567890");
        newCustomer.setStateAbbreviation("AZ");
        newCustomer.setStreetAddress("1234 Test Id, St");

        String url = BASE_URL + "/customer";
        HttpEntity entity = new HttpEntity(newCustomer, header);
        Customer createdCustomer = http.exchange(url, HttpMethod.POST, entity, Customer.class).getBody();

        Assert.assertSame(createdCustomer.getClass(), Customer.class);

    }

    @Test(expected =  HttpClientErrorException.Forbidden.class)
    public void t10_non_admin_cannot_create_customer() {
        login("user");
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("John");
        newCustomer.setLastName("Doe");
        newCustomer.setCity("City");
        newCustomer.setEmail("newemail@email.com");
        newCustomer.setPhoneNumber("1234567890");
        newCustomer.setStateAbbreviation("AZ");
        newCustomer.setStreetAddress("1234 Test Id, St");
        newCustomer.setUserId(20);

        String url = BASE_URL + "/customer";
        HttpEntity<Customer> entity = new HttpEntity(newCustomer, header);
        Customer createdCustomer = http.exchange(url, HttpMethod.POST, entity, Customer.class).getBody();
    }
}
