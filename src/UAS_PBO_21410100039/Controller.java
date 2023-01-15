/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package UAS_PBO_21410100039;

import java.sql.SQLException;
import pbo_mysql.View;

/**
 *
 * @author Aditya Kesuma
 */
public interface Controller {
    
// Login Validator

    
// Pasien
    public void pasienCreate(DaftarPasien v) throws SQLException;
    public void pasienRead(DaftarPasien v) throws SQLException;
    public void pasienUpdate(DaftarPasien v) throws SQLException;
    public void pasienDelete(DaftarPasien v) throws SQLException;
    
// Rekam Medis
    public void rekamMedisCreate(KeluhanPasien v) throws SQLException;
    public void rekamMedisRead(KeluhanPasien v, int id) throws SQLException;
//    public void pasienUpdate(DaftarPasien v) throws SQLException;
//    public void pasienDelete(DaftarPasien v) throws SQLException;
 
    
    // Invoices
    public void invoiceCreate(KeluhanPasien v)throws SQLException;
    public void invoiceRead(Finance v)throws SQLException;
    public void invoiceUpdate(Finance v)throws SQLException;
    
    
    public void detailInvoiceCreate(KeluhanPasien v)throws SQLException;
    public void detailInvoiceRead(detailInvoice v, int id) throws SQLException;
    
    // Employees
    public void employeesRead(Employees v)throws SQLException;
    public void employeesUpdate(Employees v)throws SQLException;
    public void employeesCreate(Employees v)throws SQLException;
    
    // Items
    public void itemsRead(Items v)throws SQLException;
    public void itemsCreate(Items v)throws SQLException;
    public void itemsUpdate(Items v)throws SQLException;
    public void itemsDelete(Items v)throws SQLException;
    
}
