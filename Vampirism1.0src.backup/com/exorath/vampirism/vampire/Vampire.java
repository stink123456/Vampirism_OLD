package com.exorath.vampirism.vampire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.managers.BuildingManager;
import com.exorath.vampirism.managers.DisplayManager;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;
import com.exorath.vampirism.vampire.items.damage.BurstGem;
import com.exorath.vampirism.vampire.items.income.ClawsOfFortune;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Vampire {

	private Player player;
	DisplayManager dm;
	private BuildingManager bm;

	private boolean invisible = false;
	private boolean immune = false;
	// final stats (after calcs)
	// attack speed is per second
	private int fDamage = 1;
	private float fAttackSpeed = 1;
	private int fArmor = 0;
	private int fArmorReducement = 0;

	private int health = 150;
	private int mana = 100;
	private int fMaxHealth = 150;
	private int fMaxMana = 100;
	
	private float fHealthPerSec = 0.25f;
	private float fManaPerSec = 0.91f;
	// item stats
	private int extraArmor = 0;
	private int extraHealth = 0;
	private int extraMana = 0;
	private int extraAttackSpeed = 0;

	// attributes
	private int agility = 5;
	private int strength = 5;
	private int intelligence = 5;
	private int damage = 1;

	// level
	private int level = 1;
	private int expToNext = 50;
	private int exp = 0;
	// resources
	private int gold = 50;

	// static (per level)
	public final static int attrPerLevel = 1;
	public final static int healthPerLevel = 1;
	public final static int manaPerLevel = 1;
	public final static int armorPerLevel = 1;

	// base values
	public final static int BASEHP = 150;
	public final static int BASEMP = 100;

	// modifiers
	private int damageReduced = 0;
	// amount of towers that are reducing damage, if 0, damagereduced will be 0
	// too.
	private int damageReducedTowers = 0;

	// items layout stuff
	private List<VampireItem> items = new ArrayList<VampireItem>();
	private List<Items> itemTypes = new ArrayList<Items>();
	private HashMap<VampireItem, Integer> itemsSlots = new HashMap<VampireItem, Integer>();
	private HashMap<Integer, VampireItem> itemsSlotsBySlot = new HashMap<Integer, VampireItem>();
	public final static int MAXITEMS = 5;

	// items stuff
	private boolean clawsOfFortune = false;

	// constructor, ran when game starts
	public Vampire(Player player, DisplayManager dm, BuildingManager bm) {
		this.player = player;
		this.dm = dm;
		this.bm = bm;
		dm.addDisplayVampire(this);

		generateInventory();
		//VampireItem v = new VampireItem(1, this, Items.VAMPIREITEM);
		//v.setItemStack(new ItemStack(Material.IRON_AXE, 1));
		
		VampireItem v3 = new BurstGem(1, this);

		//addItem(v);
		addItem(v3);
	}

	public void updateFinalStats() {

		// add strength values
		fMaxHealth = BASEHP + extraHealth +15 * strength + level * healthPerLevel;
		fHealthPerSec = 0.25f + 0.05f * strength;

		// add intelligence values
		fMaxMana = BASEMP + extraMana + 15 * intelligence;
		fManaPerSec = 0.91f + 0.05f * intelligence;

		// add agility values
		fArmor = (int) (0.3f * agility) + extraArmor;
		fAttackSpeed = 1 + 0.02f * agility +extraAttackSpeed;

		// add damage value (percential reduced with the damageReduced percent)
		fDamage = (damage) / 100 * (100 - damageReduced);

		fArmorReducement = 100 / (100 + fArmor);
		dm.updateVampireAttributes(this);
	}

	public void generateInventory() {
		Inventory i = player.getInventory();
		i.setItem(0, CustomItems.VAMPIREWEAPON);
		for (int n = 4; n < i.getSize(); n++) {
			if (n < 9) {
				i.setItem(n, CustomItems.UNUSEDSLOT);
			} else {
				i.setItem(n, CustomItems.UNUSABLESLOT);
			}
		}
	}

	public void addItem(VampireItem item) {
		// check if theres an item of the same type in the inventory already, if
		// so stop the purchase
		if (!itemTypes.contains(item.getItemtype())) {
			if (items.size() <= MAXITEMS) {
				if (this.hasGold(item.getCost())) {
					addGold(-item.getCost());
					items.add(item);
					itemTypes.add(item.getItemtype());
					Inventory inv = player.getInventory();
					for (int n = 4; n < 9; n++) {
						if (inv.getItem(n).getType() == Material.SEEDS) {
							inv.setItem(n, item.getItemStack());
							itemsSlots.put(item, n);
							itemsSlotsBySlot.put(n, item);
							getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your item has been bought. " + ChatColor.RED + item.getCost() + " gold " + ChatColor.DARK_GREEN + "has been subtracted.");
							break;
						}
					}
				} else {
					getPlayer().sendMessage(ChatColor.DARK_GREEN + "You do not have enough" + ChatColor.RED + " gold " + ChatColor.GREEN + "for this item.");
				}
			} else {
				getPlayer().sendMessage(ChatColor.DARK_GREEN + "No space for another item, right click an item to remove it");
			}
		} else {
			getPlayer().sendMessage(ChatColor.DARK_GREEN + "You already have an item of this type.");
		}
		getPlayer().closeInventory();
	}

	public void removeItem(Items i) {

		for (VampireItem item : items) {
			if (item.getItemtype() == i) {
				// TODO: remove the itemstack from the inventory
				getPlayer().getInventory().setItem(itemsSlots.get(item), CustomItems.UNUSEDSLOT);

				// remove the item from all registered places
				item.removeItem();
				items.remove(item);
				itemTypes.remove(item.getItemtype());
				itemsSlotsBySlot.remove(itemsSlots.get(item));
				itemsSlots.remove(item);
				
				break;
			}
		}
	}
	public void removeItem(VampireItem i) {

		for (VampireItem item : items) {
			if (item == i) {
				// TODO: remove the itemstack from the inventory
				getPlayer().getInventory().setItem(itemsSlots.get(item), CustomItems.UNUSEDSLOT);
				// remove the item from all registered places
				item.removeItem();
				items.remove(item);
				itemTypes.remove(item.getItemtype());
				itemsSlotsBySlot.remove(itemsSlots.get(item));
				itemsSlots.remove(item);
				break;
			}
		}
	}
	public void setDamageReduced(int reducedDamage) {
		if (damageReduced == 0) {
			getPlayer().sendMessage(ChatColor.GRAY + "Your damage has been reduced by " + reducedDamage + "%.");
		}
		this.damageReduced = reducedDamage;
		damageReducedTowers++;
		updateFinalStats();

		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getServer().getPluginManager().getPlugin("Vampirism"), new Runnable() {
			@Override
			public void run() {
				damageReducedTowers--;
				if (damageReducedTowers == 0) {
					getPlayer().sendMessage(ChatColor.GRAY + "Your are now dealing 100% damage again.");
					damageReduced = 0;
					updateFinalStats();
				}

			}
		}, 30l);
	}

	public VampireItem getItem(Items type) {
		for (VampireItem item : getItems()) {
			if (item.getItemtype() == type) {
				return item;
			}
		}
		return null;
	}

	public int getfDamage() {
		return fDamage;
	}

	public void setfDamage(int fDamage) {
		this.fDamage = fDamage;
	}

	public float getfAttackSpeed() {
		return fAttackSpeed;
	}

	public void setfAttackSpeed(float fAttackSpeed) {
		this.fAttackSpeed = fAttackSpeed;
	}

	public int getfArmor() {
		return fArmor;
	}

	public void setfArmor(int fArmor) {
		this.fArmor = fArmor;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public float getfHealthPerSec() {
		return fHealthPerSec;
	}

	public void setfHealthPerSec(float fHealthPerSec) {
		this.fHealthPerSec = fHealthPerSec;
	}

	public float getfManaPerSec() {
		return fManaPerSec;
	}

	public void setfManaPerSec(float fManaPerSec) {
		this.fManaPerSec = fManaPerSec;
	}

	public int getExtraArmor() {
		return extraArmor;
	}

	public void setExtraArmor(int extraArmor) {
		this.extraArmor = extraArmor;
	}

	public int getExtraHealth() {
		return extraHealth;
	}

	public void setExtraHealth(int extraHealth) {
		this.extraHealth = extraHealth;
	}

	public int getExtraMana() {
		return extraMana;
	}

	public void setExtraMana(int extraMana) {
		this.extraMana = extraMana;
	}

	public int getExtraAttackSpeed() {
		return extraAttackSpeed;
	}

	public void setExtraAttackSpeed(int extraAttackSpeed) {
		this.extraAttackSpeed = extraAttackSpeed;
	}

	public int getAgility() {
		return agility;
	}

	public void setAgility(int agility) {
		this.agility = agility;
		updateFinalStats();
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
		updateFinalStats();
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
		updateFinalStats();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void addGold(int gold) {
		this.gold += gold;
		dm.updateVampireGold(this);

	}

	public boolean hasGold(int gold) {
		if (this.gold >= gold)
			return true;

		return false;
	}

	public Player getPlayer() {
		return player;
	}

	public int getExpToNext() {
		return expToNext;
	}

	public void setExpToNext(int expToNext) {
		this.expToNext = expToNext;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getDamageReduced() {
		return damageReduced;
	}

	// health modification
	public void damage(int amount) {
		if (!isImmune()) {
			int damage = amount / 100 * (100 - damageReduced);

			if (health - damage > 0) {
				health -= damage;
				// damage effects here:

			} else {
				health = 0;
				dead();
			}
		}else{
			getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.FIZZ, 1, 1);
			ParticleEffects.sendToLocation(ParticleEffects.LARGE_SMOKE, getPlayer().getLocation().add(0,1,0), 1, 1, 1, 0.3f, 15);
		}
	}

	public void damageTrue(int damage) {
		if (!isImmune()) {
			if (health - damage > 0) {
				health -= damage;
				// damage effects here:

			} else {
				health = 0;
				dead();
			}
		}else{
			getPlayer().getWorld().playSound(getPlayer().getLocation(), Sound.FIZZ, 1, 1);
			ParticleEffects.sendToLocation(ParticleEffects.LARGE_SMOKE, getPlayer().getLocation().add(0,1,0), 1, 1, 1, 0.3f, 15);
		}
	}

	public void heal(int healAmount) {
		if (health + damage <= fMaxHealth) {
			health += damage;
			// healing effects

		} else {
			health = fMaxHealth;
		}
	}

	public void dead() {
		Bukkit.broadcastMessage("Vampire has died!");
	}

	public void updateHealth() {
		getPlayer().setHealth((double) health / fMaxHealth);
	}

	public List<VampireItem> getItems() {
		return items;
	}

	public void setItems(List<VampireItem> items) {
		this.items = items;
	}

	public List<Items> getItemTypes() {
		return itemTypes;
	}

	public void setItemTypes(List<Items> itemTypes) {
		this.itemTypes = itemTypes;
	}

	public boolean hasClawsOfFortune() {
		return clawsOfFortune;
	}

	public void setClawsOfFortune(boolean clawsOfFortune) {
		this.clawsOfFortune = clawsOfFortune;
	}

	public BuildingManager getBuildingManager() {
		return bm;
	}

	public void setBuildingManager(BuildingManager bm) {
		this.bm = bm;
	}

	public int getfMaxMana() {
		return fMaxMana;
	}

	public void setfMaxMana(int fMaxMana) {
		this.fMaxMana = fMaxMana;
	}

	public int getfArmorReducement() {
		return fArmorReducement;
	}

	public void setfArmorReducement(int fArmorReducement) {
		this.fArmorReducement = fArmorReducement;
	}

	public boolean isInvisible() {
		return invisible;
	}

	public void setInvisible(boolean invisible) {
		this.invisible = invisible;
		if(invisible){
			for(Player p : Bukkit.getOnlinePlayers()){
				p.hidePlayer(getPlayer());
			}
		}else{
			for(Player p : Bukkit.getOnlinePlayers()){
				p.showPlayer(getPlayer());
			}
		}
	}

	public boolean isImmune() {
		return immune;
	}

	public void setImmune(boolean immune) {
		this.immune = immune;
	}

	public HashMap<VampireItem, Integer> getItemsSlots() {
		return itemsSlots;
	}

	public void setItemsSlots(HashMap<VampireItem, Integer> itemsSlots) {
		this.itemsSlots = itemsSlots;
	}

	public HashMap<Integer, VampireItem> getItemsSlotsBySlot() {
		return itemsSlotsBySlot;
	}

	public void setItemsSlotsBySlot(HashMap<Integer, VampireItem> itemsSlotsBySlot) {
		this.itemsSlotsBySlot = itemsSlotsBySlot;
	}
}
