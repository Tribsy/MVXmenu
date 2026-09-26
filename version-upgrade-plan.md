# MVXmenu Version Upgrade Plan

> Goal: Move the project off its stale 1.21.4 Fabric assumptions and onto the correct modern unobfuscated Minecraft 26.1+ target, while preserving the stable menu architecture and validating the mod at each migration step.
> Status: Active upgrade plan. This file is intentionally separate from the older architecture document.
> Last updated: 2026-09-23

---

## 1. Problem Statement

The project currently contains older build metadata and documentation tied to Minecraft 1.21.4 / Fabric 0.19.5, but the target direction is now the newer unobfuscated 26.1+ Minecraft line. That mismatch must be addressed explicitly before any major feature work continues.

The migration goal is not a redesign. The menu shell, category system, and modular insertion model remain the stable foundation. The change is at the version-compatibility layer: build toolchain, mapping assumptions, mixin signatures, and rendering API compatibility.

---

## 2. Upgrade Strategy

### 2.1 Decision rule
The project should target a single confirmed modern Minecraft 26.1+ release family and the matching Fabric toolchain. No code-level work should continue under the older 1.21.4 assumptions.

### 2.2 Scope boundaries
- In scope: version target confirmation, Gradle/Fabric/Java updates, API migration, mixin migration, compile validation, runtime validation.
- Out of scope: broad redesign of the menu system, major feature rewrites, or unrelated cleanup not required for version compatibility.

---

## 3. Execution Plan

### Phase 1: Confirm target and toolchain
1. Identify the exact Minecraft 26.1+ target release intended by the project.
2. Confirm the Fabric Loader, Loom, and Fabric API versions that match that target.
3. Confirm the Java version required by the target stack.
4. Record the target decision in the project docs to prevent drift back to 1.21.4.

### Phase 2: Build metadata migration
1. Update `gradle.properties` for the real modern target values.
2. Update `build.gradle` for the correct Fabric Loom and dependency set.
3. Ensure `fabric.mod.json` matches the upgraded target metadata.
4. Clean stale build artifacts and regenerate metadata before compiling.

### Phase 3: API and mixin migration
1. Audit all version-specific Minecraft calls.
2. Review Mixin injection targets and method signatures for the new target.
3. Update any renamed or removed render, GUI, or client lifecycle APIs.
4. Fix the mod so it initializes under the modern runtime without stale 1.21.4 signatures.

### Phase 4: Runtime validation
1. Run a fresh Gradle compile.
2. Run the dev client and confirm the mod initializes.
3. Verify the UI shell loads and the module registry still behaves correctly.
4. Fix any startup or client initialization issues before continuing feature work.

### Phase 5: Preserve architecture while upgrading
1. Keep the existing menu shell and layout model intact.
2. Adapt the UI widgets to the new runtime contract without rewriting the shell structure.
3. Only add feature-level changes after the version upgrade is validated.

---

## 4. Required Validation Steps

### Build validation
- Run: `./gradlew clean compileJava --no-daemon --console=plain`
- Goal: no unresolved API, mapping, or dependency problems

### Runtime validation
- Run: `./gradlew runClient --no-daemon --console=plain`
- Goal: the mod starts, the menu loads, and there are no mixin or classloading errors

### Regression guard
- Do not add feature work until the upgraded target boots cleanly.
- Preserve the modular shell and category insertion model.
- Treat any UI or module issues discovered during migration as compatibility issues, not redesign tasks.

---

## 5. Files to review during migration

- `build.gradle`
- `gradle.properties`
- `src/main/resources/fabric.mod.json`
- `src/main/resources/mixins.mvxmenu.json`
- `src/main/java/dev/mvxmenu/mixin/EntityRendererMixin.java`
- `src/main/java/dev/mvxmenu/ui/screen/MvxmenuScreen.java`
- `src/main/java/dev/mvxmenu/ui/widget/CategoryButtonWidget.java`
- `src/main/java/dev/mvxmenu/ui/widget/ModuleCardWidget.java`
- `src/main/java/dev/mvxmenu/theme/MvxmenuTheme.java`

---

## 6. Migration decisions

- The project will not keep the stale 1.21.4 conventions as the active target.
- The menu architecture remains the stable foundation.
- The actual source of truth is the modern unobfuscated version target chosen for the project.
- This upgrade is intentionally scoped to build and runtime compatibility first, with feature work deferred until the target is verified.

---

## 7. Completion checklist

- [ ] Confirm exact modern Minecraft 26.1+ target
- [ ] Confirm matching Fabric Loom / Loader / API versions
- [ ] Update build metadata
- [ ] Fix mixin and API incompatibilities
- [ ] Verify compile passes
- [ ] Verify client starts cleanly
- [ ] Confirm the modular menu framework still loads correctly
- [ ] Document the final upgraded target in project docs

---

## 8. Notes for future implementation

This file exists to separate the actual migration plan from the older historical architecture note. Once the version upgrade is validated, subsequent feature work can continue under the correct target without conflicting with the stale document trail.
