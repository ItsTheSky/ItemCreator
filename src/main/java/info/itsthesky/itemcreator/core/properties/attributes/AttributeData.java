package info.itsthesky.itemcreator.core.properties.attributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

public class AttributeData {

	private final Attribute attribute;
	private final AttributeModifier.Operation operation;
	private final Double amount;
	private final @Nullable EquipmentSlot slot;

	public AttributeData(Attribute attribute, AttributeModifier.Operation operation, double amount, @Nullable EquipmentSlot slot) {
		this.attribute = attribute;
		this.operation = operation;
		this.amount = amount;
		this.slot = slot;
	}

	public Attribute getAttribute() {
		return attribute;
	}

	public AttributeModifier.Operation getOperation() {
		return operation;
	}

	public double getAmount() {
		return amount;
	}

	public @Nullable EquipmentSlot getSlot() {
		return slot;
	}

	@Override
	public String toString() {
		return slot == null ?
				String.join(":",
						attribute.name(),
						operation.name(),
						amount.toString()) :
				String.join(":",
						attribute.name(),
						operation.name(),
						amount.toString(),
						slot.name());
	}
}
