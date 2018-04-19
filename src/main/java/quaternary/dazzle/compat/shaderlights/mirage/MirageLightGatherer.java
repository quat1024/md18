package quaternary.dazzle.compat.shaderlights.mirage;

import com.elytradev.mirage.event.GatherLightsEvent;
import com.elytradev.mirage.lighting.Light;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.compat.shaderlights.*;

@SideOnly(Side.CLIENT)
public class MirageLightGatherer {
	@SubscribeEvent
	public static void mirageLights(GatherLightsEvent e) {
		StaticShaderLightManager sslm = ColoredLightingMods.getStaticLightManager();
		
		sslm.lights.values().forEach(MirageLightGatherer::buildMirage);
	}
	
	public static Light buildMirage(WrappedLight wlight) {
		return Light.builder().color(wlight.color, false).intensity(wlight.intensity).pos(wlight.pos).radius(wlight.radius).build();
	}
}
