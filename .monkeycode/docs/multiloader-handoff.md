# Multiloader Handoff (Fabric + NeoForge + Forge all GREEN)

Last updated: 2026-09-27

## Status

- Full CI matrix is GREEN: 35 jobs = 18 Fabric + 12 NeoForge + 5 Forge.
  Verified by run `36321081634` on commit `46a235f` (all jobs `success`).
- Fabric 18 versions (1.20.1 .. 1.21.11): Loom `1.17.21`, official Mojang mappings.
- NeoForge 12 versions (all 1.21.x): ModDevGradle `2.0.147`.
- Forge 5 versions (1.20.1, 1.20.2, 1.20.3, 1.20.4, 1.20.6; no Forge 1.20.5):
  ForgeGradle `7.0.40`.
- Sources are shared across loaders and use Stonecutter loader/version conditionals.
- CI builds one Stonecutter node per job and uploads a jar artifact.

## How the Build Works Now

- `settings.gradle.kts`: `stonecutter create(rootProject)`; `loadersFor(version)` returns
  - `fabric` for every version,
  - `neoforge` when the version starts with `1.21`,
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

## Release Pipeline (updated, not yet exercised end-to-end)

- `.github/workflows/build.yml` now builds the full 35-node matrix
  (`:<mc>-<loader>:build`) and uploads `jar-<mc>-<loader>`. `package`/`release`/`cleanup`
  are unchanged in shape: the GitHub Release gets every non-sources/dev/javadoc jar.
- `.github/workflows/modrinth.yml` `prepare` now derives `loader` from the jar filename
  (`*-neoforge-*` -> neoforge, `*-forge-*` -> forge, else fabric) in addition to `mc`.
- Each Modrinth publish uses a unique version number
  `<mod_version>+mc<mc>-<loader>` and sets `loaders: <loader>` / `game-versions: <mc>`.
  This fixes the previous bug where every jar published under the same version number.
- Not run yet: `build.yml` creates a real tag/release, which then triggers `modrinth.yml`
  (needs `MODRINTH_TOKEN`). Run the Test workflow for build verification first.

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
