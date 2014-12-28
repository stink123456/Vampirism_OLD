package com.exorath.vampirism.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.exorath.vampirism.Building;
import com.exorath.vampirism.Vampirism;
import com.exorath.vampirism.buildings.towers.ArrowTower;
import com.exorath.vampirism.buildings.utility.Citadel;
import com.exorath.vampirism.buildings.utility.GoldMineV2;
import com.exorath.vampirism.buildings.utility.House;
import com.exorath.vampirism.buildings.walls.HealingWall;
import com.exorath.vampirism.buildings.walls.Wall;
import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.managers.BuildingManager;
import com.exorath.vampirism.managers.VampireManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class BuildEvents implements Listener {
	Vampirism m;
	HashMap<Player, Location> clicked = new HashMap<Player, Location>();
	List<Player> buildable = new ArrayList<Player>();
	public BuildingManager bm;
	VampireManager vm;
	private static final String NOWOOD = ChatColor.DARK_GREEN + "You do not have enouch " + ChatColor.RED + "wood or gold " + ChatColor.DARK_GREEN + "to place this building.";

	public BuildEvents(Vampirism m) {
		this.m = m;
		bm = new BuildingManager(m);
		vm = new VampireManager(m, bm);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) throws IOException {
		if (e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().hasItemMeta() && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			// ARROW TOWER
			if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.TOWERPEARLS.getItemMeta())) {
				e.setCancelled(true);
				// check if player has resources
				if (bm.getBuilder(e.getPlayer()).hasWood(ArrowTower.COSTWOOD)) {
					final Player p = e.getPlayer();

					Location clickloc = e.getClickedBlock().getLocation();

					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 7, 14, 7)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + "50" + ChatColor.DARK_GREEN + " wood.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the building.");
						}
					} else {
						if (buildable.contains(p)) {
							try {
								Location loc = clicked.get(p);
								bm.getBuilder(p).removeWood(ArrowTower.COSTWOOD);
								new ArrowTower(loc, new Location(loc.getWorld(), loc.getX() + 7, loc.getY() + 13, loc.getZ() + 7), Schematic.loadSchematic(new File("schematics", "tower1.schematic")), 50, bm.getBuilder(p), bm);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
				// WALL OF CARNELIAN
			} else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.WALLCARNELIAN.getItemMeta())) {
				e.setCancelled(true);
				final Player p = e.getPlayer();
				Location clickloc = e.getClickedBlock().getLocation();
				if (bm.getBuilder(p).hasWood(Wall.COSTWOOD)) {
					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 5, 5, 5)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + Wall.COSTWOOD + ChatColor.DARK_GREEN + " wood.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the building.");
						}
					} else {
						if (buildable.contains(p)) {
							Location loc = clicked.get(p);
							bm.getBuilder(p).removeWood(Wall.COSTWOOD);
							new Wall(loc, new Location(loc.getWorld(), loc.getX() + 5, loc.getY() + 5, loc.getZ() + 5), Schematic.loadSchematic(new File("schematics", "wall1_1.schematic")), 50, bm.getBuilder(p), bm);
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
				// HOUSE
			} else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.HOUSE.getItemMeta())) {
				e.setCancelled(true);
				final Player p = e.getPlayer();
				if (bm.getBuilder(p).hasGold(House.COSTGOLD)) {
					Location clickloc = e.getClickedBlock().getLocation();

					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 4, 7, 5)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + House.COSTGOLD+ ChatColor.DARK_GREEN + " gold.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the building.");
						}
					} else {
						if (buildable.contains(p)) {
							Location loc = clicked.get(p);
							bm.getBuilder(p).removeGold(House.COSTGOLD);
							new House(loc, new Location(loc.getWorld(), loc.getX() + 4, loc.getY() + 7, loc.getZ() + 5), Schematic.loadSchematic(new File("schematics", "house_1.schematic")), 50, bm.getBuilder(p), bm);
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
				//Gold mine v2
			}else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.GOLDMINE.getItemMeta())) {
				e.setCancelled(true);
				final Player p = e.getPlayer();
				if (bm.getBuilder(p).hasGold(GoldMineV2.COSTGOLD)) {
					Location clickloc = e.getClickedBlock().getLocation();

					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 9, 4, 8)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + GoldMineV2.COSTGOLD+ ChatColor.DARK_GREEN + " gold.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the building.");
						}
					} else {
						if (buildable.contains(p)) {
							Location loc = clicked.get(p);
							bm.getBuilder(p).removeGold(House.COSTGOLD);
							new GoldMineV2(loc, new Location(loc.getWorld(), loc.getX() + 9, loc.getY() + 4, loc.getZ() + 8), Schematic.loadSchematic(new File("schematics", "GoldMine.schematic")),50, bm.getBuilder(p), bm);
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
				
			//HEALING WALL
			}else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.HEALWALL.getItemMeta())) {
				e.setCancelled(true);
				final Player p = e.getPlayer();
				if (bm.getBuilder(p).hasWood(HealingWall.COSTWOOD)) {
					Location clickloc = e.getClickedBlock().getLocation();
					
					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 5, 10, 5)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + HealingWall.COSTWOOD + ChatColor.DARK_GREEN + " wood.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the healing wall.");
						}
					} else {
						if (buildable.contains(p)) {
							Location loc = clicked.get(p);
							bm.getBuilder(p).removeWood(HealingWall.COSTWOOD);
							new HealingWall(loc, new Location(loc.getWorld(), loc.getX() + 5, loc.getY() + 10, loc.getZ() +  5), Schematic.loadSchematic(new File("schematics", "healwall_1.schematic")), bm.getBuilder(p), bm);
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
				//CITADEL
			}else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.CITADEL.getItemMeta())) {
				e.setCancelled(true);
				final Player p = e.getPlayer();
				if (bm.getBuilder(p).hasGold(Citadel.COSTGOLD)) {
					Location clickloc = e.getClickedBlock().getLocation();
					
					if (!clicked.containsKey(p)) {
						clicked.put(p, clickloc);
						Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
							public void run() {
								clicked.remove(p);
								if (buildable.contains(p)) {
									buildable.remove(p);
								}
							}
						}, 60l);
						if (generateBorders(clickloc.add(0, 1, 0), 7, 11, 7)) {
							p.sendMessage(ChatColor.DARK_GREEN + "Click the location again to place the building! Costs " + ChatColor.RED + Citadel.COSTGOLD + ChatColor.DARK_GREEN + " gold.");
							buildable.add(p);
						} else {
							p.sendMessage(ChatColor.RED + "Can't place the Citadel.");
						}
					} else {
						if (buildable.contains(p)) {
							Location loc = clicked.get(p);
							bm.getBuilder(p).removeGold(Citadel.COSTGOLD);
							new Citadel(loc, new Location(loc.getWorld(), loc.getX() + 7, loc.getY() + 11, loc.getZ() + 7), Schematic.loadSchematic(new File("schematics", "citadel.schematic")), bm.getBuilder(p), bm);
							Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable() {
								@Override
								public void run() {
									clicked.remove(p);
								}
							}, 20l);
							buildable.remove(p);
						}
					}
				} else {
					e.getPlayer().sendMessage(NOWOOD);
				}
			}
		}
	}

	private boolean generateBorders(final Location loc, int width, int height, int length) {
		// width = x
		// height = y
		// length = z

		ParticleEffects type = ParticleEffects.GREEN_SPARKLE;
		// check if theres a building below or above.
		for (Building building : bm.getbuildings()) {
			if (building.getMinLoc().getX() < loc.getX() && building.getMaxLoc().getX() > loc.getX() && building.getMinLoc().getZ() < loc.getZ() && building.getMaxLoc().getZ() > loc.getZ()) {
				type = ParticleEffects.INSTANT_SPELL;
				break;
			}
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				for (int z = 0; z < length; z++) {
					if (loc.getWorld().getBlockAt(new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y, loc.getZ() + z)).getType() != Material.AIR) {
						type = ParticleEffects.INSTANT_SPELL;
						break;

					}
				}
			}
		}
		// check if the space is empty
		int blockX = loc.getBlockX();
		int blockY = loc.getBlockY();
		int blockZ = loc.getBlockZ();
		// x lines

		for (int x = 0; x <= width; x++) {
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + x, blockY, blockZ), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + x, blockY + height, blockZ), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + x, blockY + height, blockZ + length), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + x, blockY, blockZ + length), 0, 0, 0, 1, 3);
		}

		// y lines
		for (int y = 0; y <= height; y++) {
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX, blockY + y, blockZ), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + width, blockY + y, blockZ + length), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX, blockY + y, blockZ + length), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + width, blockY + y, blockZ), 0, 0, 0, 1, 3);
		}

		// z lines
		for (int z = 0; z <= length; z++) {
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX, blockY, blockZ + z), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + width, blockY + height, blockZ + z), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX, blockY + height, blockZ + z), 0, 0, 0, 1, 3);
			ParticleEffects.sendToLocation(type, new Location(loc.getWorld(), blockX + width, blockY, blockZ + z), 0, 0, 0, 1, 3);
		}
		if (type == ParticleEffects.GREEN_SPARKLE) {
			loc.getWorld().playSound(loc, Sound.SUCCESSFUL_HIT, 10, 1);
			return true;
		} else {
			loc.getWorld().playSound(loc, Sound.SUCCESSFUL_HIT, 0.3f, 0.1f);
			return false;
		}

	}
}
