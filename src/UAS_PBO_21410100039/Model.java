/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package UAS_PBO_21410100039;

import java.sql.*;
import javax.swing.JOptionPane;
import java.lang.Math;

/**
 *
 * @author Aditya Kesuma
 */
public class Model implements Controller {

    /**
     * @param args the command line arguments
     */
    //1. panggil koneksi dari class koneksi
    public static Connection conn = Koneksi.getConn();

    //2. deklarasi class-class SQL (Statement, PreparedStatement, ResultSet, String sql)
    public static Statement state; // Eksekusi Query tanpa parameter Contohnya Select
    public static PreparedStatement prep; // Eksekusi Query dengan parameter (Insert, Update, Delete)
    public static ResultSet res; // menampung hasil kueri (select) -> loop

    //3. Deklarasi var penampung Query
    public static String sql;

    public static int i = 0;

    @Override
    public void pasienCreate(DaftarPasien v) throws SQLException {

        sql = "INSERT INTO `PASIENS`( `nama_lengkap`, `alamat`, `umur`, `created_at`, `updated_at`) VALUES (?,?,?,SYSDATE(),SYSDATE())";

        prep = conn.prepareStatement(sql);

        prep.setString(1, v.inputNamaLengkap.getText());
        prep.setString(2, v.inputAlamat.getText());
        prep.setInt(3, Integer.parseInt(v.inputUmur.getText()));

        prep.executeUpdate();

    }

    @Override
    public void pasienRead(DaftarPasien v) throws SQLException {
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
        sql = "SELECT * FROM PASIENS";
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(5);
            obj[4] = res.getString(6);

            v.tbm.addRow(obj);
        }

    }

    @Override
    public void pasienUpdate(DaftarPasien v) throws SQLException {
        sql = "UPDATE `PASIENS` SET nama_lengkap = ?, alamat = ?, umur = ?, updated_at = SYSDATE() WHERE id = ?";

        prep = conn.prepareStatement(sql);

        prep.setString(1, v.inputNamaLengkap.getText());
        prep.setString(2, v.inputAlamat.getText());
        prep.setInt(3, Integer.parseInt(v.inputUmur.getText()));
        prep.setInt(4, Integer.parseInt(v.inputID.getText()));

        prep.executeUpdate();
    }

    @Override
    public void pasienDelete(DaftarPasien v) throws SQLException {
        sql = "DELETE FROM `PASIENS` WHERE id = ?";

        prep = conn.prepareStatement(sql);

        prep.setInt(1, Integer.parseInt(v.inputID.getText()));

        prep.executeUpdate();
    }

    @Override
    public void rekamMedisCreate(KeluhanPasien v) throws SQLException {
//         String headerTable[] = {"ID", "Keluhan", "Diagnosis", "Penanganan", "Nama Dokter", "Tanggal CheckIn"};
        sql = "INSERT INTO `rekam_medis` (`id`, `id_pasien`, `keluhan`, `diagnosis`, `penanganan`, `id_dokter`, `created_at`, `updated_at`) VALUES (NULL, ?, ?, '-', '-', ?, SYSDATE(), SYSDATE())";

        prep = conn.prepareStatement(sql);
        prep.setInt(1, Integer.parseInt(v.inputID.getText()));
        prep.setString(2, v.inputKeluhan.getText());
        int max = 5;
        int min = 1;
        int range = max - min + 1;
        int rand = (int) (Math.random() * range) + min;
        prep.setInt(3, rand);

        prep.executeUpdate();
        i++;

    }

    @Override
    public void rekamMedisRead(KeluhanPasien v, int id) throws SQLException {
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
//         String headerTable[] = {"ID", "Keluhan", "Diagnosis", "Penanganan", "Nama Dokter", "Tanggal CheckIn"};
        sql = "SELECT X.ID,X.KELUHAN,X.DIAGNOSIS,X.PENANGANAN,Y.NAMA_LENGKAP,X.CREATED_AT FROM REKAM_MEDIS X JOIN EMPLOYEES Y ON X.ID_DOKTER = Y.ID WHERE X.ID_PASIEN = " + id;

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(4);
            obj[4] = res.getString(5);
            obj[5] = res.getString(6);
            v.tbm.addRow(obj);
        }

        sql = "SELECT * FROM PASIENS WHERE ID = " + id;

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            v.inputID.setText(res.getString(1));
            v.inputNamaLengkap.setText(res.getString(2));
            v.inputAlamat.setText(res.getString(3));
            v.inputUmur.setText(res.getString(5));
            v.inputDate.setText(res.getString(6));
        }

        sql = "SELECT * FROM LAYANANS";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        v.comboBoxLayanan.removeAllItems();

        while (res.next()) {
            String x = res.getString("nama_layanan");

            v.comboBoxLayanan.addItem(x);
        }
    }

    @Override
    public void invoiceCreate(KeluhanPasien v) throws SQLException {

        int maxId = 0;
        sql = "SELECT MAX(ID) FROM `rekam_medis`";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            maxId = res.getInt(1);
        }

        System.out.println(maxId);

        sql = "INSERT INTO `invoices`(`id`, `id_pasien`, `id_rekamMedis`, `status`, `grandTotal`, `created_at`, `updated_at`) VALUES (NULL,?,?,'Belum dibayar',0,SYSDATE(),SYSDATE())";

        prep = conn.prepareStatement(sql);
