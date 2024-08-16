package com.techelevator.dao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Customer;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public class JdbcCustomerDaoTests extends BaseDaoTests {

    private JdbcCustomerDao sut;

    private UserDao userDao;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        userDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcCustomerDao(jdbcTemplate, userDao);
    }

    @Test
    public void getCustomers_returns_all_customers(){
        List<Customer> customers = sut.getCustomers();

        Assert.assertEquals(10, customers.size());
    }

    @Test
    public void getCustomerById_returns_correct_customer(){
        int id = 1;

        Customer actualCustomer = sut.getCustomerById(id);

        Assert.assertEquals(1, actualCustomer.getCustomerId());
    }

    @Test
    public void getCustomerById_returns_null_given_invalid_id(){
        int id = -1;

        Customer actualCustomer = sut.getCustomerById(id);

        Assert.assertNull(actualCustomer);
    }

    @Test
    public void getCustomerByEmail_returns_correct_customer(){
        String email = "ligula.consectetuer.rhoncus@aol.couk";

        Customer actualCustomer = sut.getCustomerByEmail(email);

        Assert.assertEquals("ligula.consectetuer.rhoncus@aol.couk", actualCustomer.getEmail());
    }

    @Test
    public void getCustomerByEmail_returns_null_given_invalid_email(){
        String email = "consectetuer.rhoncus@aol.couk";

        Customer actualCustomer = sut.getCustomerByEmail(email);

        Assert.assertNull(actualCustomer);
    }

    @Test
    public void createCustomer_with_valid_data_returns_customer(){

      Customer customer = new Customer();
      customer.setFirstName("Jane");
      customer.setLastName("Doe");
      customer.setEmail("janedoe@gmail.com");
      customer.setPhoneNumber("1234567890");
      customer.setStateAbbreviation("AZ");
      customer.setZipcode(12345);
      customer.setCity("City");
      customer.setStreetAddress("1234 Test Drive, St");
      customer.setUser(userDao.getUserById(1));
      customer.setUserId(customer.getUser().getId());

        Customer newCustomer = sut.createCustomer(customer);

        Assert.assertEquals(customer.getEmail(), newCustomer.getEmail());
        Assert.assertEquals(customer.getStateAbbreviation(), newCustomer.getStateAbbreviation());
        Assert.assertEquals(customer.getUserId(), newCustomer.getUserId());
        Assert.assertEquals(customer.getFirstName(), newCustomer.getFirstName());
        Assert.assertEquals(customer.getLastName(), newCustomer.getLastName());
        Assert.assertEquals(customer.getUser(), newCustomer.getUser());
        Assert.assertEquals(customer.getCity(), newCustomer.getCity());
        Assert.assertEquals(customer.getStreetAddress(), newCustomer.getStreetAddress());





    }

    @Test
    public void updateCustomer_with_valid_data_returns_customer(){
        Customer customer = sut.getCustomerById(1);
        customer.setFirstName("Jane");
        customer.setLastName("Doe");
        customer.setEmail("janedoe@gmail.com");
        customer.setPhoneNumber("1234567890");
        customer.setStateAbbreviation("AZ");
        customer.setZipcode(12345);
        customer.setCity("City");
        customer.setStreetAddress("1234 Test Drive, St");
        customer.setUserId(1);

        Customer newCustomer = sut.updateCustomer(customer);

        Assert.assertEquals(customer.getCustomerId(), newCustomer.getCustomerId());
        Assert.assertEquals(customer.getEmail(), newCustomer.getEmail());
        Assert.assertEquals(customer.getStateAbbreviation(), newCustomer.getStateAbbreviation());
        Assert.assertEquals(customer.getUserId(), newCustomer.getUserId());
        Assert.assertEquals(customer.getFirstName(), newCustomer.getFirstName());
        Assert.assertEquals(customer.getLastName(), newCustomer.getLastName());
        Assert.assertEquals(customer.getCity(), newCustomer.getCity());
        Assert.assertEquals(customer.getStreetAddress(), newCustomer.getStreetAddress());





    }


    @Test(expected = ResponseStatusException.class)
    public void deleteCustomerById_returns_Response_Status_Exception_with_invalidId(){
        int id = -1;
        sut.deleteCustomerById(id);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteCustomerByUserId_returns_Response_Status_Exception_with_invalidId(){
        int id = -1;
        sut.deleteCustomerByUserId(id);
    }
}
