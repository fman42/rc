package me.valenwe.rustcraft.clans;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import me.valenwe.rustcraft.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ClanListener implements Listener {
  static Main plugin;
  
  public ClanListener(Main main) {
    plugin = main;
  }
  
  @EventHandler
  public void claim(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    Clan clan = Main.getClan(player.getUniqueId());
    if (checkDisplayName(player.getInventory().getItemInMainHand(), ChatColor.DARK_GREEN + "Clan Chest")) {
      if (block.getWorld().getName().endsWith("_nether")) {
        event.setCancelled(true);
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.error_nether", ChatColor.RED, player.getUniqueId()));
        return;
      } 
      if (block.getWorld().getName().endsWith("_end")) {
        event.setCancelled(true);
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.error_ender", ChatColor.RED, player.getUniqueId()));
        return;
      } 
      if (!player.getWorld().getName().equals(Main.world_name)) {
        event.setCancelled(true);
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.error_world", ChatColor.RED, player.getUniqueId()));
        return;
      } 
      if (clan != null && !clan.isChanging().booleanValue()) {
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.error_chest", ChatColor.RED, player.getUniqueId()));
        event.setCancelled(true);
        return;
      } 
      if (!Main.canClaim(player.getLocation(), Main.radius_lvl_5).booleanValue()) {
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.error_close", ChatColor.RED, player.getUniqueId()));
        event.setCancelled(true);
        return;
      } 
      for (Location loc : Main.list_chest) {
        if (chestNearBlock(loc.getBlock(), block)) {
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_clan_chest", ChatColor.RED, 
                player.getUniqueId()));
          event.setCancelled(true);
          return;
        } 
      } 
      if (clan == null) {
        HashMap<UUID, String> player_list = new HashMap<>();
        player_list.put(player.getUniqueId(), player.getDisplayName());
        int default_count = 0;
        for (Clan c : Main.clan_list) {
          if (c.getName().contains("Clan"))
            default_count++; 
        } 
        Clan new_clan = new Clan(player.getUniqueId(), block.getX(), block.getY(), block.getZ(), 
            "Clan" + ++default_count, 1, player_list, Boolean.valueOf(true), Boolean.valueOf(false), Boolean.valueOf(false), Boolean.valueOf(false), null, 0, "private", 
            "Description");
        Main.clan_list.add(new_clan);
        player.setPlayerListName(new_clan.getPlayerTabString(player));
        for (Player online : Bukkit.getOnlinePlayers())
          online.sendMessage(plugin.getTextFromLanguage("clan_listener.clan_creation", ChatColor.GREEN, 
                player.getUniqueId())); 
      } else {
        clan.setChestLocation(block.getX(), block.getY(), block.getZ());
        clan.setIsChanging(Boolean.valueOf(false));
        player.sendMessage(plugin.getTextFromLanguage("clan_listener.location_changed_1", ChatColor.GREEN, 
              player.getUniqueId()));
        for (Player online : Bukkit.getOnlinePlayers()) {
          if (clan.isClanMember(online.getUniqueId()).booleanValue())
            online.sendMessage(plugin.getTextFromLanguage("clan_listener.location_changed_2", 
                  ChatColor.GREEN, player.getUniqueId())); 
        } 
      } 
      if (player.getGameMode().equals(GameMode.CREATIVE))
        player.getInventory().remove(player.getInventory().getItemInMainHand()); 
      return;
    } 
    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) {
      if (clan != null && chestNearBlock(clan.getChestLocation().getBlock(), block)) {
        event.setCancelled(true);
        player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_chest", ChatColor.RED, 
              player.getUniqueId()));
        return;
      } 
      double x = block.getX();
      double y = block.getY();
      double z = block.getZ();
      World world = block.getWorld();
      Location modified = new Location(world, x + 1.0D, y, z);
      if (modified.getBlock().getType() == Material.CHEST) {
        Chest chest = (Chest)modified.getBlock().getState();
        if (chest.getCustomName() != null && (
          chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest") || 
          chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest"))) {
          event.setCancelled(true);
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_rustcraft_chest", 
                ChatColor.RED, player.getUniqueId()));
          return;
        } 
      } 
      modified = new Location(world, x - 1.0D, y, z);
      if (modified.getBlock().getType() == Material.CHEST) {
        Chest chest = (Chest)modified.getBlock().getState();
        if (chest.getCustomName() != null && (
          chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest") || 
          chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest"))) {
          event.setCancelled(true);
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_rustcraft_chest", 
                ChatColor.RED, player.getUniqueId()));
          return;
        } 
      } 
      modified = new Location(world, x, y, z + 1.0D);
      if (modified.getBlock().getType() == Material.CHEST) {
        Chest chest = (Chest)modified.getBlock().getState();
        if (chest.getCustomName() != null && (
          chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest") || 
          chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest"))) {
          event.setCancelled(true);
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_rustcraft_chest", 
                ChatColor.RED, player.getUniqueId()));
          return;
        } 
      } 
      modified = new Location(world, x, y, z - 1.0D);
      if (modified.getBlock().getType() == Material.CHEST) {
        Chest chest = (Chest)modified.getBlock().getState();
        if (chest.getCustomName() != null && (
          chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest") || 
          chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest"))) {
          event.setCancelled(true);
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.error_close_rustcraft_chest", 
                ChatColor.RED, player.getUniqueId()));
          return;
        } 
      } 
      if (block.getType().equals(Material.CHEST) || block.getType().equals(Material.TRAPPED_CHEST))
        Main.list_chest.add(block.getLocation()); 
      return;
    } 
    if (block.getType() == Material.DRAGON_EGG && block.getY() >= plugin.getConfig().getDouble("dragon_egg_height") && 
      clan.isIn(block.getLocation()).booleanValue()) {
      Location loc = block.getLocation();
      loc.setY(loc.getY() - 1.0D);
      if (loc.getBlock().getType() != Material.AIR || loc.getBlock().getType() != null)
        clan.setHasEgg(Boolean.valueOf(true)); 
      return;
    } 
  }
  
  @EventHandler
  public void onItemDrop(PlayerDropItemEvent event) {
    Item drop = event.getItemDrop();
    if (drop.getItemStack().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Clan Chest")) {
      event.getItemDrop().remove();
      event.getPlayer().sendMessage(plugin.getTextFromLanguage("clan_listener.drop_clan_chest", ChatColor.RED, 
            event.getPlayer().getUniqueId()));
    } 
  }
  
  @EventHandler
  public void zone(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();
    if (block == null)
      return; 
    Iterator<Clan> iterator = Main.clan_list.iterator();
    if (iterator.hasNext()) {
      Clan c = iterator.next();
      if (c.isIn(block.getLocation()).booleanValue() && !c.isClanMember(player.getUniqueId()).booleanValue() && c.isProtected().booleanValue()) {
        PotionEffect effect = new PotionEffect(PotionEffectType.SLOW_DIGGING, 199980, 2, true, false, false);
        event.getPlayer().addPotionEffect(effect);
        return;
      } 
      event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
      return;
    } 
  }
  
  @EventHandler
  public void cancelBlock(BlockPlaceEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    if (plugin.getConfig().getBoolean("allow_ennemy_building"))
      return; 
    for (Clan c : Main.clan_list) {
      if (c.isIn(block.getLocation()).booleanValue() && !c.isClanMember(player.getUniqueId()).booleanValue()) {
        event.setCancelled(true);
        if (block.getType().equals(Material.TNT)) {
          ItemStack item_tnt = new ItemStack(Material.TNT, 1);
          player.getInventory().removeItem(new ItemStack[] { item_tnt });
          player.updateInventory();
          TNTPrimed tnt = (TNTPrimed)event.getBlockPlaced().getWorld()
            .spawn(event.getBlockPlaced().getLocation().add(0.5D, 0.0D, 0.5D), TNTPrimed.class);
          tnt.setFuseTicks(90);
          tnt.getWorld().playSound(tnt.getLocation(), Sound.ENTITY_TNT_PRIMED, 10.0F, 1.0F);
        } 
        return;
      } 
    } 
  }
  
  @EventHandler(priority = EventPriority.HIGHEST)
  public void onBlockBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    for (Clan c : Main.clan_list) {
      if (block.getLocation().equals(c.getChestLocation())) {
        event.setCancelled(true);
        event.getPlayer().sendMessage(plugin.getTextFromLanguage("clan_listener.destroy_clan_chest", 
              ChatColor.RED, player.getUniqueId()));
        return;
      } 
      if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)
        if (c.isIn(block.getLocation()).booleanValue() && !c.isClanMember(player.getUniqueId()).booleanValue() && c.isProtected().booleanValue()) {
          event.setCancelled(true);
          event.getPlayer().sendMessage(plugin.getTextFromLanguage("clan_listener.destroy_protected_chest", 
                ChatColor.RED, player.getUniqueId()));
          return;
        }  
    } 
    if (block.getType() == Material.CHEST) {
      Chest chest = (Chest)block.getState();
      if (chest.getCustomName() != null && (
        chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest") || 
        chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest")))
        for (ItemStack item : block.getDrops()) {
          if (item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "RustCraft Chest") || 
            item.getItemMeta().getDisplayName()
            .equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest")) {
            block.getDrops().clear();
            event.setCancelled(true);
            block.setType(Material.AIR);
            return;
          } 
        }  
    } 
    if (block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)
      for (int i = 0; i < Main.list_chest.size(); i++) {
        if (block.getLocation().equals(((Location)Main.list_chest.get(i)).getBlock().getLocation())) {
          Main.list_chest.remove(i);
          return;
        } 
      }  
    Location loc = block.getLocation();
    loc.setY(loc.getY() + 1.0D);
    if (loc.getBlock().getType().equals(Material.DRAGON_EGG))
      for (Clan c : Main.clan_list) {
        if (c.isIn(block.getLocation()).booleanValue()) {
          event.setCancelled(true);
          event.getPlayer().sendMessage(
              String.valueOf(plugin.getTextFromLanguage("clan_listener.break_egg/0", ChatColor.RED, player.getUniqueId())) + 
              plugin.getTextFromLanguage("clan_listener.break_egg/1", ChatColor.DARK_PURPLE, 
                player.getUniqueId()) + 
              ChatColor.RED + "!");
          return;
        } 
      }  
  }
  
  @EventHandler
  public void disableEggDestroyed(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    if (block == null)
      return; 
    if (block.getType() == Material.DRAGON_EGG) {
      event.setCancelled(true);
      if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
        for (Clan c : Main.clan_list) {
          if (c.isIn(block.getLocation()).booleanValue())
            c.setHasEgg(Boolean.valueOf(false)); 
        } 
        ItemStack egg = new ItemStack(Material.DRAGON_EGG);
        event.getClickedBlock().setType(Material.AIR);
        block.getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), egg);
        return;
      } 
    } 
  }
  
  @EventHandler
  public void enemyOpeningClanChest(PlayerInteractEvent event) {
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();
    if (block == null)
      return; 
    if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (
      block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) {
      for (Clan c : Main.clan_list) {
        if (c.getChestLocation().equals(block.getLocation()) && !c.isClanMember(player.getUniqueId()).booleanValue()) {
          event.setCancelled(true);
          player.sendMessage(plugin.getTextFromLanguage("clan_listener.access_clan_chest", ChatColor.RED, 
                player.getUniqueId()));
          return;
        } 
        if (c.isIn(block.getLocation()).booleanValue() && !c.isClanMember(player.getUniqueId()).booleanValue() && c.isProtected().booleanValue()) {
          for (Player online : Bukkit.getOnlinePlayers()) {
            if (c.isClanMember(online.getUniqueId()).booleanValue())
              return; 
          } 
          for (Location loc : Main.list_chest) {
            if (c.isIn(loc).booleanValue()) {
              event.setCancelled(true);
              return;
            } 
          } 
        } 
      } 
      Chest chest = (Chest)block.getState();
      if (chest.getCustomName() == null)
        return; 
      if (chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest")) {
        int i;
        for (i = 0; i < chest.getBlockInventory().getSize(); i++) {
          if (chest.getBlockInventory().getItem(i) != null)
            return; 
        } 
        for (i = 0; i < chest.getBlockInventory().getSize(); i++) {
          int chance = ThreadLocalRandom.current().nextInt(1, 100);
          int selection_item = ThreadLocalRandom.current().nextInt(0, Main.loot_rust_chest_items.size());
          if (chance <= ((Double)Main.loot_rust_chest_chance.get(selection_item)).doubleValue() * 100.0D) {
            chest.getBlockInventory().setItem(i, Main.loot_rust_chest_items.get(selection_item));
            int chance_amount = ThreadLocalRandom.current().nextInt(1, 100);
            if (chance_amount <= ((Double)Main.loot_rust_chest_chance.get(selection_item)).doubleValue() * 100.0D * 2.0D)
              chest.getBlockInventory().getItem(i).setAmount(ThreadLocalRandom.current().nextInt(1, 
                    plugin.getConfig().getInt("loot_chances.rustcraft_chest.maximum_amount_item"))); 
          } 
        } 
        return;
      } 
      if (chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest")) {
        int i;
        for (i = 0; i < chest.getBlockInventory().getSize(); i++) {
          if (chest.getBlockInventory().getItem(i) != null)
            return; 
        } 
        for (i = 0; i < chest.getBlockInventory().getSize(); i++) {
          int chance = ThreadLocalRandom.current().nextInt(1, 100);
          int selection_item = ThreadLocalRandom.current().nextInt(0, 
              Main.loot_rust_advanced_chest_items.size());
          if (chance <= ((Double)Main.loot_rust_advanced_chest_chance.get(selection_item)).doubleValue() * 100.0D) {
            chest.getBlockInventory().setItem(i, Main.loot_rust_advanced_chest_items.get(selection_item));
            int chance_amount = ThreadLocalRandom.current().nextInt(1, 100);
            if (chance_amount <= ((Double)Main.loot_rust_advanced_chest_chance.get(selection_item)).doubleValue() * 100.0D * 2.0D)
              chest.getBlockInventory().getItem(i)
                .setAmount(ThreadLocalRandom.current().nextInt(1, 1 + plugin.getConfig()
                    .getInt("loot_chances.rustcraft_advanced_chest.maximum_amount_item"))); 
          } 
        } 
      } 
    } 
  }
  
  @EventHandler
  public void claimChestExplode(EntityExplodeEvent event) {
    byte b;
    int i;
    Block[] arrayOfBlock;
    for (i = (arrayOfBlock = (Block[])event.blockList().toArray((Object[])new Block[event.blockList().size()])).length, b = 0; b < i; ) {
      Block block = arrayOfBlock[b];
      for (Clan c : Main.clan_list) {
        if (c.getChestLocation().equals(block.getLocation())) {
          event.blockList().remove(block);
          continue;
        } 
        for (Location loc : Main.list_chest) {
          if (loc.equals(block.getLocation())) {
            if (c.isIn(loc).booleanValue() && c.isProtected().booleanValue()) {
              event.blockList().remove(block);
              continue;
            } 
            Main.list_chest.remove(loc);
          } 
        } 
      } 
      if (block.getType() == Material.CHEST) {
        Chest chest = (Chest)block.getState();
        if (chest.getCustomName() != null && (
          chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest") || 
          chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest")))
          event.blockList().remove(block); 
      } 
      b++;
    } 
  }
  
  @EventHandler
  public void claimchestInventoryClick(InventoryClickEvent event) {
    if (event.getCurrentItem() == null)
      return; 
    if (event.getCurrentItem().getItemMeta() == null)
      return; 
    if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Clan Chest"))
      event.setCancelled(true); 
  }
  
  @EventHandler
  public void checkDeathWithCC(PlayerDeathEvent event) {
    Player player = event.getEntity();
    List<ItemStack> drops = event.getDrops();
    for (int i = 0; i < drops.size(); i++) {
      ItemMeta item_meta = ((ItemStack)drops.get(i)).getItemMeta();
      if (item_meta.getDisplayName().equals(ChatColor.DARK_GREEN + "Clan Chest"))
        drops.remove(i); 
    } 
    checkWar(player);
  }
  
  @EventHandler
  public void onJoinEvent(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (!player.hasPlayedBefore()) {
      player.sendMessage(plugin.getTextFromLanguage("clan_listener.welcome_message/0", ChatColor.AQUA, 
            player.getUniqueId()));
      player.sendMessage(
          String.valueOf(plugin.getTextFromLanguage("clan_listener.welcome_message/1", ChatColor.GOLD, player.getUniqueId())) + 
          ChatColor.RED + "/rustcraft info" + ChatColor.GOLD + plugin.getTextFromLanguage(
            "clan_listener.welcome_message/2", ChatColor.GOLD, player.getUniqueId()));
    } 
    Clan clan = Main.getClan(player.getUniqueId());
    if (clan == null)
      return; 
    player.setPlayerListName(clan.getPlayerTabString(player));
    clan.getClanMembers().replace(player.getUniqueId(), player.getDisplayName());
    if (clan.isInWar().booleanValue()) {
      player.sendMessage(plugin.getBoldedTextFromLanguage("clan_listener.another_war", ChatColor.DARK_RED, 
            player.getUniqueId()));
      checkWar(player);
    } 
    for (Clan c : Main.clan_list) {
      if (c.isIn(player.getLocation()).booleanValue()) {
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.enters_territory_1", ChatColor.BLUE, c.getLeader()));
        break;
      } 
    } 
  }
  
  @EventHandler
  public void messageClanPlayer(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    for (Clan c : Main.clan_list) {
      if (c.isIn(event.getTo()).booleanValue() && !c.isIn(event.getFrom()).booleanValue()) {
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.enters_territory_2", ChatColor.BLUE, c.getLeader()));
        return;
      } 
      if (c.isIn(event.getFrom()).booleanValue() && !c.isIn(event.getTo()).booleanValue()) {
        player.sendMessage(
            plugin.getTextFromLanguage("clan_listener.left_territory", ChatColor.BLUE, c.getLeader()));
        return;
      } 
    } 
  }
  
  @EventHandler
  public void onMobSpawn(CreatureSpawnEvent event) {
    if (!plugin.getConfig().getBoolean("allow_mob_spawn"))
      return; 
    if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL) && event.getEntity() instanceof org.bukkit.entity.Monster)
      for (Clan clan : Main.clan_list) {
        if (clan.isIn(event.getLocation()).booleanValue())
          event.setCancelled(true); 
      }  
  }
  
  public static boolean checkDisplayName(ItemStack item, String name) {
    return (item != null && item.getType() != Material.AIR && item.hasItemMeta() && 
      item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().equals(name));
  }
  
  public static int getValueInventory(Chest chest) {
    int diamond = 0, netherite = 0, iron = 0, or = 0, emeraude = 0, special = 0, potion = 0, golden_apple = 0;
    int enchanted_book = 0, enchanted_equipment = 0, equipment = 0, value = 0, diamond_equipment = 0;
    int enchanted_diamond_equipment = 0, netherite_equipment = 0, enchanted_netherite_equipment = 0;
    for (int i = 0; i < chest.getBlockInventory().getSize(); i++) {
      ItemStack item = chest.getBlockInventory().getItem(i);
      if (item != null) {
        if (item.getType() == Material.DIAMOND)
          diamond += item.getAmount(); 
        if (item.getType() == Material.DIAMOND_BLOCK)
          diamond += item.getAmount() * 9; 
        if (item.getType() == Material.NETHERITE_INGOT)
          netherite += item.getAmount(); 
        if (item.getType() == Material.NETHERITE_BLOCK)
          netherite += item.getAmount() * 9; 
        if (item.getType() == Material.IRON_INGOT)
          iron += item.getAmount(); 
        if (item.getType() == Material.IRON_BLOCK)
          iron += item.getAmount() * 9; 
        if (item.getType() == Material.GOLD_INGOT)
          or += item.getAmount(); 
        if (item.getType() == Material.GOLD_BLOCK)
          or += item.getAmount() * 9; 
        if (item.getType() == Material.EMERALD)
          emeraude += item.getAmount(); 
        if (item.getType() == Material.EMERALD_BLOCK)
          emeraude += item.getAmount() * 9; 
        if (item.getType() == Material.ENCHANTED_GOLDEN_APPLE || item.getType() == Material.TOTEM_OF_UNDYING)
          special += item.getAmount(); 
        if (item.getType() == Material.POTION || item.getType() == Material.SPLASH_POTION)
          potion += item.getAmount(); 
        if (item.getType() == Material.GOLDEN_APPLE)
          golden_apple += item.getAmount(); 
        if (item.getType() == Material.ENCHANTED_BOOK)
          enchanted_book += item.getAmount(); 
        if (!item.getType().name().startsWith("DIAMOND") && !item.getType().name().startsWith("STONE") && 
          !item.getType().name().startsWith("WOOD") && (
          item.getType().name().endsWith("_CHESTPLATE") || item.getType().name().endsWith("_BOOTS") || 
          item.getType().name().endsWith("_HELMET") || 
          item.getType().name().endsWith("_LEGGINGS") || 
          item.getType().name().endsWith("_SWORD") || item.getType().name().endsWith("_SHOVEL") || 
          item.getType().name().endsWith("_PICKAXE") || item.getType().name().endsWith("_AXE") || 
          item.getType().name().endsWith("BOW"))) {
          equipment++;
          if (item.getItemMeta().hasEnchants()) {
            enchanted_equipment++;
            equipment--;
          } 
        } 
        if (item.getType().name().startsWith("DIAMOND") && (item.getType().name().endsWith("_CHESTPLATE") || 
          item.getType().name().endsWith("_BOOTS") || item.getType().name().endsWith("_HELMET") || 
          item.getType().name().endsWith("_LEGGINGS") || item.getType().name().endsWith("_SWORD") || 
          item.getType().name().endsWith("_SHOVEL") || item.getType().name().endsWith("_PICKAXE") || 
          item.getType().name().endsWith("_AXE"))) {
          diamond_equipment++;
          if (item.getItemMeta().hasEnchants()) {
            enchanted_diamond_equipment++;
            diamond_equipment--;
          } 
        } 
        if (item.getType().name().startsWith("NETHERITE") && (item.getType().name().endsWith("_CHESTPLATE") || 
          item.getType().name().endsWith("_BOOTS") || item.getType().name().endsWith("_HELMET") || 
          item.getType().name().endsWith("_LEGGINGS") || item.getType().name().endsWith("_SWORD") || 
          item.getType().name().endsWith("_SHOVEL") || item.getType().name().endsWith("_PICKAXE") || 
          item.getType().name().endsWith("_AXE"))) {
          netherite_equipment++;
          if (item.getItemMeta().hasEnchants()) {
            enchanted_netherite_equipment++;
            netherite_equipment--;
          } 
        } 
      } 
      value = plugin.getConfig().getInt("values.diamond") * diamond + 
        plugin.getConfig().getInt("values.iron") * iron + plugin.getConfig().getInt("values.gold") * or + 
        plugin.getConfig().getInt("values.emerald") * emeraude + 
        plugin.getConfig().getInt("values.special") * special + 
        plugin.getConfig().getInt("values.potion") * potion + 
        plugin.getConfig().getInt("values.golden_apple") * golden_apple + 
        plugin.getConfig().getInt("values.enchanted_book") * enchanted_book + 
        plugin.getConfig().getInt("values.equipment") * equipment + 
        plugin.getConfig().getInt("values.enchanted_equipment") * enchanted_equipment + 
        plugin.getConfig().getInt("values.diamond_equipment") * diamond_equipment + 
        plugin.getConfig().getInt("values.enchanted_diamond_equipment") * enchanted_diamond_equipment + 
        plugin.getConfig().getInt("values.netherite") * netherite + 
        plugin.getConfig().getInt("values.netherite_equipment") * netherite_equipment + 
        plugin.getConfig().getInt("values.enchanted_netherite_equipment") * enchanted_netherite_equipment;
    } 
    return value;
  }
  
  public static int getClanChestValue(Block clan_chest) {
    Chest chest = (Chest)clan_chest.getState();
    Inventory inv = chest.getBlockInventory();
    int value = 0;
    for (int i = 0; i < inv.getSize(); i++) {
      ItemStack item = inv.getItem(i);
      if (item != null) {
        if (item.getType() == Material.CAKE)
          value += item.getAmount() * plugin.getConfig().getInt("chest_values.cake"); 
        if (item.getType() == Material.DIAMOND)
          value += item.getAmount() * plugin.getConfig().getInt("chest_values.diamond"); 
        if (item.getType() == Material.IRON_INGOT)
          value += item.getAmount() * plugin.getConfig().getInt("chest_values.iron"); 
        if (item.getType() == Material.EMERALD)
          value += item.getAmount() * plugin.getConfig().getInt("chest_values.emerald"); 
        if (item.getType() == Material.NETHERITE_INGOT)
          value += item.getAmount() * plugin.getConfig().getInt("chest_values.netherite"); 
      } 
    } 
    return value;
  }
  
  public static boolean removeRessourcesNeeded(Block clan_chest, int value) {
    Inventory inv = ((Chest)clan_chest.getState()).getBlockInventory();
    boolean filled = false;
    int i = 0;
    while (i < inv.getSize() && value >= plugin.getConfig().getInt("chest_values.cake")) {
      ItemStack item = inv.getItem(i);
      if (item != null && 
        item.getType() == Material.CAKE)
        while (item.getAmount() > 0 && value >= plugin.getConfig().getInt("chest_values.cake")) {
          item.setAmount(item.getAmount() - 1);
          value -= plugin.getConfig().getInt("chest_values.cake");
        }  
      i++;
    } 
    i = 0;
    while (i < inv.getSize() && value >= plugin.getConfig().getInt("chest_values.netherite")) {
      ItemStack item = inv.getItem(i);
      if (item != null && 
        item.getType() == Material.NETHERITE_INGOT)
        while (item.getAmount() > 0 && value >= plugin.getConfig().getInt("chest_values.netherite")) {
          item.setAmount(item.getAmount() - 1);
          value -= plugin.getConfig().getInt("chest_values.netherite");
        }  
      i++;
    } 
    i = 0;
    while (i < inv.getSize() && value >= plugin.getConfig().getInt("chest_values.emerald")) {
      ItemStack item = inv.getItem(i);
      if (item != null && 
        item.getType() == Material.EMERALD)
        while (item.getAmount() > 0 && value >= plugin.getConfig().getInt("chest_values.emerald")) {
          item.setAmount(item.getAmount() - 1);
          value -= plugin.getConfig().getInt("chest_values.emerald");
        }  
      i++;
    } 
    i = 0;
    while (i < inv.getSize() && value >= plugin.getConfig().getInt("chest_values.diamond")) {
      ItemStack item = inv.getItem(i);
      if (item != null && 
        item.getType() == Material.DIAMOND)
        while (item.getAmount() > 0 && value >= plugin.getConfig().getInt("chest_values.diamond")) {
          item.setAmount(item.getAmount() - 1);
          value -= plugin.getConfig().getInt("chest_values.diamond");
        }  
      i++;
    } 
    i = 0;
    while (i < inv.getSize() && value >= plugin.getConfig().getInt("chest_values.iron")) {
      ItemStack item = inv.getItem(i);
      if (item != null && 
        item.getType() == Material.IRON_INGOT)
        while (item.getAmount() > 0 && value >= plugin.getConfig().getInt("chest_values.iron")) {
          item.setAmount(item.getAmount() - 1);
          value -= plugin.getConfig().getInt("chest_values.iron");
        }  
      i++;
    } 
    if (value == 0) {
      filled = true;
    } else {
      filled = false;
    } 
    return filled;
  }
  
  public static boolean chestNearBlock(Block block, Block block_placed) {
    boolean near = false;
    Location loc_block = block.getLocation();
    Location loc_block_placed = block_placed.getLocation();
    double x = loc_block.getX();
    double y = loc_block.getY();
    double z = loc_block.getZ();
    World w = loc_block.getWorld();
    Location modified = new Location(w, x + 1.0D, y, z);
    if (modified.equals(loc_block_placed))
      near = true; 
    modified = new Location(w, x - 1.0D, y, z);
    if (modified.equals(loc_block_placed))
      near = true; 
    modified = new Location(w, x, y, z + 1.0D);
    if (modified.equals(loc_block_placed))
      near = true; 
    modified = new Location(w, x, y, z - 1.0D);
    if (modified.equals(loc_block_placed))
      near = true; 
    return near;
  }
  
  public void checkWar(Player player) {
    Clan clan = Main.getClan(player.getUniqueId());
    if (clan == null)
      return; 
    Clan clan_target = null;
    if (clan.getWarTarget() == null)
      return; 
    for (Clan c : Main.clan_list) {
      if (c.getChestLocation().equals(clan.getWarTarget()))
        clan_target = c; 
    } 
    if (clan_target == null)
      return; 
    clan_target.addWarKill();
    Scoreboard clan_board = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective obj = clan_board.registerNewObjective("Kills", "null", "null");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "War Kills");
    Score score = obj.getScore("");
    score.setScore(3);
    Score score1 = obj.getScore(ChatColor.YELLOW + "{" + clan.getNameWithColors() + ChatColor.YELLOW + "}: " + 
        ChatColor.AQUA + clan.getWarKills());
    score1.setScore(2);
    Score score2 = obj.getScore(ChatColor.YELLOW + "{" + clan_target.getNameWithColors() + ChatColor.YELLOW + "}: " + 
        ChatColor.AQUA + clan_target.getWarKills());
    score2.setScore(1);
    Scoreboard clan_target_board = Bukkit.getScoreboardManager().getNewScoreboard();
    obj = clan_target_board.registerNewObjective("Kills", "null", "null");
    obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    obj.setDisplayName(ChatColor.DARK_RED + "" + ChatColor.BOLD + "War Kills");
    score = obj.getScore("");
    score.setScore(3);
    score1 = obj.getScore(ChatColor.YELLOW + "{" + clan_target.getNameWithColors() + ChatColor.YELLOW + "}: " + 
        ChatColor.AQUA + clan_target.getWarKills());
    score1.setScore(2);
    score2 = obj.getScore(ChatColor.YELLOW + "{" + clan.getNameWithColors() + ChatColor.YELLOW + "}: " + 
        ChatColor.AQUA + clan.getWarKills());
    score2.setScore(1);
    for (Player online : Bukkit.getOnlinePlayers()) {
      if (clan.isClanMember(online.getUniqueId()).booleanValue())
        online.setScoreboard(clan_board); 
      if (clan_target.isClanMember(online.getUniqueId()).booleanValue())
        online.setScoreboard(clan_target_board); 
    } 
  }
}
