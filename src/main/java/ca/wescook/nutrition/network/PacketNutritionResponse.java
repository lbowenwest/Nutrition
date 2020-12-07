package ca.wescook.nutrition.network;

import ca.wescook.nutrition.capabilities.INutrientManager;
import ca.wescook.nutrition.gui.ModGuiHandler;
import ca.wescook.nutrition.nutrients.Nutrient;
import ca.wescook.nutrition.nutrients.NutrientList;
import ca.wescook.nutrition.utility.ClientData;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.HashMap;
import java.util.Map;

public class PacketNutritionResponse {
	// Message Subclass
	public static class Message {
		@CapabilityInject(INutrientManager.class)
		private static final Capability<INutrientManager> NUTRITION_CAPABILITY = null;

		// Server vars only
		PlayerEntity serverPlayer;

		// Client vars only
		Map<Nutrient, Float> clientNutrients;

		public Message() {}

		// Message data is passed along from server
		public Message(PlayerEntity player) {
			serverPlayer = player; // Get server player
		}

		// Then serialized into bytes (on server)
		public void toBytes(ByteBuf buf) {
			// Loop through nutrients from server player, and add to buffer
			Map<Nutrient, Float> nutrientData = serverPlayer.getCapability(NUTRITION_CAPABILITY, null).get();
			for (Map.Entry<Nutrient, Float> entry : nutrientData.entrySet()) {
				ByteBufUtils.writeUTF8String(buf, entry.getKey().name); // Write name as identifier
				buf.writeFloat(entry.getValue()); // Write float as value
			}
		}

		// Then deserialized (on the client)
		public static Message fromBytes(PacketBuffer buf) {
			// Loop through buffer stream to build nutrition data
			clientNutrients = new HashMap<>();
			while(buf.isReadable()) {
				String identifier = ByteBufUtils.readUTF8String(buf);
				Float value = buf.readFloat();
				clientNutrients.put(NutrientList.getByName(identifier), value);
			}
		}
	}

	// Message Handler Subclass
	// This is the client's handling of the information
	public static class Handler implements IMessageHandler<Message, IMessage> {
		@Override
		public IMessage onMessage(final Message message, final MessageContext context) {
			FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> {
				// Update local dummy nutrition data
				if (ClientData.localNutrition != null)
					ClientData.localNutrition.set(message.clientNutrients);

				// If Nutrition GUI is open, update GUI
				Screen currentScreen = Minecraft.getInstance().currentScreen;
				if (currentScreen != null && currentScreen.equals(ModGuiHandler.nutritionGui))
					ModGuiHandler.nutritionGui.redrawLabels();
			});

			return null;
		}
	}
}
