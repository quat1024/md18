package quaternary.dazzle.item;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import quaternary.dazzle.block.BlockParticleLightSource;

public class ItemParticleLight extends DzItemBlock {
	public ItemParticleLight(BlockParticleLightSource b) {
		super(b);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String colorUnloc = EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
		String color = I18n.translateToLocal("dazzle.color." + colorUnloc);
		//HACK
		return I18n.translateToLocalFormatted("tile.dazzle.particle_light_source.name", color);
	}
}