// id passien, id rekam medis
        prep.setInt(1, Integer.parseInt(v.inputID.getText()));
        prep.setInt(2, maxId);

        prep.executeUpdate();
    }

    @Override
    public void detailInvoiceCreate(KeluhanPasien v) throws SQLException {

        int maxId = 0;
        sql = "SELECT MAX(ID) FROM `invoices`";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            maxId = res.getInt(1);
        }

        int indexLayanan = v.comboBoxLayanan.getSelectedIndex();
        int harga = 0;
//        System.out.println("id layanan : " + v.comboBoxLayanan.getSelectedItem());
        indexLayanan++;

        sql = "SELECT * FROM `layanans`";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
//            System.out.println("id" + indexLayanan + " VS layananID" + res.getInt(1));
            if (indexLayanan == res.getInt(1)) {
                harga = res.getInt(3);
//                System.out.println("MASUKKKKKK");

            }
//            System.out.println(harga);
        }

        sql = "UPDATE `invoices` SET grandTotal = ? WHERE id = ?";

        prep = conn.prepareStatement(sql);

        prep.setInt(1, harga);
        prep.setInt(2, maxId);

        prep.executeUpdate();

        sql = "INSERT INTO `detail_invoices`(`id`, `id_invoice`, `id_item`, `kuantitas`, `total`, `created_at`, `updated_at`) VALUES(null," + maxId + ",?,1,?,SYSDATE(),SYSDATE())";

        prep = conn.prepareStatement(sql);
        prep.setInt(1, indexLayanan);
        prep.setInt(2, harga);

        prep.executeUpdate();
    }

    @Override
    public void invoiceRead(Finance v) throws SQLException {
        int total = 0;
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
//         String headerTable[] = {"ID", "Keluhan", "Diagnosis", "Penanganan", "Nama Dokter", "Tanggal CheckIn"};
        sql = "SELECT X.ID, X.ID_PASIEN, Y.NAMA_LENGKAP, X.STATUS, X.GRANDTOTAL, X.UPDATED_AT FROM INVOICES X JOIN PASIENS Y ON X.ID_PASIEN = Y.ID";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(4);
            obj[4] = res.getString(5);
            obj[5] = res.getString(6);

            total += res.getInt(5);
            v.tbm.addRow(obj);
        }
        v.session.setText("Semua Invoices (All Invoices)");
        v.inputGrandTotal.setText(Integer.toString(total));
        total = 0;
    }

    @Override
    public void invoiceUpdate(Finance v) throws SQLException {
        sql = "UPDATE `invoices` SET status = 'Lunas' WHERE id = ?";

        prep = conn.prepareStatement(sql);

        prep.setInt(1, Integer.parseInt(v.inputID.getText()));

        prep.executeUpdate();

    }

    @Override
    public void detailInvoiceRead(detailInvoice v, int id) throws SQLException {

        int total = 0;
        int vid = 0;
        vid = Integer.parseInt(Finance.inputID.getText());
        System.out.println(vid);
        v.inputIDInvoices.setText(Integer.toString(id));
        sql = "SELECT X.ID, X.DIAGNOSIS, X.PENANGANAN, Y.NAMA_LENGKAP FROM REKAM_MEDIS X JOIN EMPLOYEES Y ON X.ID_DOKTER = Y.ID JOIN INVOICES Z ON X.ID = Z.ID WHERE X.ID = " + vid;
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            v.inputID.setText(res.getString(1));
            v.inputDiagnosis.setText(res.getString(2));
            v.inputPenanganan.setText(res.getString(3));
            v.inputDokter.setText(res.getString(4));
        }

        sql = "SELECT DISTINCT X.NAMA_LENGKAP, X.ALAMAT, X.UMUR, X.CREATED_AT FROM PASIENS X JOIN INVOICES Y ON X.ID = Y.ID_PASIEN WHERE Y.ID = " + id;
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            v.inputNamaLengkap.setText(res.getString(1));
            v.inputAlamat.setText(res.getString(2));
            v.inputUmur.setText(res.getString(3));
            v.inputDate.setText(res.getString(4));
        }

        sql = "SELECT X.ID, CASE WHEN X.ID_ITEM = 0 THEN '-' ELSE Y.NAMA END, X.KUANTITAS, X.TOTAL, X.CREATED_AT FROM INVOICES P JOIN DETAIL_INVOICES X ON P.ID = X.id_invoice JOIN ITEMS Y ON X.ID_ITEM = Y.ID WHERE P.ID = " + id;
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(4);
            obj[4] = res.getString(5);

            total += res.getInt(4);
            v.tbm.addRow(obj);
        }
        v.inputTotal.setText(Integer.toString(total));
        total = 0;

    }

    @Override
    public void employeesRead(Employees v) throws SQLException {
        sql = "SELECT X.ID, X.NAMA_LENGKAP, Y.NAMA_JABATAN, X.STATUS, X.CREATED_AT FROM EMPLOYEES X JOIN JABATANS Y ON X.ID_JABATAN = Y.ID";
        state = conn.createStatement();
        res = state.executeQuery(sql);

        //id, nama, jabatan, status, created at
        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(4);
            obj[4] = res.getString(5);

            v.tbm.addRow(obj);
        }

        sql = "SELECT * FROM JABATANS";

        state = conn.createStatement();
        res = state.executeQuery(sql);

        v.jComboBox1.removeAllItems();

        while (res.next()) {
            String x = res.getString("nama_jabatan");

            v.jComboBox1.addItem(x);
        }
    }

    @Override
    public void employeesUpdate(Employees v) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void employeesCreate(Employees v) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void itemsRead(Items v) throws SQLException {
        // String headerTable[] = {"ID", "Nama", "Kategori", "Harga", "Deskripsi", "Tanggal"};

        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
//         String headerTable[] = {"ID", "Keluhan", "Diagnosis", "Penanganan", "Nama Dokter", "Tanggal CheckIn"};
        sql = "SELECT X.ID, X.NAMA, Y.NAMA_KATEGORI, X.HARGA, X.DESKRIPSI, X.CREATED_AT FROM ITEMS X JOIN KATEGORIS Y ON X.ID_KATEGORI = Y.ID";
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = res.getString(2);
            obj[2] = res.getString(3);
            obj[3] = res.getString(4);
            obj[4] = res.getString(5);
            obj[5] = res.getString(6);

            v.tbm.addRow(obj);
        }

        v.jComboBox1.removeAllItems();

        sql = "SELECT NAMA_KATEGORI FROM KATEGORIS ";
        state = conn.createStatement();
        res = state.executeQuery(sql);

        while (res.next()) {
            v.jComboBox1.addItem(res.getString(1));
        }

    }

    @Override
    public void itemsCreate(Items v) throws SQLException {
        sql = "INSERT INTO `items`(`id`, `nama`, `id_kategori`, `harga`, `deskripsi`, `created_at`, `updated_at`)"
                + " VALUES (null,?,?,?,?,SYSDATE(),SYSDATE())";
        prep = conn.prepareStatement(sql);
        prep.setString(1, v.inputNama.getText());
        prep.setInt(2, v.jComboBox1.getSelectedIndex());
        prep.setInt(3, Integer.parseInt(v.inputHarga.getText()));
        prep.setString(4, v.inputDeskripsi.getText());

        prep.executeUpdate();
    }

    @Override
    public void itemsUpdate(Items v) throws SQLException {
        sql = "UPDATE `items` SET nama = ?, id_kategori = ?, harga = ?, deskripsi = ?, updated_at = SYSDATE() WHERE ID = " + Integer.parseInt(v.inputID.getText());
        prep = conn.prepareStatement(sql);
        prep.setString(1, v.inputNama.getText());
//        System.out.println(v.jComboBox1.getSelectedIndex());
        prep.setInt(2, v.jComboBox1.getSelectedIndex() + 1);
        prep.setInt(3, Integer.parseInt(v.inputHarga.getText()));
        prep.setString(4, v.inputDeskripsi.getText());

        prep.executeUpdate();
    }

    @Override
    public void itemsDelete(Items v) throws SQLException {
        sql = "DELETE FROM ITEMS WHERE ID = " + Integer.parseInt(v.inputID.getText());
        prep = conn.prepareStatement(sql);
        prep.executeUpdate();
    }

}
