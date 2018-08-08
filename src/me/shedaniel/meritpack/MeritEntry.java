package me.shedaniel.meritpack;

import org.bukkit.inventory.ItemStack;

public class MeritEntry {
	
	protected ItemStack itemStack;
	protected Runnable runnable;
	protected double chance;
	
	public MeritEntry(ItemStack display, double chance, Runnable runnable) {
		this.itemStack = display;
		this.chance = chance;
		this.runnable = runnable;
	}
	
	public double getChance() {
		return chance;
	}
	
	public Runnable getRunnable() {
		return runnable;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
}
