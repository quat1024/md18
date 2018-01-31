package quaternary.dazzle.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityThrownTorch extends Entity {
	public EntityThrownTorch(World w) { super(w); }
	
	public EntityThrownTorch(World w, BlockPos pos) {
		super(w);
		
		setPosition(pos.getX() + .5, pos.getY() + 1, pos.getZ() + .5);
		
		double angle = rand.nextDouble() * Math.PI * 2;
		setVelocity(.3 * Math.cos(angle), 0.4, .3 * Math.sin(angle));
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	protected void entityInit() {
		
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		
	}
}
