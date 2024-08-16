package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Customer;
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
public class JdbcCustomerDao implements CustomerDao{
    private final JdbcTemplate db;
    private final UserDao userDao;
    private final String SELECT_SQL_BASE = "SELECT customer_id, first_name, last_name, street_address, city, zip_code, " +
            " state_abbreviation, phone_number, email, user_id FROM customer ";

    public JdbcCustomerDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.db = jdbcTemplate;
        this.userDao = userDao;
    }
    @Override
    public List<Customer> getCustomers() {
        List<Customer> allCustomers = new ArrayList<>();
        String sql = SELECT_SQL_BASE + " ORDER BY first_name";
        try {
            SqlRowSet results = db.queryForRowSet(sql);
            while (results.next()){
                allCustomers.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return allCustomers;
    }

    @Override
    public Customer getCustomerById(int id) {
        Customer customer = null;
        String sql = SELECT_SQL_BASE + "WHERE customer_id  = ?";
        try {
            SqlRowSet result = db.queryForRowSet(sql, id);
            if (result.next()){
                customer = mapRowSet(result);
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return customer;
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        Customer customer = null;
        String sql = SELECT_SQL_BASE + "WHERE email = ?";
        try {
            SqlRowSet result = db.queryForRowSet(sql, email);
            if (result.next()) {
                customer = mapRowSet(result);
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return customer;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        int newCustomerId = 0;
        String sql = "INSERT INTO customer (first_name, last_name, street_address, city, " +
                "zip_code, state_abbreviation, phone_number, email, user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING customer_id";
        try {
            newCustomerId = db.queryForObject(sql, int.class,
                    customer.getFirstName(), customer.getLastName(), customer.getStreetAddress(),
                    customer.getCity(), customer.getZipcode(), customer.getStateAbbreviation(),
                    Long.parseLong(customer.getPhoneNumber()),
                    customer.getEmail(), customer.getUserId());

        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        } catch (NullPointerException np){
            System.out.println("Null Pointer Exception");
        }

        return getCustomerById(newCustomerId);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        Customer updatedCustomer = null;
        String sql = "UPDATE customer " +
                "SET first_name = ?, last_name = ?, street_address = ?, city = ?, zip_code = ?, " +
                "state_abbreviation = ?, phone_number = ?::numeric, email = ?, user_id = ? " +
                "WHERE customer_id = ?";
        try {
            int rowsAffected = db.update(sql, customer.getFirstName(), customer.getLastName(), customer.getStreetAddress(),
                    customer.getCity(), customer.getZipcode(), customer.getStateAbbreviation(), customer.getPhoneNumber(),
                    customer.getEmail(), customer.getUserId(), customer.getCustomerId());

            if (rowsAffected == 0) {
                throw new DaoException("Zero rows affected, expected at least one");
            } else {
                updatedCustomer = getCustomerById(customer.getCustomerId());
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return updatedCustomer;
    }

    @Override
    public int deleteCustomerById(int id) {
        String sql = "DELETE FROM customer WHERE customer_id = ?";
        int numRowsAffected = 0;
        try{
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
    public int deleteCustomerByUserId(int userId) {
        String sql = "DELETE FROM customer WHERE user_id = ?";
        int numRowsAffected = 0;
        try{
            numRowsAffected = db.update(sql, userId);
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
    public Customer mapRowSet(SqlRowSet rowSet){
        Customer mappedCustomer = new Customer(
                rowSet.getInt("customer_id"),
                rowSet.getString("first_name"),
                rowSet.getString("last_name"),
                rowSet.getString("street_address"),
                rowSet.getString("city"),
                rowSet.getInt("zip_code"),
                rowSet.getString("state_abbreviation"),
                rowSet.getString("phone_number"),
                rowSet.getString("email"),
                rowSet.getInt("user_id")
        );

        mappedCustomer.setUser(userDao.getUserById(mappedCustomer.getUserId()));
        return mappedCustomer;
    }
}

