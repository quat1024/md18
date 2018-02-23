package quaternary.dazzle.tile;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import quaternary.dazzle.block.BlockLightSensor;

public class TileLightSensor extends TileEntity implements ITickable {
	private int oldLightLevel;
	public int lightLevel;
	
	@Override
	public void update() {
		//update blocks surrounding my position
		if(!world.isRemote && world.getTotalWorldTime() % 3 == 0) {
			oldLightLevel = lightLevel;
			
			IBlockState state = world.getBlockState(pos);
			Block b = state.getBlock();
			EnumFacing facing = state.getValue(BlockLightSensor.FACING);
			BlockPos posToReadLight = pos.offset(facing);
			BlockPos posToUpdateAround = pos.offset(facing.getOpposite());
			
			if(!world.isBlockLoaded(posToReadLight) || !world.isBlockLoaded(posToUpdateAround)) {
				return;
			}
			
			int lightLevelRead = world.getLightFor(EnumSkyBlock.BLOCK, posToReadLight);
			if(lightLevel > lightLevelRead) lightLevel--;
			if(lightLevel < lightLevelRead) lightLevel++;
			lightLevel = MathHelper.clamp(lightLevel, 0, 15);
			
			if(oldLightLevel != lightLevel) {
				world.neighborChanged(posToUpdateAround, b, pos);
				world.notifyNeighborsOfStateExcept(posToUpdateAround, b, facing);
			}
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		lightLevel = compound.getInteger("LightLevel");
		super.readFromNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setInteger("LightLevel", lightLevel); 
		return super.writeToNBT(compound);
	}
}
