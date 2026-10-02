# Multiloader Handoff (Fabric + NeoForge + Forge on 1.20/1.21/26.x all GREEN)

Last updated: 2026-10-02

## Status

- Full CI matrix is GREEN: 47 jobs = 19 Fabric (1.20 .. 1.21.11) + 5 Fabric 26.x
  + 5 NeoForge 26.x + 12 NeoForge 1.21.x + 6 Forge. Verified by Test.yaml run
  `36814867831` on commit `ef9171b` (47/47 `success`).
- Fabric 1.20 .. 1.21.11: official Mojang mappings.
- Fabric & NeoForge 26.x (26.1, 26.1.1, 26.1.2, 26.2, 26.3): unobfuscated, Java 25
  (see the 26.x section below).
- NeoForge: ModDevGradle `2.0.148` (12 versions on 1.21.x + 5 on 26.x).
- Forge 6 versions (1.20, 1.20.1, 1.20.2, 1.20.3, 1.20.4, 1.20.6; no Forge 1.20.5):
  ForgeGradle `7.0.40`.
- `1.20 (forge)` needs special loot handling: Forge 1.20 (46.x) predates the
  `LootTable#addPool` patch, so `ForgeLootEvents` uses `addPool` on `>=1.20.1` and
  falls back to the mutable `pools` list via reflection on `1.20` (tries both the
  official dev name `pools` and the SRG name `f_79109_`).
- Sources are shared across loaders and use Stonecutter loader/version conditionals.
- CI builds one Stonecutter node per job and uploads a jar artifact.

## 26.x (26.1 .. 26.3) — Fabric and NeoForge

- `loadersFor()` returns `fabric` for every version and `neoforge` for versions
  starting with `1.21` or `26.`, so each 26.x version builds both loaders.
- 26.x is unobfuscated (Mojang ships no `client_mappings`). `build.fabric.gradle.kts`
  sets `unobfuscated = sc.current.parsed >= "26.1"`, and the per-version props set
  `fabric.loom.disableObfuscation=true`. In that mode Loom creates no remap
  configurations, so the project must use plain `implementation` (not
  `modImplementation`) and skip `mappings`/`officialMojangMappings()`.
- 26.x uses Java 25 (toolchain) and Loom `1.18.2`. CRITICAL: Loom 1.18.2 is on the
  buildscript classpath of EVERY subproject (fabric, neoforge, forge) and requires a
  JVM 25 runtime, so ALL CI jobs run on Java 25. A Java 25 JDK still compiles the
  older nodes targeting `--release` 17/21, so no per-node Java split is needed.
- 26.x API boundaries handled with Stonecutter guards:
  - Since 26.1: `Level.random` is protected (use `world.getRandom()`);
    `Player.displayClientMessage` was removed (use `sendOverlayMessage` for the
    action bar).
  - Since 26.3: `Entity.hurtMarked` was removed (fall-flying shooter velocity sync
    is automatic in `ServerEntity`, so guard the field write out); the loot number
    providers moved to `...number.ints.ContextIntProviders.exactly(int)` (returns a
    `Holder`, accepted by `LootPool.Builder.setRolls`) and
    `...number.ints.ConstantValue` / `...number.floats.ConstantValue`.
- fabric-api versions: 26.1=`0.145.1+26.1`, 26.1.1=`0.145.4+26.1.1`,
  26.1.2=`0.155.3+26.1.2`, 26.2=`0.161.0+26.2`, 26.3=`0.161.0+26.3`.
- NeoForge 26.x pinned loader versions: 26.1=`26.1.0.19-beta`, 26.1.1=`26.1.1.15-beta`,
  26.1.2=`26.1.2.112`, 26.2=`26.2.0.88`, 26.3=`26.3.0.39-beta` (in
  `versions/<mc>-neoforge/gradle.properties` as `deps.neo_loader`).
- NeoForge 26.x uses ModDevGradle `2.0.148` and Java 25 (`build.neoforge.gradle.kts`
  branches on `sc.current.parsed >= "26.1"`). The 26.x NeoForm is unobfuscated; NFRT
  handles it without extra config.

## How the Build Works Now

- `settings.gradle.kts`: `stonecutter create(rootProject)`; `loadersFor(version)` returns
  - `fabric` for every version,
  - `neoforge` when the version starts with `1.21` or `26.`,
  - `forge` when the version starts with `1.20` and is not `1.20.5`.
  Each node is `versions/<mc>-<loader>` with buildscript `build.<loader>.gradle.kts`.
  When env `SC_VERSION` is set (CI), only that single version's nodes are created.
- `stonecutter.gradle.kts`: `active` is `null` when env `SC_DETACHED=true` (CI).
  Detached mode forces `stonecutterGenerate` per node, so `replacements` apply.
  Locally the active node stays `1.21.11-fabric`.
- `replacements`: `ResourceLocation` -> `Identifier` and `location()` -> `identifier()`
  for `>=1.21.11`.
- Per-version props live in `versions/<mc>-<loader>/gradle.properties`
  (`deps.fabric_api_version`, `deps.fabric_loader`, `deps.neo_loader`, `deps.forge_loader`).
- Gradle wrapper is `9.7.0`; Stonecutter `0.9.8`.

## ForgeGradle 7 Notes (why 7 and not 6)

