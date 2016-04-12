package com.nrholding.backend.connectors;

import java.sql.*;

/**
 * Created by pbechynak on 5.2.2016.
 */
public class OracleClient {
        public static void main(String[] argv) {
            System.out.println("-------- Oracle JDBC Connection Testing ------");
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (ClassNotFoundException e) {
                System.out.println("Where is your Oracle JDBC Driver?");
                e.printStackTrace();
                return;
            }
            System.out.println("Oracle JDBC Driver Registered!");
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(
                        "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(LOAD_BALANCE=OFF)(FAILOVER=ON)(ADDRESS=(PROTOCOL=tcp)(HOST=webcluster.prod)(PORT=1521))(ADDRESS=(PROTOCOL=tcp)(HOST=webora-dg.mall.local)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=IWPBACKEND)(FAILOVER_MODE=(TYPE=SELECT)(METHOD=BASIC)(RETRIES=20)(DELAY=15))))"
                        ,"IBP",
                        "371kQUTVbJTqLofw8EQC");
                viewTable(connection);
            } catch (SQLException e) {
                System.out.println("Connection Failed! Check outpuonsole");
                e.printStackTrace();
                return;
            }

            if (connection != null) {
                System.out.println("You made it, take control your database now!");
            } else {
                System.out.println("Failed to make connection!");
            }


        }

    public static void viewTable(Connection con)
            throws SQLException {

        Statement stmt = null;
        String query = "select PRODUCT_ID, LANGUAGE_ID, TITLE from IMWC36.product_t";

        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                String product_id = rs.getString("PRODUCT_ID");
                String language_id = rs.getString("LANGUAGE_ID");
                String title = rs.getString("TITLE");

                System.out.println(product_id + "\t" + language_id +
                        "\t" + title);
            }
        } catch (SQLException e ) {
            System.out.println(e);
        } finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    }

