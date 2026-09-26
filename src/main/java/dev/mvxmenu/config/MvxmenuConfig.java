package dev.mvxmenu.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.nbt.NbtCompound;

public class MvxmenuConfig {

    @Expose
    @SerializedName("version")
    private String version = "1.0.0";

    @Expose
    @SerializedName("gui_scale")
    private int guiScale = 3;

    @Expose
    @SerializedName("theme_accent")
    private String themeAccent = "DEFAULT (PURPLE)";

    @Expose
    @SerializedName("blur_effects")
    private boolean blurEffects = true;

    @Expose
    @SerializedName("scanline_overlay")
    private boolean scanlineOverlay = false;

    @Expose
    @SerializedName("tick_rate_limit")
    private int tickRateLimit = 100;

    @Expose
    @SerializedName("render_backend")
    private String renderBackend = "AUTO-DETECT";

    @Expose
    @SerializedName("telemetry")
    private boolean telemetry = true;

    @Expose
    @SerializedName("high_contrast")
    private boolean highContrast = false;

    @Expose
    @SerializedName("bg_opacity")
    private int bgOpacity = 90;

    @Expose
    @SerializedName("custom_accent")
    private int customAccent = 0xFF8B5CF6;

    @Expose
    @SerializedName("use_custom_accent")
    private boolean useCustomAccent = false;

    @Expose
    @SerializedName("animation_speed")
    private int animationSpeed = 100;

    @Expose
    @SerializedName("panel_rounding")
    private int panelRounding = 12;

    public MvxmenuConfig() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getGuiScale() {
        return guiScale;
    }

    public void setGuiScale(int guiScale) {
        this.guiScale = guiScale;
    }

    public String getThemeAccent() {
        return themeAccent;
    }

    public void setThemeAccent(String themeAccent) {
        this.themeAccent = themeAccent;
    }

    public boolean isBlurEffects() {
        return blurEffects;
    }

    public void setBlurEffects(boolean blurEffects) {
        this.blurEffects = blurEffects;
    }

    public boolean isScanlineOverlay() {
        return scanlineOverlay;
    }

    public void setScanlineOverlay(boolean scanlineOverlay) {
        this.scanlineOverlay = scanlineOverlay;
    }

    public int getTickRateLimit() {
        return tickRateLimit;
    }

    public void setTickRateLimit(int tickRateLimit) {
        this.tickRateLimit = tickRateLimit;
    }

    public String getRenderBackend() {
        return renderBackend;
    }

    public void setRenderBackend(String renderBackend) {
        this.renderBackend = renderBackend;
    }

    public boolean isTelemetry() {
        return telemetry;
    }

    public void setTelemetry(boolean telemetry) {
        this.telemetry = telemetry;
    }

    public boolean isHighContrast() {
        return highContrast;
    }

    public void setHighContrast(boolean highContrast) {
        this.highContrast = highContrast;
    }

    public int getBgOpacity() {
        return bgOpacity;
    }

    public void setBgOpacity(int bgOpacity) {
        this.bgOpacity = Math.max(0, Math.min(100, bgOpacity));
    }

    public int getCustomAccent() {
        return customAccent;
    }

    public void setCustomAccent(int customAccent) {
        this.customAccent = customAccent;
    }

    public boolean isUseCustomAccent() {
        return useCustomAccent;
    }

    public void setUseCustomAccent(boolean useCustomAccent) {
        this.useCustomAccent = useCustomAccent;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(int animationSpeed) {
        this.animationSpeed = Math.max(50, Math.min(200, animationSpeed));
    }

    public int getPanelRounding() {
        return panelRounding;
    }

    public void setPanelRounding(int panelRounding) {
        this.panelRounding = Math.max(0, Math.min(8, panelRounding));
    }
}