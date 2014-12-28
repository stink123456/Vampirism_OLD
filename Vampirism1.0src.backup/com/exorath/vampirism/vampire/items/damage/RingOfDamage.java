package com.exorath.vampirism.vampire.items.damage;

import com.exorath.vampirism.Building;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;
import com.exorath.vampirism.vampire.items.VampireItem;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class RingOfDamage extends VampireItem{
	
	float goldPerMin = 1;
	int taskId;
	public RingOfDamage(int cost, Vampire vampire) {
		super(cost, vampire, Items.DAMAGE);
		taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Bukkit.getPluginManager().getPlugin("Vampirism"), new Runnable(){
			@Override
			public void run(){
				getVampire().getPlayer().getWorld().playSound(getVampire().getPlayer().getLocation(), Sound.FIZZ, 5, 1);
				
				damageNearby();
				
			}
		}, 20, 20*60);
		
	}
	private void damageNearby(){
		Player p = getVampire().getPlayer();
		for(Building b: getVampire().getBuildingManager().getbuildings()){
			if(b.getMiddleBottom().distance(p.getLocation())<= 8){
				b.damage(getVampire().getDamage()/2 + 1);
				ParticleEffects.sendToLocation(ParticleEffects.FIRE, b.getMiddle(), (float) (b.getMaxLoc().getX() - b.getMinLoc().getX()) / 2, (float) (b.getMaxLoc().getY() - b.getMinLoc().getY()) / 2, (float) (b.getMaxLoc().getZ() - b.getMinLoc().getZ()) / 2, 0.1f, 100);
				
			}
		}
	}
	@Override
	public void removeItem(){
		Bukkit.getScheduler().cancelTask(taskId);
	}
}
