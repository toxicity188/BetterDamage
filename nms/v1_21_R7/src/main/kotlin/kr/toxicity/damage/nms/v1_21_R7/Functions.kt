package kr.toxicity.damage.nms.v1_21_R7

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket

internal fun Packet<ClientGamePacketListener>.toBundlePacket() = listOf(this).toBundlePacket()
internal fun Iterable<Packet<ClientGamePacketListener>>.toBundlePacket() = ClientboundBundlePacket(PluginBundlePacket(this))

internal data class PluginBundlePacket(
    val iterable: Iterable<Packet<ClientGamePacketListener>>
) : Keyed, Iterable<Packet<ClientGamePacketListener>> by iterable {
    private companion object {
        val key = Key.key("betterdamage")
    }
    override fun key(): Key = key
}