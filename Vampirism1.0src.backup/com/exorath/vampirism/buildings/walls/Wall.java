package com.exorath.vampirism.buildings.walls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.managers.BuildingManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class  Wall extends Building {
	Plugin plugin;
	int healthUp;
	static int maxLevel = 3;
	
	//Building costs:
	public static int COSTGOLD = 0;
	public static int COSTWOOD = 50;
	
	
	public Wall(Location minLoc, Location maxLoc, Schematic schematic, int health, Builder owner, BuildingManager bm) {
		super(minLoc, maxLoc, schematic, health, owner, bm);
		this.plugin = bm.getPlugin();
		super.name = "Wall";
		healthUp = super.getMaxHealth() + 20;
		inv = Bukkit.createInventory(owner.getPlayer(), 9, ChatColor.DARK_GRAY + name);
		
		//Building costs lvl2:
		costWood = 200;
		costGold = 0;
	}

	@Override
	public void show(Player p) {
		if (isBuild) {
			inv.setItem(0, getInfoItem());
			inv.setItem(4, CustomItems.REMOVE);
			switch (level) {
			default:
				inv.setItem(8, getUpgradeItem(healthUp));

			}
			p.openInventory(inv);
		}
	}

	@Override
	public void levelUp() throws IOException {
		switch (level) {
		case 1:
			isAlive = false;
			level = (short) (level + 1);
			
			setMaxHealth(healthUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your wall is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("wall1_2.schematic");
			// properties for lvl3:
			healthUp = getMaxHealth() + 50;
			costWood = 1500;

			break;
		case 2:
			isBuild = false;
			level = (short) (level + 1);
			setMaxHealth(healthUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your wall is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("wall1_3.schematic");

			break;
		default:
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your wall is " + ChatColor.RED + "Max Leveled!");
		}

	}

	public ItemStack getInfoItem() {
		ItemStack item = new ItemStack(Material.GHAST_TEAR, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + name + ChatColor.GOLD + " Lv" + level);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Health: " + ChatColor.GOLD + this.getHealth() + ChatColor.GRAY + "/" + ChatColor.GOLD + getMaxHealth());
		lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level);
		lore.add(" ");
		lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getUpgradeItem(int maxHealthUp) {
		ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (level != maxLevel) {
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade to" + ChatColor.GOLD + " Lv" + (level + 1));
			lore.add(ChatColor.GRAY + "Health: " + ChatColor.GOLD + getMaxHealth() + ChatColor.GRAY + " -> " + ChatColor.GOLD + healthUp);
			lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level + ChatColor.GRAY + " -> " + ChatColor.GOLD + (level + 1));
			lore.add(" ");
			lore.add(ChatColor.GRAY + "Cost: " + costWood + ChatColor.DARK_GREEN + " wood");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		} else {
			meta.setDisplayName(ChatColor.DARK_GREEN + "Your building is " + ChatColor.RED + "max level");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
