package com.exorath.vampirism.managers;

import com.exorath.vampirism.Builder;
import com.exorath.vampirism.Building;
import com.exorath.vampirism.buildings.utility.GoldMineV2;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.Sound;

public class IncomeManager {
	final static int INTERVAL = 20;
	final static int INTERVALGOLD = 200;
	BuildingManager bm;

	public IncomeManager(BuildingManager bm) {
		this.bm = bm;
		income();
		incomeGold();
	}

	// iterate trough all builders their units and give income according to the
	// income data
	@SuppressWarnings({"deprecation"})
	public void income() {
		for (Builder b : bm.getBuilders()) {
			for (NPC npc : b.getUnits()) {
				if ((boolean) npc.data().has("incoming") && npc.data().has("income") && !npc.getNavigator().isNavigating()) {
					b.addWood((int) npc.data().get("income"));
					ParticleEffects.sendToLocation(ParticleEffects.WOODCRACK, npc.getEntity().getLocation().add(0, 1.5, 0).add(npc.getBukkitEntity().getEyeLocation().toVector().normalize()), 0.5f, 0.5f, 0.5f, 1, 20);
					npc.getEntity().getWorld().playSound(npc.getEntity().getLocation(), Sound.DIG_WOOD, 10, 1);
				}
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(bm.getPlugin(), new Runnable() {
			@Override
			public void run() {
				income();
			}
		}, INTERVAL);
	}

	public void incomeGold() {

		for (Building b : bm.getbuildings()) {
			if (b.isAlive) {
				if (b instanceof GoldMineV2) {
					b.getBuilder().addGold(1);
					ParticleEffects.sendToLocation(ParticleEffects.SPLASH, b.getMinLoc().add(b.getMaxLoc()).multiply(0.5), 2, 1, 3, 1, 30);
					b.getMinLoc().getWorld().playSound(b.getMinLoc(), Sound.DIG_STONE, 10, 1);

				}
			}
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(bm.getPlugin(), new Runnable() {
			@Override
			public void run() {
				incomeGold();
			}
		}, INTERVALGOLD);
	}
}
