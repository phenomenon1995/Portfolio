package com.techelevator.dao;

import com.techelevator.model.Pizza;
import com.techelevator.model.Product;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<Product> getProducts();
    Map<Integer,String> getCategories();

    Product getProductById(int productId);
    List<Product> getProductsByInvoiceId(int invoiceId);
    List<Product> filterOutPizzaProducts(List<Product> productList, List<Pizza> pizzaList);
    List<Product> getProductsByCategoryIds(int[] categoryIds);
    List<Product> getProductsByCategoryDescription(List<String> categoryDescriptions);
    Product createProduct(Product product);
    int deleteProductById(int productId);
    Product updateProduct (Product product);


    Product mapRowSet (SqlRowSet rowSet);
}
