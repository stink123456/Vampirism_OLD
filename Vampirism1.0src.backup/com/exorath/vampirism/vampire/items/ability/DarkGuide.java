package com.exorath.vampirism.vampire.items.ability;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class DarkGuide extends VampireItem {
	private int cooldown = 10;
	private long lastUse = 0;
	Calendar cal;

	public DarkGuide(int cost, Vampire vampire) {
		super(cost, vampire, Items.DAMAGE);
		this.setItemStack(ITEMSTACK);
		cal = Calendar.getInstance();
	}

	public static final ItemStack ITEMSTACK = new ItemStack(Material.BLAZE_POWDER, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Dark guide");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Create a beam of darkness");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "towards the closest builder");
			l.add(" ");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.DARK_RED + "10s");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};

	public void findNearby() {

		if (lastUse + cooldown * 1000 <= cal.getTimeInMillis()) {
			Location loc = null;
			for (Builder b : getVampire().getBuildingManager().getBuilders()) {
				if (loc != null) {
					if (b.getPlayer().getLocation().distance(getVampire().getPlayer().getLocation()) <= loc.distance(getVampire().getPlayer().getLocation())) {
						loc = b.getPlayer().getLocation();
					}
				} else {
					loc = b.getPlayer().getLocation();
				}

			}
			if(loc != null){
			sendBeam(loc, 1);
			}
			
			lastUse = cal.getTimeInMillis();
		} else {
			// getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN +
			// "You can't use this item for another " + ChatColor.RED +
			// -(cal.getTimeInMillis() - lastUse - 30000 )/1000 +
			// ChatColor.DARK_GREEN +" seconds.");
			getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Burst gem: " + ChatColor.RESET + "" + ChatColor.DARK_RED + -(cal.getTimeInMillis() - lastUse - cooldown * 1000) / 1000 + "s");

		}
	}

	@Override
	public void removeItem() {
		getVampire().setClawsOfFortune(false);
	}
	private void sendBeam(final Location loc, final int distance){
		ParticleEffects.sendToLocation(ParticleEffects.FIRE, getVampire().getPlayer().getLocation().add(loc.toVector().normalize().multiply(0.2f).multiply(distance)), 1, 1, 1, 0.01f, 10);
		if(distance <= 10){
			sendBeam(loc, distance+1);
		}
		
	}

}