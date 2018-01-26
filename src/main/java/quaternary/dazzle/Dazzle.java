package quaternary.dazzle;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.block.*;
import quaternary.dazzle.block.stadium.*;

import java.util.*;

@Mod(modid = Dazzle.MODID, name = Dazzle.NAME, version = Dazzle.VERSION)
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "0.0.0";
	
	public static final List<BlockBase> BLOCKS = new ArrayList<>();
	
	private static final String[] LAMP_VARIANTS = new String[] {"classic", "modern"};
	
	static {
		for(String variant : LAMP_VARIANTS) {
			for(EnumDyeColor c : EnumDyeColor.values()) {
				BLOCKS.add(new BlockDigitalLamp(c, variant));
			}
		}
		
		for(String variant : LAMP_VARIANTS) {
			for(EnumDyeColor c : EnumDyeColor.values()) {
				BlockAnalogLamp normal = new BlockAnalogLamp(c, variant, false);
				BlockAnalogLamp inverse = new BlockAnalogLamp(c, variant, true);
				normal.setInverseBlockstate(inverse.getDefaultState());
				inverse.setInverseBlockstate(normal.getDefaultState());
				
				BLOCKS.add(normal);
				BLOCKS.add(inverse);
			}
		}
		
		BLOCKS.add(new BlockLightPanel());
		BLOCKS.add(new BlockStadiumLightBottom());
		BLOCKS.add(new BlockStadiumLightPole());
		BLOCKS.add(new BlockStadiumLightTop());
		
		BLOCKS.add(new BlockInvisibleLightSource());
	}
	
	@Mod.EventBusSubscriber(modid = MODID)
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(BlockBase b : BLOCKS) {
				reg.register(b);
			}
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(BlockBase b : BLOCKS) {
				if(b.hasItemForm()) {
					reg.register(b.getItemForm());
				}
			}
		}
	}
	
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {			
			for(BlockBase b : BLOCKS) {
				if(b.hasItemForm()) {
					Item i = b.getItemForm();
					ModelResourceLocation mrl = new ModelResourceLocation(i.getRegistryName(), "inventory");
					ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
				}
				
				if(b.hasCustomStatemapper()) {
					ModelLoader.setCustomStateMapper(b, b.getCustomStatemapper());
				}
			}
		}
		
		@SubscribeEvent
		public static void blockColors(ColorHandlerEvent.Block e) {
			BlockColors colors = e.getBlockColors();
			
			for(BlockBase b : BLOCKS) {
				if(b.hasBlockColors()) {
					colors.registerBlockColorHandler(b.getBlockColors(), b);
				}
			}
		}
		
		@SubscribeEvent
		public static void itemColors(ColorHandlerEvent.Item e) {
			ItemColors colors = e.getItemColors();
			
			for(BlockBase b : BLOCKS) {
				if(b.hasItemForm() && b.hasBlockColors()) {
					colors.registerItemColorHandler(b.getItemColors(), b.getItemForm());
				}
			}
		}
	}
}
