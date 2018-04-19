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
		@Config.Name("compat")
		public final Compat COMPAT = new Compat();
		
		public static final class Compat {
			@Config.Name("shader_lights")
			@Config.Comment("If Mirage or Albedo is installed, certain lights will create colored lighting shader effects.\n\nThis config option does nothing without Mirage or Albedo installed. And yeah, it supports both!")
			@Config.LangKey(Dazzle.MODID + ".config.shader_lighting_support")
			public boolean SHADER_LIGHT_SUPPORT = true;
		}
	}
		
	@SubscribeEvent
	public static void configChange(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(Dazzle.MODID)) ConfigManager.sync(Dazzle.MODID, Config.Type.INSTANCE);
	}
}
