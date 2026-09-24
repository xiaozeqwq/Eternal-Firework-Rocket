# Multiloader Handoff (Fabric done, NeoForge/Forge pending)

Last updated: 2026-09-24

## Status

- Fabric 18-version matrix (1.20.1 .. 1.21.11) is GREEN across `build.yml` and `Test.yaml`.
- Sources are on Mojang official mappings with Stonecutter conditionals.
- CI builds one Stonecutter node per job and uploads artifacts.

## How the Build Works Now

- `settings.gradle.kts`: `stonecutter create(rootProject)`, `match(version, loader...)`
  creates `versions/<mc>-<loader>` with `build.<loader>.gradle.kts`.
  When env `SC_VERSION` is set (CI), only that single node is created.
- `stonecutter.gradle.kts`: `active` is `null` when env `SC_DETACHED=true` (CI).
  Detached mode forces `stonecutterGenerate` per node, so `replacements` apply.
  Locally the active node stays `1.21.11-fabric`.
- `replacements`: `ResourceLocation` -> `Identifier` and `location()` -> `identifier()`
  for `>=1.21.11`.
- Per-version props live in `versions/<mc>-fabric/gradle.properties` (old folder name).
- `.github/workflows/build.yml` and `Test.yaml` both set `SC_VERSION` + `SC_DETACHED`.

## Remaining: Add NeoForge 1.21.x and Forge 1.20.x

Targets (loaders dimension):
- neoforge: 1.21.1, 1.21.4, 1.21.8, 1.21.10, 1.21.11
- forge: 1.20.1, 1.20.2, 1.20.3, 1.20.4, 1.20.6  (NO 1.20.5 build exists)
- fabric: existing 18 versions

NeoForge latest per minor: 1.21.1=21.1.251, 1.21.4=21.4.157, 1.21.8=21.8.54,
1.21.10=21.10.64, 1.21.11=21.11.45.
Forge latest per minor: 1.20=46.0.14, 1.20.1=47.4.23, 1.20.2=48.1.0,
1.20.3=49.0.2, 1.20.4=49.2.9, 1.20.6=50.2.10. ForgeGradle latest 6.0.54.
ModDevGradle latest 2.0.147 (template uses 2.0.140). Stonecutter 0.9.8.

### Steps

1. `settings.gradle.kts`: extend `match()` calls per loader, e.g.
   `match("1.21.1", "fabric", "neoforge")`, `match("1.20.1", "fabric", "forge")`.
   Keep the `SC_VERSION` single-node path.
2. Add `build.neoforge.gradle.kts` (ModDevGradle) and `build.forge.gradle.kts`
   (ForgeGradle 6). Mirror `template-multiloader` in `/tmp/opencode/tml/`.
3. Per-loader metadata: `META-INF/neoforge.mods.toml`, `META-INF/mods.toml`.
   Exclude the other loader's toml in processResources (template does this).
4. Source loader conditionals in `src/` using `//? if fabric {}`, `//? if neoforge {}`,
   `//? if forge {}` (constants are created by `constants.match(loader, ...)`):
   - Entrypoint: `@Mod` for neo/forge vs Fabric initializer.
   - Event bus: `NeoForge.EVENT_BUS` / `MinecraftForge.EVENT_BUS`.
   - Loot table hook: NeoForge `LootTableLoadEvent`; Forge equivalent.
   - Entity renderer registration (client): loader-specific event.
   - Config dir: `FMLPaths.CONFIGDIR` for neo/forge.
5. CI: add loader to the matrix in both workflows (prefix artifact name with loader),
   and remember fabric node path is `versions/<mc>-<loader>`.
6. Update `.github/workflows/modrinth.yml` for the loader dimension.

## CI Verification Loop

- Helper: `/tmp/opencode/gh.sh` sets `GH_TOKEN` from the git credential helper.
  Do NOT run `gh auth login`.
- Push: `git push origin main`
- Trigger: `/tmp/opencode/gh.sh workflow run Test.yaml --ref main`
- Poll: `/tmp/opencode/gh.sh run view <id> -R xiaozeqwq/Eternal-Firework-Rocket --json status,conclusion,jobs`
- Logs: `/tmp/opencode/gh.sh run view --job <jobId> -R <repo> --log`

## Local Environment

- No JDK/Gradle locally (`java`/`gradle` not found). ALL compilation is on GitHub Actions.
- `jq` absent; `python3` 3.11.2 present.

## Tooling Artifacts (in /tmp/opencode)

- `yarn2moj.json` + `buildmap.py` + `query.py`: Yarn->Mojang symbol table/query.
- `client-<ver>.txt`: official client mappings per boundary version.
- `tml/`: multiloader template build files (ModDevGradle, ForgeGradle, properties.toml).
- `scdocs/`: Stonecutter docs.
- `forge-meta.xml`: Forge version metadata.
