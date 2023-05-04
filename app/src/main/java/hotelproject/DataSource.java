package hotelproject;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
  private static HikariConfig config = new HikariConfig("database.properties");
  private static HikariDataSource ds;

  static {
    config.setJdbcUrl("jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Europe/Stockholm");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    ds = new HikariDataSource(config);
  }

  private DataSource() {}

  /**
   * Set the JdbcUrl so it connects to "silicone_hotel".
   */
  public static void setDatabase() {
    config.setJdbcUrl(
        "jdbc:mysql://localhost:3306/silicone_hotel?useSSL=false&serverTimezone=Europe/Stockholm"
    );
    ds = new HikariDataSource(config);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
