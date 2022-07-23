package me.valenwe.rustcraft;

import java.util.List;
import java.util.Map;
import me.valenwe.rustcraft.clans.Clan;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AcceptDeclineCommand implements CommandExecutor {
  static Main plugin;
  
  public AcceptDeclineCommand(Main main) {
    plugin = main;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (label.equalsIgnoreCase("accept")) {
      if (sender instanceof Player) {
        Player player = (Player)sender;
        for (int i = 0; i < Main.requestClan.size(); i++) {
          if (((Player)((List<Player>)Main.requestClan.get(i)).get(1)).getUniqueId().equals(player.getUniqueId())) {
            for (int I = 0; I < 9; I++) {
              ItemStack item = player.getInventory().getItem(i);
              if (item != null && 
                item.getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Clan Chest"))
                player.getInventory().remove(item); 
            } 
            player.sendMessage(plugin.getTextFromLanguage("invitations.join", ChatColor.GREEN, (
                  (Player)((List<Player>)Main.requestClan.get(i)).get(0)).getUniqueId()));
            Clan clan = Main.getClan(((Player)((List<Player>)Main.requestClan.get(i)).get(0)).getUniqueId());
            clan.addPlayer(((Player)((List<Player>)Main.requestClan.get(i)).get(1)).getUniqueId(), (
                (Player)((List<Player>)Main.requestClan.get(i)).get(1)).getDisplayName());
            player.setPlayerListName(clan.getPlayerTabString(player));
            for (Player online : Bukkit.getOnlinePlayers()) {
              if (clan.isClanMember(online.getUniqueId()).booleanValue() && 
                !online.getUniqueId().equals(((Player)((List<Player>)Main.requestClan.get(i)).get(1)).getUniqueId()))
                online.sendMessage(plugin.getTextFromLanguage("invitations.joined", ChatColor.GREEN, 
                      player.getUniqueId())); 
            } 
            Main.requestClan.remove(i);
            return true;
          } 
        } 
        player.sendMessage(
            plugin.getTextFromLanguage("invitations.error_no_invit", ChatColor.RED, player.getUniqueId()));
        return true;
      } 
      sender.sendMessage(plugin.getConfig().getString("text.clan_command.error_console"));
    } 
    if (label.equalsIgnoreCase("decline")) {
      if (sender instanceof Player) {
        Player player = ((Player)sender).getPlayer();
        for (int i = 0; i < Main.requestClan.size(); i++) {
          if (((Player)((List<Player>)Main.requestClan.get(i)).get(1)).getUniqueId() == player.getUniqueId()) {
            ((Player)((List<Player>)Main.requestClan.get(i)).get(0)).sendMessage(plugin.getTextFromLanguage("invitations.refuse/0", 
                  ChatColor.RED, player.getUniqueId()));
            Main.requestClan.remove(i);
            player.sendMessage(plugin.getTextFromLanguage("invitations.refuse/1", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
        } 
        player.sendMessage(
            plugin.getTextFromLanguage("invitations.error_no_invit", ChatColor.RED, player.getUniqueId()));
        return true;
      } 
      sender.sendMessage(plugin.getConfig().getString("text.clan_command.error_console"));
    } 
    return false;
  }
  
  public static <K, V> K getKey(Map<K, V> map, V value) {
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (entry.getValue().equals(value))
        return entry.getKey(); 
    } 
    return null;
  }
}
