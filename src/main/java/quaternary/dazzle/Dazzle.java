package quaternary.dazzle;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.block.*;
import quaternary.dazzle.block.stadium.*;
import quaternary.dazzle.compat.albedo.AlbedoCompat;
import quaternary.dazzle.item.*;
import quaternary.dazzle.particle.ParticleLightSource;
import quaternary.dazzle.proxy.ServerProxy;
import quaternary.dazzle.tile.TileLightSensor;
import quaternary.dazzle.tile.TileParticleLightSource;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = Dazzle.MODID, name = Dazzle.NAME, version = Dazzle.VERSION)
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "0.0.0";
	
	public static final List<BlockBase> BLOCKS = new ArrayList<>();

	@Mod.Instance
	public static Dazzle instance;
	
	@SidedProxy(serverSide = "quaternary.dazzle.proxy.ServerProxy", clientSide = "quaternary.dazzle.proxy.ClientProxy")
	public static ServerProxy PROXY;
	
	private static final String[] LAMP_VARIANTS = new String[] {"classic", "modern", "pulsating", "lantern"};

	private AlbedoCompat albedoCompat = new AlbedoCompat();
	
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
		
		//stadium light
		BLOCKS.add(new BlockStadiumLightBottom());
		BLOCKS.add(new BlockStadiumLightPole());
		BLOCKS.add(new BlockStadiumLightTop());
		BLOCKS.add(new BlockStadiumLightBottomStructure());
		
		BLOCKS.add(new BlockParticleLightSource());
		
		BLOCKS.add(new BlockInvisibleLightSource());
	}
	
	static final BlockDimRedstoneTorch DIM_REDSTONE_TORCH = new BlockDimRedstoneTorch();
	
	@GameRegistry.ObjectHolder("dazzle:particle_light_source")
	public static final Block OH_GOD_ITS_SO_HACKY = Blocks.AIR;
	
	static ItemTorchGrenade TORCH_GRENADE = new ItemTorchGrenade();
	
	//Temp place to put block colors until I can update Forge
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		if(e.getSide() != Side.CLIENT) return;
		
		for(BlockBase b : BLOCKS) {
			if(b.hasBlockColors()) {
				Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockColor) b.getBlockColors(), b);
				
				if(b.hasItemForm()) {
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) b.getItemColors(), b.getItemForm());
				}
			}
		}
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler((stack, tint) -> {
			return (tint != 1) ? -1 : EnumDyeColor.values()[stack.getMetadata()].getColorValue();
		}, ((BlockParticleLightSource)OH_GOD_ITS_SO_HACKY).getItemForm());

		if(Loader.isModLoaded("albedo")){
			AlbedoCompat.load();
		}
	}
	
	@Mod.EventBusSubscriber(modid = MODID)
	public static class CommonEvents {
		@SubscribeEvent
		public static void blocks(RegistryEvent.Register<Block> e) {
			IForgeRegistry<Block> reg = e.getRegistry();
			
			for(BlockBase b : BLOCKS) {
				reg.register(b);
			}
			
			//HACK: because blockdimredstonetorch doesn't extend BlockBase I can't put it in my list.
			reg.register(DIM_REDSTONE_TORCH);
			
			GameRegistry.registerTileEntity(TileLightSensor.class, Dazzle.MODID + ":light_sensor");
			GameRegistry.registerTileEntity(TileParticleLightSource.class, Dazzle.MODID + ":particle_light");
		}
		
		@SubscribeEvent
		public static void items(RegistryEvent.Register<Item> e) {
			IForgeRegistry<Item> reg = e.getRegistry();
			
			for(BlockBase b : BLOCKS) {
				if(b.hasItemForm()) {
					reg.register(b.getItemForm());
				}
			}
			
			//HACK: dim redstone torch again
			reg.register(DIM_REDSTONE_TORCH.itemForm());
			
			/*
			//HACK: this is the only not-block item my mod actually adds so i haven't bothered to do this
			//in a better way
			reg.register(TORCH_GRENADE);
			*/
		}
		
		/*
		@SubscribeEvent
		public static void entities(RegistryEvent.Register<EntityEntry> e) {
			IForgeRegistry<EntityEntry> reg = e.getRegistry();
			
			EntityEntry grenade = EntityEntryBuilder.create().entity(EntityTorchGrenade.class).id(new ResourceLocation(Dazzle.MODID, "torch_grenade"), 0).name("torch_grenade").tracker(128, 2, true).build();
			reg.register(grenade);
		}
		*/
	}
	
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {			
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
			}
			
			//dim redstone torch hack again lul
			Item dimTorchItem = DIM_REDSTONE_TORCH.itemForm();
			ModelResourceLocation mrl = new ModelResourceLocation(dimTorchItem.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(dimTorchItem, 0, mrl);
			
			/*
			ModelResourceLocation mrl2 = new ModelResourceLocation(TORCH_GRENADE.getRegistryName(), "inventory");
			ModelLoader.setCustomModelResourceLocation(TORCH_GRENADE, 0, mrl2);
			*/
		}
		
		/*
		//Not in this version of Forge.
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
		*/
		
		@SubscribeEvent
		public static void textureStitch(TextureStitchEvent.Pre e) {
			ParticleLightSource.textureStitch(e);
		}
	}

	public AlbedoCompat getAlbedoCompat(){
		return this.albedoCompat;
	}
}
