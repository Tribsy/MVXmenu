package dev.mvxmenu.networking;

public class MvxmenuS2CPacket {

    private final String channel;
    private final byte[] payload;

    public MvxmenuS2CPacket(String channel, byte[] payload) {
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
