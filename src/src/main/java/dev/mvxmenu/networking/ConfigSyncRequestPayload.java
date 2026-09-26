package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncRequestPayload(String configJson) implements CustomPayload {

    public static final CustomPayload.Id<ConfigSyncRequestPayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "config_sync_request"));
    public static final PacketCodec<PacketByteBuf, ConfigSyncRequestPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ConfigSyncRequestPayload::configJson,
            ConfigSyncRequestPayload::new
    );

    @Override
    public CustomPayload.Id<ConfigSyncRequestPayload> getId() {
        return ID;
    }
}