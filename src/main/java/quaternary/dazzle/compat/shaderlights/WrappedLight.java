package quaternary.dazzle.compat.shaderlights;

import com.elytradev.mirage.lighting.Light;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import quaternary.dazzle.etc.Box;

/** Wrap Albedo and Mirage lights. One light to rule them all!
 * Also, cache the light, since it won't move. */
@SideOnly(Side.CLIENT)
public class WrappedLight {
	BlockPos pos;
	int color;
	float intensity;
	float radius;
	
	//Welcome to Class Name Conflict Hell!
	//Enjoy your stay!
	
	//Taking advantage of generic type erasure
	Box<Light> mirageLight;
	Box<elucent.albedo.lighting.Light> albedoLight;
	
	public WrappedLight(BlockPos pos, int color, float intensity, float radius) {
		this.pos = pos;
		this.color = color;
		this.intensity = intensity;
		this.radius = radius;
	}
	
	@Optional.Method(modid = "mirage")
	public Light buildMirage() {
		if(mirageLight == null) {
			Light light = Light.builder().pos(pos).color(color, false).intensity(intensity).radius(radius).build();
			
			mirageLight = new Box<>(light);
		}
		return mirageLight.unbox();
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
			
			elucent.albedo.lighting.Light light = elucent.albedo.lighting.Light.builder().pos(pos).color(color, false).radius(radius).build(); 
			
			albedoLight = new Box<>(light);
		}
		return albedoLight.unbox();
	}
	
	@Override
	public String toString() {
		return "WrappedLight{pos=" + pos + "color=" + color + "intensity=" + intensity + "radius=" + radius + "}";
	}
}
