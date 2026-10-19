import java.sql.*;
public class ShutdownLocalDatabase {
 public static void main(String[] args) throws Exception {
  String url=System.getenv("DB_URL");
  if (!"jdbc:dm://127.0.0.1:5236".equals(url) && !"jdbc:dm://127.0.0.1:5237".equals(url)) throw new IllegalArgumentException("Local instances only");
  Class.forName("dm.jdbc.driver.DmDriver");
  try(Connection c=DriverManager.getConnection(url,"SYSDBA",System.getenv("DB_PASSWORD"));Statement s=c.createStatement()) {s.execute("SHUTDOWN NORMAL");}
 }
}
