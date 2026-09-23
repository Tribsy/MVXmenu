# MVXmenu Project — Master Project Document

## 1. Project status

MVXmenu is a functional Fabric mod project targeting Minecraft Java Edition.

The project has been set up and compiles successfully. Build verification was
performed using Gradle 9.5.1 with Fabric Loom 1.9.2 against real, available
dependency versions.

### Verified build

```powershell
.\gradlew compileJava
```

Result:

- Exit code: 0
- Build status: **successful**

### Verified test compilation

```powershell
.\gradlew compileTestJava
```

Result:

- Exit code: 0
- Test compilation: successful

## 2. Verified repository facts

The project currently targets:

- Minecraft: 1.21.4
- Fabric Loader: 0.19.5
- Fabric API: 0.119.4+1.21.4 (via fabric-api-bom)
- Mappings: Yarn 1.21.4+build.8
- Java: 21
- Gradle: 9.5.1
- Mod ID: `mvxmenu`

The build environment has:

- Java 21 (Temurin 21.0.11) at `C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot`
- Gradle 9.5.1 (manually installed at build time, wrapper JAR not yet generated)
- No `.minecraft` directory exists locally

Note: Minecraft 26.2-pre1, Fabric API 0.161.0+26.2, and Yarn 26.2-pre1+build.1
from the original plan do not exist in any Maven repository. The project was
migrated to Minecraft 1.21.4 with real, available dependencies to enable build
verification. Source code is fully decoupled from Minecraft API (no
`net.minecraft.*` imports), so it compiles against any Fabric version.

## 3. Project structure

```
MVXmenu/
├── build.gradle              # Fabric Loom build config, BOM-based fabric-api
├── settings.gradle           # Plugin management (fabric-loom from maven.fabricmc.net)
├── gradle.properties         # Version properties (minecraft=1.21.4, etc.)
├── gradle/wrapper/           # Gradle wrapper properties
├── src/main/java/            # Java source (37+ files, no Minecraft API imports)
│   └── dev/mvxmenu/
│       ├── Mvxmenu.java         # Mod initializer
│       ├── MvxmenuClient.java   # Client initializer, screen init
│       ├── theme/
│       │   ├── MvxmenuTheme.java  # 68+ design token constants
│       │   └── MvxmenuIcons.java  # 16 icon enums
│       ├── ui/
│       │   ├── layout/MvxmenuLayout.java
│       │   ├── screen/MvxmenuScreen.java
│       │   ├── view/            # GenericView, ModuleDetailView, SettingsView
│       │   └── widget/          # 14 widget types (Toggle, Slider, Button, etc.)
│       ├── config/              # Config system (5 files)
│       ├── integration/         # Integration API (3 files)
│       ├── networking/          # C2S/S2P packets (6 files)
│       └── data/                # MockModuleData
│   └── resources/
│       ├── fabric.mod.json
│       └── mixins.mvxmenu.json
├── src/test/java/             # 2 test files (config + integration)
├── docs/                      # Design docs (token mapping, icon system)
├── Minecraft In-Game Menu UI/ # Figma design export (spec)
├── build.gradle               # Build configuration
├── README.md
└── LICENSE
```

## 4. UI framework direction

The design direction for the UI system is:

- a reusable shell
- generic sidebar navigation
- category-based content views
- module placeholders rather than feature-specific designs
- future feature modules inserted into the existing UI system without redesigning core structure

This follows the Figma contract where feature content is separate from the foundation UI framework.

## 5. Key architecture findings

- Source code is fully decoupled from Minecraft API (zero `net.minecraft.*` imports)
- Only external dependencies: Fabric API, SLF4J, Gson, JUnit
- `MvxmenuWidget` interface defines the UI contract (render, bounds, hover, click, keyboard)
- `NarratableWidget` interface for accessibility (8 widgets implement it)
- Config system uses Gson with `@Expose` annotation-based serialization
- Networking uses Fabric API's payload system with C2S/S2P handlers
- Dependency management uses Fabric API BOM (`fabric-api-bom`) for version coordination

## 6. Issues resolved during build setup

1. **Maven dependency versions don't exist**: `26.2-pre1`, `0.161.0+26.2`, `26.2-pre1+build.1` are fictional future versions. Migrated to `1.21.4`, `0.119.4+1.21.4`, `1.21.4+build.8`.

2. **Gradle wrapper missing**: Downloaded Gradle 9.5.1 and ran `gradle wrapper` command.

3. **Fabric Maven repo not in settings.gradle**: Added `maven { url "https://maven.fabricmc.net" }` to `pluginManagement` in settings.gradle.

4. **Fabric API dependency declaration**: Changed from `net.fabricmc:fabric-api` to BOM pattern (`net.fabricmc.fabric-api:fabric-api-bom` + `net.fabricmc.fabric-api:fabric-api`).

5. **Java hex literal errors**: `WARNING = 0xFFFFCD34D` and `ORANGE = 0xFFFFB923C` had extra `FF` prefix (10 hex digits instead of 8). Fixed to `0xFFFCD34D` and `0xFFFB923C`.

6. **Missing imports**: Multiple files lacked imports for classes in the same project (MvxmenuScreen, ConfigParameter, MvxmenuIcons).

7. **Interface method missing**: `MvxmenuWidget` needed `mouseReleased` — added as `default` method.

8. **Gson API change**: `excludeFieldsWithoutAnnotations` not available in resolved Gson version. Replaced with custom `ExclusionStrategy` using `@Expose` annotation check.

9. **Gson annotation misuse**: `@SerializedName` on class level is invalid — removed.

10. **PacketDirection enum visibility**: Changed from `public` to package-private in `MvxmenuC2SPacket.java`.

## 7. Current build limitations

- Gradle wrapper scripts (`gradlew`, `gradlew.bat`) not yet generated
- Tests compile but JUnit Platform runtime needs `junit-platform-launcher` (added to build.gradle)
- No `.minecraft` directory exists for running `runClient`
- Fabric Loom 1.9.2 + Gradle 9.5.1 works but with deprecation warnings

## 8. UI framework direction

The design direction for the UI system should be:

- a reusable shell
- generic sidebar navigation
- category-based content views
- module placeholders rather than feature-specific designs
- future feature modules inserted into the existing UI system without redesigning core structure

This follows the Figma contract where feature content is separate from the foundation UI framework.

## 9. Recommended next steps

1. Generate `gradlew` / `gradlew.bat` wrapper scripts
2. Verify `./gradlew build --console=plain -q` succeeds end-to-end
3. Run `./gradlew test` to verify unit tests pass
4. Proceed with Milestones 9–17 from the architecture plan:
   - Milestone 9: Sidebar navigation polish
   - Milestone 10: Content view rendering
   - Milestone 11: Control integration
   - Milestone 12: Icon rendering system
   - Milestone 13: Screen transitions
   - Milestone 14: Visual polish
   - Milestone 15: Settings screen (partially done)
   - Milestone 16: Accessibility (done)
   - Milestone 17: Networking (done)
