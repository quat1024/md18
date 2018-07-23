package quaternary.dazzle.common;

import net.minecraftforge.common.config.*;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.common.etc.EnumLampVariant;

@Mod.EventBusSubscriber
public class DazzleConfig {
	public static Configuration config;
	
	public static void preinit(FMLPreInitializationEvent e) {
		config = new Configuration(e.getSuggestedConfigurationFile());
		config.load();
		
		readConfig();
	}
	
	public static void readConfig() {
		for(EnumLampVariant variant : EnumLampVariant.values()) {
			String name = variant.toString();
			Property prop = config.get("lamps", name, true, "Are the " + name + " lamps registered? Uses 48 block IDs.");
			prop.setRequiresMcRestart(true);
			
			variant.isEnabled = prop.getBoolean();
		}
		
		if(config.hasChanged()) config.save();
	}
	
	@SubscribeEvent
	public static void configChanged(ConfigChangedEvent.OnConfigChangedEvent e) {
		if(e.getModID().equals(Dazzle.MODID)) {
			readConfig();
		}
	}
}
