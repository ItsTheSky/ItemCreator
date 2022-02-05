package info.itsthesky.itemcreator.core.properties;

import com.cryptomorin.xseries.XMaterial;
import info.itsthesky.itemcreator.api.properties.simple.SimpleNBTStateProperty;

public class ICTagProperty extends SimpleNBTStateProperty {

	@Override
	public String getId() {
		return "ic_tag";
	}

	@Override
	public boolean allowClearing() {
		return false;
	}

	@Override
	public XMaterial getMaterial() {
		return XMaterial.COMMAND_BLOCK_MINECART;
	}

}
