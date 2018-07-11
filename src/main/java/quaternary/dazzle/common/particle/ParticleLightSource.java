package quaternary.dazzle.common.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import quaternary.dazzle.common.Dazzle;
import quaternary.dazzle.common.etc.Util;

public class ParticleLightSource extends Particle {
	static final ResourceLocation particleRes = new ResourceLocation(Dazzle.MODID, "particle/light_source");
	static TextureAtlasSprite sprite;
	
	public ParticleLightSource(World w, BlockPos p, EnumDyeColor color) {
		super(w, p.getX() + .6, p.getY() + .5, p.getZ() + .6);
		
		canCollide = false;
		setSize(0, 0);
		particleScale = 0;
		particleMaxAge = rand.nextInt(10) + 20;
		
		motionX = Util.map(rand.nextFloat(), 0, 1, -0.01f, 0.01f);
		motionY = Util.map(rand.nextFloat(), 0, 1, 0.02f, 0.04f);
		motionZ = Util.map(rand.nextFloat(), 0, 1, -0.01f, 0.01f);;
		
		if(sprite == null) {
			sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(particleRes.toString());
		}
		
		particleRed = 255 - (0xFF0000 & color.getColorValue()) >> 16;
		particleGreen = 255 - (0x00FF00 & color.getColorValue()) >> 8;
		particleBlue = 255 - (0x0000FF & color.getColorValue());
		
		setParticleTexture(sprite);
	}
	
	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		float s;
		if(particleAge < particleMaxAge / 2) {
			s = Util.map(particleAge, 0, particleMaxAge / 2, 0, 2);
		} else {
			s = Util.map(particleAge, particleMaxAge / 2, particleMaxAge, 2, 0);
		}
		particleScale = s;
		
		if(particleAge > particleMaxAge) {
			setExpired(); return;
		}
		
		particleAge++;
		
		move(motionX, motionY, motionZ);
	}
	
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		GlStateManager.depthMask(false);
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		GlStateManager.depthMask(true);
	}
	
	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		//format the number 15 in the weird way that IBlockAccess#getCombinedLight does :shrug:
		return (15 << 20) | (15 << 4);
	}
	
	@Override
	public int getFXLayer() {
		return 1;
	}
	
	public static void textureStitch(TextureStitchEvent.Pre e) {
		e.getMap().registerSprite(particleRes);
	}
}
