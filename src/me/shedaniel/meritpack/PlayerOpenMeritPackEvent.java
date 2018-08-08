package me.shedaniel.meritpack;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerOpenMeritPackEvent extends Event implements Cancellable {
	
	private Player player;
	private boolean isCancelled;
	
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerOpenMeritPackEvent(Player player) {
		this.player = player;
		this.isCancelled = false;
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
	
	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	@Override
	public void setCancelled(boolean arg0) {
		this.isCancelled = arg0;
	}

}
