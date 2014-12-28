package com.exorath.vampirism.buildings.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.managers.BuildingManager;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class  House extends Building {
	Plugin plugin;
	int healthUp;
	static int maxLevel = 3;
	public static final int FOODCOST= 1;
	public static final int UNITCOST = 1;
	//Building costs:
	public static int COSTGOLD = 1;
	public static int COSTWOOD = 0;
	public House(Location minLoc, Location maxLoc, Schematic schematic, int health, Builder owner, BuildingManager bm) {
		super(minLoc, maxLoc, schematic, health, owner, bm);
		this.plugin = bm.getPlugin();
		super.name = "House";
		healthUp = super.getMaxHealth() + 20;
		inv = Bukkit.createInventory(owner.getPlayer(), 9, ChatColor.DARK_GRAY + name);
		//Building costs:
		costWood = 0;
		costGold = 5;
	}

	@Override
	public void show(Player p) {
		if (isBuild) {
			inv.setItem(0, getInfoItem());
			inv.setItem(1, getBuilderItem());
			inv.setItem(4, CustomItems.REMOVE);
			inv.setItem(8, getUpgradeItem(healthUp));
			p.openInventory(inv);
		}
	}

	@Override
	public void levelUp() throws IOException {
		switch (level) {
		case 1:
			isBuild = false;
			level = (short) (level + 1);
			setMaxHealth(healthUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your house is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("wall1_2.schematic");
			// properties for lvl3:
			healthUp = getMaxHealth() + 50;
			costGold = 60;

			break;
		case 2:
			isBuild = false;
			level = (short) (level + 1);
			setMaxHealth(healthUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your house is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("wall1_3.schematic");

			break;
		default:
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your house is " + ChatColor.RED + "Max Leveled!");
		}

	}
	public void spawnMob(){
		getMinLoc().getWorld().playSound(getMinLoc(), Sound.FIZZ, 10, 1);
		ParticleEffects.sendToLocation(ParticleEffects.ENCHANTMENT_TABLE, getMinLoc().clone().add(2.5,2,4.5), 0.5f, 1, 0.5f, 1, 30);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run(){
				getMinLoc().getWorld().playSound(getMinLoc(), Sound.SUCCESSFUL_HIT, 10, 1);
				NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.ZOMBIE, "Woodcutter");
			    npc.spawn(getMinLoc().clone().add(2.5,1,4.5));
			    npc.data().set("owner",getBuilder());
			    npc.data().set("income", 1);
			    npc.data().set("tier", 1);
			    getBuilder().getUnits().add(npc);
			}
		},20l);

		
		
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
			lore.add(ChatColor.GRAY + "Cost: " + costGold + ChatColor.DARK_GREEN + " gold");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		} else {
			meta.setDisplayName(ChatColor.DARK_GREEN + "Your building is " + ChatColor.RED + "max level");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public static ItemStack getBuilderItem() {
		ItemStack item = new ItemStack(Material.SKULL_ITEM, 1);
		ItemMeta meta = item.getItemMeta();
		item.setDurability((short)3);
		meta.setDisplayName(ChatColor.DARK_GREEN + "Spawn in a " + ChatColor.GOLD + " Woodcutter" );
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Command this unit to gather wood for you");
		lore.add(ChatColor.GRAY + "Gathers: " + ChatColor.GOLD + "1 wood per second");
		lore.add(" ");
		lore.add(ChatColor.GRAY + "Cost: " + ChatColor.GOLD +UNITCOST + " gold");
		lore.add(ChatColor.GRAY + "Occupies: " + ChatColor.GOLD + FOODCOST +" food");
		lore.add(" ");
		lore.add(ChatColor.ITALIC + "This unit is under your control");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}

