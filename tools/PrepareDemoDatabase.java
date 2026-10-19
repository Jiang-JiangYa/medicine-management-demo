import java.sql.*;
public class PrepareDemoDatabase {
 public static void main(String[] args) throws Exception {
  String url=System.getenv("DB_URL");
  if (!"jdbc:dm://127.0.0.1:5237".equals(url)) throw new IllegalArgumentException("Local isolated demo instance only");
  Class.forName("dm.jdbc.driver.DmDriver");
  try(Connection c=DriverManager.getConnection(url,"SYSDBA",System.getenv("SYSDBA_PASSWORD"));Statement s=c.createStatement()) {
   if (args[0].equals("create")) {
    String password=System.getenv("DB_PASSWORD");
    if (!password.matches("[A-Za-z0-9]{16,48}")) throw new IllegalArgumentException("Generated password required");
    s.execute("CREATE USER DEMO_APP IDENTIFIED BY "+password+" DEFAULT TABLESPACE MAIN");
    s.execute("GRANT RESOURCE TO DEMO_APP");
   } else if (args[0].equals("migrate")) { s.execute("ALTER TABLE DEMO_APP.USERS MODIFY PASSWORD VARCHAR(100)"); }
   else if (args[0].equals("text")) {
    for (String line : java.nio.file.Files.readAllLines(java.nio.file.Paths.get("database/002-demo-text-width.sql"),java.nio.charset.StandardCharsets.UTF_8)) {
     if (line.startsWith("ALTER TABLE DEMO_APP.")) s.execute(line.replace(";", ""));
    }
   }
  }
 }
}
