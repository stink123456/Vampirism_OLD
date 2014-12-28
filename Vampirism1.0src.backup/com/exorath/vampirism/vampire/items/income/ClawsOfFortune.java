package com.exorath.vampirism.vampire.items.income;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

public class ClawsOfFortune extends VampireItem{
	public static final int COST = 10;
	public ClawsOfFortune(Vampire vampire) {
		super(COST,vampire, Items.GOLDINCOME);
		setItemStack(ITEMSTACK);
		vampire.setClawsOfFortune(true);
		
	}
	public static final ItemStack ITEMSTACK = new ItemStack(Material.SHEARS, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +  "Claws of Fortune");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "You have a small chanse");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "to earn gold when you");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "attack a building. ");
			l.add("");
			l.add(ChatColor.DARK_GREEN + "" + ChatColor.ITALIC + "Click to remove, refunds " + ChatColor.RED +  COST/2 + "g.");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	@Override
	public void removeItem(){
		getVampire().setClawsOfFortune(false);
	}

}
