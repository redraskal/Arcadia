package me.jedimastersoda.arcadia.velocity.listener;

import java.sql.SQLException;
import java.util.UUID;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;

import me.jedimastersoda.arcadia.common.manager.AccountManager;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;

public class PlayerPreConnectListener {

  @Subscribe
  public void onPostLogin(PostLoginEvent event) {
    UUID uuid = event.getPlayer().getUniqueId();

    AccountManager.getInstance().clearAccountFromCache(uuid);
    
    try {
      boolean banned = AccountManager.getInstance().getAccount(uuid).isBanned();

      if(banned) {
        event.getPlayer().disconnect(TextComponent.builder()
          .content("You are banned from the Arcadia network.")
          .color(TextColor.RED)
          .build()
        );
      }
    } catch (Exception e) {
      e.printStackTrace();

      event.getPlayer().disconnect(TextComponent.builder()
        .content("A database error occurred while connecting to Arcadia.")
        .color(TextColor.RED)
        .build()
      );
    }
  }
}