package com.exorath.vampirism.managers;

import java.util.HashMap;

import me.confuser.barapi.BarAPI;
import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.vampire.Vampire;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class DisplayManager {
	ScoreboardManager manager;
	Scoreboard board;
	Objective objective;
	HashMap<Builder, Score> playerWood = new HashMap<Builder, Score>();
	HashMap<Builder, Score> playerGold = new HashMap<Builder, Score>();
	HashMap<Builder, Score> playerUsedFood = new HashMap<Builder, Score>();
	HashMap<Builder, Score> playerFood = new HashMap<Builder, Score>();

	HashMap<Vampire, Score> vampireGold = new HashMap<Vampire, Score>();

	HashMap<Vampire, Score> vampireStrength = new HashMap<Vampire, Score>();
	HashMap<Vampire, Score> vampireAgility = new HashMap<Vampire, Score>();
	HashMap<Vampire, Score> vampireIntelligence = new HashMap<Vampire, Score>();

	HashMap<Vampire, Score> vampireHealth = new HashMap<Vampire, Score>();
	HashMap<Vampire, Score> vampireMana = new HashMap<Vampire, Score>();
	HashMap<Vampire, Score> vampireDamage = new HashMap<Vampire, Score>();
	public DisplayManager() {
		manager = Bukkit.getScoreboardManager();
		updateBuildingDisplay();

	}

	@SuppressWarnings({"deprecation"})
	public void addDisplay(Builder b) {
		board = manager.getNewScoreboard();
		objective = board.registerNewObjective(ChatColor.DARK_GREEN + "Builder stats", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Team team = board.registerNewTeam(b.getPlayer().getName());
		team.addPlayer(b.getPlayer());
		team.setPrefix(ChatColor.DARK_GREEN + "Builder: " + ChatColor.RED);
		team.setDisplayName(ChatColor.DARK_GREEN + "Builder stats");
		Score wood = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Wood:"));
		wood.setScore(b.getWood());
		Score gold = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Gold:"));
		gold.setScore(b.getGold());
		Score food = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Used food:"));
		food.setScore(b.getFoodUsed());
		Score maxFood = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Food:"));
		maxFood.setScore(b.getFood());

		b.getPlayer().setScoreboard(board);

		playerWood.put(b, wood);
		playerGold.put(b, gold);
		playerUsedFood.put(b, food);
		playerFood.put(b, maxFood);
	}
	@SuppressWarnings({"deprecation"})
	public void addDisplayVampire(Vampire v) {
		board = manager.getNewScoreboard();
		objective = board.registerNewObjective(ChatColor.DARK_GREEN + "Vampire stats", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		Team team = board.registerNewTeam(v.getPlayer().getName());
		team.addPlayer(v.getPlayer());
		team.setPrefix(ChatColor.DARK_GREEN + "Vampire: " + ChatColor.RED);
		team.setDisplayName(ChatColor.DARK_GREEN + "Vampire stats");
		Score gold = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GRAY + "Gold:"));
		gold.setScore(v.getGold());
		Score strength = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN + "Strength:"));
		strength.setScore(v.getStrength());
		Score agility = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN + "Agility:"));
		agility.setScore(v.getAgility());
		Score intelligence = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN + "Intelligence:"));
		intelligence.setScore(v.getIntelligence());
		Score health = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Health:"));
		health.setScore((int)v.getHealth());
		Score mana = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.YELLOW + "Mana:"));
		mana.setScore(v.getMana());
		Score damage = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "Damage:"));
		damage.setScore(v.getfDamage());

		v.getPlayer().setScoreboard(board);
		
		vampireGold.put(v, gold);
		
		vampireStrength.put(v, strength);
		vampireAgility.put(v, agility);
		vampireIntelligence.put(v, intelligence);
		
		vampireHealth.put(v, health);
		vampireMana.put(v, mana);
		vampireDamage.put(v, damage);
	}

	public void updateVampireGold(Vampire v) {
		vampireGold.get(v).setScore(v.getGold());
	}

	public void updateVampireAttributes(Vampire v) {
		vampireStrength.get(v).setScore(v.getStrength());
		vampireAgility.get(v).setScore(v.getAgility());
		vampireIntelligence.get(v).setScore(v.getIntelligence());
		
		vampireHealth.get(v).setScore(v.getHealth());
		vampireMana.get(v).setScore(v.getMana());
		vampireDamage.get(v).setScore(v.getfDamage());
	}

	public void updateFood(Builder b) {
		playerUsedFood.get(b).setScore(b.getFoodUsed());
		playerFood.get(b).setScore(b.getFood());
	}

	public void updateWood(Builder b) {
		playerWood.get(b).setScore(b.getWood());
	}

	public void updateGold(Builder b) {
		playerGold.get(b).setScore(b.getGold());
	}

	@SuppressWarnings({"deprecation"})
	public void updateBuildingDisplay() {
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getTargetBlock(null, 10).hasMetadata("building")) {
						Building b = (Building) p.getTargetBlock(null, 10).getMetadata("building").get(0).value();
						BarAPI.setMessage(p, ChatColor.DARK_GREEN + b.getName() + ChatColor.RED + " << " + b.getHealth() + "/" + b.getMaxHealth() + " >> " + ChatColor.DARK_GREEN + b.getName(), (float) b.getHealth() / (float) b.getMaxHealth() * 100f);
					} else {
						BarAPI.setMessage(ChatColor.DARK_GREEN + "Look at a building to see it's properties");
					}
				}
			}
		}, 0, 5);

	}
}
