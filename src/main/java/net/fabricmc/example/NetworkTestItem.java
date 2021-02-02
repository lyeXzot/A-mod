package net.fabricmc.example;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NetworkTestItem extends Item {

    public NetworkTestItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        user.playSound(SoundEvents.BLOCK_WOOD_BREAK, 1.0F, 1.0F);
        if(world.isClient)
        {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeBytes("this is data for test".getBytes());
            ClientPlayNetworking.send(ModNetworkingConst.TestID, buf);
            System.out.println("[debug]:client test data sent");
//            return super.use(world, user, hand);
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
