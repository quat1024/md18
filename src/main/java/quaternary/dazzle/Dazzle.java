package quaternary.dazzle;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.dazzle.block.*;
import quaternary.dazzle.item.ItemBlockLamp;
import quaternary.dazzle.item.ItemParticleLight;
import quaternary.dazzle.particle.ParticleLightSource;
import quaternary.dazzle.proxy.ServerProxy;
import quaternary.dazzle.tile.TileLightSensor;
import quaternary.dazzle.tile.TileParticleLightSource;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mod(modid = Dazzle.MODID, name = Dazzle.NAME, version = Dazzle.VERSION)
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "1.1.0";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	@SidedProxy(serverSide = "quaternary.dazzle.proxy.ServerProxy", clientSide = "quaternary.dazzle.proxy.ClientProxy")
	public static ServerProxy PROXY;
	
	private static final String[] LAMP_VARIANTS = new String[] {"classic", "modern", "pulsating", "lantern"};
	
	public static final List<Block> BLOCKS = new ArrayList<>();
	public static final List<Item> ITEMS = new ArrayList<>();
	
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
		BLOCKS.add(new BlockLightSensor());
		
		BLOCKS.add(new BlockParticleLightSource());
		BLOCKS.add(new BlockInvisibleLightSource());
		
		BlockDimRedstoneTorch asd = new BlockDimRedstoneTorch();
		BLOCKS.add(asd);
		
		//items
		for(Block b : BLOCKS) {
			if(b instanceof BlockBase && ((BlockBase) b).hasItemForm()) {
				ITEMS.add(((BlockBase)b).getItemForm());
			}
		}
		
		ItemBlock asdd = new ItemBlock(asd);
		asdd.setRegistryName(asd.getRegistryName());
		ITEMS.add(asdd);
	}
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {		
		PROXY.init();
	}
	
	@Mod.EventBusSubscriber(modid = MODID)
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(Block b : BLOCKS) {
				reg.register(b);
			}
			
			GameRegistry.registerTileEntity(TileLightSensor.class, Dazzle.MODID + ":light_sensor");
			GameRegistry.registerTileEntity(TileParticleLightSource.class, Dazzle.MODID + ":particle_light");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(Item i : ITEMS) {
				reg.register(i);
			}
		}
		
		@SubscribeEvent
		public static void missingMappings(RegistryEvent.MissingMappings<Block> e) {
			for(RegistryEvent.MissingMappings.Mapping<Block> missing : e.getMappings()) {
				ResourceLocation missingLocation = missing.key;
				if(!missingLocation.getResourceDomain().equals(Dazzle.MODID)) continue;
				
				//"inverted_white_modern_analog_lamp" to "white_modern_analog_lamp_inverted"
				Pattern analogPattern = Pattern.compile("inverted_(.*_.*_analog_lamp)");
				Matcher analogMatcher = analogPattern.matcher(missingLocation.getResourcePath());
				
				if(analogMatcher.find()) {
					String prefix = analogMatcher.group(1);
					ResourceLocation newLocation = new ResourceLocation(MODID, prefix + "_inverted");
					Block remappedBlock = Block.getBlockFromName(newLocation.toString());
					if(remappedBlock == null) {
						missing.ignore();
					} else {
						missing.remap(remappedBlock);
					}
				}
			}
		}
	}
	
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {			
			for(Item i : ITEMS) {				
				ResourceLocation res = i.getRegistryName();
				
				//Hacks for the only item that is actually using metadata
				if(i instanceof ItemParticleLight) {
					for(int a = 0; a < 16; a++) {
						ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
						ModelLoader.setCustomModelResourceLocation(i, a, mrl);
					}
					continue;
				}
				
				if(i instanceof ItemBlockLamp) {
					res = ((ItemBlockLamp) i).getModelResourceHack();
				}
				
				ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
				ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
			}
			
			for(Block b : BLOCKS) {
				if(b instanceof BlockBase) {
					BlockBase bbase = (BlockBase) b;
					
					if(bbase.hasCustomStatemapper()) {
						ModelLoader.setCustomStateMapper(bbase, bbase.getCustomStatemapper());
					}
				}
			}
			
			/*
			for(BlockBase b : BLOCKS) {
				if(b.hasItemForm()) {
					Item i = b.getItemForm();
					
					//More shitty hacks because this is the only item with metadata
					if(i instanceof ItemParticleLight) {
						ResourceLocation res = i.getRegistryName();
						for(int a = 0; a < 16; a++) {
							ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
							ModelLoader.setCustomModelResourceLocation(i, a, mrl);
						}
					} else {
						ResourceLocation res = i.getRegistryName();
						
						//Hack to set lamp item model jsons properly as they use a fancy statemapper
						if(i instanceof ItemBlockLamp) {
							res = ((ItemBlockLamp) i).getModelResourceHack();
						}
						
						ModelResourceLocation mrl = new ModelResourceLocation(res, "inventory");
						ModelLoader.setCustomModelResourceLocation(i, 0, mrl);
					}
				}
				
				if(b.hasCustomStatemapper()) {
					ModelLoader.setCustomStateMapper(b, (IStateMapper) b.getCustomStatemapper());
				}
			}*/
			
			/*
			ModelResourceLocation mrl2 = new ModelResourceLocation(TORCH_GRENADE.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(TORCH_GRENADE, 0, mrl2);
			*/
		}
		
		@SubscribeEvent
		public static void textureStitch(TextureStitchEvent.Pre e) {
			ParticleLightSource.textureStitch(e);
		}
	}
}
