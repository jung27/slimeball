package io.github.jung27.slimeball

import io.github.jung27.slimeball.plugin.SlimeballPlugin
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ItemDisplay
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Transformation
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.Random

class SlimePool(private val loc: Location) {
    lateinit var display : ItemDisplay
    private var age = 0

    init {
        loc.world.spawn(loc, ItemDisplay::class.java) {
            it.itemStack = ItemStack(Material.SLIME_BALL)

            it.interpolationDelay = 0
            it.interpolationDuration = 10
            it.transformation = Transformation(
                Vector3f(0.0f, Random().nextFloat()*0.05f, 0.0f),
                Quaternionf(-0.7f, 0.0f, 0.0f, 0.7f),
                Vector3f(0.1f, 0.1f, 0.1f),
                Quaternionf(0.0f, 0.0f, 0.0f, 1.0f)
            )

            Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                it.transformation = it.transformation.apply {
                    scale.set(3f, 3f, 3f)
                }
            }, 2L)

            display = it
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
                    with(display) {
                        interpolationDelay = 0
                        interpolationDuration = 10
                        transformation = transformation.apply {
                            scale.mul(0f, 0f, 0f)
                        }
                    }
                    Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                        display.remove()
                    }, 10L)
                    this.cancel()
                }
            }
        }.runTaskTimer(SlimeballPlugin.instance, 0L, 1L)
    }
}