package info.itsthesky.itemcreator.core.properties.spawners;

import info.itsthesky.itemcreator.ItemCreator;
import info.itsthesky.itemcreator.api.properties.base.BlockProperty;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;

public abstract class SpawnerProperty<T> extends BlockProperty<T> {

	public SpawnerProperty(ItemCreator instance) {
		super(instance);
	}

	@Override
	public void editBlock(Block block, T value) {
		update(((CreatureSpawner) block.getState()), value);
	}

	public abstract void update(CreatureSpawner spawner, T value);
}
