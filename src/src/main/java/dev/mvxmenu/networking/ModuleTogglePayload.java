package dev.mvxmenu.networking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ModuleTogglePayload(String moduleId, boolean enabled) implements CustomPayload {

    public static final CustomPayload.Id<ModuleTogglePayload> ID = new CustomPayload.Id<>(Identifier.of("mvxmenu", "module_toggle"));
    public static final PacketCodec<PacketByteBuf, ModuleTogglePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING, ModuleTogglePayload::moduleId,
            PacketCodecs.BOOLEAN, ModuleTogglePayload::enabled,
            ModuleTogglePayload::new
    );

    @Override
    public CustomPayload.Id<ModuleTogglePayload> getId() {
        return ID;
    }
}