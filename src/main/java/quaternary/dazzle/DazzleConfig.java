package quaternary.dazzle;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Dazzle.MODID, name = Dazzle.NAME)
@Mod.EventBusSubscriber(modid = Dazzle.MODID)
public class DazzleConfig {
	@Config.Name("client")
	public static final Client CLIENT = new Client();
	
	public static final class Client {
		@Config.Name("mirage_support")
		@Config.Comment("If Mirage is installed, certain lights will create colored lighting shader effects.\n\nThis config option does nothing without Mirage installed.")
		@Config.LangKey(Dazzle.MODID + ".config.mirage_support")
		public static boolean MIRAGE_SUPPORT = true;
		
		@Config.Name("dumb_injokes")
		@Config.Comment("Enable various (clientside only, non gameplay affecting) dumb things?")
		@Config.LangKey(Dazzle.MODID + ".config.dumb_injokes")
		public boolean DUMB_INJOKES = true;
		
		@Config.Name("very_dumb_injokes")
		@Config.Comment("Dumb injokes for all!")
		@Config.LangKey(Dazzle.MODID + ".config.very_dumb_injokes")
		public boolean VERY_DUMB_INJOKES = false;
	}
		
	@SubscribeEvent
	public static void configChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(Dazzle.MODID)) ConfigManager.sync(Dazzle.MODID, Config.Type.INSTANCE);
	}
}
