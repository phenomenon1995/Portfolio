package com.techelevator.dao;

import com.techelevator.model.Invoice;
import com.techelevator.model.User;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.List;

public interface InvoiceDao {
    Invoice getInvoiceById(int id);
    List<Invoice> getInvoicesByUserId(int id);

    List<Invoice> getInvoices(String from, String to, User user);
    Invoice createInvoice(Invoice invoice);
    Invoice updateInvoice(Invoice invoice);

    List<Invoice> getInvoiceByStatus(String status);
    void createInvoiceProduct(int invoiceId, int productId);
    int deleteInvoiceById(int id);
    int deleteInvoicesByUserId(int id);
    public Invoice mapRowSet(SqlRowSet rowSet);
}
