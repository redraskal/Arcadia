package me.jedimastersoda.arcadia.common.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import me.jedimastersoda.arcadia.common.db.MysqlConnection;
import me.jedimastersoda.arcadia.common.object.Account;
import me.jedimastersoda.arcadia.common.object.Rank;

public class AccountManager {

  private static AccountManager instance;

  public static AccountManager getAccountManager() {
    if (instance == null) {
      instance = new AccountManager();
    }

    return instance;
  }

  private final Cache<UUID, Account> accountCache = CacheBuilder.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(30, TimeUnit.MINUTES)
    .build();

  private AccountManager() {}

  public boolean createAccount(UUID uuid) throws ClassNotFoundException, SQLException {
    PreparedStatement preparedStatement = MysqlConnection.getInstance().prepareStatement("INSERT INTO `accounts` (uuid, rank) VALUES (?, ?)");

    preparedStatement.setString(1, uuid.toString());
    preparedStatement.setString(2, Rank.DEFAULT.toString());

    return preparedStatement.execute();
  }

  public Account getAccount(UUID uuid) throws ClassNotFoundException, SQLException {
    Account cachedAccount = this.accountCache.getIfPresent(uuid);

    if(cachedAccount != null) {
      return cachedAccount;
    }

    PreparedStatement preparedStatement = MysqlConnection.getInstance().prepareStatement("SELECT * FROM `accounts` WHERE uuid=?");

    preparedStatement.setString(1, uuid.toString());

    ResultSet resultSet = preparedStatement.executeQuery();

    if(resultSet.next()) {
      Account account = new Account(uuid, Rank.valueOf(resultSet.getString("rank")), resultSet.getBoolean("banned"));

      this.accountCache.put(uuid, account);

      return account;
    } else {
      this.createAccount(uuid);

      Account account = new Account(uuid, Rank.DEFAULT, false);

      this.accountCache.put(uuid, account);

      return account;
    }
  }

  public void clearAccountFromCache(UUID uuid) {
    if(this.accountCache.getIfPresent(uuid) != null) {
      this.accountCache.invalidate(uuid);
    }
  }
}