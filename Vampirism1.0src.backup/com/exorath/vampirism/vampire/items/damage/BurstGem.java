package com.exorath.vampirism.vampire.items.damage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.exorath.vampirism.Building;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BurstGem extends VampireItem {
	private int cooldown = 30;
	private long lastUse = 0;
	Calendar cal;
	public BurstGem(int cost, Vampire vampire) {
		super(cost, vampire, Items.DAMAGE);
		this.setItemStack(ITEMSTACK);
		cal = Calendar.getInstance();
	}

	public static final ItemStack ITEMSTACK = new ItemStack(Material.EMERALD, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Burst gem");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click a building with the");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "gem to deal 30% of the damage");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "buildings current health in damage.");
			l.add(" ");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.DARK_RED + "30s");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};

	public void damageBuilding(Building b) {
		
		if (lastUse + cooldown *1000 <= cal.getTimeInMillis()) {
			b.damage((int)((float)b.getHealth() / 100 * 30));
			ParticleEffects.sendToLocation(ParticleEffects.SLIME, b.getMiddle(), (float) (b.getMaxLoc().getX() - b.getMinLoc().getX()) / 2, (float) (b.getMaxLoc().getY() - b.getMinLoc().getY()) / 2, (float) (b.getMaxLoc().getZ() - b.getMinLoc().getZ()) / 2, 1, 500);
			b.getMinLoc().getWorld().playSound(b.getMiddle(), Sound.BAT_IDLE, 10, 1);
			lastUse = cal.getTimeInMillis();
		}else{
			//getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You can't use this item for another " + ChatColor.RED + -(cal.getTimeInMillis() - lastUse - 30000 )/1000 + ChatColor.DARK_GREEN +" seconds.");
			getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Burst gem: " + ChatColor.RESET + "" + ChatColor.DARK_RED+ -(cal.getTimeInMillis() - lastUse - cooldown*1000 )/1000 +"s");
			
		}
	}

	@Override
	public void removeItem() {
		getVampire().setClawsOfFortune(false);
	}

}
