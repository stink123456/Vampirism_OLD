package com.exorath.vampirism.vampire.items;

import com.exorath.vampirism.vampire.Vampire;

import org.bukkit.inventory.ItemStack;

public class VampireItem {

	private ItemStack itemstack;
	private int cost;
	private Vampire vampire;
	private Items itemtype;
	
	public VampireItem(int cost, Vampire vampire,Items itemtype) {
		this.cost = cost;
		this.vampire = vampire;
		this.itemtype = itemtype;
		this.itemtype = itemtype;
	}
	
	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public Vampire getVampire() {
		return vampire;
	}

	public ItemStack getItemStack() {
		return itemstack;
	}

	public void setItemStack(ItemStack itemstack) {
		this.itemstack = itemstack;
	}

	public Items getItemtype() {
		return itemtype;
	}

	public void setItemtype(Items itemtype) {
		this.itemtype = itemtype;
	}
	public void removeItem(){
		//this is an overrideable method
	}
}
