package com.techelevator.dao;

import com.techelevator.model.Customer;
import com.techelevator.model.Pizza;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface PizzaDao {
    List<Pizza> getPizzas();
    Pizza getPizzaById(int id);
    List<Pizza> getPizzasByInvoiceId(int invoiceId);
    Pizza createPizza(Pizza pizza);
    void createPizzaProduct(int pizzaId, int productId);
    Pizza updatePizza(Pizza pizza);
    int deletePizzaById(int id);
    Pizza updatePizzaComponents(Pizza pizza);
    public Pizza mapRowSet(SqlRowSet rowSet);
}
