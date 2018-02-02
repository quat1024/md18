package quaternary.dazzle.entity;

import elucent.albedo.lighting.ILightProvider;
import elucent.albedo.lighting.Light;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Optional.Interface(iface="elucent.albedo.lighting.ILightProvider", modid="albedo")
public class EntityThrownTorch extends Entity implements ILightProvider {
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

	@Override
	@Optional.Method(modid="albedo")
	public Light provideLight() {
		return new Light.Builder().
			pos(this.getEntityBoundingBox().getCenter()).
			color(1F, 1F, 1F).
			radius(5F).
			build();
	}
}
