package kr.toxicity.damage.manager

import kr.toxicity.damage.api.manager.PackManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.pack.PackGenerator
import kr.toxicity.damage.api.pack.PackPath
import kr.toxicity.damage.util.DATA_FOLDER
import java.io.File

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

    override fun pack(byteMap: Map<PackPath, ByteArray>) {
        currentGenerator.build(File(DATA_FOLDER.parent, ConfigManagerImpl.packPath()), byteMap)
    }

    override fun reload(assets: PackAssets) {
        currentGenerator = generatorMap[ConfigManagerImpl.packType()] ?: PackGenerator.ZIP
    }
}