package quaternary.dazzle.compat.shaderlights.buggyoptional;

import com.elytradev.mirage.event.GatherLightsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.compat.shaderlights.ColoredLightingMods;
import quaternary.dazzle.compat.shaderlights.StaticShaderLightManager;

public class MirageLightGatherer {
	@SubscribeEvent
	public static void mirageLights(GatherLightsEvent e) {
		StaticShaderLightManager sslm = ColoredLightingMods.getStaticLightManager();
		
		sslm.lights.values().forEach(wrappedLight -> e.add(wrappedLight.buildMirage()));
	}
}
