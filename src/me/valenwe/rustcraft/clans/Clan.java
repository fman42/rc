package me.valenwe.rustcraft.clans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import me.valenwe.rustcraft.Main;
import me.valenwe.rustcraft.TchatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Clan {
  private UUID leader;
  
  private Location clan_chest;
  
  private String clan_name;
  
  private int clan_level;
  
  private HashMap<UUID, String> player_list = new HashMap<>();
  
  private Boolean protection;
  
  private Boolean hasEgg;
  
  private Boolean isChanging;
  
  private Boolean inWar;
  
  private Location warTarget;
  
  private int warKills;
  
  private String privacy;
  
  private String description;
  
  public Clan(UUID leader, int x_chest, int y_chest, int z_chest, String clan_name, int clan_level, HashMap<UUID, String> player_list, Boolean protection, Boolean hasEgg, Boolean isChanging, Boolean inWar, Location warTarget, int warKills, String privacy, String description) {
    setLeader(leader);
    setChestLocation(x_chest, y_chest, z_chest);
    setClanName(clan_name);
    this.clan_level = clan_level;
    this.player_list = player_list;
    setProtection(protection);
    setHasEgg(hasEgg);
    setIsChanging(isChanging);
    setInWar(inWar);
    this.warTarget = warTarget;
    this.warKills = warKills;
    this.privacy = privacy;
    this.description = description;
  }
  
  public void setLeader(UUID leader) {
    this.leader = leader;
  }
  
  public void setChestLocation(int x, int y, int z) {
    this.clan_chest = Bukkit.getWorld(Main.world_name).getBlockAt(x, y, z).getLocation();
  }
  
  public void setClanName(String name) {
    this.clan_name = name;
  }
  
  public void setProtection(Boolean protection) {
    this.protection = protection;
  }
  
  public void setHasEgg(Boolean hasEgg) {
    this.hasEgg = hasEgg;
  }
  
  public void setIsChanging(Boolean isChanging) {
    this.isChanging = isChanging;
  }
  
  public void setInWar(Boolean inWar) {
    this.inWar = inWar;
  }
  
  public void declareWar(Location loc) {
    this.warTarget = loc;
    this.warKills = 0;
    setInWar(Boolean.valueOf(true));
  }
  
  public void stopWar() {
    this.warTarget = null;
    this.warKills = 0;
    setInWar(Boolean.valueOf(false));
  }
  
  public void addWarKill() {
    this.warKills++;
  }
  
  public void addPlayer(UUID uuid, String name) {
    this.player_list.put(uuid, name);
  }
  
  public void removePlayer(UUID uuid) {
    this.player_list.remove(uuid);
  }
  
  public Boolean isClanMember(UUID uuid) {
    return Boolean.valueOf(this.player_list.keySet().contains(uuid));
  }
  
  public UUID getLeader() {
    return this.leader;
  }
  
  public String getName() {
    return this.clan_name;
  }
  
  public Location getChestLocation() {
    return this.clan_chest;
  }
  
  public int getLevel() {
    return this.clan_level;
  }
  
  public HashMap<UUID, String> getClanMembers() {
    return this.player_list;
  }
  
  public Set<UUID> getMembersUUID() {
    return this.player_list.keySet();
  }
  
  public Collection<String> getMembersName() {
    return this.player_list.values();
  }
  
  public Location getWarTarget() {
    return this.warTarget;
  }
  
  public String getNameWithColors() {
    return TchatListener.stringtoChatColor(getName()) + TchatListener.removeChatColor(getName());
  }
  
  public int getWarKills() {
    return this.warKills;
  }
  
  public void addLevel() {
    if (this.clan_level <= 4)
      this.clan_level++; 
  }
  
  public void soustLevel() {
    if (this.clan_level > 1)
      this.clan_level--; 
  }
  
  public Boolean isProtected() {
    return this.protection;
  }
  
  public Boolean isIn(Location loc) {
    if (!loc.getWorld().getName().equals(Main.world_name))
      return Boolean.valueOf(false); 
    int current_range = getRange();
    if (Math.abs(getChestLocation().getX() - loc.getX()) < current_range && 
      Math.abs(getChestLocation().getZ() - loc.getZ()) < current_range)
      return Boolean.valueOf(true); 
    return Boolean.valueOf(false);
  }
  
  public int getRange() {
    int range = 0;
    switch (this.clan_level) {
      case 1:
        range = Main.radius_lvl_1;
        break;
      case 2:
        range = Main.radius_lvl_2;
        break;
      case 3:
        range = Main.radius_lvl_3;
        break;
      case 4:
        range = Main.radius_lvl_4;
        break;
      case 5:
        range = Main.radius_lvl_5;
        break;
    } 
    return range;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String d) {
    this.description = d;
  }
  
  public String getPrivacy() {
    return this.privacy;
  }
  
  public void togglePrivacy() {
    if (this.privacy.equals("private")) {
      this.privacy = "public";
    } else {
      this.privacy = "private";
    } 
  }
  
  public Boolean hasEgg() {
    return this.hasEgg;
  }
  
  public Boolean isChanging() {
    return this.isChanging;
  }
  
  public Boolean isInWar() {
    return this.inWar;
  }
  
  public String getPlayerTabString(Player player) {
    if (getLeader().equals(player.getUniqueId()))
      return "{" + TchatListener.stringtoChatColor(this.clan_name) + TchatListener.removeChatColor(this.clan_name) + 
        ChatColor.WHITE + "} " + ChatColor.DARK_RED + player.getDisplayName(); 
    return "{" + TchatListener.stringtoChatColor(this.clan_name) + TchatListener.removeChatColor(this.clan_name) + 
      ChatColor.WHITE + "} " + ChatColor.WHITE + player.getDisplayName();
  }
  
  public Boolean isPrivate() {
    return Boolean.valueOf(this.privacy.equals("private"));
  }
  
  public Boolean isPublic() {
    return Boolean.valueOf(this.privacy.equals("public"));
  }
}
