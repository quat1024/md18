package quaternary.dazzle.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.DazzleCreativeTab;
import quaternary.dazzle.common.block.AbstractBlockLamp;
import quaternary.dazzle.common.block.DazzleBlocks;

import java.util.ArrayList;
import java.util.List;

@GameRegistry.ObjectHolder(Dazzle.MODID)
public class DazzleItems {
	private static final List<ItemBlockLamp> LAMP_ITEMS = new ArrayList<>();
	
	@GameRegistry.ObjectHolder(DazzleBlocks.Names.PARTICLE_LIGHT)
	public static final ItemParticleLight PARTICLE_LIGHT = null;
	
	public static void init(IForgeRegistry<Item> reg) {
		for(AbstractBlockLamp lamp : DazzleBlocks.getLamps()) {
			if(lamp.hasItemForm()) {
				ItemBlockLamp bleh = createItemBlock(new ItemBlockLamp(lamp));
				LAMP_ITEMS.add(bleh);
				reg.register(bleh);
			}
		}
		
		reg.register(createItemBlock(new ItemBlock(DazzleBlocks.LIGHT_SENSOR)));
		
		reg.register(createItemBlock(new ItemBlock(DazzleBlocks.LIGHT_PANEL)));
		reg.register(createItemBlock(new ItemParticleLight(DazzleBlocks.PARTICLE_LIGHT)));
		
		reg.register(createItemBlock(new ItemBlock(DazzleBlocks.DIM_REDSTONE_TORCH)));
	}
	
	private static <T extends ItemBlock> T createItemBlock(T itemBlock) {
		itemBlock.setRegistryName(itemBlock.getBlock().getRegistryName());
		itemBlock.setCreativeTab(DazzleCreativeTab.INST);
		
		return itemBlock;
	}
	
	public static List<ItemBlockLamp> getLampItems() {
		return LAMP_ITEMS;
	}
}
