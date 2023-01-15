/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UAS_PBO_21410100039;

/**
 *
 * @author Aditya Kesuma
 */
import static UAS_PBO_21410100039.Model.res;
import java.sql.*;
import java.util.Stack;
import javax.swing.JOptionPane;

public class jController {

    public static void searchPasien(DaftarPasien v, String nama) throws SQLException {
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
        Model.sql = "SELECT * FROM PASIENS WHERE NAMA_LENGKAP LIKE '%" + nama + "%'";
        Model.state = Model.conn.createStatement();
        Model.res = Model.state.executeQuery(Model.sql);

        while (Model.res.next()) {
            Object[] obj = new Object[6];
            // index dimulai dari 0. tetapi hasil resultset dimulai dari 1
            obj[0] = Model.res.getString(1); // kita bisa menggunakan getInt untuk mengolah tanpa terkecuali tipe datanya nanti
            obj[1] = Model.res.getString(2);
            obj[2] = Model.res.getString(3);
            obj[3] = Model.res.getString(4);
            obj[4] = Model.res.getString(6);
            obj[5] = Model.res.getString(7);

            v.tbm.addRow(obj);
        }
    }

    public static void makeAntrians(String nama) {
        Antrians x = new Antrians(nama);
        x.setVisible(true);
        x.noUrut1.setText(Integer.toString(Model.i));
        x.noUrut.setText(nama);
    }

    public static void invoicesReadCompleted(Finance v) throws SQLException {
        int total = 0;
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
        Model.sql = "SELECT X.ID, X.ID_PASIEN, Y.NAMA_LENGKAP, X.STATUS, X.GRANDTOTAL, X.UPDATED_AT FROM INVOICES X JOIN PASIENS Y ON X.ID_PASIEN = Y.ID WHERE X.STATUS = 'Lunas'";
        Model.state = Model.conn.createStatement();
        Model.res = Model.state.executeQuery(Model.sql);

        while (Model.res.next()) {
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
        v.session.setText("Invoice Lunas (All Completed Invoices)");
        v.inputGrandTotal.setText(Integer.toString(total));
        total = 0;
    }

    public static void invoicesReadProgress(Finance v) throws SQLException {
        int total = 0;
        v.tbm.getDataVector().removeAllElements();
        v.tbm.fireTableDataChanged(); // update
        Model.sql = "SELECT X.ID, X.ID_PASIEN, Y.NAMA_LENGKAP, X.STATUS, X.GRANDTOTAL, X.UPDATED_AT FROM INVOICES X JOIN PASIENS Y ON X.ID_PASIEN = Y.ID WHERE X.STATUS = 'Belum dibayar'";
        Model.state = Model.conn.createStatement();
        Model.res = Model.state.executeQuery(Model.sql);

        while (Model.res.next()) {
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
        v.session.setText("Invoice Dalam Progress (On Progress Invoice)");
        v.inputGrandTotal.setText(Integer.toString(total));
        total = 0;
    }

    public static void debugInsert() throws SQLException {
        Model.sql = "SELECT id,id_pasien,diagnosis,penanganan FROM rekam_medis WHERE DIAGNOSIS = '-'";
        Model.state = Model.conn.createStatement();
        Model.res = Model.state.executeQuery(Model.sql);
        Stack<Integer> stackId = new Stack<>();

        int length = 0;

        String diagnosis[] = {"Sakit Panas", "Batuk-batuk", "Tipes", "Demam Berdarah", "Diabetes"};
        String penanganan[] = {"Rawat Inap", "Rawat Jalan"};
        int maxD = diagnosis.length - 1;
        int maxP = penanganan.length - 1;
        System.out.println(maxD + 1);
        System.out.println(maxP);
        int min = 0;
        int rangeD = maxD - min + 1;
        int rangeP = maxP - min + 1;
        int id = 0;
        int minA = 1;   // A = max possible item added
        int maxA = 10;
        int rangeA = maxA - minA + 1;
        int harga = 0;

        int minI = 8;   // I = max possible item added
        int maxI = 11;
        int rangeI = maxI - minI + 1;

        int hargaItem[] = {0, 0, 0, 0, 0, 0, 0, 1000, 2000, 3000, 4000, 5000};

        while (res.next()) {
            length++;
            id = res.getInt(1);
            stackId.push(id);

            Model.sql = "UPDATE `REKAM_MEDIS` SET DIAGNOSIS = ?, PENANGANAN = ? WHERE id = ?";

            Model.prep = Model.conn.prepareStatement(Model.sql);

            Model.prep.setString(1, diagnosis[((int) (Math.random() * rangeD) + min)]);
            Model.prep.setString(2, penanganan[((int) (Math.random() * rangeP) + min)]);
            Model.prep.setInt(3, id);

            Model.prep.executeUpdate();

            for (int i = 0; i < ((int) (Math.random() * rangeA) + min); i++) {
                Model.sql = "INSERT INTO `detail_invoices`(`id`, `id_invoice`, `id_item`, `kuantitas`, `total`, `created_at`, `updated_at`) "
                        + "VALUES(null," + id + ",?,?,?,SYSDATE(),SYSDATE())";
                // 1 = item ID
                int itemId = ((int) (Math.random() * rangeI) + minI);
                int qty = ((int) (Math.random() * rangeA) + minA);
                int hargaTemp = hargaItem[itemId] * qty;
                Model.prep = Model.conn.prepareStatement(Model.sql);
                Model.prep.setInt(1, itemId);
                Model.prep.setInt(2, qty);
                Model.prep.setInt(3, hargaTemp);

                Model.prep.executeUpdate();

                harga += hargaTemp;
                System.out.println("Harga : " + harga);
                System.out.println("HargaTemp : " + hargaTemp);

            }

            Model.sql = "SELECT grandTotal FROM INVOICES WHERE id = " + id;

            Model.state = Model.conn.createStatement();
            Model.res = Model.state.executeQuery(Model.sql);

            while (res.next()) {
                harga += res.getInt(1);
//                System.out.println(res.getInt(1));
            }

            Model.sql = "UPDATE `Invoices` SET grandTotal = ? WHERE id = " + id;

            Model.prep = Model.conn.prepareStatement(Model.sql);
            System.out.println("FINALHarga : " + harga);
            Model.prep.setInt(1, harga);

            Model.prep.executeUpdate();

            harga = 0;
        }

    }
}
