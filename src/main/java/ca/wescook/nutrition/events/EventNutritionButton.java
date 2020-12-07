package ca.wescook.nutrition.events;

import ca.wescook.nutrition.Nutrition;
import ca.wescook.nutrition.utility.Config;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class EventNutritionButton {
	private int NUTRITION_ID = 800;
	private ResourceLocation NUTRITION_ICON = new ResourceLocation(Nutrition.MODID, "textures/gui/gui.png");
	private ImageButton buttonNutrition;

	@SubscribeEvent
	public void guiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
		// If any inventory except player inventory is opened, get out
		Screen gui = event.getGui();
		if (!(gui instanceof InventoryScreen))
			return;

		// Get button position
		int[] pos = calculateButtonPosition(gui);
		int x = pos[0];
		int y = pos[1];

		// Create button
        buttonNutrition = new ImageButton(x, y, 20, 18, 14, 0, 19, NUTRITION_ICON, (button) -> {
			PlayerEntity player = Minecraft.getInstance().player;
			World world = Minecraft.getInstance().world;
			// Open GUI
//			player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
		});
		event.addWidget(buttonNutrition);
	}

//	@SubscribeEvent
//	public void guiButtonClick(GuiScreenEvent.MouseClickedEvent event) {
//		// Only run on GuiInventory
//		if (!(event.getGui() instanceof InventoryScreen))
//			return;
//
//		// If nutrition button is clicked
//		if (event.getButton().equals(buttonNutrition)) {
//			// Get data
//			PlayerEntity player = Minecraft.getInstance().player;
//			World world = Minecraft.getInstance().world;
//
//			// Open GUI
//			player.openGui(Nutrition.instance, ModGuiHandler.NUTRITION_GUI_ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
//		} else {
//			// Presumably recipe book button was clicked - recalculate nutrition button position
//			int[] pos = calculateButtonPosition(event.getGui());
//			int xPosition = pos[0];
//			int yPosition = pos[1];
//			buttonNutrition.setPosition(xPosition, yPosition);
//		}
//	}

	// Return array [x,y] of button coordinates
	private int[] calculateButtonPosition(Screen gui) {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;

		// Get bounding box of origin
		if (Config.buttonOrigin.equals("screen")) {
			MainWindow window = Minecraft.getInstance().getMainWindow();
			width = window.getScaledWidth();
			height = window.getScaledHeight();
		} else if (Config.buttonOrigin.equals("gui")) {
			width = ((InventoryScreen) gui).getXSize();
			height = ((InventoryScreen) gui).getYSize();
		}

		// Calculate anchor position from origin (eg. x/y pixels at right side of gui)
		// The x/y is still relative to the top/left corner of the screen at this point
		switch(Config.buttonAnchor) {
			case "top": x = width / 2; y = 0; break;
			case "right": x = width; y = height / 2; break;
			case "bottom": x = width / 2; y = height; break;
			case "left": x = 0; y = height / 2; break;
			case "top-left": x = 0; y = 0; break;
			case "top-right": x = width; y = 0; break;
			case "bottom-right": x = width; y = height; break;
			case "bottom-left": x = 0; y = height; break;
			case "center": x = width / 2; y = height / 2; break;
		}

		// If origin=gui, add the offset to the button's position
		if (Config.buttonOrigin.equals("gui")) {
			x += ((InventoryScreen) gui).getGuiLeft();
			y += ((InventoryScreen) gui).getGuiTop();
		}

		// Then add the offset as defined in the config file
		x += Config.buttonXPosition;
		y += Config.buttonYPosition;

		return new int[]{x, y};
	}
}
