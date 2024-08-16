package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Pizza;
import com.techelevator.model.Product;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Component
public class JdbcProductDao implements ProductDao{
    private JdbcTemplate db;
    private NamedParameterJdbcTemplate namedDb;

    public JdbcProductDao(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate template) {
        this.db = jdbcTemplate;
        this.namedDb = template;
    }

    @Override
    public List<Product> getProducts() {
        String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description, " +
                "p.price, p.description, p.quantity " +
                "FROM product p " +
                "JOIN product_category pc ON p.product_category_id = pc.product_category_id " +
                "ORDER BY p.product_category_id";
      List<Product> allProducts = new ArrayList<>();
        try{
            SqlRowSet results = db.queryForRowSet(sql);
            while(results.next()){
                allProducts.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return allProducts;
    }

    @Override
    public Map<Integer, String> getCategories() {
        String sql = "SELECT product_category_id, product_category_description FROM product_category " +
                "ORDER BY product_category_id";
        Map<Integer, String> categories = new HashMap<>();
        try{
            SqlRowSet results = db.queryForRowSet(sql);
            while(results.next()){
                categories.put(
                        results.getInt("product_category_id"),
                        results.getString("product_category_description"));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return categories;
    }

    @Override
    public Product getProductById(int productId) {
        Product product = null;
        String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description, price, description, quantity FROM product p " +
                "JOIN product_category pc ON p.product_category_id = pc.product_category_id " +
                "WHERE p.product_id = ?";
        try{
            SqlRowSet result = db.queryForRowSet(sql, productId);
            if(result.next()){
                product = mapRowSet(result);
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return product;
    }

    @Override
    public List<Product> getProductsByInvoiceId(int invoiceId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description, " +
                "p.price, p.description, p.quantity " +
                "FROM product p " +
                "JOIN product_category pc ON p.product_category_id = pc.product_category_id " +
                "JOIN invoice_product ip ON p.product_id = ip.product_id " +
                "WHERE ip.invoice_id = ? " +
                "ORDER BY p.product_id ";
        try{
            SqlRowSet results = db.queryForRowSet(sql, invoiceId);
            while(results.next()){
                products.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> filterOutPizzaProducts(List<Product> productList, List<Pizza> pizzaList) {
        List<Integer> pizzaComponentIds = new ArrayList<>();
        List<Product> filteredProducts = new ArrayList<>();
        for (Pizza pizza : pizzaList) {
            List<Product> components = pizza.getComponents();
            for(Product component: components){
                pizzaComponentIds.add(component.getProductId());
            }
        }
        for (Product product: productList){
            int id = product.getProductId();
            if (!pizzaComponentIds.contains(id)){
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    @Override
    public List<Product> getProductsByCategoryIds(int[] categoryIds) {
        List<Product> categoryProducts = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description, " +
                "p.price, p.description, p.quantity " +
                "FROM product p " +
                "JOIN product_category pc ON p.product_category_id = pc.product_category_id " +
                "WHERE pc.product_category_id IN (:ids) " +
                "ORDER BY p.product_category_id";
        try{
            SqlParameterSource parameters = new MapSqlParameterSource("ids", categoryIds);
            SqlRowSet results = namedDb.queryForRowSet(sql, parameters);
            while(results.next()) {
                categoryProducts.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return categoryProducts;
    }

    @Override
    public List<Product> getProductsByCategoryDescription(List<String> categoryDescriptions) {
        List<Product> categoryProducts = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description, " +
                "p.price, p.description, p.quantity " +
                "FROM product p " +
                "JOIN product_category pc ON p.product_category_id = pc.product_category_id " +
                "WHERE pc.product_category_description IN (:ids) " +
                "ORDER BY p.product_category_id";

        try{
            SqlParameterSource parameters = new MapSqlParameterSource("ids", categoryDescriptions);
            SqlRowSet results = namedDb.queryForRowSet(sql, parameters);
            while(results.next()){
                categoryProducts.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return categoryProducts;
    }

    @Override
    public Product createProduct(Product product) {
        String sql = "INSERT INTO product (product_category_id, price, description, quantity) " +
                "VALUES ((SELECT product_category_id FROM product_category WHERE product_category_description = ?), " +
                "?, ?, ?) RETURNING product_id";
        int createdProductId = 0;

        try{
            createdProductId=db.queryForObject(sql,int.class,
                    product.getProductCategoryDescription(), product.getPrice(), product.getDescription(), product.getQuantity()
                    );
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return getProductById(createdProductId);
    }

    @Override
    public int deleteProductById(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        int numRowsAffected = 0;

        try{
            numRowsAffected = db.update(sql, productId);
            if(numRowsAffected == 0){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Product Not Found");
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return numRowsAffected;
    }

    @Override
    public Product updateProduct(Product product) {
        String sql = "UPDATE product SET price = ?, description = ?, quantity = ?, " +
                "product_category_id = (SELECT product_category_id FROM product_category WHERE product_category_description = ?)" +
                "WHERE product_id = ?";
        int numRowsAffected = 0;
        Product updatedProduct = null;
        System.out.println(product.getDescription());
        System.out.println(product.getPrice());
        try{
            numRowsAffected=db.update(sql,
                    product.getPrice(), product.getDescription(), product.getQuantity(), product.getProductCategoryDescription(),
                    product.getProductId()
            );
            if (numRowsAffected == 0 ){
                throw new DaoException("Product Not Found, Check Product Id");
            } else{
                updatedProduct = getProductById(product.getProductId());
            }

        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        System.out.println(updatedProduct.getDescription());
        System.out.println(updatedProduct.getPrice());
        return updatedProduct;

    }

    @Override
    public Product mapRowSet(SqlRowSet rowSet) {
        return new Product(
                rowSet.getInt("product_id"),
                rowSet.getInt("product_category_id"),
                rowSet.getString("product_category_description"),
                rowSet.getBigDecimal("price"),
                rowSet.getString("description"),
                rowSet.getInt("quantity")
        );
    }
}
