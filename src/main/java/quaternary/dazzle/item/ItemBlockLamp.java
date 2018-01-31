package quaternary.dazzle.item;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import quaternary.dazzle.Dazzle;

public class ItemBlockLamp extends ItemBlock {
	public final EnumDyeColor color;
	final String style;
	final String langKey;
	
	public ItemBlockLamp(Block b, EnumDyeColor color, String style, String langKey) {
		super(b);
		this.color = color;
		this.style = style;
		this.langKey = langKey;
		//mojang pls
		setRegistryName(b.getRegistryName());
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String lampStyleLocal = I18n.translateToLocal("dazzle.lamp_style." + style);
		String colorLocal = I18n.translateToLocal("dazzle.color." + color.getUnlocalizedName());
		return I18n.translateToLocalFormatted(langKey, colorLocal, lampStyleLocal);
	}
	
	public ResourceLocation getModelResourceHack() {
		return new ResourceLocation(Dazzle.MODID, "lamp_" + style);
	}
}
