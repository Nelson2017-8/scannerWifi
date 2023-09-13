/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import com.google.gson.JsonObject;

/**
 *
 * @author Nelson
 */
public class deviceTable {
    private final String SQLdelete = "DELETE FROM devices WHERE id=%d;";
    private final String SQLinsert = "INSERT INTO devices (nombre, mac, ip, host, favorito, permitir) VALUES('%s', '%s', '%s', '%s', '%d', '%d' )";
    private final String SQLupdate = "";
    private final String SQLconsult = "SELECT * FROM devices LIMIT 1";
    
    public boolean create(String nombre, String mac, String ip, String host, int favorito, int permitir){
        Connection conexion = null;
        Statement stmt = null;
        
        try{
            conexion = conn.conectar();
            stmt = conexion.createStatement();
            String sql = SQLinsert;
            sql = String.format(sql, nombre, mac, ip, host, favorito, permitir);
            stmt.executeUpdate(sql);
            stmt.close();
            //conexion.commit();
            conn.cerrar(conexion);
            
        }catch(Exception e){
            System.err.println("Error de activacion");
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
            return false;
        }
        
        
        return true;
    }
    
    public int delete(long id){
        Connection conexion = null;
        Statement stmt = null;
        try{
            conexion = conn.conectar();
            stmt = conexion.createStatement();
            String sql = SQLdelete;
            sql = String.format(sql, id);
            stmt.executeUpdate(sql);
            stmt.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
        return 0;
    }

    public JsonObject all(){
        Connection conexion = null;
        Statement stmt = null;
        JsonObject data = new JsonObject();
        
        try{
            conexion = conn.conectar();
            stmt = conexion.createStatement();
            String sql = SQLconsult;
            ResultSet rs = stmt.executeQuery(sql);
            
            while( rs.next() ){
                data.addProperty("id", rs.getInt("id"));
                data.addProperty("nombre", rs.getString("nombre"));
                data.addProperty("mac", rs.getString("mac"));
                data.addProperty("ip", rs.getString("ip"));
                data.addProperty("host", rs.getString("host"));
                data.addProperty("favorito", rs.getInt("favorito"));
                data.addProperty("permitir", rs.getInt("permitir"));
            }
            rs.close();
            stmt.close();
            conexion.close();
            
        }catch(Exception e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage() );
        }
        
        
        return data;
    }
}
