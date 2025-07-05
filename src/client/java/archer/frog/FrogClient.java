package archer.frog;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrogClient implements ClientModInitializer {

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(FrogModInitializer.MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello Client Fabric world!");
		ItemTooltipCallback.EVENT.register((itemStack, tooltipContext, tooltipType, list) -> {
			if (!itemStack.isOf(ModItems.FROG_ESSENCE)) {
				return;
			}
			list.add(Text.literal("If you listen closely, you can hear a faint croaking emanating from within.")
					.withColor(Colors.GRAY));
		});

	}
}