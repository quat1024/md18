package quaternary.dazzle.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleConfig;

import javax.annotation.Nullable;
import java.util.*;

public class ItemBlockLamp extends DzItemBlock {
	public final EnumDyeColor color;
	final String style;
	final String langKey;
	
	public ItemBlockLamp(Block b, EnumDyeColor color, String style, String langKey) {
		super(b);
		this.color = color;
		this.style = style;
		this.langKey = langKey;
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		//Abandon all hope ye who enter here
		String str = I18n.translateToLocal("dazzle.tooltip.lamp");
		
		boolean doDumbShit = DazzleConfig.CLIENT.DUMB_INJOKES && (DazzleConfig.CLIENT.VERY_DUMB_INJOKES || FMLClientHandler.instance().getClient().getSession().getProfile().getId().equals(UUID.fromString("32abd7b2-19fa-41e6-bd3b-142001cb8d6f")));
		
		if(doDumbShit) {
			//YALL MADE ME DO IT OK
			char[] chars = str.toCharArray();
			StringBuilder bep = new StringBuilder();
			Random bepis = new Random();
			for(char c : chars) {
				int index = bepis.nextInt(TextFormatting.values().length);
				TextFormatting color = TextFormatting.values()[index];
				bep.append(color.toString());
				
				if(bepis.nextBoolean()) {
					bep.append(Character.toUpperCase(c));
				} else bep.append(c);
			}
			str = bep.toString();
		} else {
			str = TextFormatting.DARK_GRAY + "" + TextFormatting.ITALIC + str + TextFormatting.RESET;
		}
		
		tooltip.add(str);
		super.addInformation(stack, world, tooltip, flag);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String lampStyleLocal = I18n.translateToLocal("dazzle.lamp_style." + style);
		String colorLocal = I18n.translateToLocal("dazzle.color." + color.getUnlocalizedName());
		return I18n.translateToLocalFormatted(langKey, colorLocal, lampStyleLocal);
	}
	
	public ResourceLocation getModelResourceHack() {
		//The different lamp blocks all have different block IDs but point to the same model w/ a statemapper.
		//Same thing going on here. I just point all 32 item models for a lamp at the same model.
		return new ResourceLocation(Dazzle.MODID, "lamp_" + style);
	}
	
	@SideOnly(Side.CLIENT)
	public IItemColor getItemColors() {
		return (ItemStack stack, int tintIndex) -> color.getColorValue();
	}
}
