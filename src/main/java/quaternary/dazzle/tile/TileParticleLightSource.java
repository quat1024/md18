package quaternary.dazzle.tile;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.block.BlockParticleLightSource;

public class TileParticleLightSource extends TileEntity implements ITickable {
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
}