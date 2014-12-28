package com.exorath.vampirism.libraries;

import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.buildings.towers.ArrowTower;
import com.exorath.vampirism.buildings.utility.Citadel;
import com.exorath.vampirism.buildings.utility.GoldMineV2;
import com.exorath.vampirism.buildings.utility.House;
import com.exorath.vampirism.buildings.walls.HealingWall;
import com.exorath.vampirism.buildings.walls.Wall;
import com.exorath.vampirism.vampire.items.income.ClawsOfFortune;
import com.exorath.vampirism.vampire.items.income.UrnOfDracula;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {
	public static final ItemStack TOWERS = new ItemStack(Material.ARROW, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Towers");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to go pick a tower to build");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack WALLS = new ItemStack(Material.COBBLE_WALL, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Walls");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to go pick a wall to build");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};

	public static final ItemStack UTILITY = new ItemStack(Material.REDSTONE, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Utility");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to go pick a utility building to build");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack BACK = new ItemStack(Material.NETHER_STAR, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Go back");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to go back to the category selector");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack AIR = new ItemStack(Material.AIR, 1);

	public static final ItemStack TOWERPEARLS = new ItemStack(Material.ARROW, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Tower of Pearls " + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.DARK_GREEN + ArrowTower.COSTWOOD + " wood" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to build this arrow shooting tower");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack WALLCARNELIAN = new ItemStack(Material.COBBLE_WALL, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Carnelian Wall " + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.DARK_GREEN + Wall.COSTWOOD + " wood" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to place this wall");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack HOUSE = new ItemStack(Material.WORKBENCH, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "House" + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + House.COSTGOLD + " gold" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to place this house");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack GOLDMINE = new ItemStack(Material.GOLD_BLOCK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Gold mine" + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + GoldMineV2.COSTGOLD + " gold" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to place this Mine");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack HEALWALL = new ItemStack(Material.INK_SACK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Heal wall" + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.DARK_GREEN + HealingWall.COSTWOOD + " wood" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to place this healing wall");
			meta.setLore(l);
			this.setItemMeta(meta);
			this.setDurability((short) 10);
		}
	};
	public static final ItemStack CITADEL = new ItemStack(Material.QUARTZ_BLOCK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Citadel" + ChatColor.DARK_AQUA + " [" + ChatColor.GRAY + "Cost: " + ChatColor.YELLOW + Citadel.COSTGOLD + " gold" + ChatColor.DARK_AQUA + "]");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Right click to place the citadel");
			meta.setLore(l);
			this.setItemMeta(meta);
			this.setDurability((short) 2);
		}
	};
	public static final ItemStack REMOVE = new ItemStack(Material.NETHER_STALK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Click to remove the building");
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack AGILITYITEM = new ItemStack(Material.INK_SACK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GREEN + "Agility:" + ChatColor.DARK_GRAY + " +10 ");
			List<String> l = new ArrayList<String>();
			l.add("");
			l.add(ChatColor.DARK_GRAY + "Costs: " + ChatColor.GOLD + "100 gold.");
			l.add("");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Each point of agility gives");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "you 0.3 extra armor and");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "it makes you attack faster.");
			l.add("");
			l.add(ChatColor.RED + "" + ChatColor.ITALIC + "Left click to add 10 agility");
			meta.setLore(l);
			this.setItemMeta(meta);
			// set color
			this.setDurability((short) 2);
		}
	};
	public static final ItemStack STRENGTHITEM = new ItemStack(Material.INK_SACK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "Strength:" + ChatColor.DARK_GRAY + " +10");
			List<String> l = new ArrayList<String>();
			l.add("");
			l.add(ChatColor.DARK_GRAY + "Costs: " + ChatColor.GOLD + "100 gold.");
			l.add("");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Each point of strength gives");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "you 15 extra health and");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "0.05 health regen per second.");
			l.add("");
			l.add(ChatColor.RED + "" + ChatColor.ITALIC + "Left click to add 10 strength");
			meta.setLore(l);
			this.setItemMeta(meta);
			// set color
			this.setDurability((short) 2);
		}
	};
	public static final ItemStack INTELLIGENCEITEM = new ItemStack(Material.INK_SACK, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW + "Intelligence:" + ChatColor.DARK_GRAY + " +10");
			List<String> l = new ArrayList<String>();
			l.add("");
			l.add(ChatColor.DARK_GRAY + "Costs: " + ChatColor.GOLD + "100 gold.");
			l.add("");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "Each point of intelligence gives");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "you 15 extra mana and");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "0.05 mana regen per second.");
			l.add("");
			l.add(ChatColor.RED + "" + ChatColor.ITALIC + "Left click to add 10 itelligence");
			meta.setLore(l);
			this.setItemMeta(meta);
			// set color
			this.setDurability((short) 2);
		}
	};
	public static final ItemStack UNUSEDSLOT = new ItemStack(Material.SEEDS, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GRAY + "Item slot - items you buy will go here");
			this.setItemMeta(meta);
		}
	};

	public static final ItemStack UNUSABLESLOT = new ItemStack(Material.MELON_SEEDS, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.RED + "closed slot - you can't use this");
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack VAMPIREWEAPON = new ItemStack(Material.GOLD_SWORD, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.RED +  "Vampiristic scepter - left click to attack");
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack GOLDINCOMECATEGORY = new ItemStack(Material.GOLD_INGOT, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN +  "Category: " + ChatColor.RED + "Gold Income");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "These items help you with earning gold.");
			l.add(" ");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Claws of fortune: " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Urn of dracula: " + ChatColor.GOLD + "10g");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack DAMAGECATEGORY = new ItemStack(Material.IRON_SWORD, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN +  "Category: " + ChatColor.RED + "Damage");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "These items are meant to deal mayor damage to buildings.");
			l.add(" ");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Claws of attack: " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Burst gem: " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Ring of damage: " + ChatColor.GOLD + "10g");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack DEFENSECATEGORY = new ItemStack(Material.GOLD_CHESTPLATE, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN +  "Category: " + ChatColor.RED + "Defense");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "These items are meant to deal mayor damage to buildings.");
			l.add(" ");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Cloak of immunity: " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Vampiristic shield: " + ChatColor.GOLD + "10g");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack UTILITYCATEGORY = new ItemStack(Material.FEATHER, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN +  "Category: " + ChatColor.RED + "Utility");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "These items are meant to deal mayor damage to buildings.");
			l.add(" ");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Draculas cloak : " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Vampiristic shield: " + ChatColor.GOLD + "10g");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack ABILITYCATEGORY = new ItemStack(Material.STRING, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.DARK_GREEN +  "Category: " + ChatColor.RED + "Ability");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "These items will give you special abilities.");
			l.add(" ");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Draculas cloak : " + ChatColor.GOLD + "10g");
			l.add(ChatColor.BLACK + "" + ChatColor.BOLD + "� " + ChatColor.RESET + ChatColor.DARK_GREEN + "Vampiristic shield: " + ChatColor.GOLD + "10g");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack CLAWSOFFORTUNE = new ItemStack(Material.SHEARS, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +  "" + ChatColor.BOLD +"Claws of Fortune");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.AQUA + "Description: " + ChatColor.GRAY + "" + ChatColor.ITALIC + "You have");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "a small chanse to earn gold");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "when you attack a building.");
			l.add(" ");
			l.add(ChatColor.DARK_GRAY + "Cost: " + ChatColor.GOLD + ClawsOfFortune.COST + " Gold");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack URNOFDRACULA = new ItemStack(Material.FLOWER_POT_ITEM, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.YELLOW +    "" + ChatColor.BOLD + "Urn of Dracula");
			List<String> l = new ArrayList<String>();
			l.add(ChatColor.AQUA + "Description: " +ChatColor.GRAY + "" + ChatColor.ITALIC + "Every");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "minute you earn gold");
			l.add(ChatColor.GRAY + "" + ChatColor.ITALIC + "increasing over time.");
			l.add(" ");
			l.add(ChatColor.DARK_GRAY + "Cost: " + ChatColor.GOLD + UrnOfDracula.COST + " Gold");
			meta.setLore(l);
			this.setItemMeta(meta);
		}
	};
	public static final ItemStack CATEGORYRETURN = new ItemStack(Material.NETHER_STAR, 1) {
		{
			ItemMeta meta = this.getItemMeta();
			meta.setDisplayName(ChatColor.GRAY +  "Return to category");
			this.setItemMeta(meta);
		}
	};
	public static ItemStack addGlow(ItemStack item) {
		net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null)
			tag = nmsStack.getTag();
		NBTTagList ench = new NBTTagList();
		tag.set("ench", ench);
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

}
