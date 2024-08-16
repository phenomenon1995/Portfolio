package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Authority;
import com.techelevator.model.Invoice;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class JdbcInvoiceDao implements InvoiceDao{
    private final JdbcTemplate db;

    public JdbcInvoiceDao(JdbcTemplate jdbcTemplate) {
        this.db = jdbcTemplate;
    }

    @Override
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT invoice_id, user_id, total, is_delivery, status, timestamp FROM invoice " +
                "WHERE invoice_id = ?";
        Invoice invoice = null;
        try{
            SqlRowSet result = db.queryForRowSet(sql, id);
            if(result.next()){
                invoice = mapRowSet(result);
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return invoice;
    }

    @Override
    public List<Invoice> getInvoicesByUserId(int id) {
        String sql = "SELECT invoice_id, user_id, total, is_delivery, status, timestamp FROM invoice " +
                "WHERE user_id = ?" +
                " ORDER BY invoice_id";
        List<Invoice> customerInvoices = new ArrayList<>();
        try{
            SqlRowSet results = db.queryForRowSet(sql, id);
            while (results.next()){
                customerInvoices.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return customerInvoices;
    }

    @Override
    public List<Invoice> getInvoices(String from, String to, User user) {
        String username = user.getUsername();
        System.out.println(username);
        List<Invoice> invoices = new ArrayList<>();
        List<Integer> fromInts = new ArrayList<>();
        List<Integer> toInts = new ArrayList<>();
        String sql = "SELECT invoice_id, invoice.user_id, total, is_delivery, status, timestamp" +
                " FROM invoice " +
                "JOIN customer c ON invoice.user_id = c.user_id ";
        if(!from.equals("0")) {
            for (String s : from.split("-")) {
                fromInts.add(Integer.parseInt(s));
            }
        }
        if (!to.equals("0")) {
            for (String s : to.split("-")) {
                toInts.add(Integer.parseInt(s));
            }
        }

        SqlRowSet results = null;
        try{

            if(!from.equals("0") && !to.equals("0")){
                sql+="WHERE timestamp BETWEEN make_date(?,?,?) AND make_date(?,?,?) ORDER BY invoice_id DESC, status DESC;";
                results = db.queryForRowSet(sql,
                        fromInts.get(0),fromInts.get(1),fromInts.get(2),
                        toInts.get(0), toInts.get(1), toInts.get(2));
            } else if (!from.equals("0") && to.equals("0")) {
                sql += "WHERE timestamp >= make_date(?,?,?) ORDER BY invoice_id DESC, status DESC;";
                results = db.queryForRowSet(sql,
                        fromInts.get(0),fromInts.get(1),fromInts.get(2));
            }else if (from.equals("0") && !to.equals("0")){
                sql += "WHERE timestamp <= make_date(?,?,?) ORDER BY invoice_id DESC, status DESC;";
                results = db.queryForRowSet(sql,
                        toInts.get(0), toInts.get(1), toInts.get(2));
            } else {
                sql += " ORDER BY invoice_id DESC, status DESC;";
                results = db.queryForRowSet(sql);
            }

            while(results.next()) {
                Set<Authority> authorities = user.getAuthorities();
                List<String> authorityNames = new  ArrayList<>();
                for (Authority a: authorities){
                    authorityNames.add(a.getName());
                }
                if(authorityNames.contains("ROLE_ADMIN")){
                    invoices.add(mapRowSet(results));
                } else {
                    if(results.getInt("user_id") == user.getId()){
                        invoices.add(mapRowSet(results));
                    }
                }
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return invoices;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        String sql = "INSERT INTO invoice (user_id, total, is_delivery, status) " +
                "VALUES (?, ?, ?, ?) RETURNING invoice_id";
        int createdInvoiceId;
        try{
             createdInvoiceId = db.queryForObject(sql, int.class,
                    invoice.getUserId(), invoice.getTotal(), invoice.isDelivery(), invoice.getStatus());
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return getInvoiceById(createdInvoiceId);
    }

    @Override
    public Invoice updateInvoice(Invoice invoice) {
        String sql = "UPDATE invoice " +
                "SET user_id = ?, total = ?, is_delivery = ?, status = ?, timestamp = ? " +
                "WHERE invoice_id = ?";
        int numRowsAffected = 0;
        try {
            numRowsAffected = db.update(sql, invoice.getUserId(), invoice.getTotal(),
                    invoice.isDelivery(), invoice.getStatus(), invoice.getTimestamp(), invoice.getInvoiceId());
            if (numRowsAffected == 0){
                throw new DaoException("No matching Invoice found, check Invoice ID");
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return getInvoiceById(invoice.getInvoiceId());
    }

    @Override
    public List<Invoice> getInvoiceByStatus(String status) {

        String sql = "SELECT invoice_id, user_id, total, is_delivery, status, timestamp FROM invoice " +
                "WHERE status = ?" +
                " ORDER BY invoice_id";
        List<Invoice> statusInvoices = new ArrayList<>();
        try{
            SqlRowSet results = db.queryForRowSet(sql, status);
            while (results.next()){
                statusInvoices.add(mapRowSet(results));
            }
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
        return statusInvoices;
    }

    @Override
    public void createInvoiceProduct(int invoiceId, int productId) {
        String sql = "INSERT INTO invoice_product (invoice_id, product_id) " +
                "VALUES (?,?) ";
        try{
            db.update(sql, invoiceId, productId);
        } catch (DataIntegrityViolationException dive) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, dive.getMessage());
        } catch (CannotGetJdbcConnectionException cgjce) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, cgjce.getMessage());
        }
    }

    @Override
    public int deleteInvoiceById(int id) {
        String sql = "DELETE FROM invoice WHERE invoice_id = ?";
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
    public int deleteInvoicesByUserId(int id) {
        String sql = "DELETE FROM invoice WHERE user_id = ?";
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
    public Invoice mapRowSet(SqlRowSet rowSet) {
        return new Invoice(
                rowSet.getInt("invoice_id"),
                rowSet.getInt("user_id"),
                rowSet.getBigDecimal("total"),
                rowSet.getBoolean("is_delivery"),
                rowSet.getString("status"),
                rowSet.getTimestamp("timestamp")
        );
    }
}
