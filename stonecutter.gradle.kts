plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.11-fabric" /* [SC] DO NOT EDIT */

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
