package quaternary.dazzle.client;

import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import quaternary.dazzle.common.block.BlockLamp;
import quaternary.dazzle.common.block.DazzleBlocks;
import quaternary.dazzle.common.item.DazzleItems;
import quaternary.dazzle.common.item.ItemBlockLamp;

public class ClientEvents {
	@SubscribeEvent
	public static void blockColors(ColorHandlerEvent.Block e) {
		for(BlockLamp lamp : DazzleBlocks.getLamps()) {
			e.getBlockColors().registerBlockColorHandler(lamp::getBlockColor, lamp);
		}
	}
	
	@SubscribeEvent
	public static void itemColors(ColorHandlerEvent.Item e) {
		for(ItemBlockLamp lamp : DazzleItems.getLampItems()) {
			e.getItemColors().registerItemColorHandler((stack, tintIndex) -> lamp.color.getColorValue(), lamp);
		}
	}
}
