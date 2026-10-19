import java.sql.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
/** Copies original local business rows into the isolated demo, excluding USERS. */
public class CopyExistingBusinessData {
 static final String[] TABLES={"MEDI_CATALOGINFO","MEDI_DRUGINFO","MEDI_DRUGSTOREBATCHINFO","MEDI_DRUGSTOREFREEZEONE","MEDI_DRUGSTOREINFO","MEDI_INSTOREBILLDETAIL","MEDI_INSTOREBILLMASTER","MEDI_OUTSTOREBILLDETAIL","MEDI_OUTSTOREBILLMASTER","MEDI_REFUNDINGBILLDETAIL","MEDI_REFUNDINGBILLMASTER","MEDI_STOCKBATCHINFO","MEDI_STOCKINFO","MEDI_VARIETYINFO"};
 static String quote(String s){ if(!s.matches("[A-Z0-9_]+"))throw new IllegalArgumentException();return "\""+s+"\""; }
 public static void main(String[] args) throws Exception {
  Class.forName("dm.jdbc.driver.DmDriver");
  try(Connection source=DriverManager.getConnection("jdbc:dm://127.0.0.1:5236","SYSDBA",System.getenv("SOURCE_DB_PASSWORD"));
      Connection target=DriverManager.getConnection("jdbc:dm://127.0.0.1:5237","DEMO_APP",System.getenv("DEMO_DB_PASSWORD"))) {
   source.setReadOnly(true); source.setAutoCommit(false);
   Map<String,Integer> counts=new LinkedHashMap<>();
   // Preflight any UTF-8 expansion before beginning the atomic row replacement.
   for(String table:TABLES) {
    try(Statement q=source.createStatement();ResultSet rows=q.executeQuery("SELECT * FROM SYSDBA."+quote(table))) {
     ResultSetMetaData m=rows.getMetaData();int n=m.getColumnCount();int[] max=new int[n];
     while(rows.next()) for(int i=1;i<=n;i++) {
      int type=m.getColumnType(i);
      if(type==Types.VARCHAR||type==Types.CHAR||type==Types.NVARCHAR||type==Types.NCHAR) {
       String value=rows.getString(i);if(value!=null)max[i-1]=Math.max(max[i-1],value.getBytes(StandardCharsets.UTF_8).length);
      }
     }
     for(int i=1;i<=n;i++) if(max[i-1]>0) {
      String col=m.getColumnName(i).toUpperCase(Locale.ROOT);
      try(ResultSet dest=target.getMetaData().getColumns(null,"DEMO_APP",table,col)) {
       if(!dest.next())throw new IllegalStateException("Missing column "+table+"."+col);
       int size=dest.getInt("COLUMN_SIZE");
       if(max[i-1]>size) {
        int type=m.getColumnType(i);String sqlType=(type==Types.CHAR||type==Types.NCHAR)?"CHAR":"VARCHAR";
        try(Statement alter=target.createStatement()) {alter.execute("ALTER TABLE DEMO_APP."+quote(table)+" MODIFY "+quote(col)+" "+sqlType+"("+max[i-1]+")");}
       }
      }
     }
    }
   }
   target.setAutoCommit(false);
   try {
    for(String table:TABLES)try(Statement d=target.createStatement()){d.executeUpdate("DELETE FROM DEMO_APP."+quote(table));}
    for(String table:TABLES) {
     int count=0;
     try(Statement q=source.createStatement();ResultSet rows=q.executeQuery("SELECT * FROM SYSDBA."+quote(table))) {
      ResultSetMetaData m=rows.getMetaData();int n=m.getColumnCount();StringJoiner cols=new StringJoiner(","),marks=new StringJoiner(",");
      for(int i=1;i<=n;i++){cols.add(quote(m.getColumnName(i).toUpperCase(Locale.ROOT)));marks.add("?");}
      try(PreparedStatement insert=target.prepareStatement("INSERT INTO DEMO_APP."+quote(table)+" ("+cols+") VALUES ("+marks+")")) {
       while(rows.next()) {
        for(int i=1;i<=n;i++)insert.setObject(i,rows.getObject(i));
        insert.addBatch();count++;
        if(count%100==0)insert.executeBatch();
       }
       insert.executeBatch();
      }
     }
     try(Statement q=target.createStatement();ResultSet rows=q.executeQuery("SELECT COUNT(*) FROM DEMO_APP."+quote(table))) {
      rows.next();if(rows.getInt(1)!=count)throw new IllegalStateException("Row count mismatch: "+table);
     }
     counts.put(table,count);
    }
    target.commit();source.rollback();
   } catch(Exception e) {target.rollback();throw e;}
   StringBuilder report=new StringBuilder("# 原毕设业务数据副本\n\n原库只读，目标为本机5237演示实例；USERS未复制，保留演示账号。\n\n| 表 | 原库/副本行数 |\n|---|---:|\n");
   for(Map.Entry<String,Integer> entry:counts.entrySet()){report.append("| ").append(entry.getKey()).append(" | ").append(entry.getValue()).append(" |\n");System.out.println(entry.getKey()+": "+entry.getValue());}
   Files.write(Paths.get("private/existing-data-report.md"),report.toString().getBytes(StandardCharsets.UTF_8));
  }
 }
}
