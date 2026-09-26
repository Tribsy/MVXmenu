package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record KeybindAckPayload(String keybindId, boolean success) implements CustomPayload {

    public static final CustomPayload.Id<KeybindAckPayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "keybind_ack"));
    public static final PacketCodec<PacketByteBuf, KeybindAckPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, KeybindAckPayload::keybindId,
            PacketCodecs.BOOLEAN, KeybindAckPayload::success,
            KeybindAckPayload::new
    );

    @Override
    public CustomPayload.Id<KeybindAckPayload> getId() {
        return ID;
    }
}