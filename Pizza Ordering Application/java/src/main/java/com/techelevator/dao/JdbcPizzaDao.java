package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Pizza;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPizzaDao implements PizzaDao{
    private final JdbcTemplate db;
    private final ProductDao productDao;

    public JdbcPizzaDao(JdbcTemplate jdbcTemplate, ProductDao productDao){
        this.db = jdbcTemplate;
        this.productDao = productDao;
    }
    @Override
    public List<Pizza> getPizzas() {
        String sql = "SELECT invoice_id, pizza_id, pizza_name, total, additional_instructions FROM pizza pi ";
        /*String sql = "SELECT p.product_id, p.product_category_id, pc.product_category_description,p.price, " +
                "p.description, pi.pizza_id, pi.invoice_id,pi.pizza_name, pi.total, pi.additional_instructions, p.quantity " +
                "FROM pizza pi " +
                "JOIN pizza_product pp ON pi.pizza_id = pp.product_id  " +
                "JOIN product p ON pp.product_id = p.product_id JOIN product_category pc ON p.product_category_id = pc.product_category_id  " +
                "ORDER BY pi.pizza_id;";

         */
        List<Pizza> allPizzas = new ArrayList<>();

        try {
            SqlRowSet results = db.queryForRowSet(sql);
            while(results.next()){
                allPizzas.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return allPizzas;
    }

    @Override
    public Pizza getPizzaById(int id) {
        String sql ="SELECT " +
                "pi.pizza_id, pi.invoice_id, " +
                "pi.pizza_name, pi.total, pi.additional_instructions FROM pizza pi " +
                "WHERE pi.pizza_id = ? ";
        Pizza pizza = null;
        try {
            SqlRowSet result = db.queryForRowSet(sql, id);
            if (result.next()){
                pizza = mapRowSet(result);
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return pizza;
    }

    @Override
    public List<Pizza> getPizzasByInvoiceId(int invoiceId) {
        List<Pizza> pizzas = new ArrayList<>();
        String sql = "SELECT pi.pizza_id, pi.invoice_id, " +
                "pi.pizza_name, pi.total, pi.additional_instructions FROM pizza pi " +
                "WHERE invoice_id = ? ";
        try{
            SqlRowSet results = db.queryForRowSet(sql, invoiceId);
            while(results.next()){
                pizzas.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return pizzas;
    }

    @Override
    public Pizza createPizza(Pizza pizza) {
        String sql = "INSERT INTO pizza (invoice_id, pizza_name, total, additional_instructions) " +
                "VALUES (?, ?, ?, ?) RETURNING pizza_id";
        int createdPizzaId = 0;

        try {
            createdPizzaId = db.queryForObject(sql, int.class,
                    pizza.getInvoiceId(), pizza.getPizzaName(), pizza.getTotal(),
                    pizza.getAdditionalInstructions());
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        Pizza createdPizza = getPizzaById(createdPizzaId);

        return createdPizza ;
    }

    @Override
    public void createPizzaProduct(int pizzaId, int productId) {
        String sql = "INSERT INTO pizza_product (pizza_id, product_id) " +
                "VALUES (?,?) ";
        try{
            db.update(sql, pizzaId, productId);
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
    }

    @Override
    public Pizza updatePizza(Pizza pizza) {
        String sql = "UPDATE pizza " +
                "SET invoice_id = ?, pizza_name = ?, total = ?, additional_instructions = ? " +
                "WHERE pizza_id = ?";
        int numRowsAffected = 0;
        try {
            numRowsAffected = db.update(sql, pizza.getInvoiceId(), pizza.getPizzaName(),
                    pizza.getTotal(), pizza.getAdditionalInstructions(), pizza.getPizzaId());
            if (numRowsAffected == 0){
                throw new DaoException("No matching Pizza found, check Pizza ID");
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return getPizzaById(pizza.getPizzaId());
    }

    @Override
    public int deletePizzaById(int id) {
        String sql = "DELETE FROM pizza WHERE pizza_id = ?";
        int numRowsAffected = 0;
        try {
            numRowsAffected = db.update(sql, id);
            if (numRowsAffected == 0 ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invoice Not Found");
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return numRowsAffected;
    }

    @Override
    public Pizza updatePizzaComponents(Pizza pizza) {
        String sql = "SELECT product_id FROM pizza_product " +
                "WHERE pizza_id = ?";

        try {
            SqlRowSet results = db.queryForRowSet(sql, pizza.getPizzaId());
            while(results.next()){
                pizza.addComponent(productDao.getProductById(results.getInt("product_id")));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return pizza;
    }

    @Override
    public Pizza mapRowSet(SqlRowSet rowSet) {
        Pizza pizza =  new Pizza(
                rowSet.getInt("pizza_id"),
                rowSet.getInt("invoice_id"),
                rowSet.getString("pizza_name"),
                rowSet.getBigDecimal("total"),
                rowSet.getString("additional_instructions")
                );
        pizza = updatePizzaComponents(pizza);

        return pizza;
    }
}
