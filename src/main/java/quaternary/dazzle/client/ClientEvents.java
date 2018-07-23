package quaternary.dazzle.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.client.statemapper.IgnoreAllStateMapper;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.block.*;
import quaternary.dazzle.common.item.DazzleItems;
import quaternary.dazzle.common.item.ItemBlockLamp;
import quaternary.dazzle.client.particle.ParticleLightSource;

import java.util.Collections;

public class ClientEvents {
	@SubscribeEvent
	public static void blockColors(ColorHandlerEvent.Block e) {
		BlockColors colors = e.getBlockColors();
		
		for(AbstractBlockLamp lamp : DazzleBlocks.getLamps()) {
			colors.registerBlockColorHandler(lamp::getBlockColor, lamp);
		}
		
		//Despite this block having no model, registering a blockcolor for it applies colors to the breaking particles
		//Looks pretty spiffy.
		colors.registerBlockColorHandler(((state, worldIn, pos, tintIndex) -> state.getValue(BlockParticleLightSource.COLOR).getColorValue()), DazzleBlocks.PARTICLE_LIGHT);
	}
	
	@SubscribeEvent
	public static void itemColors(ColorHandlerEvent.Item e) {
		ItemColors colors = e.getItemColors();
		
		for(ItemBlockLamp lamp : DazzleItems.getLampItems()) {
			colors.registerItemColorHandler((stack, tintIndex) -> lamp.color.getColorValue(), lamp);
		}
		
		colors.registerItemColorHandler((stack, tintIndex) -> EnumDyeColor.byMetadata(stack.getMetadata()).getColorValue(), DazzleItems.PARTICLE_LIGHT);
	}
	
	@SubscribeEvent
	public static void models(ModelRegistryEvent e) {
		for(ItemBlockLamp lamp : DazzleItems.getLampItems()) {
			setFixedMRL(lamp, "lamp_" + lamp.getStyle());
		}
		
		setDefaultMRL(Item.getItemFromBlock(DazzleBlocks.LIGHT_SENSOR));
		setDefaultMRL(Item.getItemFromBlock(DazzleBlocks.LIGHT_PANEL));
		setDefaultMRL(Item.getItemFromBlock(DazzleBlocks.DIM_REDSTONE_TORCH));
		
		setItemColor16ColorsMRL(Item.getItemFromBlock(DazzleBlocks.PARTICLE_LIGHT));
		
		for(AbstractBlockLamp lamp : DazzleBlocks.getLamps()) {
			ModelLoader.setCustomStateMapper(lamp, new IgnoreAllStateMapper("lamp_" + lamp.getVariant()));
		}
		
		ModelLoader.setCustomStateMapper(DazzleBlocks.PARTICLE_LIGHT, new IgnoreAllStateMapper("particle_light_dummy"));
		
		ModelLoader.setCustomStateMapper(DazzleBlocks.INVISIBLE_LIGHT, (state) -> Collections.emptyMap());
	}
	
	private static void setDefaultMRL(Item i) {
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
	
	private static void setItemColor16ColorsMRL(Item i) {
		for(int a = 0; a < 16; a++) {
			ModelLoader.setCustomModelResourceLocation(i, a, new ModelResourceLocation(i.getRegistryName(), "inventory"));
		}
	}
	
	private static void setFixedMRL(Item i, String fixedName) {
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(new ResourceLocation(Dazzle.MODID, fixedName), "inventory"));
	}
	
	@SubscribeEvent
	public static void textureStitch(TextureStitchEvent.Pre e) {
		ParticleLightSource.textureStitch(e);
	}
}
