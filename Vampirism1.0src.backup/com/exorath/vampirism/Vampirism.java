package com.exorath.vampirism;

import com.exorath.vampirism.events.BattleEvents;
import com.exorath.vampirism.events.BuildEvents;
import com.exorath.vampirism.events.MenuEvents;
import com.exorath.vampirism.events.PlayerListener;
import com.exorath.vampirism.libraries.Particles.ParticleEffects;
import com.exorath.vampirism.managers.DisplayManager;
import com.exorath.vampirism.managers.IncomeManager;
import com.exorath.vampirism.vampire.Vampire;
import com.exorath.vampirism.vampire.items.Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Vampirism extends JavaPlugin implements Listener {
	BuildEvents be;
	DisplayManager dm;
	IncomeManager im;

	private static Plugin instance;

	/**
	 * @return the Bukkit plugin instance
	 */
	public static Plugin getInstance() {
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;

		dm = new DisplayManager();
		be = new BuildEvents(this);
		im = new IncomeManager(be.bm);
		Bukkit.getPluginManager().registerEvents(new MenuEvents(), this);
		Bukkit.getPluginManager().registerEvents(be, this);
		Bukkit.getPluginManager().registerEvents(new BattleEvents(), this);
		Bukkit.getPluginManager().registerEvents(this, this);

		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	@Override
	public void onDisable() {
		for (Building b : be.bm.getbuildings()) {
			b.remove();
		}

		instance = null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("vampirism")) {
			Player p = (Player) sender;
			ParticleEffects.sendToPlayer(ParticleEffects.FIRE, p, p.getLocation().add(0, 2.1, 0), 0, 0, 0, 0.04f, 10);
			sender.sendMessage("nice command");
			if (args.length == 2) {
				if (args[0].equals("set")) {
					if (args[1].equals("v")) {
						if (be.bm.isBuilder((Player) sender)) {
							be.bm.removeBuilder(be.bm.getBuilder((Player) sender));
						}
						sender.sendMessage(ChatColor.DARK_GREEN + "You are now a " + ChatColor.RED + "Vampire.");
						be.bm.addVampire(new Vampire((Player) sender, dm, be.bm));
					} else if (args[1].equals("b")) {
						if (be.bm.isVampire((Player) sender)) {
							be.bm.removeVampire(be.bm.getVampire((Player) sender));
						}
						sender.sendMessage(ChatColor.DARK_GREEN + "You are now a " + ChatColor.RED + "Builder.");
						be.bm.addBuilder(new Builder((Player) sender, dm));
					}
				}
			} else if (args.length == 1) {
				String a = args[0];
				if (a.equals("atr")) {
					p.getLocation().clone().add(0, -1, 0).getBlock().setMetadata("attribute", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("Vampirism"), null));
					p.sendMessage("this block is now an attribute shop!");
				}else if (a.equals("shop")) {
					p.getLocation().clone().add(0, -1, 0).getBlock().setMetadata("shop", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("Vampirism"), null));
					p.sendMessage("this block is now an item shop!");
				}else if(a.equals("remove")){
					Vampire v = be.bm.getVampire((Player)sender);
					v.removeItem(Items.VAMPIREITEM);
				}
			}
		} else {
			Bukkit.broadcastMessage("dafaq");
		}
		return true;
	}

	@EventHandler
	public void onItemDrop(PlayerDropItemEvent e) {
		if (e.getPlayer().isOp() && e.getItemDrop().getItemStack().getType() == Material.GOLD_INGOT) {
			if (be.bm.isBuilder(e.getPlayer())) {
				be.bm.getBuilder(e.getPlayer()).addGold(20);
				dm.updateGold(be.bm.getBuilder(e.getPlayer()));
			} else if (be.bm.isVampire(e.getPlayer())) {
				be.bm.getVampire(e.getPlayer()).addGold(20);
				dm.updateGold(be.bm.getBuilder(e.getPlayer()));
			}
		} else if (e.getPlayer().isOp() && e.getItemDrop().getItemStack().getType() == Material.LOG) {
			if (be.bm.isBuilder(e.getPlayer())) {
				be.bm.getBuilder(e.getPlayer()).addWood(50);
				dm.updateWood(be.bm.getBuilder(e.getPlayer()));
			}
		}
		e.setCancelled(true);
	}
}
