package ca.wescook.nutrition.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketNutritionRequest {
	// Message Subclass
	// Empty message just to request information
	public static class Message {
		public Message() {}

		public static void toBytes(Message msg, PacketBuffer buf) {}

		public static Message fromBytes(PacketBuffer buf) {
			return new Message();
		}
	}

	// Message Handler Subclass
	// Handled on server
	public static class Handler  {
		public static void onMessage(final Message message, Supplier<NetworkEvent.Context> contextSupplier) {
//			FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() -> {
//				// Return message
//				ServerPlayerEntity player = context.getServerHandler().player; // Get Player on server
//				ModPacketHandler.NETWORK_CHANNEL.sendTo(new PacketNutritionResponse.Message(player), player);
//			});
		}
	}
}
