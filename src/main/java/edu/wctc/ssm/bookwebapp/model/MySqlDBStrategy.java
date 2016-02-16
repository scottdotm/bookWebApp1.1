package edu.wctc.ssm.bookwebapp.model;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Scott
 */
public class MySqlDBStrategy implements DBStrategy {
    private Connection conn;
    
    @Override
    public void openConnection(String driverClass, String url, 
            String userName, String password) throws ClassNotFoundException, SQLException {
        
        Class.forName (driverClass);
        conn = DriverManager.getConnection(url,userName, password);
        
    }
    
    @Override
    public void closeConnection() throws SQLException {
        conn.close();
    }
    
    /**
     *  Make sure you open and close connection when using method.
     * Future optimization may include change the return type of an Array.
     * @param tableName
     * @param maxRecords - limit records found to first maxRecords or if maxRecords is zero (0)
     * then no limit.
     * @throws java.sql.SQLException
     * @return records
     */
    @Override
    public List<Map<String, Object>> findAllRecords(String tableName, int maxRecords) throws SQLException{
        String sql;
        if(maxRecords <= 1){
                sql = "select * from " + tableName;
                }else{
                sql = "select * from " + tableName + " limit " + maxRecords;
                }
   
      //String sql = "select * from " + tableName + " limit " + maxRecords;
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      ResultSetMetaData rsmd = rs.getMetaData();
      int columnCount = rsmd.getColumnCount();
      List<Map<String, Object>> records = new ArrayList<>();
      
      while(rs.next()){
          Map<String, Object> record=new HashMap<>();
          for(int colNo = 1; colNo <= columnCount; colNo++){
              Object colData = rs.getObject(colNo);
              String colName = rsmd.getColumnName(colNo);
              record.put(colName, colData);
          }
          records.add(record);
      }
      
      return records;
    }
    
    /**
     * Deletes a record by its Id
     * @param tableName
     * @param id
     * @throws ClassNotFoundException, SQLException
     */
    @Override
    public void deleteById(String tableName, String id) throws ClassNotFoundException, SQLException {
        //Need to evaluate the parameters being passed in.
        String pKeyColumnName = null;
        
        
        DatabaseMetaData dmd = conn.getMetaData();
        ResultSet rs = null;
      
        rs = dmd.getPrimaryKeys(null, null, tableName);
       
            
        while(rs.next()){
        pKeyColumnName = rs.getString("COLUMN_NAME");
        
        String sql2 = "delete from " + tableName + " where " + pKeyColumnName + "=?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql2);
       
        pstmt.setInt(1, Integer.parseInt(id));
        
        pstmt.executeUpdate(); 
        }

    }
//}
    
    
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        DBStrategy db = new MySqlDBStrategy();
        db.openConnection("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/books", "root", "admin");
        System.out.println(db.findAllRecords("author", 0).toString());
        db.deleteById("author", "3");
        System.out.println(db.findAllRecords("author", 0).toString());
        db.closeConnection();
        
    }}
//    
//}
//    public static void main(String[] args) throws SQLException, ClassNotFoundException{
//DBStrategy db = new MySqlDBStrategy();
//db.openConnection("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/books","root","admin");
//List<Map<String,Object>> rawData=db.findAllRecords("author",0);
//db.closeConnection();
//System.out.println(rawData);
//}
//
//}

