package com.exorath.vampirism;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;

import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.managers.BuildingManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Building {

	public String name;
	public boolean isAlive = true;
	public boolean isBuild = false;
	Location minLoc;
	Location maxLoc;
	Location middle;
	Location middleBottom;
	Location top;
	Rectangle rect;
	Schematic schematic;
	int health;
	private int maxHealth;
	public int interval = 40;
	Builder owner;
	BuildingManager bm;
	public short level = 1;
	public short levelTier2 = 0;
	public Inventory inv;
	public int costWood;
	public int costGold;
	private boolean isolated = false;
	private boolean disabled = false;

	//building object, instantiated when player places a building (in the BuildingManager)
	public Building(Location minLoc, Location maxLoc, Schematic schematic, int health, Builder owner, BuildingManager bm) {
		this.minLoc = minLoc;
		this.maxLoc = maxLoc;
		rect = new Rectangle((int) minLoc.getX(), (int) minLoc.getZ(), (int) maxLoc.getX() - (int) minLoc.getX(), (int) maxLoc.getZ() - (int) minLoc.getZ());
		this.schematic = schematic;
		this.health = health;
		this.maxHealth = health;
		this.owner = owner;
		this.bm = bm;
		bm.addBuilding(this);
		construct(schematic, minLoc);

		this.middle = minLoc.clone().add(maxLoc).multiply(0.5);
		this.middleBottom = minLoc.clone().add(maxLoc).multiply(0.5).add(0, -maxLoc.getY() / 2, 0);
		this.top = new Location(minLoc.getWorld(), (minLoc.getX() + maxLoc.getX()) / 2 + 0.5, maxLoc.getY() + 2, (minLoc.getZ() + maxLoc.getZ()) / 2 + 0.5);
	}

	public void construct(Schematic schematic, Location loc) {
		long i = Schematic.pasteSchematic(loc.getWorld(), loc, schematic, this);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable() {
			@Override
			public void run() {
				if (isAlive = true) {
					isBuild = true;
					runTheRunTask();
					owner.getPlayer().sendMessage(ChatColor.GREEN + "Your building has been build!");
				} else {
					owner.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your " + ChatColor.RED + name + ChatColor.DARK_GREEN + " has been destroyed.");
					remove();
				}
			}
		}, i);

	}

	public void heal(int health) {
		if (!isolated) {
			if (this.health + health > maxHealth) {
				this.health = maxHealth;
			} else {
				this.health = this.health + health;
			}
		}
	}

	private void runTheRunTask() {
		if (isAlive()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable() {
				@Override
				public void run() {
					runTheRunTask();
					if(!disabled)
					loop();
				}
			}, interval);
		}
	}

	public void remove() {
		minLoc.getWorld().playSound(minLoc, Sound.EXPLODE, 10, 1);
		ParticleEffects.sendToLocation(ParticleEffects.HUGE_EXPLODE, new Location(minLoc.getWorld(), (minLoc.getX() + maxLoc.getX()) / 2, (minLoc.getY() + maxLoc.getY()) / 2, (minLoc.getZ() + maxLoc.getZ()) / 2), 0, 0, 0, 1, 1);
		isAlive = false;
		owner.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your " + ChatColor.RED + name + ChatColor.DARK_GREEN + " has been destroyed.");
		bm.removeBuilding(this);
		clearArea(minLoc, maxLoc);
	}

	public void show(Player p) {

	}

	public void loop() {
	}

	// getters and setters
	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}

	public Location getTop() {
		return top;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public Location getMinLoc() {
		return minLoc;
	}

	public void setMinLoc(Location minLoc) {
		this.minLoc = minLoc;
	}

	public Location getMaxLoc() {
		return maxLoc;
	}

	public void setMaxLoc(Location maxLoc) {
		this.maxLoc = maxLoc;
	}

	public Location getMiddle() {
		return middle;
	}

	public String getName() {
		return name;
	}

	public Schematic getSchematic() {
		return schematic;
	}

	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	public void setSchematic(Schematic schematic) {
		this.schematic = schematic;
	}

	public Builder getBuilder() {
		return owner;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public void levelUp() throws IOException {

	}

	public Rectangle getRect() {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public void retexture(String fileName) throws IOException {
		clearArea(minLoc, maxLoc);
		long i = Schematic.pasteSchematic(getMinLoc().getWorld(), getMinLoc(), Schematic.loadSchematic(new File("schematics", fileName)), this);
		Bukkit.getScheduler().scheduleSyncDelayedTask(bm.getPlugin(), new Runnable() {
			@Override
			public void run() {
				isBuild = true;
			}
		}, i);
	}

	public void damage(int damage) {
		if (this.isAlive()) {
			health = health - damage;
		}
		if (this.isAlive() && health <= 0) {
			remove();
		}
	}

	public static void clearArea(Location minLoc, Location maxLoc) {
		World w = minLoc.getWorld();
		Location loc;
		int width = Math.abs(maxLoc.getBlockX() - minLoc.getBlockX());
		int height = Math.abs(maxLoc.getBlockY() - minLoc.getBlockY());
		int length = Math.abs(maxLoc.getBlockZ() - minLoc.getBlockZ());
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < length; z++) {
				for (int y = 0; y < height; y++) {
					loc = new Location(w, x + minLoc.getX(), y + minLoc.getY(), z + minLoc.getZ());
					loc.getBlock().removeMetadata("building", Bukkit.getPluginManager().getPlugin("Vampirism"));
					loc.getBlock().setType(Material.AIR);
				}
			}
		}
	}

	public Location getMiddleBottom() {
		return middleBottom;
	}

	public void setMiddleBottom(Location middleBottom) {
		this.middleBottom = middleBottom;
	}

	public boolean isIsolated() {
		return isolated;
	}

	public void setIsolated(boolean isolated) {
		this.isolated = isolated;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
