/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;


public class create {
    private static final String DEVICES = "CREATE TABLE devices (\n" +
    "    id       INTEGER PRIMARY KEY,\n" +
    "    mac      TEXT,\n" +
    "    nombre,\n" +
    "    ip,\n" +
    "    host,\n" +
    "    favorito INTEGER,\n" +
    "    permitir INTEGER\n" +
    ");";
    
    private static void createTable(String TABLE) throws SQLException{
        Connection connection = conn.conectar();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(TABLE);
        }catch(SQLException e){
            e.printStackTrace();
        }finally{
            if (connection!=null){
                try{
                    connection.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            } 
        }
    }
    
    public static void database() throws SQLException {
        createTable(DEVICES);
    }
}
