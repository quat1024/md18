package quaternary.dazzle.tile;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fml.common.Optional;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.block.BlockParticleLightSource;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class TileParticleLightSource extends TileEntity implements ITickable, ILightProvider {
	EnumDyeColor color;
	public TileParticleLightSource() {
		
	}
	
	public TileParticleLightSource(EnumDyeColor color) {
		this.color = color;
	}
	
	@Override
	public void update() {
		if(!world.isRemote || world.getTotalWorldTime() % 5 != 0) return;
		
		color = world.getBlockState(pos).getValue(BlockParticleLightSource.COLOR);
		
		Dazzle.PROXY.spawnLightSourceParticle(world, pos, color);
	}

	@Override
	@Optional.Method(modid="albedo")
	public Light provideLight() {
		return new Light.Builder().
				pos(pos).
				color(color.getColorValue(), false).
				radius(15F).
				build();
	}
}
