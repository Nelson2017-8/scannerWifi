/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Nelson
 */
public class conn {
    // ruta
    private static final String root = "database.db";
    
    // conexion
    public static Connection conectar(){
        Connection connection = null;
        try{
            // carga el controlador 
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + root);
        }catch(ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
        return connection;
    }
    
    // metodo para ejecutar una consulta SQL
    public static ResultSet consulta(String consulta){
        ResultSet resultSet = null;
        try(Connection connection = conectar()){
            Statement statement = connection.createStatement();
            statement.executeQuery(consulta);
        }catch(SQLException e){
            e.printStackTrace();
        }
        return resultSet;
    }
    
    // cerrar base de datos
    public static void cerrar(Connection connection){
        try{
            if(connection != null){
                connection.close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
