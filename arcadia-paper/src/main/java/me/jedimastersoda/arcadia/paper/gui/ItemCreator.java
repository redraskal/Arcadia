package me.jedimastersoda.arcadia.paper.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

public class ItemCreator {

  public static ItemStack BLANK_ITEMSTACK = new ItemStack(Material.AIR);

  public static List<String> colorizeList(List<String> list) {
    List<String> result = new ArrayList<String>();

    for(String line : list) {
      result.add(ChatColor.translateAlternateColorCodes('&', line));
    }

    return result;
  }

  public static ItemStack createItem(Material type, int amount, int data, String name, List<String> lore) {
    ItemStack item = new ItemStack(type, 1, (short) data);
    ItemMeta meta = item.getItemMeta();

    item.setAmount(amount);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    meta.setLore(colorizeList(lore));
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createItemPotion(Material type, int amount, int data, String name, List<String> lore) {
    ItemStack item = new ItemStack(type, 1, (short) data);
    PotionMeta meta = (PotionMeta) item.getItemMeta();

    meta.clearCustomEffects();
    item.setAmount(amount);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    meta.setLore(colorizeList(lore));
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createItem(Material type, int amount, int data, String name) {
    ItemStack item = new ItemStack(type, 1, (short) data);
    ItemMeta meta = item.getItemMeta();

    item.setAmount(amount);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createBanner(Material type, DyeColor dyeColor, int amount, int data, String name) {
    ItemStack item = new ItemStack(type, 1, (short) data);
    BannerMeta meta = (BannerMeta) item.getItemMeta();

    item.setAmount(amount);
    meta.setBaseColor(dyeColor);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createBanner(Material type, DyeColor dyeColor, int amount, int data, String name, List<String> lore) {
    ItemStack item = new ItemStack(type, 1, (short) data);
    BannerMeta meta = (BannerMeta) item.getItemMeta();

    item.setAmount(amount);
    meta.setBaseColor(dyeColor);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    meta.setLore(colorizeList(lore));
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createArmour(Material type, int amount, Color color, String name) {
    ItemStack item = new ItemStack(type, 1, (short) 0);
    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

    item.setAmount(amount);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    meta.setColor(color);
    item.setItemMeta(meta);

    return item;
  }

  public static ItemStack createArmour(Material type, int amount, Color color, String name, List<String> lore) {
    ItemStack item = new ItemStack(type, 1, (short) 0);
    LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();

    item.setAmount(amount);
    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
    meta.setLore(colorizeList(lore));
    meta.setColor(color);
    item.setItemMeta(meta);

    return item;
  }

  // public static ItemStack createSkull(int amount, String owner, String name) {
  //   ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount, (short) 3);
  //   SkullMeta meta = (SkullMeta) item.getItemMeta();

  //   item.setAmount(amount);
  //   meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

  //   if(Bukkit.getPlayer(owner) != null) {
  //       Player o = Bukkit.getPlayer(owner);
  //       CraftPlayer cp = ((CraftPlayer) o);
  //       try {
  //           Field profileField = meta.getClass().getDeclaredField("profile");
  //           profileField.setAccessible(true);
  //           profileField.set(meta, cp.getProfile());
  //       } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e1) {
  //           e1.printStackTrace();
  //       }
  //   } else {
  //       meta.setOwner(owner);
  //   }

  //   item.setItemMeta(meta);

  //   return item;
  // }

  // public static ItemStack createSkull(int amount, String owner, String name, List<String> lore) {
  //     ItemStack item = new ItemStack(Material.SKULL_ITEM, amount, (short) 3);
  //     SkullMeta meta = (SkullMeta) item.getItemMeta();

  //     item.setAmount(amount);
  //     meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));

  //     if(Bukkit.getPlayer(owner) != null) {
  //         Player o = Bukkit.getPlayer(owner);
  //         CraftPlayer cp = ((CraftPlayer) o);
  //         try {
  //             Field profileField = meta.getClass().getDeclaredField("profile");
  //             profileField.setAccessible(true);
  //             profileField.set(meta, cp.getProfile());
  //         } catch (NoSuchFieldException|IllegalArgumentException|IllegalAccessException e1) {
  //             e1.printStackTrace();
  //         }
  //     } else {
  //         meta.setOwner(owner);
  //     }

  //     meta.setLore(colorizeList(lore));
  //     item.setItemMeta(meta);

  //     return item;
  // }
}