package com.techelevator.dao;
import com.techelevator.exception.DaoException;
import com.techelevator.model.Invoice;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.apache.tomcat.jni.Time;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class JdbcInvoiceDaoTests extends BaseDaoTests {

    static Timestamp timestamp = Timestamp.valueOf(("2022-01-15 10:30:00"));

    protected static final Invoice INVOICE_1 = new Invoice(1, 3, BigDecimal.valueOf(150.75), true, "Pending", timestamp);



    private JdbcInvoiceDao sut;


    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcInvoiceDao(jdbcTemplate);
    }

    @Test
    public void getInvoiceById_given_invalid_invoice_id_returns_null() {
        Invoice actualInvoice = sut.getInvoiceById(-1);

        Assert.assertNull(actualInvoice);
    }

    @Test
    public void getInvoiceById_given_valid_invoice_id_returns_invoice(){
        int id = 1;

        Invoice actualInvoice = sut.getInvoiceById(id);

        Assert.assertEquals(id, actualInvoice.getInvoiceId());
    }

    @Test
    public void getInvoicesByUserId_given_valid_id_returns_invoices(){
        int id = 3;

        List<Invoice> invoicesList = sut.getInvoicesByUserId(id);

        Assert.assertNotNull(invoicesList);
        Assert.assertEquals(3, invoicesList.size());
    }
    
    @Test(expected = ResponseStatusException.class)
    public void createInvoice_with_invalid_data_returns_bad_request_status_exception(){
        Invoice invoice = new Invoice();
        Assert.assertNull(sut.createInvoice(invoice));
    }

    @Test(expected = ResponseStatusException.class)
    public void createInvoice_with_valid_data_returns_invoice(){
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(20);
        invoice.setTotal(new BigDecimal(25.25));
        invoice.setDelivery(true);
        invoice.setStatus("Pending");
        invoice.setUserId(20);
        invoice.setTimestamp(timestamp);

        Invoice newInvoice = sut.createInvoice(invoice);

        Assert.assertEquals(invoice.getInvoiceId(), newInvoice.getInvoiceId());
        Assert.assertEquals(invoice.getTotal(), newInvoice.getTotal());
        Assert.assertEquals(invoice.getStatus(), newInvoice.getStatus());
        Assert.assertEquals(invoice.getUserId(), newInvoice.getUserId());
        Assert.assertEquals(invoice.getTimestamp(), newInvoice.getTimestamp());

    }

    @Test(expected = ResponseStatusException.class)
    public void deleteInvoiceById_returns_Response_Status_Exception_with_invalidId(){
        int id = -1;
        sut.deleteInvoiceById(id);
    }

    @Test(expected = ResponseStatusException.class)
    public void deleteInvoicesByUserId_returns_Response_Status_Exception_with_invalidId(){
        int id = -1;
        sut.deleteInvoicesByUserId(id);
    }

    @Test
    public void updateInvoice_returns_correct_number_of_rows_affected(){
        Invoice invoice = sut.getInvoiceById(1);
        invoice.setInvoiceId(10);
        invoice.setTotal(BigDecimal.valueOf(25.25));
        invoice.setDelivery(true);
        invoice.setStatus("Pending");
        invoice.setUserId(5);
        invoice.setTimestamp(timestamp);

        Invoice newInvoice = sut.updateInvoice(invoice);

        Assert.assertEquals(BigDecimal.valueOf(25.25), newInvoice.getTotal());
        Assert.assertEquals(invoice.getStatus(), newInvoice.getStatus());
        Assert.assertEquals(invoice.getUserId(), newInvoice.getUserId());
        Assert.assertEquals(invoice.getInvoiceId(), newInvoice.getInvoiceId());
        Assert.assertEquals(invoice.getTimestamp(), newInvoice.getTimestamp());


    }
}