# User Instruction Memory

This file records user instructions, preferences, and teachings for reference in future interactions.

## Format

### User Instruction Entry
[User Instruction Summary]
- Date: [YYYY-MM-DD]
- Context: [Mentioned scenario or time]
- Instructions:
  - [Content of user teaching or instruction, described line by line]

### Project Knowledge Entry
[Project Knowledge Summary]
- Date: [YYYY-MM-DD]
- Context: Discovered by Agent while performing [specific task description]
- Category: [Operations & Deployment|Build Methods|Testing Methods|Troubleshooting & Debugging|Workflow & Collaboration|Environment Configuration]
- Instructions:
  - [Specific knowledge points, described line by line]

## Deduplication Strategy
- Before adding a new entry, check for similar or identical instructions.
- If a duplicate is found, skip the new entry or merge it with the existing one.
- When merging, update the context or date information.
- This helps avoid redundant entries and keeps the memory file tidy.

## Entries

[Project Knowledge Summary]
- Date: 2026-09-24
- Context: Discovered by Agent while setting up multiloader Stonecutter builds for Eternal Firework Rocket
- Category: Build Methods
- Instructions:
  - This project has NO local JDK/Gradle (`java` and `gradle` not found). All compilation and verification runs on GitHub Actions.
  - Build must run in Stonecutter detached mode in CI: set env `SC_DETACHED=true` so `stonecutter active null` is used, which forces `stonecutterGenerate` per node and makes `replacements` apply.
  - Set env `SC_VERSION=<mc>` so `settings.gradle.kts` creates only that one node; otherwise every job configures all 18 subprojects and is much slower.
  - Build a specific loader node with `./gradlew :<mc>-<loader>:build`.
  - Fabric jar output path is `versions/<mc>-fabric/build/libs/`.

[Project Knowledge Summary]
- Date: 2026-09-24
- Context: Discovered by Agent while verifying builds through GitHub Actions
- Category: Workflow & Collaboration
- Instructions:
  - Do NOT run `gh auth login`. Use the helper `/tmp/opencode/gh.sh`, which injects `GH_TOKEN` from the git credential helper.
  - Trigger: `/tmp/opencode/gh.sh workflow run Test.yaml --ref main`
  - Poll: `/tmp/opencode/gh.sh run view <runId> -R xiaozeqwq/Eternal-Firework-Rocket --json status,conclusion,jobs`
  - Logs: `/tmp/opencode/gh.sh run view --job <jobId> -R xiaozeqwq/Eternal-Firework-Rocket --log`
  - `jq` is not installed; `python3` 3.11.2 is available for JSON parsing.

[Project Knowledge Summary]
- Date: 2026-09-27
- Context: Discovered by Agent while adding Forge 1.20.x support to the multiloader build
- Category: Build Methods
- Instructions:
  - This project must use ForgeGradle 7 (not 6) for Forge nodes. ForgeGradle 6 rejects Gradle 9.x, while Stonecutter 0.9.x requires Gradle >= 9. ForgeGradle 7 requires Gradle >= 9.3.0, so the Gradle wrapper stays at 9.7.0.
  - Do NOT downgrade the Gradle wrapper to pair with ForgeGradle 6; that breaks Stonecutter.
  - Forge build DSL (`build.forge.gradle.kts`): `repositories { minecraft.mavenizer(this); maven(fg.forgeMaven); maven(fg.minecraftLibsMaven); mavenCentral() }`, then `minecraft.mappings("official", mcVersion)`, then `"implementation"(minecraft.dependency("net.minecraftforge:forge:$mcVersion-$forgeVersion"))`.
  - Access the extensions via `extensions.getByType<MinecraftExtensionForProject>()` and `extensions.getByType<ForgeGradleExtensionForProject>()`.
  - NeoForge nodes use ModDevGradle (`2.0.148` since NeoForge 26.x support); Fabric nodes use Loom `1.18.2`.

[Project Knowledge Summary]
- Date: 2026-09-27
- Context: Discovered by Agent while running the full multiloader CI matrix (37 jobs)
- Category: Troubleshooting & Debugging
- Instructions:
  - NeoForge jobs may fail with HTTP 502 from `https://maven.neoforged.net/mojang-meta/...` during `createMinecraftArtifacts` (resolving `net.neoforged:minecraft-dependencies:<mc>`). This is upstream flakiness, not a code issue; rerun only the failed jobs with `/tmp/opencode/gh.sh run rerun <runId> --failed -R xiaozeqwq/Eternal-Firework-Rocket`.
  - `git push` occasionally fails with `gnutls_handshake() failed`; retry with a short loop.
  - Forge 1.20 (46.x) lacks the `LootTable#addPool`/`getPool`/`removePool` patch that was added in Forge 1.20.1. Loot injection on 1.20 must mutate the mutable `pools` field via reflection, trying both the official dev name (`pools`) and the SRG name (`f_79109_`). `src/main/.../loot/ForgeLootEvents.java` handles this with a `//? if >=1.20.1` conditional.
  - Minecraft `1.20` is the floor version for this project (Fabric + Forge). Forge does not support 1.20.5, so `loadersFor()` excludes forge for `1.20.5`.

