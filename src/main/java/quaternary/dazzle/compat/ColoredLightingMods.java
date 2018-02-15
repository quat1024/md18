package quaternary.dazzle.compat;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.Dazzle;

public class ColoredLightingMods {
	public static boolean shouldUseShaderLights() {
		return Dazzle.PROXY.shouldUseShaderLights();
	}
	
	/** Wrap Albedo and Mirage lights. One light to rule them all!
	 * Also, cache the light, since it won't move. */
	@SideOnly(Side.CLIENT)
	public static class WrappedLight {
		BlockPos pos;
		int color;
		float intensity;
		float radius;
		
		//Erase the type because there's nothing like Optional.Field.
		//One Light type might not exist if only Albedo or only Mirage is installed.
		//todo Do I really need to do this
		Object mirageLight;
		Object albedoLight;
		
		public WrappedLight(BlockPos pos, int color, float intensity, float radius) {
			this.pos = pos;
			this.color = color;
			this.intensity = intensity;
			this.radius = radius;
		}
		
		//FULLY QUALIFIED CLASS NAMES INCOMINGGGGGGGGGGGG
		//(they're both named Light)
		
		@Optional.Method(modid = "mirage")
		public com.elytradev.mirage.lighting.Light buildMirage() {
			if(mirageLight == null) {
				mirageLight = com.elytradev.mirage.lighting.Light.builder().pos(pos).color(color, false).intensity(intensity).radius(radius).build();
			}
			return (com.elytradev.mirage.lighting.Light) mirageLight;
		}
		
		@Optional.Method(modid = "albedo")
		public elucent.albedo.lighting.Light buildAlbedo() {
			if(albedoLight == null) {
				//Albedo doesn't support light intensity like Mirage does.
				//Luckily I can get pretty close to the same result by just multiplying the RGB.
				
				int r = (color & 0xFF0000) >> 16;
				int g = (color & 0x00FF00) >> 8;
				int b = (color & 0x0000FF);
				
				r *= intensity;
				g *= intensity;
				b *= intensity;
				
				color = (r << 16) | (g << 8) | b;
				
				albedoLight = elucent.albedo.lighting.Light.builder().pos(pos).color(color, false).radius(radius).build();
			}
			return (elucent.albedo.lighting.Light) albedoLight;
		}
	}
}
