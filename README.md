# MeritPack
This is a simple merit pack coded by Danielshe. <br>
This plugin cannot be used itself, it has to be used as an api.

### API Usage
Event called even player clicked "Start" on merit pack gui:
```java
@EventHandler
public void onPlayerOpenMeritPack(PlayerOpenMeritPackEvent e) {
	
}
```
You can cancel the event like this:
```java
@EventHandler
public void onPlayerOpenMeritPack(PlayerOpenMeritPackEvent e) {
  if (randomFunction(e.getPlayer())) {
    e.setCancelled(true);
    e.getPlayer().closeInventory();
    //You should add some messages like "Not enough coins" or idk to notify the player
  }
}
```
Next, you must specify the entries to the plugin: (This is just an example)<br>
*The Item should be named to let the player know what is it*
```java
@EventHandler
public void getMeritPackItems(MeritPackGetEntryEvent e) {
	e.setEntries(Arrays.asList(new MeritEntry[]{
			new MeritEntry(new ItemStack(Material.GOLD_NUGGET), 0.6f, new Runnable() {
				@Override
				public void run() {
					//Random
				}
			})
	}));
}
```
The entry should be like this:
```java
new MeritEntry(stack, chance, runnable)
```
stack = The ItemStack displayed when the gui is rolling<br>
chance = The chance the item is selected (The isn't a need to get all equals to 1.0, it is just a standard) \[0.0 - 1.0]<br>
runnable = The thing that runs after the item is selected after the roll
