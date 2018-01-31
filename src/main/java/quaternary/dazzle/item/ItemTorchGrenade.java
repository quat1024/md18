package quaternary.dazzle.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import quaternary.dazzle.Dazzle;
import quaternary.dazzle.DazzleCreativeTab;
import quaternary.dazzle.entity.EntityTorchGrenade;

public class ItemTorchGrenade extends Item {
	public ItemTorchGrenade() {
		setRegistryName(new ResourceLocation(Dazzle.MODID, "torch_grenade"));
		setUnlocalizedName(Dazzle.MODID + ".torch_grenade");
		setCreativeTab(DazzleCreativeTab.INST);
		
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack heldStack = player.getHeldItem(hand);
		
		if(!player.capabilities.isCreativeMode) heldStack.shrink(1);
		
		if(!world.isRemote) {
			EntityTorchGrenade grenade = new EntityTorchGrenade(world, player, 5);
			grenade.shoot(player, player.rotationPitch, player.rotationYaw, 0, 1f, .5f);
			world.spawnEntity(grenade);
		}
		
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, heldStack);
	}
}
