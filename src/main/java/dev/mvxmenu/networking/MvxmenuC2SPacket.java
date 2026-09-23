package dev.mvxmenu.networking;

enum PacketDirection {
    CLIENT_TO_SERVER,
    SERVER_TO_CLIENT
}

public class MvxmenuC2SPacket {

    private final String channel;
    private final byte[] payload;

    public MvxmenuC2SPacket(String channel, byte[] payload) {
        this.channel = channel;
        this.payload = payload;
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getPayload() {
        return payload;
    }
}
