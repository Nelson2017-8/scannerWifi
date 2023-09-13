/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import com.google.gson.JsonObject;
import java.net.UnknownHostException;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import static org.jnetpcap.protocol.JProtocol.ARP;

/**
 *
 * @author Nelson
 * 
 */

public class EscanerRed {
    //public static final String networkAddress = "172.26.144.";
    public String networkAddress = "192.168.0.";
    public JTable tabla;
    public DefaultTableModel modelo;
    public JsonObject data;
    
    public String convertirIpv4toString(short prflen){
        int shft = 0xffffffff<<(32-prflen);
        int oct1 = ((byte) ((shft&0xff000000)>>24)) & 0xff;
        int oct2 = ((byte) ((shft&0x00ff0000)>>16)) & 0xff;
        int oct3 = ((byte) ((shft&0x0000ff00)>>8)) & 0xff;
        int oct4 = ((byte) (shft&0x000000ff)) & 0xff;
        String submask = oct1+"."+oct2+"."+oct3+"."+oct4;
        return submask;
    }
    public InetAddress getIPv4LocalNetMask(InetAddress ip, int netPrefix) {

        try {
            // Since this is for IPv4, it's 32 bits, so set the sign value of
            // the int to "negative"...
            int shiftby = (1<<31);
            // For the number of bits of the prefix -1 (we already set the sign bit)
            for (int i=netPrefix-1; i>0; i--) {
                // Shift the sign right... Java makes the sign bit sticky on a shift...
                // So no need to "set it back up"...
                shiftby = (shiftby >> 1);
            }
            // Transform the resulting value in xxx.xxx.xxx.xxx format, like if
            /// it was a standard address...
            String maskString = Integer.toString((shiftby >> 24) & 255) + "." + Integer.toString((shiftby >> 16) & 255) + "." + Integer.toString((shiftby >> 8) & 255) + "." + Integer.toString(shiftby & 255);
            // Return the address thus created...
            return InetAddress.getByName(maskString);
        }
            catch(Exception e){e.printStackTrace();
        }
        // Something went wrong here...
        return null;
    }
    
