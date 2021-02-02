package net.fabricmc.example;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ExampleMod implements ModInitializer {

	public static final NetworkTestItem test_item = new NetworkTestItem(new Item.Settings().group(ItemGroup.MISC));

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("modid", "test_item"), test_item);

		ClientPlayNetworking.registerGlobalReceiver(ModNetworkingConst.TestID,
				(minecraftClient, clientPlayNetworkHandler, packetByteBuf, packetSender) -> {
					//客户端收到数据包执行 预览
					byte[] data = packetByteBuf.array();
					minecraftClient.execute(()->{
						System.out.println("client have received" + new String(data));
					});
		});

		ServerPlayNetworking.registerGlobalReceiver(
				ModNetworkingConst.TestID,
				(minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
					//服务器收到数据包执行 转发
					for(ServerPlayerEntity player : PlayerLookup.all(minecraftServer)) {
//						if(player.getEntityId() == serverPlayerEntity.getEntityId())
//							continue;  //为发包人
						System.out.println("data preview " + new String(packetByteBuf.array()));
						ServerPlayNetworking.send(player, ModNetworkingConst.TestID, packetByteBuf);
					}
		});
	}


}
