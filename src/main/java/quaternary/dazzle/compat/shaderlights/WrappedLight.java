package quaternary.dazzle.compat.shaderlights;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Wrap Albedo and Mirage lights. One light to rule them all!
 * It is OK to load this class on a server. */
public class WrappedLight {
	public BlockPos pos;
	public int color;
	public float intensity;
	public float radius;
	
	public WrappedLight(BlockPos pos, int color, float intensity, float radius) {
		this.pos = pos;
		this.color = color;
		this.intensity = intensity;
		this.radius = radius;
	}
	
	@Override
	public String toString() {
		return "WrappedLight{pos=" + pos + "color=" + color + "intensity=" + intensity + "radius=" + radius + "}";
	}
}
