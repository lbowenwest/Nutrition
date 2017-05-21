package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCake;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlockSpecial;
import net.minecraft.item.ItemFood;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.StringJoiner;

public class EventTooltip {
	@SubscribeEvent
	public void tooltipEvent(ItemTooltipEvent event) {
		String tooltip = null;

		// Regular Food
		Item item = event.getItemStack().getItem();
		if (item instanceof ItemFood)
			tooltip = getTooltip(item);

		// Vanilla Cake
		if (item instanceof ItemBlockSpecial) {
			Block block = ((ItemBlockSpecial) item).getBlock();
			if (block instanceof BlockCake)
				tooltip = getTooltip(block);
		}

		// Generic Cake
		Block block = Block.getBlockFromItem(item);
		if (block instanceof BlockCake)
			tooltip = getTooltip(block);

		// Tooltip
		if (tooltip != null)
			event.getToolTip().add(tooltip);
	}

	private <T> String getTooltip(T food) {
		// Create readable list of nutrients
		StringJoiner stringJoiner = new StringJoiner(", ");
		List<Nutrient> foundNutrients = NutrientUtils.getFoodNutrients(food);
		for (Nutrient nutrient : foundNutrients) // Loop through nutrients from food
			stringJoiner.add(I18n.format("nutrient." + Nutrition.MODID + ":" + nutrient.name));
		String nutrientString = stringJoiner.toString();

		// Get nutrition value
		float nutritionValue = NutrientUtils.calculateNutrition(food, foundNutrients);

		// Return tooltip
		if (!nutrientString.equals("")) {
			return I18n.format("tooltip." + Nutrition.MODID + ":nutrients") + " " +
				TextFormatting.DARK_GREEN + nutrientString +
				TextFormatting.DARK_AQUA + " (" + String.format("%.1f", nutritionValue) + "%)";
		}

		return null;
	}
}