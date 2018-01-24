package quaternary.dazzle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.block.*;
import quaternary.dazzle.block.stadium.BlockStadiumLightBase;
import quaternary.dazzle.block.stadium.BlockStadiumLightPole;

import java.util.*;

@Mod(modid = Dazzle.MODID, name = Dazzle.NAME, version = Dazzle.VERSION)
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "0.0.0";
	
	public static final List<BlockBase> BLOCKS = Lists.newArrayList(
		new BlockInvisibleLightSource(),
		new BlockLightPanel(),
		new BlockAnalogLamp(),
		new BlockStadiumLightBase(),
		new BlockStadiumLightPole()
	);
	
	static {
		for(EnumDyeColor c : EnumDyeColor.values()) {
			BLOCKS.add(new BlockDigitalLamp(c));
		}
	}
	
	@Mod.EventBusSubscriber(modid = MODID)
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			//No I can't do registerall because varargs doesn't work on lists
			//Java is good
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
	}
}
