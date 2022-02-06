package info.itsthesky.itemcreator.utils;

import org.bukkit.potion.PotionEffectType;

public class Potion {

	private final PotionEffectType type;
	private final int duration;
	private final int amplifier;

	public Potion(PotionEffectType type, int duration, int amplifier) {
		this.type = type;
		this.duration = duration;
		this.amplifier = amplifier;
	}

	public PotionEffectType getType() {
		return type;
	}

	public int getDuration() {
		return duration;
	}

	public int getAmplifier() {
		return amplifier;
	}
}
