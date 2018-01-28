package quaternary.dazzle.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import quaternary.dazzle.block.BlockLightSensor;

public class TileEntityLightSensor extends TileEntity implements ITickable {
	@Override
	public void update() {
		//update blocks surrounding my position
		if(!world.isRemote && world.getTotalWorldTime() % 10 == 0) {
			IBlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			EnumFacing facing = state.getValue(BlockLightSensor.FACING);
			BlockPos posToUpdateAround = pos.offset(facing.getOpposite());
			
			world.neighborChanged(posToUpdateAround, b, pos);
			world.notifyNeighborsOfStateExcept(posToUpdateAround, b, facing);
			/*
			world.scheduleUpdate(posToUpdateAround, world.getBlockState(posToUpdateAround).getBlock(), 0);
			for(EnumFacing whichWay : EnumFacing.values()) {
				if(whichWay == facing) continue;
				BlockPos u = posToUpdateAround.offset(whichWay);
				world.scheduleUpdate(u, world.getBlockState(u).getBlock(), 0);
			}
			*/
		}
	}
}
