package me.shedaniel.meritpack;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.ArrayList;
import java.util.List;

public class MeritPackGetEntryEvent extends Event {
	
	private Player player;
	private List<MeritEntry> entries;
	
	private static final HandlerList handlers = new HandlerList();
	
	public MeritPackGetEntryEvent(Player player) {
		this.player = player;
		this.entries = new ArrayList<>();
	}
	
	public List<MeritEntry> getEntries() {
		return entries;
	}
	
	public void setEntries(List<MeritEntry> entries) {
		this.entries = entries;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
}
