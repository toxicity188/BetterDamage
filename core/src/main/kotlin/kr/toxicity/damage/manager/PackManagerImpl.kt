package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.PackManager
import kr.toxicity.damage.api.nms.NMSVersion
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.pack.PackGenerator
import kr.toxicity.damage.api.pack.PackPath
import kr.toxicity.damage.api.pack.PackSupplier
import kr.toxicity.damage.util.*
import java.io.File
import java.security.MessageDigest
import java.util.*

object PackManagerImpl : PackManager {

    private val generatorMap = mutableMapOf(
        "folder" to PackGenerator.FOLDER,
        "zip" to PackGenerator.ZIP
    )

    private var currentGenerator = PackGenerator.ZIP

    override fun addPackGenerator(name: String, generator: PackGenerator) {
        generatorMap[name] = generator
    }

    override fun currentGenerator(): PackGenerator = currentGenerator

    override fun pack(byteMap: Map<PackPath, ByteArray>): File {
        val target = File(DATA_FOLDER.parentFile, currentGenerator.extension().run {
            if (isEmpty()) CONFIG.packPath() else "${CONFIG.packPath()}.$this"
        })
        File(DATA_FOLDER.subFolder(".cache"), "zip-hash.txt").run {
            runCatching {
                MessageDigest.getInstance("SHA-256")
            }.map { digest ->
                byteMap.values.forEach {
                    digest.update(it)
                }
                UUID.nameUUIDFromBytes(digest.digest()).toString()
            }.getOrNull()?.let {
                if (target.exists() && exists() && readText() == it) {
                    return target
                } else {
                    writeText(it)
                }
            }
        }
        currentGenerator.build(target, byteMap)
        return target
    }

    override fun reload(assets: PackAssets) {
        currentGenerator = generatorMap[CONFIG.packType()] ?: PackGenerator.ZIP
        assets.add(PackPath("pack.mcmeta"), PackSupplier.of(jsonObjectOf(
            "pack" to jsonObjectOf(
                "pack_format" to PLUGIN.nms().version().metaVersion,
                "description" to "BetterDamage's resource pack.",
                "supported_formats" to jsonObjectOf(
                    "min_inclusive" to NMSVersion.entries.first().metaVersion,
                    "max_inclusive" to NMSVersion.entries.last().metaVersion
                )
            )
        )))
        PLUGIN.getResource("icon.png")?.buffered()?.use {
            val read = it.readAllBytes()
            assets.add(PackPath("pack.png")) {
                read
            }
        }
    }
}