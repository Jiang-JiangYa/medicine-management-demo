import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
public class ImportPostgresDemo {
 public static void main(String[] args) throws Exception {
  Class.forName("org.postgresql.Driver");
  String url=System.getenv("JDBC_DATABASE_URL");
  if(url==null||!url.startsWith("jdbc:postgresql://"))throw new IllegalArgumentException("PostgreSQL JDBC URL required");
  try(Connection c=DriverManager.getConnection(url,System.getenv("DB_USERNAME"),System.getenv("DB_PASSWORD"));Statement s=c.createStatement()) {
   try(ResultSet r=s.executeQuery("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'")) {
    r.next();if(r.getInt(1)!=0)throw new IllegalStateException("Refusing to overwrite a nonempty database");
   }
   String sql=new String(Files.readAllBytes(Paths.get(args[0])),StandardCharsets.UTF_8);
   s.execute(sql);
   try(ResultSet r=s.executeQuery("SELECT (SELECT count(*) FROM medi_druginfo), (SELECT count(*) FROM medi_cataloginfo), (SELECT count(*) FROM medi_stockinfo), (SELECT count(*) FROM users)")) {
    r.next();if(r.getInt(1)!=37||r.getInt(2)!=19||r.getInt(3)!=30||r.getInt(4)!=2)throw new IllegalStateException("Unexpected migrated row counts");
    System.out.println("Verified PostgreSQL: 37 drugs, 19 categories, 30 stock rows, 2 demo accounts.");
   }
  }
 }
}
