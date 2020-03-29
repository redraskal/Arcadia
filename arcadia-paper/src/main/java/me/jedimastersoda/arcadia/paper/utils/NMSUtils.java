package me.jedimastersoda.arcadia.paper.utils;

import java.lang.reflect.Field;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

public class NMSUtils {

  private static String nms_version;

  public static void unregisterBukkitCommands(String[] commands)
      throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    Object commandMap = accessPrivateField(Bukkit.getServer().getPluginManager(), "commandMap");

    Object knownCommandsObject = accessPrivateField(SimpleCommandMap.class, commandMap, "knownCommands");

    @SuppressWarnings("unchecked")
    Map<String, Command> knownCommands = (Map<String, Command>) knownCommandsObject;

    for(String command : commands) {
      knownCommands.remove(command);
    }
  }

  @SuppressWarnings("all")
  public static CommandDispatcher accessCommandDispatcher() throws ClassNotFoundException, NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException {
    Object craftServer = getCraftServer();
    Field console = craftServer.getClass().getDeclaredField("console");

    console.setAccessible(true);

    Object dedicatedServer = console.get(craftServer);

    console.setAccessible(false);

    Class<?> c_minecraftServer = Class.forName(fetchMinecraftClass("MinecraftServer"));
    Field commandDispatcherField = c_minecraftServer.getDeclaredField("commandDispatcher");

    Object commandDispatcher = commandDispatcherField.get(dedicatedServer);
    Object b = accessPrivateField(commandDispatcher, "b");

    return (CommandDispatcher) b;
  }

  @SuppressWarnings("all")
  public static <S> Map<String, CommandNode<S>> accessCommandDispatcherChildren() throws ClassNotFoundException,
      NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    CommandDispatcher commandDispatcher = accessCommandDispatcher();
    Object children = accessPrivateField(CommandNode.class, commandDispatcher.getRoot(), "children");

    return (Map<String, CommandNode<S>>) children;
  }

  public static void unregisterMinecraftCommands(String[] commands) throws ClassNotFoundException, NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException {
    Map<String, CommandNode<Object>> children = accessCommandDispatcherChildren();

    for(String command : commands) {
      children.remove(command);
    }
  }

  public static String fetchNMSVersion() {
    if(nms_version == null) {
      String _package = Bukkit.getServer().getClass().getPackage().getName();
      _package = _package.substring(_package.lastIndexOf(".") + 1);

      nms_version = _package;
    }

    return nms_version;
  }

  public static Object getCraftServer() throws ClassNotFoundException {
    Class<?> c_craftServer = Class.forName(fetchCraftbukkitClass("CraftServer"));

    return c_craftServer.cast(Bukkit.getServer());
  }

  public static String fetchCraftbukkitClass(String clazz) {
    return "org.bukkit.craftbukkit." + fetchNMSVersion() + "." + clazz;
  }

  public static String fetchMinecraftClass(String clazz) {
    return "net.minecraft.server." + fetchNMSVersion() + "." + clazz;
  }

  public static Object accessPrivateField(Object object, String field) throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    Class<?> clazz = object.getClass();
    Field objectField = clazz.getDeclaredField(field);

    objectField.setAccessible(true);

    Object fieldObject = objectField.get(object);
    
    objectField.setAccessible(false);

    return fieldObject;
  }

  public static Object accessPrivateField(Class<?> clazz, Object object, final String field) throws NoSuchFieldException, SecurityException,
      IllegalArgumentException, IllegalAccessException {
    Field objectField = clazz.getDeclaredField(field);

    objectField.setAccessible(true);

    Object fieldObject = objectField.get(object);
    
    objectField.setAccessible(false);

    return fieldObject;
  }
}