package com.techelevator.controller;

import com.techelevator.controller.ProductController;
import com.techelevator.dao.BaseDaoTests;
import com.techelevator.dao.JdbcProductDao;
import com.techelevator.dao.ProductDao;
import com.techelevator.model.LoginDto;
import com.techelevator.model.Product;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ProductControllerTests{

    private RestTemplate http;
    private HttpHeaders header;
    private final String BASE_URL = "http://localhost:9000";

    @Before
    public void setup(){

        http = new RestTemplate();
        header = new HttpHeaders();
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
    public void t01_API_returns_all_products_with_no_categories() {
        login("admin");
        String url = BASE_URL + "/menu";

        Map<String, List<String>> requestBody = new HashMap<>();
        List<String> categories = new ArrayList<>();
        requestBody.put("categories", categories);
        HttpEntity entity = new HttpEntity( requestBody, header );
        ResponseEntity response = http.postForEntity(url, entity, Map.class);

        Map products = (Map)response.getBody();
        System.out.println(products.keySet());
        Assert.assertNotNull(products);
        Assert.assertTrue(products.keySet().size() == 10 );


    }
    @Test
    public void t02_API_returns_all_products_with_some_categories() {
        login("admin");
        String url = BASE_URL + "/menu";

        Map<String, List<String>> requestBody = new HashMap<>();
        List<String> categories = new ArrayList<>();
        categories.add("Drink");
        categories.add("Salad");
        categories.add("Dessert");

        requestBody.put("categories", categories);
        HttpEntity entity = new HttpEntity( requestBody, header );
        ResponseEntity response = http.postForEntity(url, entity, Map.class);

        Map products = (Map)response.getBody();
        System.out.println(products.keySet());
        Assert.assertNotNull(products);
        Assert.assertTrue(products.keySet().size() == 3 );
        Assert.assertTrue(products.keySet().containsAll(categories));


    }

    @Test(expected = HttpClientErrorException.BadRequest.class)
    public void t03_API_throws_exception_when_body_for_getProducts_invalid(){
        login("admin");
        String url = BASE_URL + "/menu";

        Map<String, List<String>> requestBody = new HashMap<>();
        List<String> categories = new ArrayList<>();
        requestBody.put("categories", categories);
        HttpEntity entity = new HttpEntity(header);
        ResponseEntity response = http.postForEntity(url, entity, Map.class);

        Map products = (Map)response.getBody();
        System.out.println(products.keySet());
        Assert.assertNull(products);
    }

    @Test
    public void t04_API_returns_single_product_correctly(){
        List<Integer> productIds = new ArrayList<>();
        productIds.add(10);
        productIds.add(11);
        productIds.add(20);
        productIds.add(22);
        for (int id : productIds) {
            String url = BASE_URL + "/menu/" + id;
            HttpEntity entity = new HttpEntity(header);
            System.out.println(url);
            ResponseEntity<Product> response = http.exchange(url, HttpMethod.GET, entity, Product.class);
            Product product = response.getBody();
            Assert.assertNotNull(product);
        }
    }
    @Test
    public void t05_API_returns_null_with_invalid_product_id(){
        List<Integer> productIds = new ArrayList<>();
        productIds.add(-1);
        for (int id : productIds) {
            String url = BASE_URL + "/menu/";
            url += id;
            HttpEntity entity = new HttpEntity(header);
            ResponseEntity<Product> response = http.exchange(url, HttpMethod.GET, entity, Product.class);
            Product product = response.getBody();
            Assert.assertNull(product);
        }
    }

    @Test(expected =  Exception.class)
    public void t06_API_throws_exception_with_non_integer_product_id(){
        List<String> productIds = new ArrayList<>();
        productIds.add("A");
        for (String id : productIds) {
            String url = BASE_URL + "/menu/";
            url += id;
            HttpEntity entity = new HttpEntity(header);
            ResponseEntity<Product> response = http.exchange(url, HttpMethod.GET, entity, Product.class);
            Product product = response.getBody();
            Assert.assertNull(product);
        }
    }

    @Test
    public void t07_admin_can_create_products_but_not_users(){
        login("admin");
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("price", "1.50");
        newProduct.put("description", "test product");
        newProduct.put("product_category_description", "Drink");
        newProduct.put("quantity","20");



        String url = BASE_URL + "/menu/add";
        HttpEntity entity = new HttpEntity(newProduct, header);
        Product createdProduct = http.exchange(url, HttpMethod.POST, entity, Product.class).getBody();

        Assert.assertTrue( createdProduct.getClass() == Product.class);
        Assert.assertTrue(createdProduct.getPrice().compareTo(
                new BigDecimal(newProduct.get("price")))==0);
        Assert.assertTrue(createdProduct.getDescription().equals(newProduct.get("description")));
        Assert.assertTrue(createdProduct.getProductCategoryDescription().equals(
                newProduct.get("product_category_description")));
        Assert.assertTrue(createdProduct.getQuantity() == Integer.parseInt(newProduct.get("quantity")));

    }
    @Test (expected = HttpClientErrorException.BadRequest.class)
    public void t08_api_throws_exception_when_invalid_product_added(){
        login("admin");
        Product newProduct =new Product();

        String url = BASE_URL + "/menu/add";
        HttpEntity entity = new HttpEntity(newProduct, header);
        Product createdProduct = http.exchange(url, HttpMethod.POST, entity, Product.class).getBody();

    }
    @Test(expected =  HttpClientErrorException.Forbidden.class)
    public void t09_non_admin_cannot_create_products() {
        login("user");
        Product newProduct = new Product();
        newProduct.setDescription("Test Product");
        newProduct.setPrice(new BigDecimal("1.50"));
        newProduct.setProductCategoryDescription("Drink");
        newProduct.setQuantity(20);

        String url = BASE_URL + "/menu/add";
        HttpEntity entity = new HttpEntity(newProduct, header);
        Product createdProduct = http.exchange(url, HttpMethod.POST, entity, Product.class).getBody();
    }

    @Test
    public void t10_admin_can_delete_product(){
        login("admin");
        Map<String, String> newProduct = new HashMap<>();
        newProduct.put("price", "1.50");
        newProduct.put("description", "test product");
        newProduct.put("product_category_description", "Drink");
        newProduct.put("quantity","20");
        String url = BASE_URL + "/menu/add";
        HttpEntity entity = new HttpEntity(newProduct, header);
        Product createdProduct = http.exchange(url, HttpMethod.POST, entity, Product.class).getBody();

        int testProductId = createdProduct.getProductId();
        url = BASE_URL +  "/menu/" + testProductId;
        entity = new HttpEntity(header);

        http.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    @Test(expected = HttpClientErrorException.Forbidden.class)
    public void t11_non_admin_deleting_product_throws_exception(){
        login("user");
        int testProductId = 10;
        String url = BASE_URL +  "/menu/" + testProductId;
        HttpEntity entity = new HttpEntity(header);

        http.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    @Test
    public void t12_admin_can_update_product(){
        login("admin");
        int testProductId = 10;
        HttpEntity getEntity = new HttpEntity(header);
        Product modifiedProduct = http.exchange(BASE_URL+"/menu/"+testProductId, HttpMethod.GET, getEntity, Product.class).getBody();
        modifiedProduct.setQuantity(0);
        modifiedProduct.setProductCategoryDescription("Drink");
        modifiedProduct.setPrice(BigDecimal.TEN);

        String url = BASE_URL + "/menu/" + testProductId;
        HttpEntity entity = new HttpEntity(modifiedProduct, header);
        Product updatedProduct = http.exchange(url, HttpMethod.PUT, entity, Product.class).getBody();

        Assert.assertNotNull(updatedProduct);
    }
    @Test(expected = HttpClientErrorException.Forbidden.class)
    public void t13_non_admin_update_product_throws_exception(){
        login("user");
        int testProductId = 10;
        HttpEntity getEntity = new HttpEntity(header);
        Product modifiedProduct = http.exchange(BASE_URL+"/menu/"+testProductId, HttpMethod.GET, getEntity, Product.class).getBody();
        modifiedProduct.setQuantity(0);
        modifiedProduct.setProductCategoryDescription("Drink");
        modifiedProduct.setPrice(BigDecimal.TEN);

        String url = BASE_URL + "/menu/" + testProductId;
        HttpEntity entity = new HttpEntity(modifiedProduct, header);
        Product updatedProduct = http.exchange(url, HttpMethod.PUT, entity, Product.class).getBody();

    }
}


