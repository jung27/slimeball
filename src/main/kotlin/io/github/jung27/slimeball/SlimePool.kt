package io.github.jung27.slimeball

import io.github.jung27.slimeball.plugin.SlimeballPlugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.TextDisplay
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f

class SlimePool(private val loc: Location) {
    private val displays = mutableListOf<TextDisplay>()
    private var age = 0

    init {
        loc.world.spawn(loc, TextDisplay::class.java) {
            it.text(Component.text("❶").color(NamedTextColor.GREEN))
            it.backgroundColor = Color.fromARGB(0, 0, 0, 0)

            it.interpolationDelay = 0
            it.interpolationDuration = 10
            it.transformation = Transformation(
                Vector3f(0.0f, 0.0f, 0.0f),
                Quaternionf(-0.7f, 0.0f, 0.0f, 0.7f),
                Vector3f(1f, 1f, 1f),
                Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)
            )

            Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                it.transformation = it.transformation.apply {
                    scale.mul(10f, 10f, 10f)
                    translation.add(-0.2f, 0f, 1.5f)
                }
            }, 2L)

            displays.add(it)
        }
        loc.world.spawn(loc, TextDisplay::class.java) {
            it.text(Component.text("■").color(NamedTextColor.GREEN))
            it.backgroundColor = Color.fromARGB(0, 0, 0, 0)

            it.interpolationDelay = 0
            it.interpolationDuration = 10
            it.transformation = Transformation(
                Vector3f(0.0f, 0.0f, 0.0f),
                Quaternionf(-0.7f, 0.0f, 0.0f, 0.7f),
                Vector3f(1f, 1f, 1f),
                Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)
            )

            Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                it.transformation = it.transformation.apply {
                    scale.mul(10f, 10f, 10f)
                    translation.add(-0.2f, 0f, 1.5f)
                }
            }, 2L)

            displays.add(it)
        }

        object : BukkitRunnable() {
            override fun run() {
                loc.getNearbyEntitiesByType(LivingEntity::
                class.java, 1.0).forEach{
                    it.addPotionEffect(PotionEffect(PotionEffectType.SLOW, 3, 4, false, false, false))
                }
                age++
                if(age >= 60)
                {
                    displays.forEach {
                        it.interpolationDelay = 0
                        it.interpolationDuration = 10
                        it.transformation = it.transformation.apply {
                            scale.mul(0.1f, 0.1f, 0.1f)
                            translation.add(0.2f, 0f, -1.5f)
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                        displays.forEach {
                            it.remove()
                        }
                        displays.clear()
                    }, 10L)
                    this.cancel()
                }
            }
        }.runTaskTimer(SlimeballPlugin.instance, 0L, 1L)
    }
}