[Project Knowledge Summary]
- Date: 2026-09-27
- Context: Discovered by Agent while exercising the release pipeline (build.yml -> modrinth.yml) for v1.0.8
- Category: Operations & Deployment
- Instructions:
  - Release flow: bump `mod.version` in `gradle.properties`, push, then run `build.yml` (workflow_dispatch). It builds the full matrix (47 nodes since NeoForge 26.x support) and creates GitHub Release `v<mod.version>` with all jars.
  - The GitHub Release body / Modrinth changelog is generated from the commits since the previous version tag: the `release` job checks out with `fetch-depth: 0`, picks the previous tag with `git tag --sort=-version:refname`, then lists `git log <prev>..HEAD --no-merges` subjects (strips the `Co-authored-by:` trailer, drops `chore: bump version` lines). Do not expect a hand-written changelog.
  - Use SemVer: new loader/feature support = minor bump (e.g. `1.0.9` -> `1.1.0`); pure fixes = patch bump.
  - `modrinth.yml` (`on: release: published`) does NOT auto-run for a release created by `build.yml`, because GitHub suppresses workflow runs for events created with the default `GITHUB_TOKEN`. Dispatch it manually: `/tmp/opencode/gh.sh workflow run modrinth.yml --ref main -R xiaozeqwq/Eternal-Firework-Rocket`.
  - `modrinth.yml` derives its publish matrix from the latest release's jar filenames (loader from `-neoforge-`/`-forge-` in `archivesName`, mc from the `+<mc>` suffix); no edits needed when versions are added.
  - `MODRINTH_TOKEN` secret is configured; each publish is `<ver>+mc<mc>-<loader>`.

[Project Knowledge Summary]
- Date: 2026-09-30
- Context: Discovered by Agent while adding Fabric 26.x (26.1, 26.1.1, 26.1.2, 26.2, 26.3) support and verifying it in CI
- Category: Build Methods
- Instructions:
  - 26.x started as Fabric-only in this repo. (`loadersFor()` was later extended to add NeoForge for 26.x; see the 2026-10-02 entry for the final 47-job matrix.)
  - 26.x jars are unobfuscated (no `client_mappings`). Set `fabric.loom.disableObfuscation=true` in `versions/26.x-fabric/gradle.properties`; then Loom creates no remap configurations, so use plain `implementation` (not `modImplementation`) and skip `mappings`/`officialMojangMappings()`. `build.fabric.gradle.kts` branches on `sc.current.parsed >= "26.1"`.
  - 26.x needs Java 25 toolchain and Loom `1.18.2`. CRITICAL: Loom 1.18.2 is on the buildscript classpath of EVERY subproject (fabric, neoforge, forge) and requires a JVM 25 runtime, so ALL CI jobs must run on Java 25. A Java 25 JDK still compiles the older nodes targeting `--release` 17/21, so no per-node Java split is needed.
  - API boundaries (Stonecutter guards): since 26.1, `Level.random` is protected (use `world.getRandom()`) and `Player.displayClientMessage` was removed (use `sendOverlayMessage` for the action bar). Since 26.3, `Entity.hurtMarked` was removed (fall-flying shooter velocity sync is automatic in `ServerEntity`, so guard the field write out) and the loot number providers were reorganized: use `net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders.exactly(int)` (returns `Holder`, accepted by `LootPool.Builder.setRolls`) plus `...number.ints.ConstantValue` / `...number.floats.ConstantValue` in place of the old `...number.ConstantValue`.
  - 26.x fabric-api versions used: 26.1=0.145.1+26.1, 26.1.1=0.145.4+26.1.1, 26.1.2=0.155.3+26.1.2, 26.2=0.161.0+26.2, 26.3=0.161.0+26.3.

[Project Knowledge Summary]
- Date: 2026-10-02
- Context: Discovered by Agent while adding NeoForge 26.x (26.1, 26.1.1, 26.1.2, 26.2, 26.3) support
- Category: Build Methods
- Instructions:
  - NeoForge publishes 26.x builds with version scheme `26.<mc>.<patch>.<build>[-beta]`. Pinned versions: 26.1=`26.1.0.19-beta`, 26.1.1=`26.1.1.15-beta`, 26.1.2=`26.1.2.112`, 26.2=`26.2.0.88`, 26.3=`26.3.0.39-beta`; each goes in `versions/26.<mc>-neoforge/gradle.properties` as `deps.neo_loader`.
  - `loadersFor()` adds `neoforge` for versions starting with `26.` as well as `1.21`.
  - ModDevGradle bumped to `2.0.148` (from `2.0.147`); it delegates to NFRT and handles the unobfuscated 26.x NeoForm without extra config. `build.neoforge.gradle.kts` uses Java 25 when `sc.current.parsed >= "26.1"`.
  - CI matrix is now 47 jobs: 19 fabric (1.20 .. 1.21.11) + 5 fabric 26.x + 5 neoforge 26.x + 12 neoforge 1.21.x + 6 forge. Verified green by Test.yaml run `36814867831` on commit `ef9171b` (47/47 success).
  - `actions/setup-java` bumped to `v5` in Test.yaml and build.yml.
