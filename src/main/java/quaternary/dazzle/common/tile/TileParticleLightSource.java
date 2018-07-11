package quaternary.dazzle.common.tile;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.block.BlockParticleLightSource;

public class TileParticleLightSource extends TileEntity implements ITickable {
	@Override
	public void update() {
		if(!world.isRemote || world.getTotalWorldTime() % 5 != 0) return;
		
		EnumDyeColor color = world.getBlockState(pos).getValue(BlockParticleLightSource.COLOR);
		
		Dazzle.PROXY.spawnLightSourceParticle(world, pos, color);
	}
}
