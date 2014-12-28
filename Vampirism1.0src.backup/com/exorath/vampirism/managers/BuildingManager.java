package com.exorath.vampirism.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.Vampirism;
import com.exorath.vampirism.buildings.towers.ArrowTower2.ArcaneTower;
import com.exorath.vampirism.buildings.towers.ArrowTower2.FireTower;
import com.exorath.vampirism.buildings.towers.ArrowTower2.WeaknessTower;
import com.exorath.vampirism.buildings.utility.Citadel;
import com.exorath.vampirism.buildings.utility.House;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.libraries.Schematic;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.VampireItem;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class BuildingManager implements Listener {
	private HashMap<Player, NPC> currentNPC = new HashMap<Player, NPC>();
	private List<Builder> builders;
	private List<Vampire> vampires;
	private List<Player>  builderPlayers;
	private List<Player>  vampirePlayers;
	private List<Building> buildings;
	private Vampirism main;

	public BuildingManager(Vampirism main) {
		this.main = main;
		builders = new ArrayList<Builder>();
		vampires = new ArrayList<Vampire>();
		buildings = new ArrayList<Building>();
		builderPlayers = new ArrayList<Player>();
		vampirePlayers = new ArrayList<Player>();
		Bukkit.getPluginManager().registerEvents(this, main);

	}
	public void addItem(Vampire v, VampireItem item){
		//TODO: create this function!
	}
	public Builder getBuilder(Player p) {
		for (Builder builder : builders) {
			if (builder.getPlayer().equals(p)) {
				return builder;
			}
		}
		return null;

	}
	public Vampire getVampire(Player p) {
		for (Vampire v : vampires) {
			if (v.getPlayer().equals(p)) {
				return v;
			}
		}
		return null;

	}
	public void removeBuilder(Builder b){
		//reset builder's scoreboard
		b.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		
		//remove players building
		for(Building building : getbuildings()){
			if(b== building.getBuilder()){
				building.remove();
			}
		}
		//remove player from all lists
		builders.remove(b);
		builderPlayers.remove(b.getPlayer());
		
		//Should be everything!
	}
	public void removeVampire(Vampire v){
		//reset vampire's scoreboard
		v.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		//remove player from all lists
		vampires.remove(v);
		vampirePlayers.remove(v.getPlayer());
		
		//Should be everything!
	}
	public boolean isVampire(Player p){
		if(vampirePlayers.contains(p)) return true;
		return false;
	}
	public boolean isBuilder(Player p){
		if(builderPlayers.contains(p)) return true;
		return false;
	}

	public Plugin getPlugin() {
		return main;
	}

	public void addBuilder(Builder b) {
		builders.add(b);
		builderPlayers.add(b.getPlayer());
	}
	public void addVampire(Vampire v) {
		vampires.add(v);
		vampirePlayers.add(v.getPlayer());
	}

	public List<Building> getbuildings() {
		return buildings;
	}

	public void addBuilding(Building building) {
		buildings.add(building);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			Location loc = e.getClickedBlock().getLocation();
			for (Building b : buildings) {
				if (b.getMinLoc().getX() <= loc.getX() && b.getMinLoc().getY() <= loc.getY() && b.getMinLoc().getZ() <= loc.getZ() && b.getMaxLoc().getX() > loc.getX() && b.getMaxLoc().getY() > loc.getY() && b.getMaxLoc().getZ() > loc.getZ()) {
					if (b.getBuilder().getPlayer().equals(e.getPlayer())) {
						b.show(e.getPlayer());
						break;
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClickEvent(InventoryClickEvent e) throws IOException {
		if (e.getInventory().contains(Material.GHAST_TEAR)) {
			if (e.getCurrentItem().getType() == Material.GOLD_NUGGET) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						if (b.getBuilder().hasWood(b.costWood) && b.getBuilder().hasGold(b.costGold)) {
							b.getBuilder().removeGold(b.costGold);
							b.getBuilder().removeWood(b.costWood);

							b.levelUp();
						} else {
							b.getBuilder().getPlayer().sendMessage(ChatColor.GREEN + "You do not have enough " + ChatColor.RED + "resources.");
						}
						e.getWhoClicked().closeInventory();
						break;
					}
				}
			}else if (e.getCurrentItem().getType() == Material.FLINT) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						if (b.getBuilder().hasWood(WeaknessTower.COSTWOOD) && b.getBuilder().hasGold(WeaknessTower.COSTGOLD)) {
							b.getBuilder().removeGold(WeaknessTower.COSTGOLD);
							b.getBuilder().removeWood(WeaknessTower.COSTWOOD);
							Location loc = b.getMinLoc();
							Builder builder = b.getBuilder();
							b.remove();
							new WeaknessTower(loc, new Location(loc.getWorld(), loc.getX() + 7, loc.getY() + 13, loc.getZ() + 7), Schematic.loadSchematic(new File("schematics", "tower1.schematic")), 50, b.getBuilder(), this);

							
						} else {
							b.getBuilder().getPlayer().sendMessage(ChatColor.GREEN + "You do not have enough " + ChatColor.RED + "resources.");
						}
						e.getWhoClicked().closeInventory();
						break;
					}
				}
			}else if (e.getCurrentItem().getType() == Material.FIREBALL) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						if (b.getBuilder().hasWood(FireTower.COSTWOOD) && b.getBuilder().hasGold(FireTower.COSTGOLD)) {
							b.getBuilder().removeGold(FireTower.COSTGOLD);
							b.getBuilder().removeWood(FireTower.COSTWOOD);
							Location loc = b.getMinLoc();
							Builder builder = b.getBuilder();
							b.remove();
							new FireTower(loc, new Location(loc.getWorld(), loc.getX() + 7, loc.getY() + 13, loc.getZ() + 7), Schematic.loadSchematic(new File("schematics", "tower1.schematic")), 50, b.getBuilder(), this);
							
						} else {
							b.getBuilder().getPlayer().sendMessage(ChatColor.GREEN + "You do not have enough " + ChatColor.RED + "resources.");
						}
						e.getWhoClicked().closeInventory();
						break;
					}
				}
			}else if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						if (b.getBuilder().hasWood(ArcaneTower.COSTWOOD) && b.getBuilder().hasGold(ArcaneTower.COSTGOLD)) {
							b.getBuilder().removeGold(ArcaneTower.COSTGOLD);
							b.getBuilder().removeWood(ArcaneTower.COSTWOOD);
							Location loc = b.getMinLoc();
							Builder builder = b.getBuilder();
							b.remove();
							new ArcaneTower(loc, new Location(loc.getWorld(), loc.getX() + 7, loc.getY() + 13, loc.getZ() + 7), Schematic.loadSchematic(new File("schematics", "tower1.schematic")), 50, b.getBuilder(), this);

							
						} else {
							b.getBuilder().getPlayer().sendMessage(ChatColor.GREEN + "You do not have enough " + ChatColor.RED + "resources.");
						}
						e.getWhoClicked().closeInventory();
						break;
					}
				}
			}else if (e.getCurrentItem().getType() == Material.NETHER_STALK) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						b.remove();
						e.getWhoClicked().closeInventory();
						break;
					}
				}
			} else if (e.getCurrentItem().getType() == Material.SKULL_ITEM) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {
						if (b instanceof House) {
							House h = (House) b;
							if (b.getBuilder().hasGold(House.UNITCOST) && b.getBuilder().hasFood(House.FOODCOST)) {
								h.spawnMob();
								b.getBuilder().addFoodUsed(House.FOODCOST);
								b.getBuilder().removeGold(House.UNITCOST);
							} else {
								b.getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You do not have enough" + ChatColor.RED + " wood or food.");
							}
							e.getWhoClicked().closeInventory();
							break;
						} else if (b instanceof Citadel) {
							Citadel c = (Citadel) b;
							if (b.getBuilder().hasGold(Citadel.UNITCOST) && b.getBuilder().hasFood(Citadel.FOODCOST)) {
								c.spawnMob();
								b.getBuilder().removeGold(Citadel.UNITCOST);
								b.getBuilder().addFoodUsed(Citadel.FOODCOST);
							} else {
								b.getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You do not have enough" + ChatColor.RED + " wood or food.");
							}
							e.getWhoClicked().closeInventory();
							break;
						}
					}
				}
			} else if (e.getCurrentItem().equals(Citadel.getTierUpgrade())) {
				for (Building b : getbuildings()) {
					if (e.getInventory().equals(b.inv)) {

						if (b instanceof Citadel) {
							if (b.getBuilder().getTier() < 2) {
								if (b.getBuilder().hasGold(Citadel.TIER2COST)) {
									b.getBuilder().setTier(2);
									b.getBuilder().removeGold(Citadel.TIER2COST);
									b.getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You are now tier " + ChatColor.DARK_RED + "2");
								} else {
									b.getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You do not have enough " + ChatColor.RED + "gold " + ChatColor.DARK_GREEN + Citadel.TIER2COST + " gold is required.");
								}

							} else {
								b.getBuilder().getPlayer().sendMessage(ChatColor.DARK_GREEN + "You are already tier 2.");
							}
						}
						break;
					}
				}
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onMobControlSetTarget(PlayerInteractEvent e) {
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK)
			if (currentNPC.containsKey(e.getPlayer())) {
				Builder b = currentNPC.get(e.getPlayer()).data().get("owner");
				if (b.getPlayer().equals(e.getPlayer())) {
					NPC npc = currentNPC.get(e.getPlayer());
					if (e.getClickedBlock().getType() == Material.LOG) {
						if (npc.getEntity() instanceof Zombie) {
							Zombie z = (Zombie) npc.getEntity();
							z.getEquipment().setItemInHand(new ItemStack(Material.WOOD_AXE));
						}
						npc.data().set("incoming", true);
					} else {
						if (npc.data().has("incoming"))
							npc.data().remove("incoming");
					}
					npc.getDefaultGoalController().clear();
					npc.getNavigator().setTarget(e.getClickedBlock().getLocation());
					npc.getNavigator().setPaused(false);
					currentNPC.remove(e.getPlayer());
					e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ANVIL_LAND, 0.3f, 1);
				}
			}
	}

	@EventHandler
	public void onMobController(PlayerInteractEntityEvent e) {
		if (e.getRightClicked().hasMetadata("NPC")) {
			NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getRightClicked());
			if (npc.data().has("owner")) {
				Player p = ((Builder) npc.data().get("owner")).getPlayer();
				if (p.equals(e.getPlayer())) {
					if (!e.getPlayer().isSneaking()) {
						// right click = set target
						for (Builder b : builders) {
							if (b.getUnits().contains(npc)) {
								currentNPC.put(e.getPlayer(), npc);
								b.getPlayer().sendMessage(ChatColor.RED + "Right click " + ChatColor.DARK_GREEN + "where you want your npc to move to.");
								e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), Sound.ANVIL_LAND, 0.3f, 1);
								break;

							}
						}
					} else {
						// shift right click = destroy unit
						Builder b = npc.data().get("owner");
						if (b.equals(getBuilder(e.getPlayer())) && npc.data().has("tier")) {
							b.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Your unit has succesfully been" + ChatColor.RED + " destroyed" + ChatColor.DARK_GREEN + ".");
							switch ((int) npc.data().get("tier")) {
							case 1:
								b.removeFoodUsed(House.FOODCOST);
								break;
							case 2:
								b.removeFoodUsed(Citadel.FOODCOST);
								break;

							}

							ParticleEffects.sendToLocation(ParticleEffects.LARGE_EXPLODE, npc.getEntity().getLocation().add(0, 1, 0), 0, 0, 0, 1, 1);
							npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), Sound.ZOMBIE_DEATH, 10, 1);

							npc.destroy();
							b.getUnits().remove(npc);
						}
					}
				}
			}
		}
	}

	public void removeBuilding(Building b) {
		buildings.remove(b);
	}

	public List<Builder> getBuilders() {
		return builders;
	}
	public void removeItem(Vampire vamp, VampireItem i){
		
	}
}
