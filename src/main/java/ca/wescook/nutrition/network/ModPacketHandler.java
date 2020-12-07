package ca.wescook.nutrition.network;

import ca.wescook.nutrition.Nutrition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.IndexedMessageCodec;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Optional;

public class ModPacketHandler {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(Nutrition.MODID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);

	// Message IDs
	private static int MESSAGE_NUTRITION_REQUEST = 0;
	private static int MESSAGE_NUTRITION_RESPONSE = 1;

	// Register messages on run
	public static void registerMessages() {
		NETWORK_CHANNEL.registerMessage(
				MESSAGE_NUTRITION_REQUEST,
				PacketNutritionRequest.Message.class,
				PacketNutritionRequest.Message::toBytes,
				PacketNutritionRequest.Message::fromBytes,
				PacketNutritionRequest.Handler::onMessage,
				Optional.of(NetworkDirection.PLAY_TO_SERVER)
		);
		NETWORK_CHANNEL.registerMessage(
				MESSAGE_NUTRITION_RESPONSE,
				PacketNutritionResponse.Message.class,
				PacketNutritionResponse.Message::toBytes,
				PacketNutritionResponse.Message::fromBytes,
				PacketNutritionResponse.Handler::onMessage,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT)
		);
//		NETWORK_CHANNEL.registerMessage(PacketNutritionRequest.Handler.class, PacketNutritionRequest.Message.class, MESSAGE_NUTRITION_REQUEST, Side.SERVER);
//		NETWORK_CHANNEL.registerMessage(PacketNutritionResponse.Handler.class, PacketNutritionResponse.Message.class, MESSAGE_NUTRITION_RESPONSE, MixinEnvironment.Side.CLIENT);
	}
}
