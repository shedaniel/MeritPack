package me.shedaniel.meritpack;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PlayerDoStuff implements Runnable {
	
	enum InventoryStatus {
		WAITING, GET, DONE;
	}
	
	protected Player player;
	protected InventoryStatus status;
	protected List<MeritEntry> entries, list;
	protected int num, max, wait;
	protected Inventory doing;
	protected ItemStack rainbowPane[] = new ItemStack[]{
			getItem(Material.STAINED_GLASS_PANE, 1, 0, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 1, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 2, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 3, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 4, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 5, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 6, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 7, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 8, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 9, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 10, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 11, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 12, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 13, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 14, " "),
			getItem(Material.STAINED_GLASS_PANE, 1, 15, " "),
	};
	
	public PlayerDoStuff(Player p) {
		this.player = p;
		this.status = InventoryStatus.WAITING;
		this.list = new ArrayList<>();
	}
	
	public void setStatus(InventoryStatus status) {
		this.status = status;
	}
	
	public InventoryStatus getStatus() {
		return status;
	}
	
	@Override
	public void run() {
		openInventory();
		Random random = new Random();
		while (true) {
			try {
				Thread.sleep(1l);
				if (status.equals(InventoryStatus.GET) && doing != null) {
					for (int i = 0; i <= 8; i++) {
						if (i == 4) {
							doing.setItem(i, getItem(Material.GLASS, 1, 0, " "));
							doing.setItem(i + 18, getItem(Material.GLASS, 1, 0, " "));
						} else {
							doing.setItem(i, rainbowPane[random.nextInt(rainbowPane.length)]);
							doing.setItem(i + 18, rainbowPane[random.nextInt(rainbowPane.length)]);
						}
					}
					wait++;
					int speed = 0;
					if (wait < max * 0.1)
						speed = 1;
					else if (wait < max * 0.2)
						speed = 2;
					else if (wait < max * 0.3)
						speed = 3;
					else if (wait < max * 0.4)
						speed = 4;
					else if (wait < max * 0.5)
						speed = 5;
					else if (wait < max * 0.6)
						speed = 6;
					else if (wait < max * 0.7)
						speed = 7;
					else if (wait < max * 0.8)
						speed = 8;
					else speed = 12;
					if (wait % speed == 0 && wait < max) {
						num++;
						for (int i = 9; i <= 17; i++) {
							int n = i - 9 + (num % list.size());
							if (n >= list.size())
								n = n - list.size();
							else if (n < 0)
								n = n + list.size();
							ItemStack stack = list.get(n).getItemStack();
							doing.setItem(9 - (i - 9) + 8, stack);
						}
					}
					if (wait % speed == 0 && wait < max && player.getOpenInventory().getTopInventory() != null && player.getOpenInventory().getTopInventory().getTitle().equals(doing.getTitle())) {
						player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 16);
					}
					if (wait > max + 20) {
						for (MeritEntry entry : entries) {
							if (entry.getItemStack().equals(doing.getItem(13))) {
								entry.getRunnable().run();
								player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
								doing = null;
								setStatus(InventoryStatus.WAITING);
								player.closeInventory();
								break;
							}
						}
					}
					player.updateInventory();
					Thread.sleep(50l);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addEntry(MeritEntry entry) {
		entries.add(entry);
	}
	
	public void start() {
		if (status.equals(InventoryStatus.GET)) {
			entries = MeritPack.getPlugin().getEntries(player);
			list = new ArrayList<>();
			Random random = new Random();
			for (MeritEntry entry : entries) {
				for (int i = 0; i < entry.getChance() * 100; i++) {
					list.add(entry);
				}
			}
			for (int i = 0; i < list.size(); i++) {
				int a = random.nextInt(list.size()), b = random.nextInt(list.size());
				MeritEntry temp = list.get(a);
				list.set(a, list.get(b));
				list.set(b, temp);
			}
			num = 0;
			wait = 0;
			max = random.nextInt(list.size()) + list.size() * 5;
		}
	}
	
	public void openInventory() {
		if (status.equals(InventoryStatus.WAITING)) {
			Inventory inventory = Bukkit.createInventory(null, 27, "Merit Pack");
			inventory.setItem(12, getItem(Material.STAINED_CLAY, 1, 13, "§aStart"));
			inventory.setItem(14, getItem(Material.STAINED_CLAY, 1, 14, "§cCancel"));
			player.openInventory(inventory);
		} else if (status.equals(InventoryStatus.GET)) {
			doing = Bukkit.createInventory(null, 27, "Merit Pack");
			player.openInventory(doing);
		}
	}
	
	private ItemStack getItem(Material material, int count, int damage, String name, String... lores) {
		ItemStack stack = new ItemStack(material, count, (short) damage);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lores));
		stack.setItemMeta(meta);
		return stack;
	}
	
}
