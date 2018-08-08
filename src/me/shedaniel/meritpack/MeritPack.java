package me.shedaniel.meritpack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MeritPack extends JavaPlugin implements Listener {
	
	private ExecutorService executorService;
	private static MeritPack plugin;
	private Map<Player, PlayerDoStuff> runnables;
	
	@Override
	public void onEnable() {
		this.executorService = Executors.newCachedThreadPool();
		this.plugin = this;
		this.runnables = new HashMap<>();
		
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@Override
	public void onDisable() {
		executorService.shutdown();
	}
	
	public ExecutorService getExecutorService() {
		return executorService;
	}
	
	public static MeritPack getPlugin() {
		return plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.ENDER_CHEST) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			event.setCancelled(true);
			Player p = event.getPlayer();
			if (!getPlugin().runnables.containsKey(p)) {
				PlayerDoStuff runnable = new PlayerDoStuff(p);
				getPlugin().runnables.put(p, runnable);
				getPlugin().executorService.submit(runnable);
			} else {
				PlayerDoStuff runnable = getPlugin().runnables.get(p);
				runnable.openInventory();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerClickInventory(InventoryClickEvent event) {
		if (event.getInventory().getTitle().equalsIgnoreCase("Merit Pack")) {
			event.setCancelled(true);
			Player p = (Player) event.getWhoClicked();
			if (getPlugin().runnables.containsKey(p)) {
				PlayerDoStuff runnable = getPlugin().runnables.get(p);
				if (runnable.getStatus().equals(PlayerDoStuff.InventoryStatus.WAITING)) {
					if (event.getSlot() == 12) {
						PlayerOpenMeritPackEvent e = new PlayerOpenMeritPackEvent(p);
						Bukkit.getPluginManager().callEvent(e);
						if (!e.isCancelled()) {
							runnable.setStatus(PlayerDoStuff.InventoryStatus.GET);
							runnable.start();
							runnable.openInventory();
						}
					} else if (event.getSlot() == 14) {
						runnable.setStatus(PlayerDoStuff.InventoryStatus.WAITING);
						p.closeInventory();
					}
				}
			}
		}
	}
	
	public List<MeritEntry> getEntries(Player player) {
		MeritPackGetEntryEvent e = new MeritPackGetEntryEvent(player);
		Bukkit.getPluginManager().callEvent(e);
		return e.getEntries();
	}
	
}
