package kr.toxicity.damage.compatibility.mythiclib

import io.lumine.mythic.lib.api.event.PlayerAttackEvent
import io.lumine.mythic.lib.player.PlayerMetadata
import kr.toxicity.damage.api.data.DamageEffectData
import kr.toxicity.damage.api.trigger.DamageTrigger
import kr.toxicity.damage.api.trigger.DamageTriggerType
import kr.toxicity.damage.compatibility.Compatibility
import kr.toxicity.damage.manager.TriggerManagerImpl

class MythicLibCompatibility : Compatibility {
    override fun start() {
        TriggerManagerImpl.addTrigger(DamageTrigger.builder(PlayerAttackEvent::class.java)
            .type(DamageTriggerType("mythiclib_damage"))
            .mapper {
                it.attack.run {
                    val attacker = (attacker as? PlayerMetadata)?.player ?: return@mapper null
                    if (damage.isWeaponCriticalStrike || damage.isSkillCriticalStrike) return@mapper null
                    else DamageEffectData(attacker, target, damage.damage)
                }
            }
            .build())
        TriggerManagerImpl.addTrigger(DamageTrigger.builder(PlayerAttackEvent::class.java)
            .type(DamageTriggerType("mythiclib_crit_damage"))
            .mapper {
                it.attack.run {
                    val attacker = (attacker as? PlayerMetadata)?.player ?: return@mapper null
                    if (damage.isWeaponCriticalStrike || damage.isSkillCriticalStrike) DamageEffectData(attacker, target, damage.damage)
                    else null
                }
            }
            .build())
    }
}