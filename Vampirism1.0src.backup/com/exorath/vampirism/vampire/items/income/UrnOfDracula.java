package com.exorath.vampirism.vampire.items.income;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

public class UrnOfDracula extends VampireItem{
	public final static int COST = 10;
	float goldPerMin = 1;
	int taskId;
	public UrnOfDracula(Vampire vampire) {
		super(COST, vampire, Items.GOLDINCOME);
		setItemStack(ITEMSTACK);
		
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable(){
			@Override
			public void run(){
				getVampire().getPlayer().sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You earned " + Math.round(goldPerMin) + " gold because you have an urn of Dracula.");
				getVampire().addGold(Math.round(goldPerMin));
				goldPerMin += 2;
				
			}
		}, 20, 20*60);
		
	}
	public static final ItemStack ITEMSTACK = new ItemStack(Material.FLOWER_POT_ITEM, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +  "Urn of Dracula");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Every minute you");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "earn gold increasing");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "over time.");
			l.add("");
			l.add(ChatColor.RED + "" + ChatColor.ITALIC + "Click to remove, refunds " + COST/2 + " g.");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	@Override
	public void removeItem(){
		Bukkit.getScheduler().cancelTask(taskId);
	}
}
