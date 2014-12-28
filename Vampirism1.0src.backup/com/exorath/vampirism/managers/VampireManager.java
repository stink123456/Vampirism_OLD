package com.exorath.vampirism.managers;

import com.exorath.vampirism.Building;
import com.exorath.vampirism.Vampirism;
import com.exorath.vampirism.libraries.CustomItems;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.damage.BurstGem;
import com.exorath.vampirism.vampire.items.defense.CloakOfImmunity;
import com.exorath.vampirism.vampire.items.income.ClawsOfFortune;
import com.exorath.vampirism.vampire.items.income.UrnOfDracula;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class VampireManager implements Listener {
	Vampirism plugin;
	BuildingManager bm;
	Inventory attributesInv;
	private Inventory itemShopInv;
	private Inventory incomeCategoryInv;
	private static final String NOGOLD = ChatColor.DARK_GREEN + "You do not have enough " + ChatColor.RED + "gold" + ChatColor.DARK_GREEN + ".";

	public VampireManager(Vampirism plugin, BuildingManager bm) {
		this.plugin = plugin;
		this.bm = bm;
		Bukkit.getPluginManager().registerEvents(this, plugin);
		setupInvAtr();
		setupInvItemCategories();

	}

	private void setupInvAtr() {
		attributesInv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Attribute shop");
		attributesInv.setItem(6, CustomItems.AGILITYITEM);
		attributesInv.setItem(7, CustomItems.INTELLIGENCEITEM);
		attributesInv.setItem(8, CustomItems.STRENGTHITEM);
	}

	private void setupInvItemCategories() {
		itemShopInv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Item shop - categories");
		itemShopInv.setItem(2, CustomItems.GOLDINCOMECATEGORY);
		itemShopInv.setItem(3, CustomItems.DAMAGECATEGORY);
		itemShopInv.setItem(4, CustomItems.UTILITYCATEGORY);
		itemShopInv.setItem(5, CustomItems.ABILITYCATEGORY);
		itemShopInv.setItem(6, CustomItems.DEFENSECATEGORY);

		setupIncomeCategory();

	}

	private void setupIncomeCategory() {
		incomeCategoryInv = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Category: Gold income");
		incomeCategoryInv.setItem(3, CustomItems.CLAWSOFFORTUNE);
		incomeCategoryInv.setItem(5, CustomItems.URNOFDRACULA);
		incomeCategoryInv.setItem(8, CustomItems.CATEGORYRETURN);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
			e.setCancelled(true);
			if (bm.isVampire(e.getPlayer()) && e.getClickedBlock().hasMetadata("building")) {
				Vampire v = bm.getVampire(e.getPlayer());
				if (v.getPlayer().getInventory().getItemInHand().getItemMeta().equals(CustomItems.VAMPIREWEAPON.getItemMeta())) {
					// vampire attacks building
					Location loc = e.getClickedBlock().getLocation();
					Building b = (Building) e.getClickedBlock().getMetadata("building").get(0).value();
					b.damage(v.getfDamage());

					if (v.hasClawsOfFortune()) {
						v.getPlayer().sendMessage("you got those claws");
						// add gold randomly when a player attacks a building
						if (Math.random() < 0.5) {
							v.addGold(1);

							// particleEffects
							ParticleEffects.sendToLocation(ParticleEffects.GOLDCRACK, loc.add(0.5, 0.5, 0.5), 0.5f, 0.5f, 0.5f, 1, 200);
							loc.getWorld().playSound(loc, Sound.LEVEL_UP, 1, 0.5f);
						} else {
							ParticleEffects.sendToLocation(ParticleEffects.BLOODCRACK, loc.add(0.5, 0.5, 0.5), 0.5f, 0.5f, 0.5f, 1, 30);
							loc.getWorld().playSound(loc, Sound.ZOMBIE_WOODBREAK, 1, 0.5f);
						}
					} else {
						v.getPlayer().sendMessage("what de shit");
						ParticleEffects.sendToLocation(ParticleEffects.BLOODCRACK, loc.add(0.5, 0.5, 0.5), 0.5f, 0.5f, 0.5f, 1, 30);
						loc.getWorld().playSound(loc, Sound.ZOMBIE_WOODBREAK, 1, 0.5f);
					}

				} else if (v.getPlayer().getInventory().getItemInHand().getItemMeta().equals(BurstGem.ITEMSTACK.getItemMeta())) {
					if (v.getItemTypes().contains(Items.DAMAGE)) {
						BurstGem item = (BurstGem) v.getItem(Items.DAMAGE);
						if (v.getPlayer().getTargetBlock(null, 7).hasMetadata("building")) {
							Building b = (Building) v.getPlayer().getTargetBlock(null, 7).getMetadata("building").get(0).value();
							item.damageBuilding(b);
						}
					}
				} else if (v.getPlayer().getInventory().getItemInHand().getItemMeta().equals(CloakOfImmunity.ITEMSTACK.getItemMeta())) {
					if (v.getItemTypes().contains(Items.DEFENSE)) {
						CloakOfImmunity item = (CloakOfImmunity) v.getItem(Items.DEFENSE);

					}
				}

			}
		} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().hasMetadata("attribute")) {
				e.setCancelled(true);
				if (bm.isVampire(e.getPlayer())) {
					e.getPlayer().openInventory(attributesInv);
				}
			} else if (e.getClickedBlock().hasMetadata("shop")) {
				e.setCancelled(true);
				if (bm.isVampire(e.getPlayer())) {
					e.getPlayer().openInventory(itemShopInv);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if (bm.isVampire((Player) e.getWhoClicked()))
			e.setCancelled(true);

		if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && bm.isVampire((Player) e.getWhoClicked())) {
			Vampire v = bm.getVampire((Player) e.getWhoClicked());
			if (e.getCurrentItem().getItemMeta().equals(CustomItems.STRENGTHITEM.getItemMeta())) {

				if (v.hasGold(100)) {
					v.setHealth(v.getHealth() + 150);
					v.setStrength(v.getStrength() + 10);
				} else {
					v.getPlayer().sendMessage(NOGOLD);
					v.getPlayer().closeInventory();
				}
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.AGILITYITEM.getItemMeta())) {

				if (v.hasGold(100)) {
					v.setAgility(v.getAgility() + 10);
				} else {
					v.getPlayer().sendMessage(NOGOLD);
					v.getPlayer().closeInventory();
				}
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.INTELLIGENCEITEM.getItemMeta())) {

				if (v.hasGold(100)) {
					v.setMana(v.getMana() + 150);
					v.setIntelligence(v.getIntelligence() + 10);
				} else {
					v.getPlayer().sendMessage(NOGOLD);
					v.getPlayer().closeInventory();
				}
				// from here on only main shop items
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.DAMAGECATEGORY.getItemMeta())) {
				// TODO: Open inventory
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.ABILITYCATEGORY.getItemMeta())) {
				// TODO: Open inventory
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.DEFENSECATEGORY.getItemMeta())) {
				// TODO: Open inventory
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.GOLDINCOMECATEGORY.getItemMeta())) {
				e.getWhoClicked().openInventory(incomeCategoryInv);
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.UTILITYCATEGORY.getItemMeta())) {
				// TODO: Open inventory
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.CATEGORYRETURN.getItemMeta())) {
				e.getWhoClicked().openInventory(itemShopInv);

				// from here on item right clicks
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.CLAWSOFFORTUNE.getItemMeta())) {
				if (v.getItemTypes().contains(Items.GOLDINCOME)) {
					v.removeItem(Items.GOLDINCOME);
				}
				v.addItem(new ClawsOfFortune(v));
			} else if (e.getCurrentItem().getItemMeta().equals(CustomItems.URNOFDRACULA.getItemMeta())) {
				v.addItem(new UrnOfDracula(v));
			}
			if (e.getInventory().getHolder().equals(v.getPlayer().getInventory().getHolder())) {
				if (v.getItemsSlotsBySlot().containsKey(e.getSlot())){
					v.getPlayer().sendMessage(ChatColor.DARK_GREEN + "Item removed. You earned 50% back. "  +ChatColor.RED + v.getItemsSlotsBySlot().get(e.getSlot()).getCost()/2 + " gold"+ ChatColor.DARK_GREEN +" has been added to your account.");
					v.addGold(v.getItemsSlotsBySlot().get(e.getSlot()).getCost()/2);
					v.removeItem(v.getItemsSlotsBySlot().get(e.getSlot()));


				}
			}
		}
	}

	public Inventory getItemShopInventory() {
		return itemShopInv;
	}

}
