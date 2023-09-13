/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;

import controllers.DeviceDetector;
import controllers.EscanerRed;
import controllers.InformacionInterfazRed;
import java.io.IOException;
import ui.main;

/**
 *
 * @author Nelson
 */
public class run {
    public static void main(String[] args) throws IOException, Exception {
        EscanerRed scanner = new EscanerRed();
        //System.out.println("Hola mundo");
        //EscanerRed.getAddressIpCurrent();
        //NetworkAddress.run();
        //EscanerRed.getInfDevice("192.168.0.100");
        //DeviceDetector.main(args);
        scanner.getInfDevice("192.168.0.100");
        main.main(args);
        //EscanerRed.calRangeAddressIp();
        //EscanerRed.prueba1();
        //EscanerRed.getAddressIpInterfaceSystem();
        //InformacionInterfazRed.main(args);
    }
}
