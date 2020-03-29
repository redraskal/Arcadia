package me.jedimastersoda.arcadia.common.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MysqlConnection {

  private static MysqlConnection instance;

  public static MysqlConnection getInstance() {
    if(instance == null) {
      instance = new MysqlConnection();
    }

    return instance;
  }

  private Connection sqlConnection;

  private MysqlConnection() {}

  public boolean openConnection() throws SQLException, ClassNotFoundException {
    if(this.sqlConnection == null || this.sqlConnection.isClosed()) {
      Class.forName("com.mysql.jdbc.Driver"); // Loads the MySQL driver into memory (Java is stupid and won't find it otherwise)

      this.sqlConnection = DriverManager.getConnection("jdbc:mysql://" 
        + Credentials.mysql_host + ":3306/" 
        + Credentials.mysql_database + "?autoReconnect=true", 
        Credentials.mysql_username, Credentials.mysql_password
      );

      return true;
    } else {
      return false;
    }
  }

  public Connection getSQLConnection() throws SQLException, ClassNotFoundException {
    // java.sql.Connection#isValid(2) queries the DB with a 2 second timeout to check if it is properly functioning
    if(this.sqlConnection == null || this.sqlConnection.isClosed() || !this.sqlConnection.isValid(2)) {
      this.closeConnection();
      this.openConnection();
    }

    return this.sqlConnection;
  }

  public PreparedStatement prepareStatement(String statement) throws ClassNotFoundException, SQLException {
    return this.getSQLConnection().prepareStatement(statement);
  }

  public boolean closeConnection() throws SQLException {
    if(this.sqlConnection != null && !this.sqlConnection.isClosed()) {
      this.sqlConnection.close();

      return true;
    } else {
      return false;
    }
  }
}