- ForgeGradle 6 rejects Gradle 9.x ("Gradle 9.0 and newer are not supported yet").
- Stonecutter 0.9.x requires Gradle >= 9.
- ForgeGradle 7 requires Gradle >= 9.3.0, so both can coexist on wrapper `9.7.0`.
- `build.forge.gradle.kts` uses the FG7 mavenizer DSL:
  - `repositories { minecraft.mavenizer(this); maven(fg.forgeMaven); maven(fg.minecraftLibsMaven); mavenCentral() }`
  - `minecraft.mappings("official", mcVersion)`
  - `dependencies { "implementation"(minecraft.dependency("net.minecraftforge:forge:$mcVersion-$forgeVersion")) }`
  - extensions fetched via `extensions.getByType<MinecraftExtensionForProject>()` and
    `extensions.getByType<ForgeGradleExtensionForProject>()`.
- Forge 1.20.4 and older technically need the experimental `net.minecraftforge.renamer`
  plugin, but FG7 "magic" handles it automatically for the standard official-mappings
  case; the 5 Forge jobs build without applying renamer explicitly.

## Release Pipeline (verified end-to-end on v1.1.0, with 26.x)

- `build.yml` run for `1.1.0` (run `36812679917`, 45/45 jobs: 42 build +
  package + release + cleanup) produced the GitHub Release `v1.1.0` with 84
  assets (42 jars + 42 sources jars), including the 5 Fabric 26.x jars.
- `1.2.0` adds NeoForge 26.x (47-node matrix). Only the Test.yaml matrix has been
  run for it so far (run `36814867831`, 47/47); `build.yml`/`modrinth.yml` were not
  run for `1.2.0` at the time of writing.
- The release body is generated from the commits since the previous version tag,
  not from a single commit. The `release` job checks out with `fetch-depth: 0`,
  resolves the previous tag via `git tag --sort=-version:refname`, then lists
  `git log <prev>..HEAD --no-merges` subjects (strips the `Co-authored-by:`
  trailer, drops `chore: bump version` lines).
- `modrinth.yml` run `36813406302` (1 prepare + 42 publish, all success)
  published 42 unique Modrinth versions `<ver>+mc<mc>-<loader>`. Verified via the
  Modrinth API: the 5 new entries `1.1.0+mc26.{1,1.1,1.2,2,3}-fabric` carry
  `loaders: [fabric]` and the matching `game_versions`.
- `modrinth.yml` does NOT auto-trigger from that release: GitHub suppresses
  workflow runs caused by events created with the default `GITHUB_TOKEN`
  (`release: published`). Dispatch it manually:
  `/tmp/opencode/gh.sh workflow run modrinth.yml --ref main`.

Earlier verified on v1.0.9/v1.0.8: same flow; v1.0.8 was `build.yml` `36324757133`
(40/40) then `modrinth.yml` `36325339055` (38/38).

- `.github/workflows/build.yml` builds the full 47-node matrix
  (`:<mc>-<loader>:build`) and uploads `jar-<mc>-<loader>`. `package`/`release`/`cleanup`
  are unchanged in shape: the GitHub Release gets every non-sources/dev/javadoc jar.
- `.github/workflows/modrinth.yml` `prepare` now derives `loader` from the jar filename
  (`*-neoforge-*` -> neoforge, `*-forge-*` -> forge, else fabric) in addition to `mc`.
- Each Modrinth publish uses a unique version number
  `<mod_version>+mc<mc>-<loader>` and sets `loaders: <loader>` / `game-versions: <mc>`.
  This fixes the previous bug where every jar published under the same version number.
- Release loader detection relies on the jar naming from the build scripts:
  `archivesName = $modId-$mcVersion-<loader>` for forge/neoforge (so `-<loader>-`
  appears in the name) and `$modId-$mcVersion` for fabric (defaults to fabric).

## CI Verification Loop

- Helper: `/tmp/opencode/gh.sh` sets `GH_TOKEN` from the git credential helper.
  Do NOT run `gh auth login`.
- Push: `for i in 1 2 3; do git push origin main && break; sleep 3; done`
  (transient `gnutls_handshake() failed` happens).
- Trigger: `/tmp/opencode/gh.sh workflow run Test.yaml --ref main`
- Poll: `/tmp/opencode/gh.sh run view <id> -R xiaozeqwq/Eternal-Firework-Rocket --json status,conclusion,jobs`
- Rerun transient failures only: `/tmp/opencode/gh.sh run rerun <id> --failed -R xiaozeqwq/Eternal-Firework-Rocket`
- Logs: `/tmp/opencode/gh.sh run view --job <jobId> -R <repo> --log`

## Known Transient Failures

- NeoForge jobs can fail with HTTP 502 from `https://maven.neoforged.net/mojang-meta/...`
  (`createMinecraftArtifacts` -> `net.neoforged:minecraft-dependencies`). This is upstream
  flakiness; `gh run rerun <id> --failed` clears it (confirmed on run `36320754662`).

## Local Environment

- No JDK/Gradle locally (`java`/`gradle` not found). ALL compilation is on GitHub Actions.
- `jq` absent; `python3` 3.11.2 present.

## Tooling Artifacts (in /tmp/opencode)

- `yarn2moj.json` + `buildmap.py` + `query.py`: Yarn->Mojang symbol table/query.
- `client-<ver>.txt`: official client mappings per boundary version.
- `tml/`: multiloader template build files.
- `fg7src/` + `fg7.jar`: ForgeGradle 7.0.40 sources.
- `scdocs/`: Stonecutter docs.
- `gh.sh`: CI helper.
