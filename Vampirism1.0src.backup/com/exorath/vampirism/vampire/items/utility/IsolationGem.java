package com.exorath.vampirism.vampire.items.utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.exorath.vampirism.Building;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IsolationGem extends VampireItem {
	private int cooldown = 30;
	private long lastUse = 0;
	Calendar cal;

	public IsolationGem(int cost, Vampire vampire) {
		super(cost, vampire, Items.UTILITY);
		this.setItemStack(ITEMSTACK);
		cal = Calendar.getInstance();
	}

	public static final ItemStack ITEMSTACK = new ItemStack(Material.EMERALD, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Isolation gem");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click a building with the");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "gem to isolate it from any healing");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "or repairing, lasts 7 seconds.");
			l.add(" ");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.DARK_RED + "60s");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};

	public void isolate(Building b) {

		if (lastUse + cooldown * 1000 <= cal.getTimeInMillis()) {
			b.setIsolated(true);
			ParticleEffects.sendToLocation(ParticleEffects.ENDER, b.getMiddle(), (float) (b.getMaxLoc().getX() - b.getMinLoc().getX()) / 2, (float) (b.getMaxLoc().getY() - b.getMinLoc().getY()) / 2, (float) (b.getMaxLoc().getZ() - b.getMinLoc().getZ()) / 2, 1, 500);
			b.getMinLoc().getWorld().playSound(b.getMiddle(), Sound.DONKEY_ANGRY, 10, 1);
			lastUse = cal.getTimeInMillis();
			final Building building = b;
			final int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable() {
				@Override
				public void run() {
					if (building.isAlive) {
						building.getMinLoc().getWorld().playSound(building.getMiddle(), Sound.DONKEY_ANGRY, 10, 1);
						ParticleEffects.sendToLocation(ParticleEffects.ENDER, building.getMiddle(), (float) (building.getMaxLoc().getX() - building.getMinLoc().getX()) / 2, (float) (building.getMaxLoc().getY() - building.getMinLoc().getY()) / 2, (float) (building.getMaxLoc().getZ() - building.getMinLoc().getZ()) / 2, 1, 500);
					}
				}
			}, 0, 10l);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Vampirism"),new Runnable(){
				@Override
				public void run(){
					Bukkit.getScheduler().cancelTask(taskId);
					building.setIsolated(false);
				}
			},7*20l);
		} else {
			getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Burst gem: " + ChatColor.RESET + "" + ChatColor.DARK_RED + -(cal.getTimeInMillis() - lastUse - cooldown * 1000) / 1000 + "s");

		}
	}

	@Override
	public void removeItem() {
		getVampire().setClawsOfFortune(false);
	}

}