# MVXmenu Current Architecture Plan

> Goal: Build and stabilize the real MVXmenu mod for the working Fabric 1.21.4 toolchain, while preserving the modular screen structure and adding features in small, testable increments.
> Scope: This plan is grounded in the current repo and the build we are actively validating, not the earlier speculative 26.2-only design direction.
> Last updated: 2026-09-23

---

## 1. Current Build Baseline

The active project is built around the working Fabric 1.21.4 toolchain rather than the speculative 26.2 path.

- Minecraft: 1.21.4
- Fabric Loader: 0.19.5
- Fabric API: 0.119.4+1.21.4
- Java: 21
- Gradle: 8.12
- Loom: 1.9.2

This is the source of truth for the current implementation. The architecture should support the actual repo state and remain compatible with this environment until a deliberate version migration is chosen.

---

## 2. Current Architecture in the Repo

The repo already contains a modular menu shell that matches the stable structure we want to preserve:

- `MvxmenuScreen` handles the full shell, category routing, search, and content view management
- `MvxmenuLayout` manages fixed panel positioning and layout math
- `CategoryButtonWidget` renders sidebar category selections
- `ModuleCardWidget` renders category cards and status states
- `ModuleDetailView` and `SettingsView` handle the main content panels
- `MvxmenuTheme` centralizes the UI tokens and color decisions

This is the foundation. The plan is to evolve it, not replace it.

---

## 3. Design Principles

1. Keep the established menu shell stable.
2. Add features by inserting modules into the existing flow instead of rewriting the shell.
3. Prefer small, testable changes over large refactors.
4. Treat compatibility decisions as first-class work.
5. Only expand into more ambitious features after the base menu remains stable and build-verified.

---

## 4. Working Structure

```
dev.mvxmenu/
├── ui/
│   ├── screen/
│   │   ├── MvxmenuScreen.java
│   │   └── ...
│   ├── widget/
│   │   ├── CategoryButtonWidget.java
│   │   ├── ModuleCardWidget.java
│   │   └── ...
│   ├── layout/
│   │   └── MvxmenuLayout.java
│   ├── view/
│   │   ├── GenericView.java
│   │   ├── ModuleDetailView.java
│   │   └── SettingsView.java
│   └── theme/
│       ├── MvxmenuTheme.java
│       └── MvxmenuIcons.java
├── module/
│   ├── Module.java
│   ├── ModuleRegistry.java
│   └── ...
├── config/
│   ├── MvxmenuConfig.java
│   ├── MvxmenuConfigSerializer.java
│   └── ...
├── networking/
│   └── MvxmenuNetworking.java
├── mixin/
│   └── EntityRendererMixin.java
├── build.gradle
├── gradle.properties
├── fabric.mod.json
└── mixins.mvxmenu.json
```

---

## 5. What the current build is doing well

- The screen shell is modular and reusable.
- The sidebar/category flow is already established.
- Module cards and settings views fit the current architecture.
- Theme tokens are centralized and easier to iterate on.
- The project compiles in the current Fabric 1.21.4 environment.

This gives us a solid base for feature work without forcing a design rewrite.

---

## 6. Implementation Roadmap for the current build

### Phase 1: Stabilize and validate the active stack
- Keep the project aligned with Fabric 1.21.4, Java 21, and the active Loom version.
- Validate compile and runtime startup before adding new feature layers.
- Preserve the module shell and fixed layout approach.

### Phase 2: Tighten the existing UI framework
- Improve category state clarity and hover/active behavior.
- Refine spacing and wording for the in-game HUD aesthetic.
- Keep the visual polish within the same layout shell.

### Phase 3: Feature insertions
- Add new modules one by one without changing the category or shell structure.
- Keep each module self-contained and mapped into the category system.
- Ensure settings and toggles remain registered through the current UI path.

### Phase 4: Polish and reliability
- Verify config persistence and module state sync.
- Tune visual hierarchy and usability in the menu shell.
- Remove rough edges without rewriting the architecture.

---

## 7. Future work and remaining polish

### Remaining Polish (Future)
- Improve search/filter behavior to be clearer and more responsive.
- Refine module detail view readability and spacing.
- Add more robust hover/focus transitions for category and card states.
- Keep the menu visual language consistent across all categories.
- Expand module-specific settings without creating new shell patterns.

### Version upgrade path (later, not now)
- If a future modern Minecraft target is chosen, do it as a deliberate migration.
- Treat version migration as a separate branch of work from feature development.
- Do not let a speculative future target derail the stable build we are already working on.

---

## 8. Validation Checklist

- [ ] Project compiles on the active Fabric 1.21.4 toolchain
- [ ] Client launches without Mixins or class-loading failures
- [ ] Sidebar navigation remains stable
- [ ] Category-to-module flow still works cleanly
- [ ] Settings panel remains functional and persistent
- [ ] New modules slot into the same shell without structural refactor

---

## 9. Bottom line

The repo is currently best understood as a stable, modular Fabric 1.21.4 menu framework with a concrete UI shell and category-driven architecture. The most effective plan is to continue building in that structure, improve the shell, and only consider version migration when the project is intentionally ready for that larger change.

- The `oppyius-framework` repository (1.21.4/Yarn/native code) is NOT used as a codebase — it is version-incompatible and architecturally different
- Fabric 26.2 APIs referenced in §1 should be verified against the actual 26.2-pre1 decompiled source before implementation begins
