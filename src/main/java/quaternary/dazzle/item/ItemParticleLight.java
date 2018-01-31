package quaternary.dazzle.item;

import net.minecraft.item.*;
import net.minecraft.util.text.translation.I18n;
import quaternary.dazzle.block.BlockParticleLightSource;

public class ItemParticleLight extends ItemBlock {
	public ItemParticleLight(BlockParticleLightSource b) {
		super(b);
		setMaxDamage(0);
		setHasSubtypes(true);
		
		setRegistryName(b.getRegistryName()); //pls
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
