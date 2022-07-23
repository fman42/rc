package me.valenwe.rustcraft.moderation;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.session.ClipboardHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import me.valenwe.rustcraft.Main;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class StructureGenerator implements Listener {
   static Main plugin;

   public StructureGenerator(Main main) {
      plugin = main;
   }

   @EventHandler
   public void generateStructure(ChunkPopulateEvent event) throws FileNotFoundException, IOException, WorldEditException {
      if (event.getWorld().getName().equals(plugin.getConfig().get("world_name"))) {
         Chunk chunk = event.getChunk();
         World world = event.getWorld();
         int chance = ThreadLocalRandom.current().nextInt(1, 1000);
         int selection_structure = ThreadLocalRandom.current().nextInt(0, Main.list_structure.size());
         if ((double)chance <= (Double)Main.list_structure_chances.get(selection_structure) * 1000.0D) {
            int largeur = 0;
            int longueur = 0;
            switch(selection_structure) {
            case 0:
               largeur = 16;
               longueur = 10;
               break;
            case 1:
               largeur = 30;
               longueur = 34;
               break;
            case 2:
               largeur = 16;
               longueur = 18;
               break;
            case 3:
               largeur = 24;
               longueur = 38;
               break;
            case 4:
               largeur = 11;
               longueur = 9;
               break;
            case 5:
               largeur = 7;
               longueur = 7;
            }

            try {
               this.generateStructure(chunk, world, selection_structure, largeur, longueur);
            } catch (Exception var9) {
               System.out.println("[Rustcraft] Unable to generate structures, the .SCHEM files are unreachable.");
            }
         }

      }
   }

   @EventHandler
   public void changeChestName(InventoryCloseEvent event) {
      if (event.getInventory().getHolder() instanceof Chest) {
         Chest chest = (Chest)event.getInventory().getHolder();
         if (chest.getCustomName() == null) {
            return;
         }

         for(int i = 0; i < chest.getBlockInventory().getSize(); ++i) {
            if (chest.getBlockInventory().getItem(i) != null) {
               return;
            }
         }

         if (chest.getCustomName().equals(ChatColor.GOLD + "RustCraft Chest")) {
            chest.setCustomName(ChatColor.DARK_RED + "[LOOTED] " + ChatColor.GOLD + "RustCraft Chest");
            chest.update();
         }

         if (chest.getCustomName().equals(ChatColor.DARK_BLUE + "RustCraft Advanced Chest")) {
            chest.setCustomName(ChatColor.DARK_RED + "[LOOTED] " + ChatColor.DARK_BLUE + "RustCraft Chest");
            chest.update();
         }
      }

   }

   public void generateStructure(Chunk chunk, World world, int selection_structure, int largeur, int longueur) throws FileNotFoundException, IOException, WorldEditException {
      int x = chunk.getX() * 16;
      int y = 0;
      int z = chunk.getZ() * 16;

      int angle_factor;
      for(angle_factor = 0; angle_factor < 10; ++angle_factor) {
         for(y = world.getMaxHeight() - 1; chunk.getBlock(0, y, 0).getType() == Material.AIR; --y) {
         }
      }

      angle_factor = ThreadLocalRandom.current().nextInt(0, 4);
      if (this.hasSol(world, x, y - 1, z, Math.max(largeur, longueur) + 1, Math.max(largeur, longueur) + 1, angle_factor)) {
         File file = new File(plugin.getDataFolder().getAbsolutePath(), (String)Main.list_structure.get(selection_structure) + ".schem");
         ClipboardFormat format = ClipboardFormats.findByFile(file);
         Throwable var13 = null;
         Throwable var14 = null;

         Clipboard clipboard = null;
         ClipboardReader reader = null;
         try {
            reader = format.getReader(new FileInputStream(file));

            try {
               clipboard = reader.read();
            } finally {
               if (reader != null) {
                  reader.close();
               }

            }
         } catch (Throwable var38) {
            if (var13 == null) {
               var13 = var38;
            } else if (var13 != var38) {
               var13.addSuppressed(var38);
            }
         }

         clipboard.setOrigin(clipboard.getMinimumPoint());
         com.sk89q.worldedit.world.World WE_world = BukkitAdapter.adapt(world);
         var14 = null;
         reader = null;

         try {
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(WE_world).maxBlocks(-1).build();

            try {
               ClipboardHolder holder = new ClipboardHolder(clipboard);
               holder.setTransform((new AffineTransform()).rotateY((double)(90 * angle_factor)));
               Operation operation = holder.createPaste(editSession).to(BlockVector3.at(x, y, z)).ignoreAirBlocks(false).build();
               Operations.complete(operation);
            } finally {
               if (editSession != null) {
                  editSession.close();
               }

            }

         } catch (Throwable var36) {
            if (var14 == null) {
               var14 = var36;
            } else if (var14 != var36) {
               var14.addSuppressed(var36);
            }
         }
      }
   }

   public boolean hasSol(World world, int x, int y, int z, int largeur, int longueur, int angle_factor) {
      boolean hasSol;
      int count_wrong_block;
      hasSol = true;
      count_wrong_block = 0;
      int i;
      int I;
      Location loc;
      label121:
      switch(angle_factor) {
      case 0:
         i = 0;

         while(true) {
            if (i >= largeur) {
               break label121;
            }

            for(I = 0; I < longueur; ++I) {
               loc = new Location(world, (double)(x + i), (double)y, (double)(z + I));
               if (loc.getBlock() == null || loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) {
                  ++count_wrong_block;
               }
            }

            ++i;
         }
      case 1:
         i = 0;

         while(true) {
            if (i >= largeur) {
               break label121;
            }

            for(I = 0; I < longueur; ++I) {
               loc = new Location(world, (double)(x + i), (double)y, (double)(z - I));
               if (loc.getBlock() == null || loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) {
                  ++count_wrong_block;
               }
            }

            ++i;
         }
      case 2:
         i = 0;

         while(true) {
            if (i >= largeur) {
               break label121;
            }

            for(I = 0; I < longueur; ++I) {
               loc = new Location(world, (double)(x - i), (double)y, (double)(z - I));
               if (loc.getBlock() == null || loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) {
                  ++count_wrong_block;
               }
            }

            ++i;
         }
      case 3:
         for(i = 0; i < largeur; ++i) {
            for(I = 0; I < longueur; ++I) {
               loc = new Location(world, (double)(x - i), (double)y, (double)(z + I));
               if (loc.getBlock() == null || loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.WATER) {
                  ++count_wrong_block;
               }
            }
         }
      }

      double wrong_percentage = (double)(count_wrong_block * 100 / (largeur * longueur));
      if (wrong_percentage > 0.04D) {
         hasSol = false;
      }

      return hasSol;
   }
}
