package com.techelevator.controller;

import com.techelevator.controller.InvoiceController;
import com.techelevator.dao.BaseDaoTests;
import com.techelevator.model.Invoice;
import com.techelevator.model.LoginDto;
import com.techelevator.model.Pizza;
import com.techelevator.model.Product;
import net.bytebuddy.matcher.FilterableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
public class InvoiceControllerTests {
    private RestTemplate http;
    private HttpHeaders header;
    private final String BASE_URL = "http://localhost:9000";
    @Before
    public void setup(){
        http = new RestTemplate();
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
    private Invoice mapObjectToInvoice(Map object){
        Invoice invoice = new Invoice();
        invoice.setInvoiceId((int)object.get("invoice_id"));
        invoice.setUserId((int)object.get("user_id"));
        invoice.setTotal(BigDecimal.valueOf((Double)object.get("total")));
        invoice.setStatus((String) object.get("status"));
        invoice.setDelivery((Boolean) object.get("delivery"));

        return invoice;
    }

    @Test
    public void t01_API_returns_all_invoices_for_admins_with_default_params(){
        login("admin");

        String url = BASE_URL + "/invoice";
        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<List> response = http.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map> invoices = response.getBody();

        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() >= 0);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));
    }

    @Test
    public void t02_API_returns_all_invoices_for_admins_with_date_range(){
        login("admin");
        HttpEntity entity = new HttpEntity(header);

        String url = BASE_URL + "/invoice?from=2022-01-01&to=2023-01-01";
        ResponseEntity<List> response = http.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map> invoices = response.getBody();
        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() < 20);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));

        url = BASE_URL + "/invoice?from=2022-01-01";
         response = http.exchange(url, HttpMethod.GET, entity, List.class);
        invoices = response.getBody();
        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() > 20);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));

        url = BASE_URL + "/invoice?to=2023-01-01";
        response = http.exchange(url, HttpMethod.GET, entity, List.class);
        invoices = response.getBody();
        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() < 20);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));
    }

    @Test
    public void t03_API_returns_only_invoices_belonging_to_current_user(){
        login("1");

        String url = BASE_URL + "/invoice";
        HttpEntity entity = new HttpEntity(header);
        ResponseEntity<List> response = http.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map> invoices = response.getBody();

        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() >= 3);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));
    }

    @Test (expected = HttpClientErrorException.BadRequest.class)
    public void t04_API_throws_exception_with_bad_params(){
        login("admin");
        HttpEntity entity = new HttpEntity(header);

        String url = BASE_URL + "/invoice?from=p&to=z";
        ResponseEntity<List> response = http.exchange(url, HttpMethod.GET, entity, List.class);
        List<Map> invoices = response.getBody();
        System.out.println(invoices.size());
        Assert.assertTrue(invoices.size() < 20);
        Assert.assertNotNull(mapObjectToInvoice(invoices.get(0)));

    }

    @Test
    public void t05_API_gets_correct_invoice_with_id(){
        login("admin");
        HttpEntity entity = new HttpEntity(header);

        String url = BASE_URL + "/invoice/1";
        Map retrievedMap =  http.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
        Invoice retrievedInvoice = mapObjectToInvoice((Map)retrievedMap.get("invoice"));
        Assert.assertNotNull(retrievedInvoice);
        Assert.assertEquals("Invoice Id doesn't match" ,1, retrievedInvoice.getInvoiceId());
    }
    @Test (expected = HttpClientErrorException.Forbidden.class)
    public void t06_API_throws_forbidden_with_unauth_user_views_invoice(){
        login("1");
        HttpEntity entity = new HttpEntity(header);

        String url = BASE_URL + "/invoice/10";
        Map retrievedMap =  http.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
        Invoice retrievedInvoice = mapObjectToInvoice((Map)retrievedMap.get("invoice"));
        Assert.assertNotNull(retrievedInvoice);
        Assert.assertEquals("Invoice Id doesn't match" ,10, retrievedInvoice.getInvoiceId());
    }
    @Test (expected = HttpClientErrorException.BadRequest.class)
    public void t07_API_returns_null_invoice_with_invalid_id(){
        login("admin");
        HttpEntity entity = new HttpEntity(header);

        String url = BASE_URL + "/invoice/d";
        Map retrievedMap =  http.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
        Invoice retrievedInvoice = mapObjectToInvoice((Map)retrievedMap.get("invoice"));
        Assert.assertNotNull(retrievedInvoice);
        Assert.assertEquals("Invoice Id doesn't match" ,1, retrievedInvoice.getInvoiceId());

    }
    @Test
    public void t08_API_return_invoices_when_given_valid_order(){
        List<Pizza> pizzas = new ArrayList<>();
        List<Product>  other = new ArrayList<>();

    }}
