package quaternary.dazzle.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.DazzleCreativeTab;
import quaternary.dazzle.common.etc.EnumLampVariant;

import java.util.ArrayList;
import java.util.List;

@GameRegistry.ObjectHolder(Dazzle.MODID)
public class DazzleBlocks {
	private static final List<AbstractBlockLamp> LAMPS = new ArrayList<>();
	
	public static class Names {
		public static final String LIGHT_SENSOR = "light_sensor";
		
		public static final String LIGHT_PANEL = "panel";
		public static final String PARTICLE_LIGHT = "particle_light_source";
		public static final String INVISIBLE_LIGHT = "invisible_light_source";
		
		public static final String DIM_REDSTONE_TORCH = "dim_redstone_torch";
	}
	
	@GameRegistry.ObjectHolder(Names.LIGHT_SENSOR)
	public static final BlockLightSensor LIGHT_SENSOR = null;
	
	@GameRegistry.ObjectHolder(Names.LIGHT_PANEL)
	public static final BlockLightPanel LIGHT_PANEL = null;
	
	@GameRegistry.ObjectHolder(Names.PARTICLE_LIGHT)
	public static final BlockParticleLightSource PARTICLE_LIGHT = null;
	
	@GameRegistry.ObjectHolder(Names.INVISIBLE_LIGHT)
	public static final BlockInvisibleLightSource INVISIBLE_LIGHT = null;
	
	@GameRegistry.ObjectHolder(Names.DIM_REDSTONE_TORCH)
	public static final BlockDimRedstoneTorch DIM_REDSTONE_TORCH = null;
	
	public static void init(IForgeRegistry<Block> reg) {
		populateLamps();
		
		for(Block b : LAMPS) {
			reg.register(b);
		}
		
		reg.register(createBlock(new BlockLightSensor(), Names.LIGHT_SENSOR));
		
		reg.register(createBlock(new BlockLightPanel(), Names.LIGHT_PANEL));
		reg.register(createBlock(new BlockParticleLightSource(), Names.PARTICLE_LIGHT));
		reg.register(createBlock(new BlockInvisibleLightSource(), Names.INVISIBLE_LIGHT));
		
		reg.register(createBlock(new BlockDimRedstoneTorch(), Names.DIM_REDSTONE_TORCH));
	}
	
	private static <T extends Block> T createBlock(T block, String name) {
		block.setRegistryName(new ResourceLocation(Dazzle.MODID, name));
		block.setUnlocalizedName(Dazzle.MODID + "." + name);
		block.setCreativeTab(DazzleCreativeTab.INST);
		
		return block;
	}
	
	private static void populateLamps() {
		for(EnumDyeColor color : EnumDyeColor.values()) {
			for(EnumLampVariant variant : EnumLampVariant.values()) {
				String digitalName = String.format("%s_%s_digital_lamp", color.getName(), variant);
				LAMPS.add(createBlock(new BlockDigitalLamp(color, variant), digitalName));
				
				String analogName = String.format("%s_%s_analog_lamp", color.getName(), variant);
				BlockAnalogLamp analogNormal = createBlock(new BlockAnalogLamp(color, variant, false), analogName);
				BlockAnalogLamp analogInvert = createBlock(new BlockAnalogLamp(color, variant, true), "inverted_" + analogName);
				
				analogNormal.setInverseBlockstate(analogInvert.getDefaultState());
				analogInvert.setInverseBlockstate(analogNormal.getDefaultState());
				
				LAMPS.add(analogNormal);
				LAMPS.add(analogInvert);
			}
		}
	}
	
	public static List<AbstractBlockLamp> getLamps() {
		return LAMPS;
	}
}
