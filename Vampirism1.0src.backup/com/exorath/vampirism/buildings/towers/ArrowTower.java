package com.exorath.vampirism.buildings.towers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.buildings.towers.ArrowTower2.ArcaneTower;
import com.exorath.vampirism.buildings.towers.ArrowTower2.FireTower;
import com.exorath.vampirism.buildings.towers.ArrowTower2.WeaknessTower;
import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.managers.BuildingManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ArrowTower extends Building {
	Location a1loc;
	Location a2loc;
	Location a3loc;
	Location a4loc;
	Location middle;
	BuildingManager bm;
	Location[] locs;
	int damage = 2;
	int damageUp = 4;
	double speedUp = 10;
	int healthUp;
	static int maxLevel = 3;
	
	//Building costs:
	public static int COSTGOLD = 0;
	public static int COSTWOOD = 50;
	

	public ArrowTower(Location minLoc, Location maxLoc, Schematic schematic, int health, Builder owner, BuildingManager bm) {
		super(minLoc, maxLoc, schematic, health, owner, bm);
		this.middle = new Location(minLoc.getWorld(), (minLoc.getX() + maxLoc.getX()) / 2, (minLoc.getY() + maxLoc.getY()) / 2, (minLoc.getZ() + maxLoc.getZ()) / 2);
		a1loc = middle.clone().add(3, 0, 0);
		a2loc = middle.clone().add(0, 0, -3);
		a3loc = middle.clone().add(-3, 0, 0);
		a4loc = middle.clone().add(0, 0, 3);
		Location[] locs = { a1loc, a2loc, a3loc, a4loc };
		this.locs = locs;
		this.bm = bm;
		super.name = "Arrow Tower";
		healthUp = super.getMaxHealth() + 20;
		inv = Bukkit.createInventory(owner.getPlayer(), 9, ChatColor.DARK_GRAY + name);
		
		//Building costs:
		costWood = 300;
		costGold = 0;
		
	}
	public static List<Player> getNearbyEntities(Location location, double range) {
		List<Player> es = new ArrayList<Player>();

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getWorld().equals(location.getWorld()) && location.distance(p.getLocation()) <= range) {
				es.add(p);
			}
		}
		return es;
	}
	@Override
	public void loop() {
		Player closest = getClosestVampire(middle, 40);
		if (closest != null && isBuild) {
			Location arrowLoc = getClosestArrow(closest, locs);
			Entity a = arrowLoc.getWorld().spawnEntity(arrowLoc, EntityType.ARROW);
			a.setMetadata("damage", new FixedMetadataValue(bm.getPlugin(), damage));
			a.getWorld().playSound(a.getLocation(), Sound.SHOOT_ARROW, 10, 1);
			Vector v = closest.getLocation().subtract(arrowLoc).add(0, 2, 0).toVector().multiply(0.15);
			a.setVelocity(v);
			ParticleEffects.sendToLocation(ParticleEffects.FIRE, a.getLocation(), 0, 0, 0, 1, 30);
			homing(a, 30);
		}

	}

	public Player getClosestVampire(Location loc, int range) {
		Player closestPlayer = null;
		for (Player p : getNearbyEntities(loc, range)) {
			if(bm.isVampire(p)) {
				if (closestPlayer == null) {
					closestPlayer = (Player) p;
				} else {
					if (closestPlayer.getLocation().distance(loc) > p.getLocation().distance(loc)) {
						closestPlayer = (Player) p;
					}
				}
			}
		}
		return closestPlayer;
	}

	public static Location getClosestArrow(Player p, Location[] locs) {
		Location closestLoc = null;
		for (Location loc : locs) {
			if (closestLoc == null) {
				closestLoc = loc;
			} else {
				if (closestLoc.distance(p.getLocation()) > loc.distance(p.getLocation())) {
					closestLoc = loc;
				}
			}
		}
		return closestLoc;
	}

	public static void homing(final Entity entity, final int repeat) {

		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable() {
			@Override
			public void run() {
				if (entity != null && !entity.isDead()&& entity.getVelocity() != new Vector(0,0,0)) {
					if (repeat != 0) {
						ParticleEffects.sendToLocation(ParticleEffects.GREEN_SPARKLE, entity.getLocation(), 0.2f, 0.2f, 0.2f, 1, 1);
						homing(entity, repeat - 1);
					} else {
						entity.remove();
					}
				}
			}
		}, 1l);
	}

	@Override
	public void show(Player p) {
		if (isBuild) {
			inv.setItem(0, getInfoItem());
			inv.setItem(4, CustomItems.REMOVE);
			if(getBuilder().getTier() == 1){
				inv.setItem(8, getUpgradeItem());
			}else{
				if(!getBuilder().hasWeaknessTower){
				inv.setItem(6, CustomItems.addGlow( getUpgradeWeaknessItem()));
				}
				inv.setItem(7, CustomItems.addGlow( getUpgradeFireItem()));
				inv.setItem(8, CustomItems.addGlow( getUpgradeArcaneItem()));
			}
			p.openInventory(inv);
		}
	}

	@Override
	public void levelUp() throws IOException {
		switch (level) {
		case 1:
			isBuild = false;
			level = (short) (level + 1);
			setMaxHealth(healthUp);
			damage = damageUp;
			interval = (int) Math.round(speedUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your arrow tower is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("tower1_2.schematic");
			// properties for lvl3:
			healthUp = getMaxHealth() + 50;
			damageUp = 8;
			speedUp = 5;
			costWood = 1500;

			break;
		case 2:
			isBuild = false;
			level = (short) (level + 1);
			setMaxHealth(healthUp);
			damage = damageUp;
			interval = (int) Math.round(speedUp);
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your arrow tower is now" + ChatColor.GOLD + " Lv" + level);
			super.retexture("tower1_3.schematic");

			break;
		default:
			getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your tower is " + ChatColor.RED + "Max Leveled!");
		}

	}

	public ItemStack getInfoItem() {
		ItemStack item = new ItemStack(Material.GHAST_TEAR, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.DARK_GREEN + name + ChatColor.GOLD + " Lv" + level);
		List<String> lore = new ArrayList<String>();
		lore.add(ChatColor.GRAY + "Health: " + ChatColor.GOLD + this.getHealth() + ChatColor.GRAY + "/" + ChatColor.GOLD + getMaxHealth());
		lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level);
		lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GOLD + damage);
		lore.add(ChatColor.GRAY + "Speed: " + ChatColor.GOLD + Math.round((interval / 20) * 100.0) / 100.0);
		lore.add(" ");
		lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getUpgradeItem() {
		ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		if (level != maxLevel) {
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade to " + ChatColor.GOLD + " Lv" + (level + 1));
			lore.add(ChatColor.GRAY + "Health: " + ChatColor.GOLD + getMaxHealth() + ChatColor.GRAY + " -> " + ChatColor.GOLD + healthUp);
			lore.add(ChatColor.GRAY + "Level: " + ChatColor.GOLD + level + ChatColor.GRAY + " -> " + ChatColor.GOLD + (level + 1));
			lore.add(ChatColor.GRAY + "Damage: " + ChatColor.GOLD + this.damage + ChatColor.GRAY + " -> " + ChatColor.GOLD + damageUp);
			lore.add(ChatColor.GRAY + "Speed: " + ChatColor.GOLD + Math.round((interval / 20) * 100.0) / 100.0 + ChatColor.GRAY + " -> " + ChatColor.GOLD + Math.round((speedUp / 20) * 100.0) / 100.0);
			lore.add(" ");
			lore.add(ChatColor.GRAY + "Cost: " + costWood + ChatColor.DARK_GREEN + " wood");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		} else {
			meta.setDisplayName(ChatColor.DARK_GREEN + "Your building is " + ChatColor.RED + "max level");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getUpgradeWeaknessItem() {
		ItemStack item = new ItemStack(Material.FLINT, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade to " + ChatColor.GOLD + "Weakness tower Lv 1");
			lore.add(ChatColor.GRAY + "The weakness tower will reduce");
			lore.add(ChatColor.GRAY + "the targeted vampires damage.");
			lore.add(" ");
			lore.add(ChatColor.GRAY + "Cost: " + WeaknessTower.COSTWOOD + ChatColor.DARK_GREEN + " wood");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
			lore.add(ChatColor.ITALIC +""+ ChatColor.DARK_RED + "You can only have one weakness tower!");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getUpgradeArcaneItem() {
		ItemStack item = new ItemStack(Material.ENDER_PEARL, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade to " + ChatColor.GOLD + "Arcane tower Lv 1");
			lore.add(ChatColor.GRAY + "The arcane tower damage ignores");
			lore.add(ChatColor.GRAY + "the vampires their armor.");
			lore.add(" ");
			lore.add(ChatColor.GRAY + "Cost: " + ArcaneTower.COSTWOOD + ChatColor.DARK_GREEN + " wood");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	public ItemStack getUpgradeFireItem() {
		ItemStack item = new ItemStack(Material.FIREBALL, 1);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
			meta.setDisplayName(ChatColor.DARK_GREEN + "Upgrade to " + ChatColor.GOLD + "Fire tower Lv 1");
			lore.add(ChatColor.GRAY + "The fire tower deals high");
			lore.add(ChatColor.GRAY + "amounts of damage.");
			lore.add(" ");
			lore.add(ChatColor.GRAY + "Cost: " + FireTower.COSTWOOD + ChatColor.DARK_GREEN + " wood");
			lore.add(ChatColor.ITALIC + "" + ChatColor.DARK_GRAY + "Owned by " + getBuilder().getPlayer().getName());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
