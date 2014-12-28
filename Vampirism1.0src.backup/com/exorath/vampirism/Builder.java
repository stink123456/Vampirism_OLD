package com.exorath.vampirism;

import java.util.ArrayList;
import java.util.List;

import com.exorath.vampirism.managers.DisplayManager;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.entity.Player;

public class Builder {
	// the builder object, currently instantiated in Vampirism with ingame command /vampirism set b
	Player player;
	int wood = 50;
	int gold = 2;
	int foodUsed = 0;
	int food = 10;
	DisplayManager dm;
	int tier = 1;
	List<NPC> units = new ArrayList<NPC>();
	public boolean hasWeaknessTower = false;
	public Builder(Player player, DisplayManager dm) {
		this.dm = dm;
		this.player = player;
		dm.addDisplay(this);

	}

	public boolean hasWood(int amount) {
		if (wood >= amount) {
			return true;
		} else {
			return false;
		}
	}
	public boolean hasGold(int amount) {
		if (gold >= amount) {
			return true;
		} else {
			return false;
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void removeWood(int amount) {
		wood = wood - amount;
		dm.updateWood(this);
	}

	public void removeGold(int amount) {
		gold = gold - amount;
		dm.updateGold(this);
	}
	public void addWood(int amount) {
		wood = wood + amount;
		dm.updateWood(this);
	}

	public void addGold(int amount) {
		gold = gold + amount;
		dm.updateGold(this);
	}
	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public int getWood() {
		return wood;

	}

	public int getGold() {
		return gold;
	}
	public int getFood() {
		return food;
	}


	public int getFoodUsed() {
		return foodUsed;
	}

	public void removeFoodUsed(int foodUsed) {
		this.foodUsed -= foodUsed;
		dm.updateFood(this);
	}
	public void addFoodUsed(int foodUsed) {
		this.foodUsed += foodUsed;
		dm.updateFood(this);
	}	
	public void removeFood(int food) {
		this.food -= food;
		dm.updateFood(this);
	}
	public void addFood(int food) {
		this.food += food;
		dm.updateFood(this);
	}
	public boolean hasFood(int food){
		if(this.foodUsed + food <= this.food)
		return true;
		else
		return false;
	}
	public List<NPC> getUnits(){
		return units;
	}

}
