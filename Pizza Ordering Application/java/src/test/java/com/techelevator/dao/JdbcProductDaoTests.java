package com.techelevator.dao;

import com.fasterxml.jackson.databind.ser.std.ClassSerializer;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Product;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.apache.coyote.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcProductDaoTests extends BaseDaoTests {

    private JdbcProductDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        NamedParameterJdbcTemplate newParameter = new NamedParameterJdbcTemplate(dataSource);
        sut = new JdbcProductDao(jdbcTemplate, newParameter);
    }
    @Test
    public void t01_getPorducts_returns_all_products(){
        List<Product> products = sut.getProducts();
        Assert.assertTrue(products.size() > 0);
    }
    @Test
    public void t02_getCategories_returns_map_of_categories(){
        Map<Integer, String> categories = sut.getCategories();
        Assert.assertTrue(categories.size() > 0);
    }

    @Test
    public void t03_getProductById_returns_product_from_valid_id(){
        int testProductId = 1;
        Product product = sut.getProductById(testProductId);
        Assert.assertNotNull(product);
        Assert.assertTrue(product.getClass().equals(Product.class));
    }
    @Test (expected = NullPointerException.class)
    public void t04_getProductById_returns_null_from_invalid_id() {
        int testProductId = -1;
        Product product = sut.getProductById(testProductId);
        Assert.assertNull(product);
        Assert.assertTrue(product.getClass().equals(Product.class));
    }
    @Test
    public void t05_getInvoiceById_returns_product_from_valid_id(){
        int testInvoiceId = 1;
        Product product = sut.getProductById(testInvoiceId);
        Assert.assertNotNull(product);
        Assert.assertTrue(product.getClass().equals(Product.class));
    }
    @Test (expected = NullPointerException.class)
    public void t06_getInvoicetById_returns_null_from_invalid_id() {
        int testInvoiceId= -1;
        Product product = sut.getProductById(testInvoiceId);
        Assert.assertNull(product);
        Assert.assertTrue(product.getClass().equals(Product.class));
    }
    @Test
    public void t07_getProductsByCategoryDescription_returns_correct_products_from_valid_categories(){
        List<String> categories = new ArrayList<>();
        categories.add("Drink");
        List<Product> products = sut.getProductsByCategoryDescription(categories);
        Assert.assertTrue(products.size() == 5);
        categories.add("Dessert");
        products = sut.getProductsByCategoryDescription(categories);
        Assert.assertTrue(products.size() == 10);
    }
    @Test
    public void t08_addProduct_correctly_adds_valid_product(){
        Product product = new Product();
        product.setDescription("A test thing");
        product.setPrice(BigDecimal.ONE);
        product.setProductCategoryDescription("Regular Topping");
        product.setQuantity(20);

        Product createdProduct = sut.createProduct(product);

        Assert.assertTrue(product.getDescription().equals(createdProduct.getDescription()));
        Assert.assertTrue(product.getProductCategoryDescription().equals(createdProduct.getProductCategoryDescription()));
        Assert.assertTrue(product.getPrice().compareTo(createdProduct.getPrice())==0);
        Assert.assertTrue(product.getQuantity() == createdProduct.getQuantity());
    }
    @Test (expected = ResponseStatusException.class)
    public void t09_addProduct_throws_response_status_exception_with_invalid_product(){
        Product product = new Product();
        Product createdProduct = sut.createProduct(product);
    }
    @Test
    public void t10_deleteProduct_deletes_product_with_valid_id(){
        int testId = 1;
        Assert.assertEquals(1, sut.deleteProductById(testId));
        Assert.assertNull(sut.getProductById(testId));
    }
    @Test(expected = ResponseStatusException.class)
    public void t11_deleteProduct_throws_response_status_exception_with_invalid_id(){
       int testId = -1;
       sut.deleteProductById(testId);
    }

    @Test
    public void t12_updateProduct_correctly_updates_with_valid_product(){
        Product product = sut.getProductById(1);
        product.setQuantity(1);
        product.setProductCategoryDescription("Drink");
        product.setPrice(BigDecimal.ONE);
        product.setDescription("A new thing now.");

        Product updatedProduct = sut.updateProduct(product);
        Assert.assertTrue(product.getQuantity() == updatedProduct.getQuantity());
        System.out.println(product.getProductCategoryDescription());
        System.out.println(updatedProduct.getProductCategoryDescription());
        Assert.assertTrue(product.getProductCategoryDescription().equals(updatedProduct.getProductCategoryDescription()));
        Assert.assertTrue(product.getPrice().compareTo(updatedProduct.getPrice()) == 0);
        Assert.assertTrue(product.getDescription().equals(updatedProduct.getDescription()));
    }
    @Test (expected = DaoException.class)
    public void t13_updateProduct_throws_exception_with_invalid_product(){
        Product product = sut.getProductById(1);
        product.setProductId(100);
        Product updatedProduct = sut.updateProduct(product);
    }
}
