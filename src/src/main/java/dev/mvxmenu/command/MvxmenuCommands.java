 package dev.mvxmenu.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.mvxmenu.Mvxmenu;
import dev.mvxmenu.config.MvxmenuServerConfig;
import dev.mvxmenu.config.MvxmenuServerConfigSerializer;
import dev.mvxmenu.module.ModuleServerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MvxmenuCommands {

    private static ModuleServerManager moduleServerManager;
    private static MvxmenuServerConfigSerializer configSerializer;

    public static void initialize(ModuleServerManager manager, MvxmenuServerConfigSerializer serializer) {
        moduleServerManager = manager;
        configSerializer = serializer;
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("mvxmenu")
                .requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("whitelist")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("module", StringArgumentType.greedyString())
                                        .executes(ctx -> whitelistAdd(ctx, StringArgumentType.getString(ctx, "module")))))
                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("module", StringArgumentType.greedyString())
                                        .executes(ctx -> whitelistRemove(ctx, StringArgumentType.getString(ctx, "module")))))
                        .then(CommandManager.literal("list")
                                .executes(ctx -> whitelistList(ctx))))
                .then(CommandManager.literal("blacklist")
                        .then(CommandManager.literal("add")
                                .then(CommandManager.argument("module", StringArgumentType.greedyString())
                                        .executes(ctx -> blacklistAdd(ctx, StringArgumentType.getString(ctx, "module")))))
                        .then(CommandManager.literal("remove")
                                .then(CommandManager.argument("module", StringArgumentType.greedyString())
                                        .executes(ctx -> blacklistRemove(ctx, StringArgumentType.getString(ctx, "module")))))
                        .then(CommandManager.literal("list")
                                .executes(ctx -> blacklistList(ctx))))
                .then(CommandManager.literal("setmax")
                        .then(CommandManager.argument("module", StringArgumentType.greedyString())
                                .then(CommandManager.argument("setting", StringArgumentType.greedyString())
                                        .then(CommandManager.argument("value", IntegerArgumentType.integer())
                                                .executes(ctx -> setMax(ctx,
                                                        StringArgumentType.getString(ctx, "module"),
                                                        StringArgumentType.getString(ctx, "setting"),
                                                        IntegerArgumentType.getInteger(ctx, "value")))))))
                .then(CommandManager.literal("reload")
                        .executes(ctx -> reload(ctx)))
                .then(CommandManager.literal("status")
                        .executes(ctx -> status(ctx)))
        );
    }

    private static int whitelistAdd(CommandContext<ServerCommandSource> ctx, String module) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        moduleServerManager.addToWhitelist(player, module);
        configSerializer.save(moduleServerManager.getConfig());
        return 1;
    }

    private static int whitelistRemove(CommandContext<ServerCommandSource> ctx, String module) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        moduleServerManager.removeFromWhitelist(player, module);
        configSerializer.save(moduleServerManager.getConfig());
        return 1;
    }

    private static int whitelistList(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        MvxmenuServerConfig config = moduleServerManager.getConfig();

        source.sendFeedback(() -> Text.literal("§b=== MVXmenu Whitelist ===").formatted(Formatting.AQUA), false);
        for (String module : config.getWhitelist()) {
            source.sendFeedback(() -> Text.literal("  §a" + module), false);
        }
        source.sendFeedback(() -> Text.literal("§bWhitelist enabled: §f" + config.isWhitelistEnabled()), false);
        return 1;
    }

    private static int blacklistAdd(CommandContext<ServerCommandSource> ctx, String module) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        moduleServerManager.addToBlacklist(player, module);
        configSerializer.save(moduleServerManager.getConfig());
        return 1;
    }

    private static int blacklistRemove(CommandContext<ServerCommandSource> ctx, String module) {
        ServerPlayerEntity player = ctx.getSource().getPlayer();
        if (player == null) return 0;

        moduleServerManager.removeFromBlacklist(player, module);
        configSerializer.save(moduleServerManager.getConfig());
        return 1;
    }

    private static int blacklistList(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        MvxmenuServerConfig config = moduleServerManager.getConfig();

        source.sendFeedback(() -> Text.literal("§b=== MVXmenu Blacklist ===").formatted(Formatting.RED), false);
        for (String module : config.getBlacklist()) {
            source.sendFeedback(() -> Text.literal("  §c" + module), false);
        }
        return 1;
    }

    private static int setMax(CommandContext<ServerCommandSource> ctx, String module, String setting, int value) {
        ServerCommandSource source = ctx.getSource();
        moduleServerManager.setMaxValue(module, setting, value);
        configSerializer.save(moduleServerManager.getConfig());
        source.sendFeedback(() -> Text.literal("§aSet max for " + module + "." + setting + " to " + value), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        configSerializer.load();
        source.sendFeedback(() -> Text.literal("§aReloaded MVXmenu server config"), false);
        return 1;
    }

    private static int status(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        MvxmenuServerConfig config = moduleServerManager.getConfig();

        source.sendFeedback(() -> Text.literal("§b=== MVXmenu Server Status ==="), false);
        source.sendFeedback(() -> Text.literal("§fEnforce Config: §a" + config.isEnforceServerConfig()), false);
        source.sendFeedback(() -> Text.literal("§fWhitelist Enabled: §a" + config.isWhitelistEnabled() + " §7(" + config.getWhitelist().size() + " modules)"), false);
        source.sendFeedback(() -> Text.literal("§fBlacklist: §a" + config.getBlacklist().size() + " modules"), false);
        source.sendFeedback(() -> Text.literal("§fRestricted: §a" + config.getRestrictedModules().size() + " modules"), false);
        source.sendFeedback(() -> Text.literal("§fMax Sprint Speed: §a" + config.getMaxSprintSpeed()), false);
        source.sendFeedback(() -> Text.literal("§fMax Fly Speed: §a" + config.getMaxFlySpeed()), false);
        source.sendFeedback(() -> Text.literal("§fMax Kill Aura Range: §a" + config.getMaxKillAuraRange()), false);
        source.sendFeedback(() -> Text.literal("§fMax Timer Speed: §a" + config.getMaxTimerSpeed()), false);
        return 1;
    }
}