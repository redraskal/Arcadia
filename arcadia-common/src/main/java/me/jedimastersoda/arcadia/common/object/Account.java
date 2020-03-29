package me.jedimastersoda.arcadia.common.object;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import lombok.Getter;
import me.jedimastersoda.arcadia.common.db.MysqlConnection;

public class Account {

  @Getter private final UUID uuid;
  @Getter private Rank rank;
  @Getter private boolean banned;

  public Account(UUID uuid, Rank rank, boolean banned) {
    this.uuid = uuid;
    this.rank = rank;
    this.banned = banned;
  }

  public boolean setRank(Rank newRank) throws ClassNotFoundException, SQLException {
    PreparedStatement preparedStatement = MysqlConnection.getInstance().prepareStatement("UPDATE `accounts` SET rank=? WHERE uuid=?");

    preparedStatement.setString(1, newRank.toString());
    preparedStatement.setString(2, this.uuid.toString());

    return preparedStatement.execute();
  }

  public boolean setBanned(boolean banned) throws ClassNotFoundException, SQLException {
    PreparedStatement preparedStatement = MysqlConnection.getInstance().prepareStatement("UPDATE `accounts` SET banned=? WHERE uuid=?");

    preparedStatement.setBoolean(1, banned);
    preparedStatement.setString(2, this.uuid.toString());

    return preparedStatement.execute();
  }
}