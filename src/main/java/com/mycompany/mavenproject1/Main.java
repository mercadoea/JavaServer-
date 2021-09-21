package com.mycompany.mavenproject1;
import java.net.*;
import java.nio.Buffer;
import java.io.*;
import java.sql.*;

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
        saveData(location[0], location[1], location[2], location[3]);
        location[0] = "";
        location[1] = "";
        location[2] = "";
        location[3] = "";
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
    
  public static void saveData(String latitude, String longitude, String date, String time) {
    try {
      String db = System.getenv("FINDMYTAXI_DB");
      Connection con = getConnection();
      Statement stmt = con.createStatement();
      String sql = "INSERT INTO " + db + " (lon, lat, date, time) VALUES ('" + longitude + "','" + latitude + "','" + date + "','" + time + "')";
      stmt.executeUpdate(sql);
      con.close(); 
    }
    catch(Exception e){ 
      System.out.println(e);
    }
  }

  public static Connection getConnection() {
    try{
      String host = System.getenv("FINDMYTAXI_HOST");
      String password = System.getenv("FINDMYTAXI_PASSWORD");
      String user = System.getenv("FINDMYTAXI_USER");
      Class.forName("com.mysql.cj.jdbc.Driver");  
      Connection con=DriverManager.getConnection("jdbc:mysql://" + host + ":3306/findmytaxi?jdbcCompliantTruncation=false",user,password);
      return con;
    }
    catch(Exception e){ 
      System.out.println(e);
      return null;
    }
  }
    
}
