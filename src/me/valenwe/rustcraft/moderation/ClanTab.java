package me.valenwe.rustcraft.moderation;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ClanTab implements TabCompleter {
  List<String> clan_args = new ArrayList<>();
  
  List<String> rustcraft_args = new ArrayList<>();
  
  List<String> null_args = new ArrayList<>();
  
  List<String> confirm_args = new ArrayList<>();
  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    if (this.clan_args.isEmpty()) {
      this.clan_args.add("change");
      this.clan_args.add("name");
      this.clan_args.add("list");
      this.clan_args.add("members");
      this.clan_args.add("leave");
      this.clan_args.add("tp");
      this.clan_args.add("create");
      this.clan_args.add("needs");
      this.clan_args.add("reset");
      this.clan_args.add("war");
      this.clan_args.add("reload");
      this.clan_args.add("invite");
      this.clan_args.add("lead");
      this.clan_args.add("chat");
      this.clan_args.add("kick");
      this.clan_args.add("description");
      this.clan_args.add("info");
      this.clan_args.add("public");
      this.clan_args.add("private");
      this.clan_args.add("join");
    } 
    if (this.rustcraft_args.isEmpty()) {
      this.rustcraft_args.add("info");
      this.rustcraft_args.add("reload");
    } 
    if (this.null_args.isEmpty()) {
      this.null_args.add("invite");
      this.null_args.add("lead");
      this.null_args.add("kick");
    } 
    if (this.confirm_args.isEmpty()) {
      this.confirm_args.add("leave");
      this.confirm_args.add("reset");
      this.confirm_args.add("war");
    } 
    List<String> result = new ArrayList<>();
    if (args.length == 1) {
      if (label.equalsIgnoreCase("clan")) {
        for (String a : this.clan_args) {
          if (a.toLowerCase().startsWith(args[0].toLowerCase()))
            result.add(a); 
        } 
      } else if (label.equalsIgnoreCase("rustcraft")) {
        for (String a : this.rustcraft_args) {
          if (a.toLowerCase().startsWith(args[0].toLowerCase()))
            result.add(a); 
        } 
      } 
    } else if (args.length == 2) {
      for (String a : this.null_args) {
        if (a.toLowerCase().startsWith(args[0].toLowerCase()))
          return null; 
      } 
      if (label.equalsIgnoreCase("clan"))
        for (String a : this.confirm_args) {
          if (a.equalsIgnoreCase(args[0]))
            result.add("confirm"); 
        }  
    } else if (args.length == 3 && 
      label.equalsIgnoreCase("clan")) {
      for (String a : this.confirm_args) {
        if (a.equalsIgnoreCase(args[0]))
          result.add("confirm"); 
      } 
    } 
    return result;
  }
}
