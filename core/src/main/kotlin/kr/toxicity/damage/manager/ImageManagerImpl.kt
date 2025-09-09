package kr.toxicity.damage.manager

import kr.toxicity.damage.api.image.DamageImage
import kr.toxicity.damage.api.manager.ImageManager
import kr.toxicity.damage.api.pack.PackAssets
import kr.toxicity.damage.api.pack.PackSupplier
import kr.toxicity.damage.util.*
import net.kyori.adventure.key.Key
import java.io.File

object ImageManagerImpl : ImageManager {

    private val imageMap = hashMapOf<String, DamageImage>()

    override fun reload(assets: PackAssets) {
        imageMap.clear()
        val dir = DATA_FOLDER.subFolder("assets")
        DATA_FOLDER.subFolder("images").forEachAllYaml { key, section ->
            runCatching {
                val imageName = File(dir, section.getString("file").ifNull { "'file' value doesn't exist." })
                val image = imageName.ifEmpty {
                    "this image doesn't exist: $path"
                }.toImage()
                val codepoints = section.getString("codepoints") ?: "0123456789"
                val count = codepoints.codePoints().count().toInt()
                if (image.width % count != 0) throw RuntimeException("codepoints length mismatched: ${image.width} cannot divided into $count.")
                val space = section.getInt("space")
                assets.betterDamage().textures().add(imageName.name, PackSupplier.of(image))
                assets.betterDamage().font().add("$key.json", PackSupplier.of(jsonObjectOf("providers" to jsonArrayOf(
                    jsonObjectOf(
                        "type" to "space",
                        "advances" to jsonObjectOf(
                            DamageImage.SPACE_CODEPOINT to space
                        )
                    ).takeIf {
                        space != 0
                    },
                    jsonObjectOf(
                        "type" to "bitmap",
                        "file" to "$NAMESPACE:${imageName.name}",
                        "height" to 16,
                        "ascent" to 8,
                        "chars" to jsonArrayOf(codepoints)
                    )
                ))))
                imageMap[key] = DamageImageImpl(space, createAdventureKey(key))
            }.onFailure { e ->
                e.handle("Unable to read this image: $key")
            }
        }
    }

    private class DamageImageImpl(
        private val space: Int,
        private val key: Key
    ) : DamageImage {
        override fun space(): Int = space
        override fun key(): Key = key
    }

    override fun image(name: String): DamageImage? = imageMap[name]
}