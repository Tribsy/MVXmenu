package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncResponsePayload(String configJson) implements CustomPayload {

    public static final CustomPayload.Id<ConfigSyncResponsePayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "config_sync_response"));
    public static final PacketCodec<PacketByteBuf, ConfigSyncResponsePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ConfigSyncResponsePayload::configJson,
            ConfigSyncResponsePayload::new
    );

    @Override
    public CustomPayload.Id<ConfigSyncResponsePayload> getId() {
        return ID;
    }
}