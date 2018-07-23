package quaternary.dazzle.common;

import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import quaternary.dazzle.common.block.*;
import quaternary.dazzle.common.item.*;
import quaternary.dazzle.common.proxy.ServerProxy;
import quaternary.dazzle.common.tile.TileLightSensor;
import quaternary.dazzle.common.tile.TileParticleLightSource;

import static quaternary.dazzle.common.Dazzle.MODID;

@Mod(modid = MODID, name = Dazzle.NAME, version = Dazzle.VERSION, guiFactory = "quaternary.dazzle.client.config.DazzleGuiFactory")
@Mod.EventBusSubscriber(modid = MODID)
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "1.1.0";
	
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	
	@SidedProxy(serverSide = "quaternary.dazzle.common.proxy.ServerProxy", clientSide = "quaternary.dazzle.client.proxy.ClientProxy")
	public static ServerProxy PROXY;
	
	@Mod.EventHandler
	public static void preinit(FMLPreInitializationEvent e) {
		DazzleConfig.preinit(e);
		
		PROXY.preinit();
	}
	
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> e) {
		DazzleBlocks.init(e.getRegistry());
		
		GameRegistry.registerTileEntity(TileLightSensor.class, MODID + ":light_sensor");
		GameRegistry.registerTileEntity(TileParticleLightSource.class, MODID + ":particle_light");
	}
	
	@SubscribeEvent
	public static void items(RegistryEvent.Register<Item> e) {
		DazzleItems.init(e.getRegistry());
	}
}
