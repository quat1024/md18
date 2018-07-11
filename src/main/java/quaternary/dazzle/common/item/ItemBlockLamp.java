package quaternary.dazzle.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.block.BlockLamp;
import quaternary.dazzle.common.etc.EnumLampVariant;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockLamp extends ItemBlock {
	public final EnumDyeColor color;
	final EnumLampVariant style;
	final String langKey;
	
	public ItemBlockLamp(BlockLamp b) {
		super(b);
		this.color = b.getColor();
		this.style = b.getVariant();
		this.langKey = b.getUnlocalizedName();
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		String str = I18n.translateToLocal("dazzle.tooltip.lamp");
		str = TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + str + TextFormatting.RESET;
		tooltip.add(str);
		
		super.addInformation(stack, world, tooltip, flag);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String lampStyleLocal = I18n.translateToLocal("dazzle.lamp_style." + style);
		String colorLocal = I18n.translateToLocal("dazzle.color." + color.getUnlocalizedName());
		return I18n.translateToLocalFormatted(langKey, colorLocal, lampStyleLocal);
	}
	
	public EnumDyeColor getColor() {
		return color;
	}
	
	public EnumLampVariant getStyle() {
		return style;
	}
	
	public String getLangKey() {
		return langKey;
	}
}
