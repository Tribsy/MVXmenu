package dev.mvxmenu.config;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MvxmenuServerConfig {

    @Expose
    @SerializedName("version")
    private String version = "1.0.0";

    @Expose
    @SerializedName("enforce_server_config")
    private boolean enforceServerConfig = true;

    @Expose
    @SerializedName("whitelist_enabled")
    private boolean whitelistEnabled = false;

    @Expose
    @SerializedName("whitelist")
    private List<String> whitelist = new ArrayList<>();

    @Expose
    @SerializedName("blacklist")
    private List<String> blacklist = new ArrayList<>();

    @Expose
    @SerializedName("restricted_modules")
    private List<String> restrictedModules = new ArrayList<>();

    @Expose
    @SerializedName("max_sprint_speed")
    private int maxSprintSpeed = 130;

    @Expose
    @SerializedName("max_fly_speed")
    private int maxFlySpeed = 150;

    @Expose
    @SerializedName("max_kill_aura_range")
    private int maxKillAuraRange = 4;

    @Expose
    @SerializedName("max_timer_speed")
    private int maxTimerSpeed = 100;

    @Expose
    @SerializedName("require_permission_for_admin")
    private boolean requirePermissionForAdmin = true;

    @Expose
    @SerializedName("log_module_toggles")
    private boolean logModuleToggles = true;

    public MvxmenuServerConfig() {
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnforceServerConfig() {
        return enforceServerConfig;
    }

    public void setEnforceServerConfig(boolean enforceServerConfig) {
        this.enforceServerConfig = enforceServerConfig;
    }

    public boolean isWhitelistEnabled() {
        return whitelistEnabled;
    }

    public void setWhitelistEnabled(boolean whitelistEnabled) {
        this.whitelistEnabled = whitelistEnabled;
    }

    public List<String> getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(List<String> whitelist) {
        this.whitelist = whitelist != null ? whitelist : new ArrayList<>();
    }

    public void addToWhitelist(String moduleId) {
        if (!whitelist.contains(moduleId)) {
            whitelist.add(moduleId);
        }
    }

    public void removeFromWhitelist(String moduleId) {
        whitelist.remove(moduleId);
    }

    public List<String> getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(List<String> blacklist) {
        this.blacklist = blacklist != null ? blacklist : new ArrayList<>();
    }

    public void addToBlacklist(String moduleId) {
        if (!blacklist.contains(moduleId)) {
            blacklist.add(moduleId);
        }
    }

    public void removeFromBlacklist(String moduleId) {
        blacklist.remove(moduleId);
    }

    public List<String> getRestrictedModules() {
        return restrictedModules;
    }

    public void setRestrictedModules(List<String> restrictedModules) {
        this.restrictedModules = restrictedModules != null ? restrictedModules : new ArrayList<>();
    }

    public int getMaxSprintSpeed() {
        return maxSprintSpeed;
    }

    public void setMaxSprintSpeed(int maxSprintSpeed) {
        this.maxSprintSpeed = Math.max(100, maxSprintSpeed);
    }

    public int getMaxFlySpeed() {
        return maxFlySpeed;
    }

    public void setMaxFlySpeed(int maxFlySpeed) {
        this.maxFlySpeed = Math.max(50, maxFlySpeed);
    }

    public int getMaxKillAuraRange() {
        return maxKillAuraRange;
    }

    public void setMaxKillAuraRange(int maxKillAuraRange) {
        this.maxKillAuraRange = Math.max(1, maxKillAuraRange);
    }

    public int getMaxTimerSpeed() {
        return maxTimerSpeed;
    }

    public void setMaxTimerSpeed(int maxTimerSpeed) {
        this.maxTimerSpeed = Math.max(10, maxTimerSpeed);
    }

    public boolean isRequirePermissionForAdmin() {
        return requirePermissionForAdmin;
    }

    public void setRequirePermissionForAdmin(boolean requirePermissionForAdmin) {
        this.requirePermissionForAdmin = requirePermissionForAdmin;
    }

    public boolean isLogModuleToggles() {
        return logModuleToggles;
    }

    public void setLogModuleToggles(boolean logModuleToggles) {
        this.logModuleToggles = logModuleToggles;
    }

    public boolean isModuleAllowed(String moduleId) {
        if (blacklist.contains(moduleId)) {
            return false;
        }
        if (whitelistEnabled && !whitelist.contains(moduleId)) {
            return false;
        }
        return true;
    }

    public boolean isModuleRestricted(String moduleId) {
        return restrictedModules.contains(moduleId);
    }

    public int getMaxValueForModule(String moduleId, String settingName) {
        return switch (moduleId + ":" + settingName) {
            case "sprint:sprint_speed" -> maxSprintSpeed;
            case "flight:fly_speed" -> maxFlySpeed;
            case "kill_aura:range" -> maxKillAuraRange;
            case "timer:timer_speed" -> maxTimerSpeed;
            default -> Integer.MAX_VALUE;
        };
    }
}