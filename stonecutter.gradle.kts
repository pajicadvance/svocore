plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "26.1"

stonecutter parameters {
    swaps["mod_id"] = "\"${property("mod.id")}\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
        }

        string(current.parsed >= "26.1") {
            replace("classTweaker v1 named", "classTweaker v1 official")
        }
    }
}
