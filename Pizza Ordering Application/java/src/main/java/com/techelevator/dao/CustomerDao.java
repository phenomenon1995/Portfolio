package com.techelevator.dao;

import com.techelevator.model.Customer;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface CustomerDao {
    List<Customer> getCustomers();
    Customer getCustomerById(int id);
    Customer getCustomerByEmail(String email);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    int deleteCustomerById(int id);
    int deleteCustomerByUserId(int userId);
    public Customer mapRowSet(SqlRowSet rowSet);
}
