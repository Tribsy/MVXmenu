package dev.mvxmenu.networking;

import java.util.HashMap;
import java.util.Map;

public class MvxmenuPayloadRegistry {

    private static final MvxmenuPayloadRegistry INSTANCE = new MvxmenuPayloadRegistry();

    public static MvxmenuPayloadRegistry get() {
        return INSTANCE;
    }

    private final Map<String, PacketDirection> channels = new HashMap<>();

    private MvxmenuPayloadRegistry() {
    }

    public void register(String channel, PacketDirection direction) {
        channels.put(channel, direction);
    }

    public PacketDirection getDirection(String channel) {
        return channels.get(channel);
    }

    public boolean isRegistered(String channel) {
        return channels.containsKey(channel);
    }
}