    /**
    * Ejercicio 208: Obtener las direcciones IP de cada una de las interfaces 
    * de red del sistema.
    *
    * @author John Ortiz Ordoñez 
    * https://github.com/Fhernd/Java-Ejercicios/blob/master/JavaEjercicios/src/main/java/ejercicio0000208/IpInterfacesRed.java
    */
    public void getAddressIpInterfaceSystem() {

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            
            while(interfaces.hasMoreElements()){
                
                NetworkInterface interfaz = interfaces.nextElement();
                Enumeration<InetAddress> direcciones = interfaz.getInetAddresses();
                
                while(direcciones.hasMoreElements()){
                    
                    InetAddress direccion = direcciones.nextElement();
                    
                    if (direccion instanceof Inet4Address && !direccion.isLoopbackAddress()){
                        System.out.println(direccion);
                    }
                }
            }
            
        } catch (SocketException e) {
            System.err.println("Error -> " + e.getMessage());
        }
    }
    
    public String getMacAddress(String ipAddress) throws Exception {
        Process process = Runtime.getRuntime().exec("arp -a " + ipAddress);
        StringBuilder output = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = process.getInputStream().read(buffer)) != -1) {
            output.append(new String(buffer, 0, bytesRead));
        }
        String result = output.toString();
        //System.out.println(result);
        int index = result.indexOf(ipAddress);
        //System.out.println("index: " + index);
        
        // Patrón de expresión regular
        Pattern pattern = Pattern.compile("([0-9a-zA-Z\\-]){17}\\s");

        // Matcher para la cadena de caracteres
        Matcher matcher = pattern.matcher(result);

        // Iterar sobre los matches
        while (matcher.find()) {
            // Obtener el valor del match
            String match = matcher.group(0);

            // Imprimir el valor del match
            //System.out.println(match);
            return match;
        }
        
        //if (index != -1) {
            //index -= 29; // longitud de la dirección IP y los espacios en blanco
            //var tmp = index + 51;
        //    var tmp = index+;
        //    return result.substring(tmp, tmp + 17); // longitud de la dirección MAC
        //}
        return null;
    }
    
    public JsonObject getInfDevice(String ipAddress) throws UnknownHostException, Exception{
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            JsonObject data = new JsonObject();
            
            System.out.println("   - Dirección IP " + ipAddress);
            System.out.println("   - Nombre de host: " + inetAddress.getHostName());
            System.out.println("   - Dirección MAC: " + getMacAddress(ipAddress));
            
            data.addProperty("host", inetAddress.getHostName() );
            data.addProperty("ip", ipAddress );
            data.addProperty("mac", getMacAddress(ipAddress) );
            
            return data;
            
        } catch (Exception e) {
            System.err.println("Error -> " + e.getMessage());
        }
        
        return null;
        
    }
    
    public JsonObject calRangeAddressIp() throws UnknownHostException, IOException, Exception{
        
        int start = 0;
        int end = 100;
        JsonObject data = new JsonObject();
        List<String> users = new ArrayList<>();
        for(int i = start; i <= end; i++){
            String ipAddress = networkAddress + i;
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            if(inetAddress.isReachable(250)){
                System.out.println(ipAddress + " es real");
                users.add(ipAddress);
            }else{
                System.out.println(ipAddress + " not es real");
            }
        }
        
        System.out.println("\n\n\n");
        System.out.println("Encontrados: " + users.size());
        System.out.println("Dispositivos:");
        for (String user : users) {
            System.out.println("\n* " + user);
            JsonObject dataUser = getInfDevice(user);
            data.add(String.format("%s", user), dataUser);
        }
        
        return data;
    }
    
    public void ActualizarTablaEnTiempoReal() {
        
        modelo = new DefaultTableModel();
        modelo.addColumn("Estado");
        modelo.addColumn("Nombre");
        modelo.addColumn("IP");
        modelo.addColumn("Dirección MAC");
        modelo.addColumn("Favorito");
        modelo.addColumn("Permitido");

        tabla.setModel(modelo);
        data = new JsonObject();

        // Crear un hilo separado para realizar la búsqueda de resultados
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                int start = 1;
                int end = 255;
                for(int i = start; i <= end; i++){
                    String ipAddress = networkAddress + i;
                    InetAddress inetAddress = null;
                    try {
                        inetAddress = InetAddress.getByName(ipAddress);
                    } catch (UnknownHostException ex) {
                        Logger.getLogger(EscanerRed.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        if(inetAddress.isReachable(500)){
                            System.out.println(ipAddress + " es real");
                            JsonObject dataUser = null;
                            try {
                                dataUser = getInfDevice(ipAddress);
                            } catch (Exception ex) {
                                Logger.getLogger(EscanerRed.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String name = dataUser.get("host").getAsString();
                            System.err.println(dataUser.get("mac"));
                            String mac = ( dataUser.get("mac").toString().equals("null") ) ? "------" : dataUser.get("mac").getAsString();
                            
                            // Actualizar la tabla en tiempo real utilizando el método addRow del DefaultTableModel
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    Object[] tmp = new Object[] { "Abierto", name, ipAddress, mac, "No", "No" };
                                    modelo.addRow(tmp);
                                    JsonObject dataTmp = new JsonObject();
                                    dataTmp.addProperty("state", "En uso");
                                    dataTmp.addProperty("name", name);
                                    dataTmp.addProperty("IP", ipAddress);
                                    dataTmp.addProperty("mac", mac);
                                    dataTmp.addProperty("permitido", "no");
                                    dataTmp.addProperty("favorito", "no");
                                    data.add(ipAddress, dataTmp);
                                }
                            });
                        }else{
                            System.out.println(ipAddress + " not es real");
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    //modelo.addRow(new Object[] { "Cerrado", "-------", ipAddress, "-------","-------", "-------" });
                                    JsonObject dataTmp = new JsonObject();
                                    dataTmp.addProperty("state", "No usado");
                                    dataTmp.addProperty("name", "");
                                    dataTmp.addProperty("IP", ipAddress);
                                    dataTmp.addProperty("mac", "");
                                    dataTmp.addProperty("permitido", "");
                                    dataTmp.addProperty("favorito", "");
                                    data.add(ipAddress, dataTmp);
                                }
                            });
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(EscanerRed.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        hilo.start();
    }
    
    
    
    
    
    
}
