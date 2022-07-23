package me.valenwe.rustcraft;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.valenwe.rustcraft.clans.Clan;
import me.valenwe.rustcraft.clans.ClanCommand;
import me.valenwe.rustcraft.clans.ClanListener;
import me.valenwe.rustcraft.moderation.ClanTab;
import me.valenwe.rustcraft.moderation.StructureGenerator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Main extends JavaPlugin {
  public static ArrayList<Player> previous_list_player = new ArrayList<>();
  
  public static ArrayList<ItemStack> loot_rust_chest_items = new ArrayList<>();
  
  public static ArrayList<Double> loot_rust_chest_chance = new ArrayList<>();
  
  public static ArrayList<ItemStack> loot_rust_advanced_chest_items = new ArrayList<>();
  
  public static ArrayList<Double> loot_rust_advanced_chest_chance = new ArrayList<>();
  
  public static ArrayList<String> list_structure = new ArrayList<>();
  
  public static ArrayList<Double> list_structure_chances = new ArrayList<>();
  
  public static ArrayList<List<Player>> requestClan = new ArrayList<>();
  
  public static ArrayList<Location> list_chest = new ArrayList<>();
  
  public static boolean protection;
  
  public static boolean clan_command_cancel = false;
  
  public static String world_name;
  
  public static ArrayList<Clan> clan_list = new ArrayList<>();
  
  public static int radius_lvl_1;
  
  public static int radius_lvl_2;
  
  public static int radius_lvl_3;
  
  public static int radius_lvl_4;
  
  public static int radius_lvl_5;
  
  public String language = "english";
  
  public FileConfiguration language_yaml = null;
  
  public void onEnable() {
    saveDefaultConfig();
    this.language_yaml = loadLanguageFile();
    registerCmds();
    getServer().getPluginManager().registerEvents((Listener)new ClanListener(this), (Plugin)this);
    if (getConfig().getBoolean("tchat"))
      Bukkit.getPluginManager().registerEvents(new TchatListener(), (Plugin)this); 
    Bukkit.getPluginManager().registerEvents((Listener)new StructureGenerator(this), (Plugin)this);
    int pluginId = 7143;
    insertItemAndStructure();
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.leather")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.gunpowder")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.saddle")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.iron_sword")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.wheat")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.music_disc")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.music_disc")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.music_disc")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.music_disc")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.golden_apple")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.chainmail_stuff")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.chainmail_stuff")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.chainmail_stuff")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.chainmail_stuff")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.iron_pickaxe")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.lapis_lazuli")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.egg")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.coal")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.melon_seed")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.lead")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.gold_ingot")));
    loot_rust_chest_chance.add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_chest.name_tag")));
    loot_rust_advanced_chest_chance.add(
        Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_golden_apple")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_sword")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_pickaxe")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_stuff")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_stuff")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_stuff")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_stuff")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.enchanted_book")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.cake")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.diamond_horse_armor")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.golden_horse_armor")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.emerald")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.potion")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.potion")));
    loot_rust_advanced_chest_chance
      .add(Double.valueOf(getConfig().getDouble("loot_chances.rustcraft_advanced_chest.potion")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.ennemy_base")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.furnace_base")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.half_pipe")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.hangar")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.shack")));
    list_structure_chances.add(Double.valueOf(getConfig().getDouble("structure_generation_chances.little_tower")));
    world_name = getConfig().getString("world_name");
    radius_lvl_1 = getConfig().getInt("radius_per_level.lvl_1");
    radius_lvl_2 = getConfig().getInt("radius_per_level.lvl_2");
    radius_lvl_3 = getConfig().getInt("radius_per_level.lvl_3");
    radius_lvl_4 = getConfig().getInt("radius_per_level.lvl_4");
    radius_lvl_5 = getConfig().getInt("radius_per_level.lvl_5");
    if (getConfig().contains("data")) {
      restoreData();
      getConfig().set("data", null);
      saveConfig();
    } 
    this.language = getConfig().getString("language");
    int sec = getConfig().getInt("time_each_check_clan_value");
    final String egg_protection_0 = getTextFromLanguage("main.egg_protection/0", (ChatColor)null, (UUID)null);
    final String egg_protection_1 = getTextFromLanguage("main.egg_protection/1", (ChatColor)null, (UUID)null);
    final String egg_protection_2 = getTextFromLanguage("main.egg_protection/2", (ChatColor)null, (UUID)null);
    final String wipe_message_0 = getTextFromLanguage("main.wipe_message/0", (ChatColor)null, (UUID)null);
    final String wipe_message_1 = getTextFromLanguage("main.wipe_message/1", (ChatColor)null, (UUID)null);
    final String wipe_message_2 = getTextFromLanguage("main.wipe_message/2", (ChatColor)null, (UUID)null);
    final String wipe_message_3 = getTextFromLanguage("main.wipe_message/3", (ChatColor)null, (UUID)null);
    getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)this, new Runnable() {
          public void run() {
            for (Player online : Bukkit.getOnlinePlayers()) {
              if (!Main.previous_list_player.contains(online))
                Main.previous_list_player.add(online); 
            } 
            for (Clan clan : Main.clan_list) {
              if (clan.hasEgg().booleanValue()) {
                for (Player online : Bukkit.getOnlinePlayers()) {
                  if (clan.isClanMember(online.getUniqueId()).booleanValue())
                    online.sendMessage(ChatColor.GREEN + egg_protection_0 + ChatColor.DARK_PURPLE + 
                        ChatColor.BOLD + egg_protection_1 + ChatColor.RESET + ChatColor.GREEN + 
                        egg_protection_2); 
                } 
                continue;
              } 
              if (clan.isChanging().booleanValue()) {
                clan.setProtection(Boolean.valueOf(false));
                for (Player online : Bukkit.getOnlinePlayers()) {
                  if (clan.isClanMember(online.getUniqueId()).booleanValue())
                    online.sendMessage(ChatColor.RED + wipe_message_3); 
                } 
                continue;
              } 
              Block clan_chest = clan.getChestLocation().getBlock();
              int value = 0;
              for (Location loc : Main.list_chest) {
                if (clan.isIn(loc).booleanValue()) {
                  Chest chest = (Chest)loc.getBlock().getState();
                  value += ClanListener.getValueInventory(chest);
                } 
              } 
              clan.setProtection(Boolean.valueOf(ClanListener.removeRessourcesNeeded(clan_chest, value)));
              for (Player online : Bukkit.getOnlinePlayers()) {
                if (clan.isClanMember(online.getUniqueId()).booleanValue()) {
                  if (clan.isProtected().booleanValue()) {
                    online.sendMessage(ChatColor.GREEN + wipe_message_1);
                    online.sendMessage(ChatColor.GREEN + wipe_message_2);
                    continue;
                  } 
                  online.sendMessage(ChatColor.RED + wipe_message_0);
                } 
              } 
            } 
            Main.previous_list_player.clear();
            for (Player player : Bukkit.getOnlinePlayers())
              Main.previous_list_player.add(player); 
          }
        }, 20L * sec, 20L * sec);
    Bukkit.getConsoleSender().sendMessage("[Rustcraft] " + ChatColor.GREEN + "Rustcraft v." + 
        getDescription().getVersion() + " successfully launched!");
  }
  
  public void onDisable() {
    saveData();
  }
  
  public void registerCmds() {
    getCommand("clan").setExecutor((CommandExecutor)new ClanCommand(this));
    getCommand("clan").setTabCompleter((TabCompleter)new ClanTab());
    getCommand("accept").setExecutor(new AcceptDeclineCommand(this));
    getCommand("accept").setTabCompleter((TabCompleter)new ClanTab());
    getCommand("decline").setExecutor(new AcceptDeclineCommand(this));
    getCommand("decline").setTabCompleter((TabCompleter)new ClanTab());
    getCommand("rustcraft").setExecutor((CommandExecutor)new ClanCommand(this));
    getCommand("rustcraft").setTabCompleter((TabCompleter)new ClanTab());
  }
  
  public void saveData() {
    if (!list_chest.isEmpty())
      for (int i = 0; i < list_chest.size(); i++) {
        Location loc = list_chest.get(i);
        getConfig().set("data.list_chest." + i, getLocToString(loc));
      }  
    if (!clan_list.isEmpty())
      for (int i = 0; i < clan_list.size(); i++) {
        Clan clan = clan_list.get(i);
        getConfig().set("data.clan_list." + i + ".leader", clan.getLeader().toString());
        getConfig().set("data.clan_list." + i + ".chest_location", 
            getLocToString(clan.getChestLocation()));
        getConfig().set("data.clan_list." + i + ".name", clan.getName());
        getConfig().set("data.clan_list." + i + ".level", Integer.valueOf(clan.getLevel()));
        getConfig().set("data.clan_list." + i + ".protected", clan.isProtected());
        getConfig().set("data.clan_list." + i + ".isChanging", clan.isChanging());
        getConfig().set("data.clan_list." + i + ".hasEgg", clan.hasEgg());
        getConfig().set("data.clan_list." + i + ".privacy", clan.getPrivacy());
        getConfig().set("data.clan_list." + i + ".description", clan.getDescription());
        int I = 0;
        for (UUID uuid : clan.getClanMembers().keySet()) {
          String name = (String)clan.getClanMembers().get(uuid);
          ArrayList<String> uuid_name = new ArrayList<>();
          uuid_name.add(uuid.toString());
          uuid_name.add(name);
          getConfig().set("data.clan_list." + i + ".player_list." + I, uuid_name);
          I++;
        } 
      }  
    saveConfig();
  }
  
  public void restoreData() {
    if (getConfig().contains("data.clan_list"))
      getConfig().getConfigurationSection("data.clan_list").getKeys(false).forEach(key -> {
            String leader_string = getConfig().getString("data.clan_list." + key + ".leader");
            String chest_location_string = getConfig().getString("data.clan_list." + key + ".chest_location");
            String clan_name = getConfig().getString("data.clan_list." + key + ".name");
            String clan_level = getConfig().getString("data.clan_list." + key + ".level");
            Boolean protection = Boolean.valueOf(getConfig().getBoolean("data.clan_list." + key + ".protected"));
            Boolean isChanging = Boolean.valueOf(getConfig().getBoolean("data.clan_list." + key + ".isChanging"));
            Boolean hasEgg = Boolean.valueOf(getConfig().getBoolean("data.clan_list." + key + ".hasEgg"));
            String privacy = getConfig().getString("data.clan_list." + key + ".privacy");
            String description = getConfig().getString("data.clan_list." + key + ".description");
            HashMap<UUID, String> player_list = new HashMap<>();
            for (String nb : getConfig().getConfigurationSection("data.clan_list." + key + ".player_list").getKeys(false)) {
              List<String> id_string = getConfig().getStringList("data.clan_list." + key + ".player_list." + nb);
              player_list.put(UUID.fromString(id_string.get(0)), id_string.get(1));
            } 
            Location loc = getStringToLoc(chest_location_string);
            clan_list.add(new Clan(UUID.fromString(leader_string), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), clan_name, Integer.valueOf(clan_level).intValue(), player_list, protection, hasEgg, isChanging, Boolean.valueOf(false), null, 0, privacy, description));
          }); 
    if (getConfig().contains("data.list_chest"))
      getConfig().getConfigurationSection("data.list_chest").getKeys(false).forEach(key -> {
            String loc_str = getConfig().getString("data.list_chest." + key.toString());
            Location loc = getStringToLoc(loc_str);
            list_chest.add(loc);
          }); 
  }
  
  public static String getLocToString(Location loc) {
    return String.valueOf((int)loc.getX()) + ";" + (int)loc.getY() + ";" + (int)loc.getZ() + ";" + loc.getWorld().getUID();
  }
  
  public static Location getStringToLoc(String s) {
    String[] parts = s.split(";");
    double x = Double.parseDouble(parts[0]);
    double y = Double.parseDouble(parts[1]);
    double z = Double.parseDouble(parts[2]);
    UUID u = UUID.fromString(parts[3]);
    World w = Bukkit.getServer().getWorld(u);
    return new Location(w, x, y, z);
  }
  
  public static void insertItemAndStructure() {
    loot_rust_chest_items.add(new ItemStack(Material.LEATHER));
    loot_rust_chest_items.add(new ItemStack(Material.GUNPOWDER));
    loot_rust_chest_items.add(new ItemStack(Material.SADDLE));
    loot_rust_chest_items.add(new ItemStack(Material.IRON_SWORD));
    loot_rust_chest_items.add(new ItemStack(Material.WHEAT));
    loot_rust_chest_items.add(new ItemStack(Material.MUSIC_DISC_MELLOHI));
    loot_rust_chest_items.add(new ItemStack(Material.MUSIC_DISC_WARD));
    loot_rust_chest_items.add(new ItemStack(Material.MUSIC_DISC_CHIRP));
    loot_rust_chest_items.add(new ItemStack(Material.MUSIC_DISC_CAT));
    loot_rust_chest_items.add(new ItemStack(Material.GOLDEN_APPLE));
    loot_rust_chest_items.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
    loot_rust_chest_items.add(new ItemStack(Material.CHAINMAIL_BOOTS));
    loot_rust_chest_items.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
    loot_rust_chest_items.add(new ItemStack(Material.CHAINMAIL_HELMET));
    loot_rust_chest_items.add(new ItemStack(Material.IRON_PICKAXE));
    loot_rust_chest_items.add(new ItemStack(Material.LAPIS_LAZULI));
    loot_rust_chest_items.add(new ItemStack(Material.EGG));
    loot_rust_chest_items.add(new ItemStack(Material.COAL));
    loot_rust_chest_items.add(new ItemStack(Material.MELON_SEEDS));
    loot_rust_chest_items.add(new ItemStack(Material.LEAD));
    loot_rust_chest_items.add(new ItemStack(Material.GOLD_INGOT));
    loot_rust_chest_items.add(new ItemStack(Material.NAME_TAG));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_SWORD));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_PICKAXE));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_BOOTS));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_HELMET));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_LEGGINGS));
    ItemStack enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    EnchantmentStorageMeta enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.MENDING, 1, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.FIRE_ASPECT, 2, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.LOOT_BONUS_MOBS, 2, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, 2, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    enchanted_book = new ItemStack(Material.ENCHANTED_BOOK);
    enchanted_meta = (EnchantmentStorageMeta)enchanted_book.getItemMeta();
    enchanted_meta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3, true);
    enchanted_book.setItemMeta((ItemMeta)enchanted_meta);
    loot_rust_advanced_chest_items.add(enchanted_book);
    loot_rust_advanced_chest_items.add(new ItemStack(Material.CAKE));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.DIAMOND_HORSE_ARMOR));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.GOLDEN_HORSE_ARMOR));
    loot_rust_advanced_chest_items.add(new ItemStack(Material.EMERALD));
    ItemStack potion = new ItemStack(Material.POTION);
    PotionMeta potion_meta = (PotionMeta)potion.getItemMeta();
    PotionEffect effect = new PotionEffect(PotionEffectType.SPEED, 2400, 2);
    potion_meta.addCustomEffect(effect, true);
    potion_meta.setColor(Color.AQUA);
    potion_meta.setDisplayName(ChatColor.AQUA + "Speed Potion");
    potion.setItemMeta((ItemMeta)potion_meta);
    loot_rust_advanced_chest_items.add(potion);
    potion = new ItemStack(Material.POTION);
    potion_meta = (PotionMeta)potion.getItemMeta();
    effect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2400, 2);
    potion_meta.addCustomEffect(effect, true);
    potion_meta.setColor(Color.RED);
    potion_meta.setDisplayName(ChatColor.RED + "Strengh Potion");
    potion.setItemMeta((ItemMeta)potion_meta);
    loot_rust_advanced_chest_items.add(potion);
    potion = new ItemStack(Material.POTION);
    potion_meta = (PotionMeta)potion.getItemMeta();
    effect = new PotionEffect(PotionEffectType.WATER_BREATHING, 4000, 1);
    potion_meta.addCustomEffect(effect, true);
    potion_meta.setColor(Color.BLUE);
    potion_meta.setDisplayName(ChatColor.BLUE + "Water Breathing Potion");
    potion.setItemMeta((ItemMeta)potion_meta);
    loot_rust_advanced_chest_items.add(potion);
    list_structure.add("ennemy_base");
    list_structure.add("furnace_base");
    list_structure.add("half_pipe");
    list_structure.add("hangar");
    list_structure.add("shack");
    list_structure.add("little_tower");
  }
  
  public static Clan getClan(UUID uuid) {
    for (Clan clan : clan_list) {
      if (clan.isClanMember(uuid).booleanValue())
        return clan; 
    } 
    return null;
  }
  
  public static Clan getClanByName(String clan_name) {
    for (Clan clan : clan_list) {
      if (TchatListener.removeChatColor(clan.getName()).equals(clan_name))
        return clan; 
    } 
    return null;
  }
  
  public static Boolean canClaim(Location loc, int max_range_level) {
    Double max_range = Double.valueOf((max_range_level * 2) * Math.sqrt(2.0D));
    for (Clan clan : clan_list) {
      if (!clan.isChanging().booleanValue()) {
        Location chest_loc = clan.getChestLocation();
        if (Math.abs(chest_loc.getX() - loc.getX()) < max_range.doubleValue() && 
          Math.abs(chest_loc.getZ() - loc.getZ()) < max_range.doubleValue())
          return Boolean.valueOf(false); 
      } 
    } 
    return Boolean.valueOf(true);
  }
  
  public static String placeHolderCheck(String msg, UUID uuid, ChatColor main_color) {
    String good_msg = msg;
    if (main_color == null)
      main_color = ChatColor.YELLOW; 
    if (msg.contains("{CLAN}")) {
      Clan c = getClan(uuid);
      if (c != null) {
        good_msg = good_msg.replace("{CLAN}", String.valueOf(c.getNameWithColors()) + main_color);
      } else {
        good_msg = good_msg.replace("{CLAN}", "");
      } 
    } 
    if (msg.contains("{PLAYER}")) {
      Player p = Bukkit.getPlayer(uuid);
      if (p != null) {
        good_msg = good_msg.replace("{PLAYER}", ChatColor.YELLOW + p.getName() + main_color);
      } else {
        Clan c = getClan(uuid);
        if (c != null) {
          good_msg = good_msg.replace("{PLAYER}", 
              ChatColor.YELLOW + (String)c.getClanMembers().get(uuid) + main_color);
        } else {
          good_msg = good_msg.replace("{PLAYER}", "");
        } 
      } 
    } 
    if (msg.contains("{WAR STATUS}"))
      if (getClan(uuid) != null) {
        if (getClan(uuid).isInWar().booleanValue()) {
          good_msg = good_msg.replace("{WAR STATUS}", "IN WAR");
        } else {
          good_msg = good_msg.replace("{WAR STATUS}", "PEACEFUL");
        } 
      } else {
        good_msg = good_msg.replace("{WAR STATUS}", "");
      }  
    return good_msg;
  }
  
  public FileConfiguration loadLanguageFile() {
    try {
      if (!(new File(getDataFolder() + "/languages.yml")).exists()) {
        String all_languages = new String(getResource("languages.yml").readAllBytes());
        FileWriter write_language = new FileWriter(getDataFolder() + "/languages.yml");
        write_language.write(all_languages);
        write_language.close();
      } 
      this.language_yaml = (FileConfiguration)YamlConfiguration.loadConfiguration(new File(getDataFolder(), "languages.yml"));
      return this.language_yaml;
    } catch (IOException e1) {
      e1.printStackTrace();
      return null;
    } 
  }
  
  public String getTextFromLanguage(String path, ChatColor main_color, UUID uuid) {
    String message = "";
    if (main_color != null)
      message = String.valueOf(message) + main_color; 
    if (!this.language_yaml.getKeys(false).contains(this.language))
      this.language = "english"; 
    path = String.valueOf(this.language) + "." + path;
    if (path.contains("/")) {
      int index = Integer.valueOf(path.split("/")[1]).intValue();
      path = path.split("/")[0];
      message = String.valueOf(message) + (String)this.language_yaml.getStringList(path).get(index);
    } else {
      message = String.valueOf(message) + this.language_yaml.getString(path);
    } 
    if (uuid != null)
      return placeHolderCheck(message, uuid, main_color); 
    return message;
  }
  
  public String getBoldedTextFromLanguage(String path, ChatColor main_color, UUID uuid) {
    return main_color + "" + ChatColor.BOLD + getTextFromLanguage(path, (ChatColor)null, uuid) + ChatColor.RESET + main_color;
  }
}
