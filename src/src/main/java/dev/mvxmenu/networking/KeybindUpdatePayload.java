package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record KeybindUpdatePayload(String keybindId, String key) implements CustomPayload {

    public static final CustomPayload.Id<KeybindUpdatePayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "keybind_update"));
    public static final PacketCodec<PacketByteBuf, KeybindUpdatePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, KeybindUpdatePayload::keybindId,
            PacketCodecs.STRING, KeybindUpdatePayload::key,
            KeybindUpdatePayload::new
    );

    @Override
    public CustomPayload.Id<KeybindUpdatePayload> getId() {
        return ID;
    }
}