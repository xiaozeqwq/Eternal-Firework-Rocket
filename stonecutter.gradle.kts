plugins {
    id("dev.kikugie.stonecutter")
}

// In CI every node is built with `stonecutterGenerate`, so Stonecutter runs detached
// (`active null`). Keeping a literal active version here leaves the root `src/` linked
// for local development, but that link skips processing and thus skips replacements.
if (System.getenv("SC_DETACHED") == "true") {
    stonecutter active null
} else {
    stonecutter active "1.21.11-fabric"
}

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    val (_, loader) = current.project.split('-', limit = 2)

    // Adds constants to Stonecutter comments (i.e. for `//? if fabric {...`)
    constants {
        match(loader, "fabric", "neoforge", "forge")
    }

    // Mojang rename `ResourceLocation` to `Identifier` (and `location()` to `identifier()`) in 1.21.11
    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            replace("location()", "identifier()")
        }
    }
}
