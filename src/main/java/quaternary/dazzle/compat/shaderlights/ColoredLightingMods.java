package quaternary.dazzle.compat.shaderlights;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.Dazzle;

public class ColoredLightingMods {
	public static boolean shouldUseShaderLights() {
		return Dazzle.PROXY.shouldUseShaderLights();
	}
	
	@SideOnly(Side.CLIENT)
	private static StaticShaderLightManager sslm = null;
	
	@SideOnly(Side.CLIENT)
	public static StaticShaderLightManager getStaticLightManager() {
		if(sslm == null) sslm = new StaticShaderLightManager();
		return sslm;
	}
}
