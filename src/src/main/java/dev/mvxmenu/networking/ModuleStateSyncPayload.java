package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModuleStateSyncPayload(String moduleId, boolean enabled, boolean disabled) implements CustomPayload {

    public static final CustomPayload.Id<ModuleStateSyncPayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "module_state_sync"));
    public static final PacketCodec<PacketByteBuf, ModuleStateSyncPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ModuleStateSyncPayload::moduleId,
            PacketCodecs.BOOLEAN, ModuleStateSyncPayload::enabled,
            PacketCodecs.BOOLEAN, ModuleStateSyncPayload::disabled,
            ModuleStateSyncPayload::new
    );

    @Override
    public CustomPayload.Id<ModuleStateSyncPayload> getId() {
        return ID;
    }
}