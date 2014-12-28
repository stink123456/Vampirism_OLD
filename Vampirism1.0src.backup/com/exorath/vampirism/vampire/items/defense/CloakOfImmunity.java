package com.exorath.vampirism.vampire.items.defense;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class CloakOfImmunity extends VampireItem {
	private long lastUse = 0;
	private int cooldown = 45;
	Calendar cal = Calendar.getInstance();
	public CloakOfImmunity(int cost, Vampire vampire) {
		super(cost, vampire, Items.DEFENSE);
		setItemStack(ITEMSTACK);
		
	}
	public static final ItemStack ITEMSTACK = new ItemStack(Material.LEATHER, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +  "Cloak of immunity");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This cloak renders you");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "invulnerable for 6 seconds");
			l.add(" ");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cooldown: " + ChatColor.DARK_RED + "45s");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public void setImmune(){
		if (lastUse + cooldown *1000 <= cal.getTimeInMillis()) {
			lastUse = cal.getTimeInMillis();
			getVampire().setImmune(true);
			ParticleEffects.sendToLocation(ParticleEffects.LARGE_SMOKE, getVampire().getPlayer().getLocation().add(0,1,0), 1, 1, 1, 0.3f, 70);
			getVampire().getPlayer().getWorld().playSound(getVampire().getPlayer().getLocation(), Sound.FIZZ, 10, 1);
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable(){
				@Override
				public void run(){
					getVampire().setImmune(false);
				}
			});
		}else{
			getVampire().getPlayer().sendMessage(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Cloak of immunity: " + ChatColor.RESET + "" + ChatColor.DARK_RED+ -(cal.getTimeInMillis() - lastUse - cooldown*1000 )/1000 +"s");
		}
	}
}