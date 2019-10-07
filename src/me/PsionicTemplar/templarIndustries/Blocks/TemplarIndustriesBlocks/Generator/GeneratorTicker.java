package me.PsionicTemplar.templarIndustries.Blocks.TemplarIndustriesBlocks.Generator;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitScheduler;

import me.PsionicTemplar.templarIndustries.Start;


public class GeneratorTicker {
	private static long loopid = 0L;
	private static HashMap<TemplarGenerator, Location> active = new HashMap<TemplarGenerator, Location>();
	
	public static void tickGenerators() {
		if(loopid != 0L)
			return;
		BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
		loopid = scheduler.scheduleSyncRepeatingTask(Start.getPlugin(), new Runnable() {

			@Override
			public void run() {
				for(TemplarGenerator tg : active.keySet()) {
					if(!tg.tickGenerator(active.get(tg))) {
						active.remove(tg);
						continue;
					}
				}
				if(active.isEmpty()) {
					cancelTask(loopid);
				}
			}

		}, 0L, 10L);
	}
	
	private static void cancelTask(long i) {
		Bukkit.getServer().getScheduler().cancelTask((int) i);
		loopid = 0L;
	}
	
	public static void addToActive(Location l, TemplarGenerator tg) {
		active.put(tg, l);
	}
}
