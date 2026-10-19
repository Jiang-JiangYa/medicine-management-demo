import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
public class InspectDatabase {
 public static void main(String[] args) throws Exception {
  Class.forName("dm.jdbc.driver.DmDriver");
  try(Connection c=DriverManager.getConnection(System.getenv("DB_URL"),System.getenv("DB_USERNAME"),System.getenv("DB_PASSWORD"))) {
   c.setReadOnly(true);
   DatabaseMetaData m=c.getMetaData();
   String schema=System.getenv("DB_USERNAME").toUpperCase(Locale.ROOT);
   StringBuilder report=new StringBuilder("# 原库结构核对（仅元数据）\n\n");
   try(ResultSet ts=m.getTables(null,schema,"%",new String[]{"TABLE"})) {
    while(ts.next()) {
     String t=ts.getString("TABLE_NAME");
     if (!(t.equalsIgnoreCase("USERS") || t.toUpperCase(Locale.ROOT).startsWith("MEDI_"))) continue;
     report.append("## ").append(t).append("\n\n");
     try(ResultSet cols=m.getColumns(null,schema,t,"%")) {
      while(cols.next()) report.append("- ").append(cols.getString("COLUMN_NAME")).append(" ").append(cols.getString("TYPE_NAME")).append("(").append(cols.getInt("COLUMN_SIZE")).append(") nullable=").append(cols.getInt("NULLABLE")).append("\n");
     }
     try(ResultSet keys=m.getPrimaryKeys(null,schema,t)) {while(keys.next()) report.append("- PRIMARY KEY: ").append(keys.getString("COLUMN_NAME")).append("\n");}
     try(ResultSet keys=m.getImportedKeys(null,schema,t)) {while(keys.next()) report.append("- FOREIGN KEY: ").append(keys.getString("FKCOLUMN_NAME")).append(" -> ").append(keys.getString("PKTABLE_NAME")).append('.').append(keys.getString("PKCOLUMN_NAME")).append("\n");}
     report.append("\n");
    }
   }
   Files.write(Paths.get(args[0]),report.toString().getBytes(StandardCharsets.UTF_8));
   System.out.println("Metadata report saved; no business rows read.");
  }
 }
}
