package dev.mvxmenu;

import dev.mvxmenu.ui.screen.MvxmenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MvxmenuScreenWrapper extends Screen {

    private final MvxmenuScreen delegate;

    public MvxmenuScreenWrapper(MvxmenuScreen delegate) {
        super(Text.translatable("screen.mvxmenu.title"));
        this.delegate = delegate;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        delegate.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return delegate.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // Delegate doesn't have mouseReleased, but widgets might need it
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            close();
            return true;
        }
        return delegate.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return delegate.charTyped(codePoint, modifiers);
    }

    @Override
    public void close() {
        this.client.setScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}