package com.mycompany.mavenproject1;
import java.net.*;
import java.nio.Buffer;
import java.io.*;
import java.sql.*;

import com.google.protobuf.Empty;

public class Main {
    
    public static void main (String args[]) {

    try {

      DatagramSocket socketUDP = new DatagramSocket(49676);
      
      while (true) {
        byte[] bufer = new byte[1000];
        // Construimos el DatagramPacket para recibir peticiones
        DatagramPacket peticion =
          new DatagramPacket(bufer, bufer.length);

        // Leemos una peticion del DatagramSocket
        socketUDP.receive(peticion);
        

        System.out.print("Datagrama recibido del host: " +
                           peticion.getAddress());
        System.out.println(" desde el puerto remoto: " +
                           peticion.getPort());
        String mensaje = new String(peticion.getData());
        System.out.println(" Ubicacion " + mensaje);
        String[] location = getLocation(mensaje);
        mensaje = "";
        saveData(location[0], location[1]);
        location[0] = "";
        location[1] = "";
        bufer = new byte[0];
        
        
        
      }

    } catch (SocketException e) {
      System.out.println("Socket: " + e.getMessage());
    } catch (IOException e) {
      System.out.println("IO: " + e.getMessage());
    }
  }
    
  public static String[] getLocation(String message){
      return message.split(",");
  }
    
  public static void saveData(String latitude, String longitude) {
    try {
      Connection con = getConnection();
      Statement stmt = con.createStatement();
      String sql = "INSERT INTO location (lon, lat, date, time) VALUES ('" + longitude + "','" + latitude + "', NOW(), NOW());";
      stmt.executeUpdate(sql);
      con.close(); 
    }
    catch(Exception e){ 
      System.out.println(e);
    }
  }

  public static Connection getConnection() {
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");  
      Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/findmytaxi?jdbcCompliantTruncation=false","root","");
      return con;
    }
    catch(Exception e){ 
      System.out.println(e);
      return null;
    }
  }
    
}
