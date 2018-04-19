package quaternary.dazzle.compat.shaderlights.albedo;

import elucent.albedo.event.GatherLightsEvent;
import elucent.albedo.lighting.Light;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.compat.shaderlights.*;

import java.util.ArrayList;

public class AlbedoLightGatherer {
	@SubscribeEvent
	public static void albedoLights(GatherLightsEvent e) {
		StaticShaderLightManager sslm = ColoredLightingMods.getStaticLightManager();
		
		ArrayList<Light> albedoLightList = e.getLightList();
		sslm.lights.values().forEach(wrappedLight -> albedoLightList.add(buildAlbedo(wrappedLight)));
	}
	
	public static elucent.albedo.lighting.Light buildAlbedo(WrappedLight wlight) {
		int r = (wlight.color & 0xFF0000) >> 16;
		int g = (wlight.color & 0x00FF00) >> 8;
		int b = (wlight.color & 0x0000FF);
		
		r *= wlight.intensity;
		g *= wlight.intensity;
		b *= wlight.intensity;
		
		wlight.color = (r << 16) | (g << 8) | b;
		
		return Light.builder().pos(wlight.pos).color(wlight.color, false).radius(wlight.radius).build();
	}
}
