plugins {
    id("dev.kikugie.loom-back-compat")
    id("me.modmuss50.mod-publish-plugin") version "2.0.0-beta.2"
}

version = "${property("mod.version")}"
base.archivesName = property("mod.id") as String

val requiredJava: JavaVersion = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

val compatibleVersions: List<String> = sc.properties.rawOrNull("mod", "mc_releases")
    ?.asList().orEmpty().map { it.toString() }

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }
    strictMaven("https://www.cursemaven.com", "CurseForge", "curse.maven")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.fzzyhmstrs.me/")
    maven("https://maven.caffeinemc.net/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:${sc.current.version}")
    loomx.applyMojangMappings()

    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric_loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")

    modImplementation("maven.modrinth:mixson:${property("deps.mixson")}")
    modImplementation("com.terraformersmc:modmenu:${property("deps.modmenu")}")
    modImplementation("me.fzzyhmstrs:fzzy_config:${property("deps.fzzy_config")}")
    modImplementation("com.moulberry:mixinconstraints:${property("deps.mixinconstraints")}")
    include("com.moulberry:mixinconstraints:${property("deps.mixinconstraints")}")

    compileOnlyApi("net.caffeinemc:sodium-fabric-api:${property("deps.sodium")}")
    modRuntimeOnly("net.caffeinemc:sodium-fabric:${property("deps.sodium")}")

    compileOnly("maven.modrinth:iris:1.10.9+26.1-fabric")
    compileOnly("maven.modrinth:farmers-delight-refabricated:26.1-3.5.1")
    compileOnly("maven.modrinth:wilder-wild:4.2.8-mc26.1")
    compileOnly("maven.modrinth:trailier-tales:1.2.5-mc26.1")
    compileOnly("maven.modrinth:better-block-entities:1.3.2+mc26.1.2")
    compileOnly("maven.modrinth:reliable-gliders:1.3.1-26.1.2-fabric")
    compileOnly("maven.modrinth:reusable-vault-blocks:1.0.0-fabric,26.1")
}

loom {
    fabricModJsonPath = rootProject.file("src/main/resources/fabric.mod.json")
    accessWidenerPath = sc.process(
        rootProject.file("src/main/resources/${property("mod.id")}.ct"),
        "build/processed.ct"
    )

    decompilerOptions.named("vineflower") {
        options.put("mark-corresponding-synthetics", "1")
    }

    runConfigs.all {
        vmArgs("-Dmixin.debug.export=true")
        runDir = "../../run"
    }
}

java {
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor = JvmVendorSpec.MICROSOFT
        languageVersion = JavaLanguageVersion.of(requiredJava.majorVersion)
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            set(key, value)
        }

        val props = buildMap {
            register("id", "mod.id")
            register("name", "mod.name")
            register("version", "mod.version")
            register("minecraft", "mod.mc_compat")
        }

        filesMatching("fabric.mod.json") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }
    }

    register<Copy>("buildAndCollect") {
        group = "build"

        from(loomx.modJar.map { it.archiveFile }, loomx.modSourcesJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

publishMods {
    file.set(loomx.modJar.get().archiveFile)
    changelog.set(rootProject.file("CHANGELOG.md").readText())
    type.set(STABLE)
    modLoaders.add("fabric")
    displayName = "${property("mod.version")} for ${sc.current.version}"

    modrinth {
        projectId.set("${property("mod.modrinth_id")}")
        accessToken.set(providers.environmentVariable("MR_KEY"))
        minecraftVersions.addAll(compatibleVersions)
        requires("fabric-api", "fzzy-config", "mixson")
        optional("modmenu")
    }
}