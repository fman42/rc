package me.valenwe.rustcraft.clans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import me.valenwe.rustcraft.Main;
import me.valenwe.rustcraft.TchatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ClanCommand implements CommandExecutor {
  static Main plugin;
  
  public ClanCommand(Main main) {
    plugin = main;
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (label.equalsIgnoreCase("rustcraft")) {
      if (args.length == 1) {
        if (args[0].equalsIgnoreCase("reload")) {
          if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (!player.isOp())
              return true; 
          } 
          plugin.reloadConfig();
          plugin.language_yaml = plugin.loadLanguageFile();
          sender.sendMessage(ChatColor.GREEN + "RustCraft's files reloaded!");
          Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "RustCraft's files reloaded!");
          return true;
        } 
        if (args[0].equalsIgnoreCase("info")) {
          if (sender instanceof Player) {
            final Player player = (Player)sender;
            if (args.length == 1) {
              player.sendMessage(plugin.getBoldedTextFromLanguage("clan_command.info/0", ChatColor.GOLD, 
                    player.getUniqueId()));
              player.sendMessage(plugin.getTextFromLanguage("clan_command.info/1", ChatColor.YELLOW, 
                    player.getUniqueId()));
              player.sendMessage(plugin.getTextFromLanguage("clan_command.info/2", ChatColor.YELLOW, 
                    player.getUniqueId()));
              player.sendMessage(ChatColor.GOLD + "/clan" + plugin.getTextFromLanguage(
                    "clan_command.info/3", ChatColor.YELLOW, player.getUniqueId()));
              return true;
            } 
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /rustcraft info");
            return true;
          } 
          sender.sendMessage(plugin.getTextFromLanguage("clan_command.error_console", ChatColor.RED, null));
          return true;
        } 
      } 
      return true;
    } 
    if (label.equalsIgnoreCase("clan"))
      if (sender instanceof Player) {
        final Player player = (Player)sender;
        final Clan clan = Main.getClan(player.getUniqueId());
        if (Main.clan_command_cancel) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_use_clan_command", 
                ChatColor.DARK_RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 0) {
          player.sendMessage(plugin.getBoldedTextFromLanguage("clan_command.clan/0", ChatColor.GREEN, 
                player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan list: " + plugin
              .getTextFromLanguage("clan_command.clan/1", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan join: " + plugin
              .getTextFromLanguage("clan_command.clan/2", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan create: " + plugin
              .getTextFromLanguage("clan_command.clan/3", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan leave: " + plugin
              .getTextFromLanguage("clan_command.clan/4", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan tp: " + plugin.getTextFromLanguage("clan_command.clan/5", 
                ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan needs: " + plugin
              .getTextFromLanguage("clan_command.clan/6", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan members: " + plugin
              .getTextFromLanguage("clan_command.clan/7", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan chat: " + plugin
              .getTextFromLanguage("clan_command.clan/8", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan info: " + plugin
              .getTextFromLanguage("clan_command.clan/9", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan name: " + plugin
              .getTextFromLanguage("clan_command.clan/10", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan lead: " + plugin
              .getTextFromLanguage("clan_command.clan/11", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan invite: " + plugin
              .getTextFromLanguage("clan_command.clan/12", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan change: " + plugin
              .getTextFromLanguage("clan_command.clan/13", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan war: " + plugin
              .getTextFromLanguage("clan_command.clan/14", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan public: " + plugin
              .getTextFromLanguage("clan_command.clan/15", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan private: " + plugin
              .getTextFromLanguage("clan_command.clan/16", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan kick: " + plugin
              .getTextFromLanguage("clan_command.clan/17", ChatColor.YELLOW, player.getUniqueId()));
          player.sendMessage(ChatColor.GOLD + "/clan description: " + plugin
              .getTextFromLanguage("clan_command.clan/18", ChatColor.YELLOW, player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("create")) {
          int nb_clan = Main.clan_list.size();
          if (nb_clan > plugin.getConfig().getInt("clan_max_number")) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_max_clan_number", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          ItemStack claim_chest = new ItemStack(Material.CHEST, 1);
          ItemMeta claim_chest_meta = claim_chest.getItemMeta();
          claim_chest_meta.setDisplayName(ChatColor.YELLOW + "База клана");
          List<String> lore = new ArrayList<>();
          lore.add(plugin.getTextFromLanguage("clan_command.lore_clan_chest/0", null, player.getUniqueId()));
          lore.add(plugin.getTextFromLanguage("clan_command.lore_clan_chest/1", null, player.getUniqueId()));
          claim_chest_meta.setLore(lore);
          claim_chest.setItemMeta(claim_chest_meta);
          if (player.getInventory().contains(claim_chest)) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_clan_chest", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          if (clan != null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          boolean slot_vide = false;
          for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) == null)
              slot_vide = true; 
          } 
          if (!slot_vide) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_full_bar", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          player.getInventory().addItem(new ItemStack[] { claim_chest });
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("create")) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_create", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if ((args.length == 1 || args.length > 2) && args[0].equalsIgnoreCase("lead")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_lead", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 2 && args[0].equalsIgnoreCase("lead")) {
          final Player target = Bukkit.getPlayer(args[1]);
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (target == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.player_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (clan.isClanMember(online.getUniqueId()).booleanValue()) {
              online.sendMessage(plugin.getTextFromLanguage("clan_command.new_leader", ChatColor.GREEN, 
                    target.getUniqueId()));
              clan.setLeader(target.getUniqueId());
              online.setPlayerListName(clan.getPlayerTabString(online));
            } 
          } 
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("tp")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.isChanging().booleanValue()) {
            Block chest = clan.getChestLocation().getBlock();
            Directional chest_data = (Directional)chest.getBlockData();
            Location in_front = clan.getChestLocation().clone();
            in_front.add(new Location(in_front.getWorld(), chest_data.getFacing().getModX(), 
                  chest_data.getFacing().getModY(), chest_data.getFacing().getModZ()));
            in_front.add(0.5D, 0.0D, 0.5D);
            player.teleport(in_front);
          } else {
            player.teleport(clan.getChestLocation());
          } 
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("tp")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_tp", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args[0].equalsIgnoreCase("name") && clan == null) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length != 3 && args[0].equalsIgnoreCase("name")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_name", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 3 && args[0].equalsIgnoreCase("name")) {
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          String name = String.valueOf(args[2].toLowerCase()) + args[1];
          List<String> list_color = new ArrayList<>();
          list_color.add("yellow");
          list_color.add("purple");
          list_color.add("blue");
          list_color.add("green");
          list_color.add("red");
          list_color.add("gray");
          if (!list_color.contains(args[2].toLowerCase())) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_wrong_color/0", ChatColor.RED, 
                    player.getUniqueId())) + 
                plugin.getTextFromLanguage("clan_command.error_wrong_color/1", ChatColor.BLUE, 
                  player.getUniqueId()) + 
                
                ChatColor.LIGHT_PURPLE + " purple" + ChatColor.YELLOW + " yellow" + ChatColor.BLUE + 
                " blue" + ChatColor.GRAY + " gray" + ChatColor.RED + " red" + ChatColor.GREEN + 
                " green");
            return true;
          } 
          for (Clan c : Main.clan_list) {
            if (c != clan && TchatListener.removeChatColor(c.getName()).equals(args[1])) {
              player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_name", 
                    ChatColor.RED, player.getUniqueId()));
              return true;
            } 
          } 
          clan.setClanName(name);
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (clan.isClanMember(online.getUniqueId()).booleanValue()) {
              online.setPlayerListName(clan.getPlayerTabString(online));
              online.sendMessage(plugin.getTextFromLanguage("clan_command.new_name", ChatColor.GREEN, 
                    online.getUniqueId()));
            } 
          } 
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("change")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isChanging().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_chest_displaced", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          boolean slot_vide = false;
          for (int i = 0; i < 9; i++) {
            if (player.getInventory().getItem(i) == null)
              slot_vide = true; 
          } 
          if (!slot_vide) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_full_bar", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          Chest chest = (Chest)clan.getChestLocation().getBlock().getState();
          if (chest.getBlockInventory().firstEmpty() > 0) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_chest_not_empty", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          ItemStack claim_chest = new ItemStack(Material.CHEST, 1);
          ItemMeta claim_chest_meta = claim_chest.getItemMeta();
          claim_chest_meta.setDisplayName(ChatColor.DARK_GREEN + "Clan Chest");
          List<String> lore = new ArrayList<>();
          lore.add(plugin.getTextFromLanguage("clan_command.lore_clan_chest/0", null, player.getUniqueId()));
          lore.add(plugin.getTextFromLanguage("clan_command.lore_clan_chest/1", null, player.getUniqueId()));
          claim_chest_meta.setLore(lore);
          claim_chest.setItemMeta(claim_chest_meta);
          player.getInventory().addItem(new ItemStack[] { claim_chest });
          player.sendMessage(plugin.getTextFromLanguage("clan_command.message_not_protected", ChatColor.RED, 
                player.getUniqueId()));
          clan.setIsChanging(Boolean.valueOf(true));
          clan.setProtection(Boolean.valueOf(false));
          clan.getChestLocation().getBlock().getDrops().clear();
          clan.getChestLocation().getBlock().setType(Material.AIR);
          Block clan_chest = clan.getChestLocation().getBlock();
          clan_chest.getDrops().clear();
          clan_chest.setType(Material.AIR);
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("change")) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_change", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.confirm", ChatColor.RED, player.getUniqueId())) + 
              ChatColor.BOLD + "confirm");
          return true;
        } 
        if (args[0].equalsIgnoreCase("leave") && (args.length > 2 || !args[1].equalsIgnoreCase("confirm")))
          player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_leave_confirm", ChatColor.RED, 
                  player.getUniqueId())) + " /clan leave confirm"); 
        if (args.length == 2 && args[0].equalsIgnoreCase("leave") && args[1].equalsIgnoreCase("confirm")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.getLeader().equals(player.getUniqueId()) && clan.getMembersUUID().size() > 1) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          clan.removePlayer(player.getUniqueId());
          player.setPlayerListName(player.getDisplayName());
          if (clan.getMembersUUID().size() == 0) {
            Main.clan_list.remove(clan);
            player.sendMessage(plugin.getTextFromLanguage("clan_command.dissolve_clan", ChatColor.DARK_RED, 
                  player.getUniqueId()));
            for (int i = 0; i < Main.requestClan.size(); i++) {
              List<Player> list_player = Main.requestClan.get(i);
              if (((Player)list_player.get(0)).getUniqueId().equals(player.getUniqueId())) {
                ((Player)list_player.get(1)).sendMessage(plugin.getTextFromLanguage(
                      "clan_command.delete_invitation", ChatColor.RED, player.getUniqueId()));
                Main.requestClan.remove(i);
              } 
            } 
            return true;
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.leave_clan_1", ChatColor.DARK_RED, 
                player.getUniqueId()));
          for (UUID uuid : clan.getMembersUUID()) {
            Player member = Bukkit.getPlayer(uuid);
            if (member != null && member.isOnline())
              member.sendMessage(plugin.getTextFromLanguage("clan_command.leave_clan_2", 
                    ChatColor.DARK_RED, player.getUniqueId())); 
          } 
          return true;
        } 
        if ((args.length == 1 || args.length > 2) && args[0].equalsIgnoreCase("invite")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_add", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("members")) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_members", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("members")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.members/0", ChatColor.GREEN, 
                player.getUniqueId()));
          String leader_name = clan.getClanMembers().get(clan.getLeader());
          player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.members/1", ChatColor.DARK_RED, 
                  player.getUniqueId())) + ChatColor.YELLOW + leader_name);
          if (clan.getMembersUUID().size() > 1) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.members/2", ChatColor.GOLD, 
                  player.getUniqueId()));
            for (String player_name : clan.getMembersName()) {
              if (player_name != leader_name)
                player.sendMessage(ChatColor.YELLOW + player_name); 
            } 
          } 
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("list")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_list", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
          if (Main.clan_list.size() == 0) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.no_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.list/0", ChatColor.DARK_GREEN, 
                player.getUniqueId()));
          for (Clan c : Main.clan_list) {
            String pluriel = "";
            if (c.getClanMembers().size() > 1 && plugin.language.equalsIgnoreCase("english"))
              pluriel = "s"; 
            player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "(" + c.getPrivacy() + ") " + 
                ChatColor.RESET + c.getNameWithColors() + 
                plugin.getTextFromLanguage("clan_command.list/1", ChatColor.GREEN, 
                  player.getUniqueId()) + 
                
                c.getMembersName().size() + plugin.getTextFromLanguage("clan_command.list/2", 
                  ChatColor.GREEN, player.getUniqueId()) + 
                pluriel);
          } 
          return true;
        } 
        if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          final Player target = Bukkit.getPlayer(args[1]);
          if (target == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_no_player", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          for (Clan c : Main.clan_list) {
            if (c.isClanMember(target.getUniqueId()).booleanValue()) {
              player.sendMessage(plugin.getTextFromLanguage("clan_command.error_player_already_clan", 
                    ChatColor.RED, player.getUniqueId()));
              return true;
            } 
          } 
          if (clan.getClanMembers().size() >= plugin.getConfig().getInt("clan_members_max")) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_max_clan_members", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          for (List<Player> list_player_requested : (Iterable<List<Player>>)Main.requestClan) {
            Player player_requested = list_player_requested.get(1);
            if (player_requested.getUniqueId().equals(target.getUniqueId())) {
              player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_invited", 
                    ChatColor.DARK_RED, player.getUniqueId()));
              return true;
            } 
          } 
          final ArrayList<Player> SenderReceiver = new ArrayList<>();
          SenderReceiver.add(player);
          SenderReceiver.add(target);
          Main.requestClan.add(SenderReceiver);
          (new BukkitRunnable() {
              public void run() {
                if (Main.requestClan.contains(SenderReceiver)) {
                  Main.requestClan.remove(SenderReceiver);
                  target.sendMessage(ClanCommand.plugin.getTextFromLanguage("clan_command.error_invitation_expired", 
                        ChatColor.RED, player.getUniqueId()));
                } 
              }
            }).runTaskLater((Plugin)plugin, 2400L);
          player.sendMessage(plugin.getTextFromLanguage("clan_command.invitation/0", ChatColor.GREEN, 
                player.getUniqueId()));
          target.sendMessage(plugin.getTextFromLanguage("clan_command.invitation/1", ChatColor.GREEN, 
                player.getUniqueId()));
          target.sendMessage(
              ChatColor.GREEN + 
              "/accept " + plugin.getTextFromLanguage("clan_command.invitation/2", 
                ChatColor.DARK_GREEN, player.getUniqueId()) + 
              ChatColor.DARK_RED + "/decline");
          return true;
        } 
        if (args.length > 1 && args[0].equalsIgnoreCase("needs")) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_needs", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("needs")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isChanging().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_chest_displaced", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          int value = 0;
          if (!Main.list_chest.isEmpty())
            for (Location loc : Main.list_chest) {
              try {
                Chest chest = (Chest)loc.getBlock().getState();
                if (clan.isIn(loc).booleanValue())
                  value += ClanListener.getValueInventory(chest); 
              } catch (Exception e) {
                Main.list_chest.remove(loc);
              } 
            }  
          int value_clan_chest = ClanListener.getClanChestValue(clan.getChestLocation().getBlock());
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.needs/0", ChatColor.GREEN, player.getUniqueId())) + 
              ChatColor.AQUA + ChatColor.BOLD + value + ChatColor.GREEN + ".");
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.needs/1", ChatColor.GREEN, player.getUniqueId())) + 
              ChatColor.AQUA + ChatColor.BOLD + value_clan_chest + ChatColor.GREEN + ".");
          if (value_clan_chest < value) {
            player.sendMessage(plugin.getBoldedTextFromLanguage("clan_command.needs/2", ChatColor.RED, 
                  player.getUniqueId()));
          } else {
            player.sendMessage(plugin.getBoldedTextFromLanguage("clan_command.needs/3", ChatColor.GREEN, 
                  player.getUniqueId()));
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.values/0", ChatColor.YELLOW, 
                player.getUniqueId()));
          player.sendMessage("");
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.values/1", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + plugin.getConfig().getInt("chest_values.cake"));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.values/2", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + plugin.getConfig().getInt("chest_values.netherite"));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.values/3", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + plugin.getConfig().getInt("chest_values.emerald"));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.values/4", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + plugin.getConfig().getInt("chest_values.diamond"));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.values/5", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + plugin.getConfig().getInt("chest_values.iron"));
          if (clan.hasEgg().booleanValue())
            player.sendMessage(ChatColor.GREEN + 
                plugin.getTextFromLanguage("clan_command.egg_protection/0", ChatColor.GREEN, 
                  player.getUniqueId()) + 
                plugin.getBoldedTextFromLanguage("clan_command.egg_protection/1", 
                  ChatColor.DARK_PURPLE, player.getUniqueId()) + 
                plugin.getTextFromLanguage("clan_command.egg_protection/2", ChatColor.GREEN, 
                  player.getUniqueId())); 
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("reset") && (
          !player.isOp() || !player.hasPermission("clan.reset"))) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_permission", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("reset") && (
          args.length > 2 || (args.length == 2 && !args[1].equalsIgnoreCase("confirm")))) {
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_reset", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("reset")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.reset/0", ChatColor.RED, player.getUniqueId()));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.reset/1", ChatColor.RED, player.getUniqueId())) + 
              ChatColor.BOLD + "confirm");
          return true;
        } 
        if (args.length == 2 && args[0].equalsIgnoreCase("reset") && args[1].equalsIgnoreCase("confirm")) {
          Bukkit.broadcastMessage(plugin.getBoldedTextFromLanguage("clan_command.reset/2", ChatColor.DARK_RED, 
                player.getUniqueId()));
          Main.clan_command_cancel = true;
          int sec = 5;
          final String reset_msg = plugin.getBoldedTextFromLanguage("clan_command.reset/3", ChatColor.DARK_PURPLE, 
              player.getUniqueId());

          plugin.getServer().getScheduler().scheduleSyncDelayedTask((Plugin)plugin, new Runnable() {
                public void run() {
                  for (Player online : Bukkit.getOnlinePlayers()) {
                    for (int i = 0; i < 9; i++) {
                      ItemStack item = online.getInventory().getItem(i);
                      if (item != null && 
                        item.getItemMeta().getDisplayName()
                        .equals(ChatColor.DARK_GREEN + "Clan Chest"))
                        online.getInventory().remove(item); 
                    } 
                    online.setPlayerListName(online.getDisplayName());
                  } 
                  for (Clan c : Main.clan_list)
                    c.getChestLocation().getBlock().setType(Material.AIR); 
                  Main.clan_list.clear();
                  Main.requestClan.clear();
                  Bukkit.broadcastMessage(reset_msg);
                  Main.clan_command_cancel = false;
                }
              });
          return true;
        } 
        if (args.length > 0 && 
          args[0].equalsIgnoreCase("delete")) {
          if (args.length != 2) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_delete", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (player.isOp() || player.hasPermission("clan.delete")) {
            for (Clan c : Main.clan_list) {
              if (TchatListener.removeChatColor(c.getName()).equals(args[1])) {
                Chest chest = (Chest)c.getChestLocation().getBlock().getState();
                chest.setCustomName("Chest");
                for (Player online : Bukkit.getOnlinePlayers()) {
                  if (c.isClanMember(online.getUniqueId()).booleanValue()) {
                    online.sendMessage(plugin.getTextFromLanguage("clan_command.clan_deleted", 
                          ChatColor.RED, player.getUniqueId()));
                    online.setPlayerListName(online.getDisplayName());
                  } 
                } 
                for (Clan c2 : Main.clan_list) {
                  if (c2.getWarTarget() != null && 
                    c2.getWarTarget().equals(c.getChestLocation())) {
                    c2.stopWar();
                    for (Player online : Bukkit.getOnlinePlayers()) {
                      if (c2.isClanMember(online.getUniqueId()).booleanValue() || 
                        c.isClanMember(online.getUniqueId()).booleanValue())
                        online.setScoreboard(
                            Bukkit.getScoreboardManager().getNewScoreboard()); 
                    } 
                  } 
                } 
                for (List<Player> list_player : (Iterable<List<Player>>)Main.requestClan) {
                  if (((Player)list_player.get(0)).getUniqueId().equals(c.getLeader()) || (
                    (Player)list_player.get(1)).getUniqueId().equals(c.getLeader()))
                    Main.requestClan.remove(list_player); 
                } 
                Main.clan_list.remove(c);
              } 
            } 
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_not_found", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_permission", 
                ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if ((args.length > 3 || args.length == 1) && args[0].equalsIgnoreCase("war")) {
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.error_war", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 2 && args[0].equalsIgnoreCase("war")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_leader_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war_2", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          final Clan clan_target = Main.getClanByName(args[1]);
          if (clan_target == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_not_found", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          if (clan_target.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_target_in_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan_target.getName().equals(clan.getName())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_target_own", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          int sender_count = 0;
          int target_count = 0;
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (clan.isClanMember(online.getUniqueId()).booleanValue())
              sender_count++; 
            if (clan_target.isClanMember(online.getUniqueId()).booleanValue())
              target_count++; 
          } 
          if (sender_count > target_count + plugin.getConfig().getInt("excess_number_player") || 
            target_count == 0)
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_war_not_enough", 
                  ChatColor.RED, player.getUniqueId())); 
          String pluriel = "";
          if (target_count > 1 && plugin.language.equalsIgnoreCase("english"))
            pluriel = "s"; 
          player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.war/0", ChatColor.RED, 
                  player.getUniqueId())) + target_count + 
              plugin.getTextFromLanguage("clan_command.war/1", ChatColor.RED, player.getUniqueId()) + 
              pluriel + 
              plugin.getTextFromLanguage("clan_command.war/2", ChatColor.RED, player.getUniqueId()));
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.war/3", ChatColor.RED, player.getUniqueId()));
          return true;
        } 
        if (args.length == 3 && args[0].equalsIgnoreCase("war") && args[2].equalsIgnoreCase("confirm")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_leader_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_war_2", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          final Clan clan_target = Main.getClanByName(args[1]);
          if (clan_target == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_not_found", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          if (clan_target.isInWar().booleanValue()) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_target_in_war", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (clan_target.getName().equals(clan.getName())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_target_own", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          int sender_count = 0;
          int target_count = 0;
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (clan.isClanMember(online.getUniqueId()).booleanValue())
              sender_count++; 
            if (clan_target.isClanMember(online.getUniqueId()).booleanValue())
              target_count++; 
          } 
          if (sender_count > target_count + plugin.getConfig().getInt("excess_number_player") || 
            target_count == 0) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_war_not_enough", 
                  ChatColor.RED, player.getUniqueId()));
            return true;
          } 
          for (Player online : Bukkit.getOnlinePlayers())
            online.sendMessage(String.valueOf(plugin.getBoldedTextFromLanguage("clan_command.war_begun/0", ChatColor.RED, 
                    clan.getLeader())) + 
                
                plugin.getBoldedTextFromLanguage("clan_command.war_begun/1", ChatColor.RED, 
                  clan_target.getLeader())); 
          clan.declareWar(clan_target.getChestLocation());
          clan_target.declareWar(clan.getChestLocation());
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (clan.isClanMember(online.getUniqueId()).booleanValue()) {
              Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
              Objective obj = board.registerNewObjective("Kills", "null", "null");
              obj.setDisplaySlot(DisplaySlot.SIDEBAR);
              obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "War Kills");
              Score score = obj.getScore("");
              score.setScore(3);
              Score score1 = obj.getScore(ChatColor.YELLOW + "{" + clan.getNameWithColors() + 
                  ChatColor.YELLOW + "}: " + ChatColor.AQUA + "0");
              score1.setScore(2);
              Score score2 = obj.getScore(ChatColor.YELLOW + "{" + clan_target.getNameWithColors() + 
                  ChatColor.YELLOW + "}: " + ChatColor.AQUA + "0");
              score2.setScore(1);
              online.setScoreboard(board);
              continue;
            } 
            if (clan_target.isClanMember(online.getUniqueId()).booleanValue()) {
              Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
              Objective obj = board.registerNewObjective("Kills", "null", "null");
              obj.setDisplaySlot(DisplaySlot.SIDEBAR);
              obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "War Kills");
              Score score = obj.getScore("");
              score.setScore(3);
              Score score1 = obj.getScore(ChatColor.YELLOW + "{" + clan_target.getNameWithColors() + 
                  ChatColor.YELLOW + "}: " + ChatColor.AQUA + "0");
              score1.setScore(2);
              Score score2 = obj.getScore(ChatColor.YELLOW + "{" + clan.getNameWithColors() + 
                  ChatColor.YELLOW + "}: " + ChatColor.AQUA + "0");
              score2.setScore(1);
              online.setScoreboard(board);
            } 
          } 
          (new BukkitRunnable() {
              public void run() {
                try {
                  if (!clan.isInWar().booleanValue() || !clan_target.isInWar().booleanValue())
                    return; 
                } catch (Exception e) {
                  return;
                } 
                int winner = 0;
                if (clan.getWarKills() > clan_target.getWarKills()) {
                  winner = 1;
                } else if (clan.getWarKills() < clan_target.getWarKills()) {
                  winner = 2;
                } 
                for (Player online : Bukkit.getOnlinePlayers()) {
                  int old_clan_target_level, old_clan_level;
                  String pluriel;
                  if (clan.isClanMember(online.getUniqueId()).booleanValue() || 
                    clan_target.isClanMember(online.getUniqueId()).booleanValue())
                    online.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); 
                  online.sendMessage(String.valueOf(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_ended/0", 
                          ChatColor.RED, clan.getLeader())) + 
                      ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_ended/1", ChatColor.RED, 
                        clan_target.getLeader()));
                  switch (winner) {
                    case 1:
                      old_clan_target_level = clan_target.getLevel();
                      clan.addLevel();
                      clan_target.soustLevel();
                      pluriel = "";
                      if (clan.getWarKills() > 1 && ClanCommand.plugin.language.equalsIgnoreCase("english"))
                        pluriel = "s"; 
                      online.sendMessage(String.valueOf(ClanCommand.plugin.getTextFromLanguage("clan_command.war_bilan/0", 
                              ChatColor.RED, clan.getLeader())) + ChatColor.AQUA + clan.getWarKills() + 
                          
                          ClanCommand.plugin.getTextFromLanguage("clan_command.war_bilan/1", ChatColor.RED, 
                            clan.getLeader()) + 
                          pluriel + "!");
                      if (clan.isClanMember(online.getUniqueId()).booleanValue()) {
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/2", 
                              ChatColor.YELLOW, player.getUniqueId()));
                        continue;
                      } 
                      if (clan_target.isClanMember(online.getUniqueId()).booleanValue() && 
                        old_clan_target_level != clan_target.getLevel()) {
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/3", 
                              ChatColor.RED, player.getUniqueId()));
                        continue;
                      } 
                      if (clan_target.isClanMember(online.getUniqueId()).booleanValue() && 
                        old_clan_target_level == clan_target.getLevel())
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/4", 
                              ChatColor.RED, player.getUniqueId())); 
                    case 2:
                      old_clan_level = clan.getLevel();
                      clan_target.addLevel();
                      clan.soustLevel();
                      pluriel = "";
                      if (clan_target.getWarKills() > 1 && ClanCommand.plugin.language.equalsIgnoreCase("english"))
                        pluriel = "s"; 
                      online.sendMessage(String.valueOf(ClanCommand.plugin.getTextFromLanguage("clan_command.war_bilan/0", 
                              ChatColor.RED, clan_target.getLeader())) + ChatColor.AQUA + 
                          clan_target.getWarKills() + 
                          
                          ClanCommand.plugin.getTextFromLanguage("clan_command.war_bilan/1", ChatColor.RED, 
                            clan_target.getLeader()) + 
                          pluriel + "!");
                      if (clan_target.isClanMember(online.getUniqueId()).booleanValue()) {
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/2", 
                              ChatColor.YELLOW, player.getUniqueId()));
                        continue;
                      } 
                      if (clan.isClanMember(online.getUniqueId()).booleanValue() && 
                        old_clan_level != clan.getLevel()) {
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/3", 
                              ChatColor.RED, player.getUniqueId()));
                        continue;
                      } 
                      if (clan.isClanMember(online.getUniqueId()).booleanValue() && 
                        old_clan_level == clan.getLevel())
                        online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/4", 
                              ChatColor.RED, player.getUniqueId())); 
                    case 0:
                      online.sendMessage(ClanCommand.plugin.getBoldedTextFromLanguage("clan_command.war_bilan/5", 
                            ChatColor.RED, player.getUniqueId()));
                  } 
                } 
              }
            }).runTaskLater((Plugin)plugin, 20L * plugin.getConfig().getInt("war_time"));
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("chat")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (args.length == 1) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /clan chat <MESSAGE>");
            return true;
          } 
          String message = "";
          for (int i = 1; i < args.length; i++)
            message = String.valueOf(message) + args[i] + " "; 
          ChatColor color = ChatColor.WHITE;
          if (clan.getLeader().equals(player.getUniqueId()))
            color = ChatColor.DARK_RED; 
          for (UUID clan_member_uuid : clan.getMembersUUID()) {
            Player clan_member = Bukkit.getPlayer(clan_member_uuid);
            if (clan_member != null && clan_member.isOnline())
              clan_member.sendMessage(
                  ChatColor.GRAY + "" + ChatColor.ITALIC + "(priv) " + ChatColor.RESET + 
                  ChatColor.WHITE + "{" + clan.getNameWithColors() + ChatColor.WHITE + 
                  "} " + color + player.getName() + ChatColor.WHITE + ": " + message); 
          } 
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("kick")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (args.length == 1) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /clan kick <PSEUDO>");
            return true;
          } 
          if (args[1].equals(player.getName())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_kick_self", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          final Player target = Bukkit.getPlayer(args[1]);
          if (target == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.player_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          clan.removePlayer(target.getUniqueId());
          target.setDisplayName(target.getName());
          for (UUID clan_member_uuid : clan.getMembersUUID()) {
            Player clan_member = Bukkit.getPlayer(clan_member_uuid);
            if (clan_member != null && clan_member.isOnline())
              clan_member.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_member_removed/0", 
                      ChatColor.RED, player.getUniqueId())) + 
                  plugin.getTextFromLanguage("clan_command.clan_member_removed/1", ChatColor.RED, 
                    target.getUniqueId())); 
          } 
          target.sendMessage(plugin.getTextFromLanguage("clan_command.clan_member_removed_self", 
                ChatColor.RED, player.getUniqueId()));
          target.setPlayerListName(target.getDisplayName());
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("private")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (args.length > 1) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /clan private");
            return true;
          } 
          if (clan.isPrivate().booleanValue()) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_already_privacy", 
                    ChatColor.RED, player.getUniqueId())) + "private");
            return true;
          } 
          clan.togglePrivacy();
          player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_set_privacy", ChatColor.GREEN, 
                  player.getUniqueId())) + "private");
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("public")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (args.length > 1) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /clan public");
            return true;
          } 
          if (clan.isPublic().booleanValue()) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_already_privacy", 
                    ChatColor.RED, player.getUniqueId())) + "public");
            return true;
          } 
          clan.togglePrivacy();
          player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_set_privacy", ChatColor.GREEN, 
                  player.getUniqueId())) + "public");
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("description")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (!clan.getLeader().equals(player.getUniqueId())) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_leader", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          if (args.length == 1) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.error_info", ChatColor.RED, 
                    player.getUniqueId())) + " /clan description <DESCRIPTION>");
            return true;
          } 
          String descr = "";
          for (int i = 1; i < args.length; i++)
            descr = String.valueOf(descr) + args[i] + " "; 
          clan.setDescription(descr);
          player.sendMessage(plugin.getTextFromLanguage("clan_command.description_saved", ChatColor.GREEN, 
                player.getUniqueId()));
          return true;
        } 
        if (args.length == 1 && args[0].equalsIgnoreCase("info")) {
          if (clan == null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_not_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.clan_info/0", ChatColor.GOLD, 
                player.getUniqueId()));
          player.sendMessage(
              plugin.getTextFromLanguage("clan_command.clan_info/1", ChatColor.GOLD, clan.getLeader()));
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.clan_info/2", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.AQUA + ChatColor.BOLD + clan.getLevel());
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.clan_info/3", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.YELLOW + clan.getPrivacy());
          player.sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_command.clan_info/4", ChatColor.GOLD, player.getUniqueId())) + 
              ChatColor.YELLOW + clan.getDescription());
          if (clan.isProtected().booleanValue()) {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_info/5", ChatColor.BLUE, 
                    player.getUniqueId())) + 
                plugin.getBoldedTextFromLanguage("clan_command.clan_info/6", ChatColor.GREEN, 
                  player.getUniqueId()));
          } else {
            player.sendMessage(String.valueOf(plugin.getTextFromLanguage("clan_command.clan_info/5", ChatColor.BLUE, 
                    player.getUniqueId())) + 
                plugin.getBoldedTextFromLanguage("clan_command.clan_info/7", ChatColor.RED, 
                  player.getUniqueId()));
          } 
          return true;
        } 
        if (args.length >= 1 && args[0].equalsIgnoreCase("join")) {
          if (clan != null) {
            player.sendMessage(plugin.getTextFromLanguage("clan_command.error_already_clan", ChatColor.RED, 
                  player.getUniqueId()));
            return true;
          } 
          for (Clan c : Main.clan_list) {
            if (TchatListener.removeChatColor(c.getName()).equals(args[1])) {
              if (c.isPublic().booleanValue()) {
                player.sendMessage(
                    plugin.getTextFromLanguage("invitations.join", ChatColor.GREEN, c.getLeader()));
                for (UUID uuid : c.getMembersUUID()) {
                  Player clan_member = Bukkit.getPlayer(uuid);
                  if (clan_member != null && clan_member.isOnline())
                    clan_member.sendMessage(plugin.getTextFromLanguage("invitations.joined", 
                          ChatColor.GREEN, player.getUniqueId())); 
                } 
                c.addPlayer(player.getUniqueId(), player.getName());
                player.setPlayerListName(c.getPlayerTabString(player));
              } else {
                player.sendMessage(plugin.getTextFromLanguage("clan_command.clan_private", 
                      ChatColor.RED, c.getLeader()));
              } 
              return true;
            } 
          } 
          player.sendMessage(plugin.getTextFromLanguage("clan_command.error_clan_not_exist", ChatColor.RED, 
                player.getUniqueId()));
          return true;
        } 
        player.sendMessage(
            plugin.getTextFromLanguage("clan_command.error_args", ChatColor.RED, player.getUniqueId()));
      } else {
        sender.sendMessage(plugin.getTextFromLanguage("clan_command.error_console", null, null));
        return true;
      }  
    return false;
  }
}
