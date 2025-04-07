package kr.toxicity.damage.nms.v1_20_R3

import io.papermc.paper.adventure.PaperAdventure
import kr.toxicity.damage.api.BetterDamage
import kr.toxicity.damage.api.nms.DamageDisplay
import kr.toxicity.damage.api.nms.NMS
import kr.toxicity.damage.api.nms.NMSVersion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Brightness
import net.minecraft.world.entity.Display
import net.minecraft.world.entity.EntityType
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage
import org.bukkit.entity.Display.Billboard.*
import org.bukkit.entity.Player
import org.bukkit.util.Transformation

class NMSImpl : NMS {
    override fun version(): NMSVersion = NMSVersion.V1_20_R3
    override fun create(player: Player, location: Location): DamageDisplay = DamageDisplayImpl((player as CraftPlayer).handle, location)

    private class DamageDisplayImpl(
        player: ServerPlayer,
        location: Location
    ) : DamageDisplay {
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
        private val connection = player.connection.apply {
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
            connection.send(ClientboundBundlePacket(listOf(
                ClientboundSetEntityDataPacket(display.id, display.entityData.nonDefaultValues ?: emptyList()),
                ClientboundTeleportEntityPacket(display)
            )))
        }

        override fun frame(frame: Int) {
            display.transformationInterpolationDuration = frame
            display.entityData.set(Display.DATA_POS_ROT_INTERPOLATION_DURATION_ID, frame)
        }

        override fun opacity(opacity: Byte) {
            (display as Display.TextDisplay).textOpacity = opacity
        }

        override fun remove() {
            connection.send(ClientboundRemoveEntitiesPacket(display.id))
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