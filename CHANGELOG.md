# MVXmenu Changelog

All notable changes to this project will be documented in this file.

## [1.0.0] - 2026-09-22

### Added
- **Core GUI Framework**: Custom extensible in-game menu system for Minecraft 1.21.4 (Fabric)
- **Sidebar Navigation**: 6 category buttons + Settings button with icons, keyboard navigation (arrows, ESC, Enter)
- **Content Views**: Generic module grid, Module Detail panel, Settings view with view switching
- **Primitive Controls**: Button (5 variants), Toggle, Slider, Dropdown, Checkbox, Keybind
- **Module Cards**: Card layout with name, description, status indicator, toggle
- **Icon System**: 16 geometric icons (Shield, Crosshair, Eye, Zap, Key, Sliders, Layers, Terminal, Radar, Lock, Clock, CPU, Map, Grid, Bell, Database)
- **Advanced Controls**: IconButton, StatusBadge (6 types), Tooltips, FocusRing for accessibility
- **Settings Screen**: GUI Scale, Theme Accent, Blur Effects, Scanline Overlay, High Contrast, Tick Rate, Render Backend, Telemetry, Background Opacity, Custom Accent, Animation Speed, Panel Rounding, Export/Import/Reset
- **Accessibility**: NarratableWidget on 10 widgets, FocusRing rendering, full keyboard navigation, high contrast mode
- **Networking**: 6 CustomPayload types (C2S: ModuleToggle, ConfigSyncRequest, KeybindUpdate; S2C: ModuleStateSync, ConfigSyncResponse, KeybindAck)
- **Module System**: Abstract Module base, ModuleRegistry (singleton), 7 Setting types (Boolean, Integer, Double, String, Enum, Color, Keybind)
- **11 Implemented Modules**: Sprint, Flight, AutoEat, AutoTool, NoFall, Timer, Scaffold, Freecam, ESP, XRay, KillAura
- **Server-side Config**: Whitelist/Blacklist, restricted modules, per-setting max values, admin commands
- **Admin Commands**: `/mvxmenu whitelist`, `/mvxmenu blacklist`, `/mvxmenu setmax`, `/mvxmenu reload`, `/mvxmenu status`
- **Performance**: RenderCache (entity caching), BatchRenderer (vertex batching), MemoryMonitor (GC tracking, stats)
- **CI/CD**: GitHub Actions workflow (build, test, release, Modrinth/CurseForge publish)

### Technical
- Java 21, Fabric Loader 0.19.5, Fabric API 0.119.4, Loom 1.9.2, Gradle 9.5.1
- Yarn mappings 1.21.4+build.8
- Mod ID: `mvxmenu`, Package: `dev.mvxmenu`

## [Unreleased]

### Planned
- Module settings persistence per-world
- Search suggestions/history
- Color picker widget for custom accent
- Theme presets (Dark, Light, High Contrast, Custom)
- Module dependency system
- In-game config editor for server admins
- Discord Rich Presence integration
- Mod update notifications