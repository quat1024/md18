package quaternary.dazzle.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import quaternary.dazzle.common.DazzleCreativeTab;
import quaternary.dazzle.common.block.BlockParticleLightSource;

public class ItemParticleLight extends ItemBlock {
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
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab == DazzleCreativeTab.INST) {
			for(int data = 0; data < 15; data++) {
				items.add(new ItemStack(this, 1, data));
			}
		}
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String colorUnloc = EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
		String color = I18n.translateToLocal("dazzle.color." + colorUnloc);
		
		return I18n.translateToLocalFormatted("tile.dazzle.particle_light_source.name", color);
	}
}
