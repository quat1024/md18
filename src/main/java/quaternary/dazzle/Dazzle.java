package quaternary.dazzle;

import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;
import quaternary.dazzle.block.BlockInvisibleLightSource;
import quaternary.dazzle.block.panel.BlockLightPanel;

import java.util.Collections;

@Mod(modid = Dazzle.MODID, name = Dazzle.NAME, version = Dazzle.VERSION)
@Mod.EventBusSubscriber
public class Dazzle {
	public static final String MODID = "dazzle";
	public static final String NAME = "Dazzle";
	public static final String VERSION = "0.0.0";
	
	public static final BlockInvisibleLightSource INVISIBLE_LIGHT = new BlockInvisibleLightSource().setNames();
	public static final BlockLightPanel PANEL = new BlockLightPanel();
	
	@SubscribeEvent
	public static void blocks(RegistryEvent.Register<Block> e) {
		IForgeRegistry<Block> reg = e.getRegistry();
		
		reg.register(INVISIBLE_LIGHT);
		reg.register(PANEL);
	}
	
	@Mod.EventBusSubscriber(value = Side.CLIENT, modid = MODID)
	public static class ClientEvents {
		@SubscribeEvent
		public static void models(ModelRegistryEvent e) {
			//Suppress missing model JSON errors
			ModelLoader.setCustomStateMapper(INVISIBLE_LIGHT, block -> Collections.emptyMap());
		}
	}
}
