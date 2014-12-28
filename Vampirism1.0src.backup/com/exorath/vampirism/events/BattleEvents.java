package com.exorath.vampirism.events;

import com.exorath.vampirism.managers.BuildingManager;
import com.exorath.vampirism.vampire.Vampire;

import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class BattleEvents implements Listener {
	BuildingManager bm;
	private static final int WEAKNESS_TICKS = 20;

	@EventHandler
	public void arrowDamageEvent(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Arrow) {
			if (e.getEntity() instanceof Player) {
				Player p = (Player) e.getEntity();
				if (bm.isVampire(p)) {
					Vampire v = bm.getVampire(p);
					if (e.getDamager().hasMetadata("arcane")) {
						
						v.damageTrue(e.getDamager().getMetadata("arcane").get(0).asInt());
					} else if (e.getDamager().hasMetadata("damage")){
						
						v.damage(e.getDamager().getMetadata("damage").get(0).asInt());
					}else if (e.getDamager().hasMetadata("weakness")) {
						
					    v.setDamageReduced(e.getDamager().getMetadata("weakness").get(0).asInt());
					}
					e.getDamager().remove();
					
					e.setCancelled(true);
				}
			}
		}
	}
}
