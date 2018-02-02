package quaternary.dazzle.entity;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class EntityTorchGrenade extends EntityThrowable implements ILightProvider{
	int torchCount;
	
	public EntityTorchGrenade(World w) { super(w); }
	
	public EntityTorchGrenade(World worldIn, EntityLivingBase throwerIn, int torchCount)
	{
		super(worldIn, throwerIn);
		this.torchCount = torchCount;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
	}
	
	@Override
	protected void onImpact(RayTraceResult result) {
		if(world.isRemote) return;
		
		for(int i=0; i < torchCount; i++) {
			EntityThrownTorch torch = new EntityThrownTorch(world, getPosition());
			world.spawnEntity(torch);
		}
		
		setDead();
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound cmp) {
		torchCount = cmp.getInteger("TorchCount");
		super.readEntityFromNBT(cmp);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound cmp) {
		cmp.setInteger("TorchCount", torchCount);
		super.writeEntityToNBT(cmp);
	}

	@Override
	@Optional.Method(modid="albedo")
	public Light provideLight() {
		float radius =  4F + (this.torchCount);
		return new Light.Builder().
				pos(this.getEntityBoundingBox().getCenter()).
				color(1F, 1F, 1F).
				radius(radius).
				build();
	}
}
