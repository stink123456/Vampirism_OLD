package com.exorath.vampirism.vampire.items.ability;

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
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CloudOfBats extends VampireItem {
	private static final int cooldown = 30;
	private long lastUse = 0;
	private static final int BATAMOUNT = 20;
	private static final int DISABLEDTIME = 300;
	Calendar cal;

	public CloudOfBats(int cost, Vampire vampire) {
		super(cost, vampire, Items.DAMAGE);
		this.setItemStack(ITEMSTACK);
		cal = Calendar.getInstance();
	}

	public static final ItemStack ITEMSTACK = new ItemStack(Material.STRING, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Cloud of Bats");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Generate a big cloud of bats");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "confusing towers around them");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "making them target the bats.");
			l.add(" ");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.DARK_RED + "90s");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};

	public void generateBats() {
			for(int i = 0; i <= BATAMOUNT; i++){
				getVampire().getPlayer().getWorld().spawnEntity(getVampire().getPlayer().getLocation(), EntityType.BAT);
				ParticleEffects.sendToLocation(ParticleEffects.LARGE_SMOKE, getVampire().getPlayer().getLocation(), 1, 1, 1, 1, 10);
				getVampire().getPlayer().getWorld().playSound(getVampire().getPlayer().getLocation(), Sound.BAT_TAKEOFF, 1, 1);
			}
			for(Building b : getVampire().getBuildingManager().getbuildings()){
				if(b.getMiddleBottom().distance(getVampire().getPlayer().getLocation()) < 7){
					b.setDisabled(true);
					final Building building = b;
					Bukkit.getScheduler().scheduleSyncDelayedTask(getVampire().getBuildingManager().getPlugin(), new Runnable(){
						@Override
						public void run(){
							building.setDisabled(false);
						}
					},DISABLEDTIME);
				}
			}
	}

	@Override
	public void removeItem() {
		getVampire().setClawsOfFortune(false);
	}
}
