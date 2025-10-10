package kr.toxicity.damage.nms.v1_21_R6

import io.papermc.paper.adventure.PaperAdventure
import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.nms.DamageDisplay
import kr.toxicity.damage.api.nms.NMS
import kr.toxicity.damage.api.nms.NMSVersion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.util.Brightness
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.PositionMoveRotation
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.craftbukkit.util.CraftChatMessage
import org.bukkit.entity.Display.Billboard.*
import org.bukkit.entity.Player
import org.bukkit.util.Transformation
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class NMSImpl : NMS {
    override fun version(): NMSVersion = NMSVersion.V1_21_R5
    override fun create(location: Location): DamageDisplay = DamageDisplayImpl(location)

    private class DamageDisplayImpl(
        location: Location
    ) : DamageDisplay {

        private val connectionMap = ConcurrentHashMap<UUID, ServerGamePacketListenerImpl>()

        private fun Display.moveTo(x: Double, y: Double, z: Double, yaw: Float, pitch: Float) {
            setPos(x, y, z)
            yRot = yaw
            xRot = pitch
        }

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
                    ).toBundlePacket())
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
            val sync = ClientboundEntityPositionSyncPacket(display.id, PositionMoveRotation.of(display), display.onGround)
            val packet = display.entityData.packDirty()?.let {
                listOf(
                    sync,
                    ClientboundSetEntityDataPacket(display.id, it)
                ).toBundlePacket()
            } ?: sync
            connectionMap.values.forEach {
                it.send(packet)
            }
        }

        override fun frame(frame: Int) {
            display.transformationInterpolationDuration = frame
            display.entityData.set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, frame)
        }

        override fun opacity(opacity: Byte) {
            (display as Display.TextDisplay).textOpacity = opacity
        }

        override fun remove() {
            val packet = ClientboundRemoveEntitiesPacket(display.id).toBundlePacket()
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