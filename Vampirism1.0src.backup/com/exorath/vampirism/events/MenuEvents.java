package com.exorath.vampirism.events;

import com.exorath.vampirism.libraries.CustomItems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class MenuEvents implements Listener{
	//ooh :(
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		clearToolbar(e.getPlayer());
		defaultLayout(e.getPlayer());
	}

	@SuppressWarnings({"deprecation"})
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if(e.getPlayer().getItemInHand() != null && e.getPlayer().getItemInHand().hasItemMeta()){
		if (e.getPlayer().getItemInHand().getItemMeta().equals((CustomItems.TOWERS.getItemMeta()))) {
			clearToolbar(e.getPlayer());
			e.setCancelled(true);
			e.getPlayer().getInventory().setItem(4,CustomItems.TOWERPEARLS);
			
			e.getPlayer().getInventory().setItem(8, CustomItems.BACK);
			e.getPlayer().updateInventory();
		} else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.WALLS.getItemMeta())) {
			clearToolbar(e.getPlayer());
			e.setCancelled(true);
			e.getPlayer().getInventory().setItem(3, CustomItems.WALLCARNELIAN);
			e.getPlayer().getInventory().setItem(5, CustomItems.HEALWALL);
			e.getPlayer().getInventory().setItem(8, CustomItems.BACK);
			e.getPlayer().updateInventory();
		} else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.UTILITY.getItemMeta())) {
			e.setCancelled(true);
			clearToolbar(e.getPlayer());
			e.getPlayer().getInventory().setItem(2, CustomItems.HOUSE);
			e.getPlayer().getInventory().setItem(4, CustomItems.GOLDMINE);
			e.getPlayer().getInventory().setItem(6, CustomItems.CITADEL);
			e.getPlayer().getInventory().setItem(8, CustomItems.BACK);
			e.getPlayer().updateInventory();
		} else if (e.getPlayer().getItemInHand().getItemMeta().equals(CustomItems.BACK.getItemMeta())) {
			e.setCancelled(true);
			defaultLayout(e.getPlayer());
		}
	}
	}

	@SuppressWarnings({"deprecation"})
	public static void defaultLayout(Player p) {
		clearToolbar(p);
		p.getInventory().setItem(2, CustomItems.TOWERS);
		p.getInventory().setItem(4, CustomItems.WALLS);
		p.getInventory().setItem(6, CustomItems.UTILITY);
		p.updateInventory();
	}

	public static void clearToolbar(Player p) {
		for (byte n = 0; n < 9; n++) {
			p.getInventory().setItem(n, CustomItems.AIR);
		}
	}
}

