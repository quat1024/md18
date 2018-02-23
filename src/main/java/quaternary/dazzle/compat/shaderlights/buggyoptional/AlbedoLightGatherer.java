package quaternary.dazzle.compat.shaderlights.buggyoptional;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.compat.shaderlights.ColoredLightingMods;
import quaternary.dazzle.compat.shaderlights.StaticShaderLightManager;

import java.util.ArrayList;

public class AlbedoLightGatherer {
	@SubscribeEvent
	public static void albedoLights(GatherLightsEvent e) {
		StaticShaderLightManager sslm = ColoredLightingMods.getStaticLightManager();
		
		ArrayList<Light> albedoLightList = e.getLightList();
		sslm.lights.values().forEach(wrappedLight -> albedoLightList.add(wrappedLight.buildAlbedo()));
	}
}
