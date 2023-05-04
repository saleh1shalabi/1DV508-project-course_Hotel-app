package hotelproject;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {
  private static DataHandler instance;

  private DataHandler() {}

  static {
    instance = new DataHandler();
  }

  public static DataHandler getInstance() {
    return instance;
  }


  /**
   * Takes inputs from a list where the first string is the query.
   * All following Strings are inputs for that query.
   * @return Result from query in form of List of Hashmaps.
   */
  public static List<Map<String, Object>> getData(List<String> query) {
    ResultSet result = null;
    Connection conn = null;
    PreparedStatement st = null;
    List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
    Map<String, Object> row = null;
    try {
      conn = DataSource.getConnection();
      
      st = conn.prepareStatement(query.get(0));
      if (query.size() > 1) {
        for (int i = 1; i < query.size(); i++) {
          st.setString(i, query.get(i));
        }
      }
      result = st.executeQuery();

      ResultSetMetaData metaData = result.getMetaData();
      Integer columnCount = metaData.getColumnCount();
      while (result.next()) {
        row = new HashMap<String, Object>();
        for (int i = 1; i <= columnCount; i++) {
          row.put(metaData.getColumnName(i), result.getObject(i));
        }
        resultList.add(row);
      }
    } catch (SQLException e) {
      System.out.println(e);
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } 
      try {
        if (result != null) {
          result.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } 
      try {
        if (st != null) {
          st.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      } 
    }
    return resultList;
  }

  /**
   * Takes inputs from a list where the first string is the query.
   * All following Strings are inputs for that query.
   */
  public static void setData(List<String> query) throws SQLException {
    try (Connection conn = DataSource.getConnection();
        PreparedStatement st = conn.prepareStatement(query.get(0));) {
      if (query.size() > 1) {
        for (int i = 1; i < query.size(); i++) {
          st.setString(i, query.get(i));
        }
      }
      st.executeUpdate();
    }
  }

  /**
   * Hashes string with SHA-512.
   */
  public static String hashString(String text) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512"); 
      byte[] messageDigest = md.digest(text.getBytes(Charset.forName("UTF-8"))); 
      BigInteger no = new BigInteger(1, messageDigest); 
      String hashtext = no.toString(16); 
      while (hashtext.length() < 32) { 
        hashtext = "0" + hashtext; 
      } 
      return hashtext; 
    } catch (Exception e) {
      return null;
    }
  }
}
