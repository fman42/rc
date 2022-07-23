package me.valenwe.rustcraft;

import java.util.ArrayList;
import java.util.List;
import me.valenwe.rustcraft.clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TchatListener implements Listener {
  @EventHandler
  public void tchat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    Clan clan = Main.getClan(player.getUniqueId());
    if (clan == null)
      return; 
    event.setCancelled(true);
    ChatColor color = ChatColor.WHITE;
    if (clan.getLeader().equals(player.getUniqueId()))
      color = ChatColor.DARK_RED; 
    Bukkit.broadcastMessage("{" + clan.getNameWithColors() + ChatColor.WHITE + "} " + color + player.getName() + 
        ChatColor.WHITE + ": " + event.getMessage());
  }
  
  public static ChatColor stringtoChatColor(String str) {
    ChatColor cc = ChatColor.WHITE;
    if (str.toLowerCase().contains("yellow"))
      cc = ChatColor.YELLOW; 
    if (str.toLowerCase().contains("purple"))
      cc = ChatColor.LIGHT_PURPLE; 
    if (str.toLowerCase().contains("blue"))
      cc = ChatColor.BLUE; 
    if (str.toLowerCase().contains("green"))
      cc = ChatColor.GREEN; 
    if (str.toLowerCase().contains("red"))
      cc = ChatColor.RED; 
    if (str.toLowerCase().contains("gray"))
      cc = ChatColor.GRAY; 
    return cc;
  }
  
  public static String removeChatColor(String str) {
    List<String> list_color = new ArrayList<>();
    list_color.add("yellow");
    list_color.add("purple");
    list_color.add("blue");
    list_color.add("green");
    list_color.add("red");
    list_color.add("gray");
    list_color.add("null");
    String color = null;
    for (int i = 0; i < list_color.size(); i++) {
      color = list_color.get(i);
      if (str.contains(color))
        str = str.replaceAll(color, ""); 
    } 
    return str;
  }
}
