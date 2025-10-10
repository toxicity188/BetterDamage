package kr.toxicity.damage.nms.v1_21_R6

import kr.toxicity.library.sharedpackets.PluginBundlePacket
import net.kyori.adventure.key.Key
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBundlePacket

private val KEY = Key.key("betterdamage")

fun Packet<ClientGamePacketListener>.toBundlePacket() = listOf(this).toBundlePacket()
fun Iterable<Packet<ClientGamePacketListener>>.toBundlePacket() = ClientboundBundlePacket(PluginBundlePacket.of(
    KEY,
    this
))