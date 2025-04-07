package kr.toxicity.damage.nms.v1_20_R1

import io.papermc.paper.adventure.PaperAdventure
import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.nms.DamageDisplay
import kr.toxicity.damage.api.nms.NMS
import kr.toxicity.damage.api.nms.NMSVersion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.protocol.game.*
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.util.Brightness
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R1.util.CraftChatMessage
import org.bukkit.entity.Display.Billboard.*
import org.bukkit.entity.Player
import org.bukkit.util.Transformation
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class NMSImpl : NMS {
    override fun version(): NMSVersion = NMSVersion.V1_20_R1
    override fun create(location: Location): DamageDisplay = DamageDisplayImpl(location)

    private class DamageDisplayImpl(
        location: Location
    ) : DamageDisplay {

        private val connectionMap = ConcurrentHashMap<UUID, ServerGamePacketListenerImpl>()

        private val display: Display = Display.TextDisplay(EntityType.TEXT_DISPLAY, (location.world as CraftWorld).handle).apply { //Remap bug :(
            entityData.set(Display.TextDisplay.DATA_LINE_WIDTH_ID, Int.MAX_VALUE)
            entityData.set(Display.TextDisplay.DATA_BACKGROUND_COLOR_ID, 0)
            moveTo(
                location.x,
                location.y,
                location.z,
                location.yaw,
                0F
            )
        }

        override fun spawn(player: Player) {
            connectionMap.computeIfAbsent(player.uniqueId) {
                (player as CraftPlayer).handle.connection.apply {
                    send(ClientboundAddEntityPacket(
                        display.id,
                        display.uuid,
                        display.x,
                        display.y,
                        display.z,
                        display.xRot,
                        display.yRot,
                        display.type,
                        0,
                        display.deltaMovement,
                        display.yHeadRot.toDouble()
                    ))
                }
            }
        }

        override fun teleport(location: Location) {
            display.moveTo(
                location.x,
                location.y,
                location.z,
                location.yaw,
                0F
            )
        }

        override fun text(component: Component) {
            (display as Display.TextDisplay).text = if (BetterDamage.IS_PAPER) PaperAdventure.asVanilla(component) else CraftChatMessage.fromJSON(GsonComponentSerializer.gson().serialize(component))
        }

        override fun transformation(transformation: Transformation) {
            display.setTransformation(com.mojang.math.Transformation(
                transformation.translation,
                transformation.leftRotation,
                transformation.scale,
                transformation.rightRotation
            ))
        }

        override fun brightness(brightness: org.bukkit.entity.Display.Brightness) {
            display.brightnessOverride = Brightness(brightness.blockLight, brightness.skyLight)
        }

        override fun update() {
            val packet = ClientboundBundlePacket(listOf(
                ClientboundSetEntityDataPacket(display.id, display.entityData.nonDefaultValues ?: emptyList()),
                ClientboundTeleportEntityPacket(display)
            ))
            connectionMap.values.forEach {
                it.send(packet)
            }
        }

        override fun frame(frame: Int) {
            display.interpolationDuration = frame
        }

        override fun opacity(opacity: Byte) {
            (display as Display.TextDisplay).textOpacity = opacity
        }

        override fun remove() {
            val packet = ClientboundRemoveEntitiesPacket(display.id)
            connectionMap.values.removeIf {
                it.send(packet)
                true
            }
        }

        override fun billboard(billboard: org.bukkit.entity.Display.Billboard) {
            display.billboardConstraints = when (billboard) {
                FIXED -> Display.BillboardConstraints.FIXED
                VERTICAL -> Display.BillboardConstraints.VERTICAL
                HORIZONTAL -> Display.BillboardConstraints.HORIZONTAL
                CENTER -> Display.BillboardConstraints.CENTER
            }
        }
    }
}