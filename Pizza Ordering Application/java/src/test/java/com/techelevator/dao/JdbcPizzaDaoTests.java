package com.techelevator.dao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Pizza;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

public class JdbcPizzaDaoTests extends BaseDaoTests {

    private JdbcPizzaDao sut;
    private JdbcProductDao productDao;
    private NamedParameterJdbcTemplate namedTemplate;
    private JdbcTemplate jdbcTemplate;
    @Before
    public void setup() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedTemplate = new NamedParameterJdbcTemplate(dataSource);
        productDao = new JdbcProductDao(jdbcTemplate, namedTemplate);
        sut = new JdbcPizzaDao(jdbcTemplate,productDao);
        System.out.println(jdbcTemplate);
        System.out.println(productDao);
        System.out.println(namedTemplate);
        System.out.println(sut);

    }

    @Test
    public void t01_getPizzas_returns_list_of_pizzas(){
        List<Pizza> pizzas = sut.getPizzas();
        System.out.println(productDao.getCategories());
        System.out.println(pizzas);
        Assert.assertTrue("getPizzas didn't return list of pizzas", pizzas.size() > 0);
        Pizza pizza = pizzas.get(0);
        Assert.assertEquals("pizzaId  didn't match expected result", 100, pizza.getPizzaId() );
        Assert.assertEquals("invoice_id  didn't match expected result", 1, pizza.getInvoiceId() );
        Assert.assertEquals("pizza_name  didn't match expected result", "Custom Pizza 1", pizza.getPizzaName() );
        Assert.assertEquals("total  didn't match expected result", 0, pizza.getTotal().compareTo(BigDecimal.valueOf(10.00)) );
        Assert.assertEquals("additional_instructions  didn't match expected result", 100, pizza.getPizzaId() );
    }

    @Test
    public void t02_getPizzaById_returns_correct_pizza_when_valid_id_given(){
        //(1,'Custom Pizza 1', 10, 'None')
        int testId  = 100;
        Pizza actual = sut.getPizzaById(testId);
        Assert.assertEquals(testId,actual.getPizzaId());
        Assert.assertEquals(1,actual.getInvoiceId());
        Assert.assertEquals("Custom Pizza 1",actual.getPizzaName());
        Assert.assertEquals(0,actual.getTotal().compareTo(BigDecimal.TEN));
        Assert.assertEquals("None",actual.getAdditionalInstructions());
    }
    @Test
    public void t03_getPizzaById_returns_data_integrity_violation_exception_when_invalid_id_given() {
        //(1,'Custom Pizza 1', 10, 'None')
        int testId = -1;
        Assert.assertNull( sut.getPizzaById(testId));
        testId = 1;
        Assert.assertNull(sut.getPizzaById(testId));

    }
    @Test
    public void t04_createPizza_creates_and_returns_pizza_from_valid_pizza_object(){
        Pizza newPizza = new Pizza();
        newPizza.setPizzaName("Test Pizza");
        newPizza.setInvoiceId(1);
        newPizza.setAdditionalInstructions("Make sure this works");
        newPizza.setTotal(BigDecimal.valueOf(20.00));

        Pizza createdPizza = sut.createPizza(newPizza);

        Assert.assertTrue("Pizza names didn't match",newPizza.getPizzaName().equals(createdPizza.getPizzaName()));
        Assert.assertTrue("Invoice Ids didn't match",newPizza.getInvoiceId() == createdPizza.getInvoiceId());
        Assert.assertTrue("Additional instructions didn't match",newPizza.getAdditionalInstructions().equals(createdPizza.getAdditionalInstructions()));
        Assert.assertTrue("Totals didn't match",newPizza.getTotal().compareTo(createdPizza.getTotal()) == 0 );


    }
    @Test(expected = ResponseStatusException.class)
    public void t05_createPizza_creates_and_returns_bad_request_status_exception_from_invalid_pizza_object() {
        Pizza newPizza = new Pizza();
        Assert.assertNull(sut.createPizza(newPizza));
    }

    @Test(expected = ResponseStatusException.class)
    public void t06_deletePizza_throws_response_status_exception_with_invalid_id(){
        int testId = -1;
        sut.deletePizzaById(testId);
    }
    @Test
    public void t07_deletePizza_returns_correct_number_of_rows_affected_with_valid_id(){
        int testId = 100;
        Assert.assertEquals(1, sut.deletePizzaById(testId));
        Assert.assertNull(sut.getPizzaById(testId));
    }
    @Test
    public void t08_updatePizza_returns_correct_number_of_rows_affected_with_valid_pizza(){
        Pizza pizza = sut.getPizzaById(100);
        System.out.println(pizza);
        pizza.setTotal(BigDecimal.ZERO);
        pizza.setAdditionalInstructions("None");
        pizza.setInvoiceId(2);
        pizza.setPizzaName("A boring pizza");
        Pizza updatedPizza = sut.updatePizza(pizza);
        System.out.println(updatedPizza);

        Assert.assertEquals(0, updatedPizza.getTotal().compareTo(pizza.getTotal()));
        Assert.assertTrue(pizza.getInvoiceId() == updatedPizza.getInvoiceId());
        Assert.assertTrue(pizza.getPizzaId() == updatedPizza.getPizzaId());
        Assert.assertTrue(pizza.getPizzaName().equals(updatedPizza.getPizzaName()));

    }


}
