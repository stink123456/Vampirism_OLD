package com.exorath.vampirism.vampire.items.defense;

import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class VampiristicShield extends VampireItem{
	public static final int HEALTHBONUS = 120;
	public static final int ARMORBONUS = 15;
	public VampiristicShield(int cost, Vampire vampire) {
		super(cost, vampire, Items.DEFENSE);
		setItemStack(ITEMSTACK);
		getVampire().setExtraHealth(getVampire().getExtraHealth() + HEALTHBONUS);
		getVampire().setExtraArmor(getVampire().getExtraArmor()   + ARMORBONUS);
		
	}
	public static final ItemStack ITEMSTACK = new ItemStack(Material.ACTIVATOR_RAIL, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +  "Vampiristic shield");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "This shield gives you");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "120 extra health and 15");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "extra armor.");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	@Override
	public void removeItem(){
		getVampire().setExtraHealth(getVampire().getExtraHealth() - HEALTHBONUS);
		getVampire().setExtraArmor(getVampire().getExtraArmor()   - ARMORBONUS);
	}

}