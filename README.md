# MVXmenu

Custom extensible in-game GUI mod for Minecraft Java Edition 1.21.4 (Fabric).

## Features

### 🎮 In-Game Menu System
- **Sidebar Navigation**: 6 category tabs + Settings with keyboard navigation
- **Real-time Search**: Filter modules by name/description instantly
- **Three View Modes**: Module grid, Module detail with settings, Settings panel
- **Responsive Layout**: 1200x800 base resolution, scales with GUI Scale setting

### 🎨 Theme & Customization
- **8 Theme Accents**: Default Green, Blue, Red, Purple, Orange, Yellow, Cyan, Pink
- **Custom Accent Color**: Full ARGB color picker via dropdown presets
- **Background Opacity**: 0-100% transparency control
- **Animation Speed**: 50-200% transition speed
- **Panel Rounding**: 0-8px corner radius
- **High Contrast Mode**: WCAG AA compliant accessibility
- **Scanline Overlay**: Retro CRT effect
- **Blur Effects**: Frosted glass backgrounds

### ⚙️ Module System
- **11 Built-in Modules** across 7 categories:
  - **Combat**: KillAura (target players/mobs, range, wall check)
  - **Movement**: Sprint (omni-directional), Flight (creative-style), Scaffold (auto-bridge)
  - **Player**: AutoEat (smart food selection), AutoTool (best tool/sword), NoFall
  - **Render**: ESP (player/mob/chest highlights), XRay (ore/chest detection)
  - **Exploit**: Timer (game speed), Freecam (detached camera)
  - **Misc**: (extensible)

- **Per-Module Settings**: Boolean, Integer, Double, String, Enum, Color, Keybind
- **Server-Side Enforcement**: Whitelist/blacklist, per-setting max values
- **Config Persistence**: Auto-saves to `mvxmenu.json` / `mvxmenu-server.json`

### ♿ Accessibility
- Screen reader support via `NarratableWidget`
- Focus indicators on all interactive elements
- Full keyboard navigation (Tab, Arrows, Enter, ESC)
- High contrast mode with enhanced borders
- Tooltips on hover

### 🌐 Networking & Multiplayer
- Client↔Server config sync on join
- Module state synchronization
- Admin commands for server management
- Fabric 1.21.4 CustomPayload API

## Installation

### Requirements
- Minecraft 1.21.4
- Fabric Loader 0.19.5+
- Fabric API 0.119.4+

### Building
```powershell
# Build mod JAR
./gradlew build --no-daemon

# Run development client
./gradlew runClient --no-daemon

# Run tests
./gradlew test --no-daemon
```

### Output
Built JARs in `build/libs/`:
- `mvxmenu-1.0.0.jar` - Development build
- `mvxmenu-1.0.0-all.jar` - Remapped release build

## Usage

### Opening the Menu
Press **R** (default) in-game to open/close the menu.

### Navigation
| Key | Action |
|-----|--------|
| ← / → | Switch categories |
| ↑ / ↓ | Navigate module list (when focused) |
| Enter | Open module detail / toggle |
| ESC | Close view / go back |
| Type | Search modules (sidebar focused) |

### Admin Commands (Server)
```
/mvxmenu whitelist add <module>     # Allow module
/mvxmenu whitelist remove <module>  # Remove from whitelist
/mvxmenu whitelist list             # Show whitelist
/mvxmenu blacklist add <module>     # Block module
/mvxmenu blacklist remove <module>  # Remove from blacklist
/mvxmenu blacklist list             # Show blacklist
/mvxmenu setmax <module> <setting> <value>  # Set max value
/mvxmenu reload                     # Reload server config
/mvxmenu status                     # Show server status
```

### Configuration Files
- Client: `config/mvxmenu.json`
- Server: `config/mvxmenu-server.json`

## Development

### Project Structure
```
src/main/java/dev/mvxmenu/
├── Mvxmenu.java                 # Server entry point
├── MvxmenuClient.java           # Client entry point
├── command/                     # Server commands
├── config/                      # Config (client + server)
├── module/                      # Module system
│   ├── Module.java              # Abstract base
│   ├── ModuleRegistry.java      # Singleton registry
│   ├── ModuleServerManager.java # Server enforcement
│   ├── Setting.java             # Setting base + 7 types
│   └── impl/                    # 11 module implementations
├── networking/                  # CustomPayload networking
├── performance/                 # Render cache, batch renderer, memory monitor
├── ui/
│   ├── screen/                  # Main screen + wrapper
│   ├── layout/                  # Coordinate system
│   ├── view/                    # Generic, Detail, Settings views
│   ├── widget/                  # 13 widget types
│   └── icon/                    # 16 geometric icons
└── theme/                       # Design tokens + icon renderer
```

### Adding a New Module
```java
public class MyModule extends Module {
    private final BooleanSetting mySetting;

    public MyModule() {
        super("my_module", "My Module", "Description", Category.PLAYER);
        this.mySetting = registerSetting(new BooleanSetting("enabled", "Enabled", "Toggle feature", true));
    }

    @Override
    public void onTick() {
        if (mySetting.getValue()) {
            // Feature logic here
        }
    }
}

// Register in ModuleLoader.loadAll()
registry.register(new MyModule());
```

### Adding a Setting Type
Extend `Setting<T>` and implement `validate()` and `getDisplayValue()`.

## Design System

### Design Tokens (`theme/MvxmenuTheme.java`)
- **Colors**: 68 ARGB constants (BG_0-3, TX_0-3, AC, BD_0-3, Semantic, Status)
- **Typography**: 4 scales (Display, Heading, Body, Caption) with weights
- **Spacing**: 5 tokens (4, 8, 16, 24, 32px)
- **Border Radius**: 4 tokens (0, 2, 4, 8px)
- **Shadows**: 3 elevations
- **Motion**: 4 durations (80-200ms)

### Icons (`theme/MvxmenuIcons.java`)
16 geometric icons rendered via `DrawContext.fill()`:
SHIELD, CROSSHAIR, EYE, ZAP, KEY, SLIDERS, LAYERS, TERMINAL, RADAR, LOCK, CLOCK, CPU, MAP, GRID, BELL, DATABASE

## Performance

- **RenderCache**: Entity position caching (256 entries, 20-tick TTL)
- **BatchRenderer**: Vertex batching for box rendering (16K vertices)
- **MemoryMonitor**: Heap tracking, automatic GC at 85% usage
- **PerformanceManager**: Integrated into client tick loop

## CI/CD

GitHub Actions workflow (`.github/workflows/build.yml`):
- Build on push/PR
- Test on build success
- Release on version tag (v*)
- Auto-publish to Modrinth & CurseForge

## License

MIT License - see LICENSE file

## Credits

- Fabric Loader & API teams
- Minecraft by Mojang Studios
- Figma design specification (canonical visual reference)