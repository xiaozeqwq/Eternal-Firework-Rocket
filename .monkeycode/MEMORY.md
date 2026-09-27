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
  - NeoForge nodes use ModDevGradle `2.0.147`; Fabric nodes use Loom `1.17.21`.

[Project Knowledge Summary]
- Date: 2026-09-27
- Context: Discovered by Agent while running the full multiloader CI matrix (37 jobs)
- Category: Troubleshooting & Debugging
- Instructions:
  - NeoForge jobs may fail with HTTP 502 from `https://maven.neoforged.net/mojang-meta/...` during `createMinecraftArtifacts` (resolving `net.neoforged:minecraft-dependencies:<mc>`). This is upstream flakiness, not a code issue; rerun only the failed jobs with `/tmp/opencode/gh.sh run rerun <runId> --failed -R xiaozeqwq/Eternal-Firework-Rocket`.
  - `git push` occasionally fails with `gnutls_handshake() failed`; retry with a short loop.
  - Forge 1.20 (46.x) lacks the `LootTable#addPool`/`getPool`/`removePool` patch that was added in Forge 1.20.1. Loot injection on 1.20 must mutate the mutable `pools` field via reflection, trying both the official dev name (`pools`) and the SRG name (`f_79109_`). `src/main/.../loot/ForgeLootEvents.java` handles this with a `//? if >=1.20.1` conditional.
  - Minecraft `1.20` is the floor version for this project (Fabric + Forge). Forge does not support 1.20.5, so `loadersFor()` excludes forge for `1.20.5`.
