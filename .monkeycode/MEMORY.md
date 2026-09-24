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
