package io.github.jung27.slimeball.event

import io.github.jung27.slimeball.SlimePool
import io.github.jung27.slimeball.plugin.SlimeballPlugin
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.entity.Snowball
import org.bukkit.entity.TextDisplay
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Transformation
import org.bukkit.util.Vector
import org.joml.Quaternionf
import org.joml.Vector3f
import java.util.Random

class EventListener : Listener {
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_AIR){
            if(event.item?.type == Material.SLIME_BALL){
                val random = Random()
                event.player.launchProjectile(Snowball::class.java, event.player.location.direction
                    .add(Vector((0.5-random.nextDouble())*0.1, 0.0, (0.5-random.nextDouble())*0.1))) {
                    it.item = ItemStack(Material.SLIME_BALL)
                }
            }
        }
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent){
        val entity = event.entity
        if (entity is Snowball && entity.item?.type == Material.SLIME_BALL) {
            val location = entity.location.toBlockLocation().add(0.5, 0.0, 0.5)
            SlimePool(location)
        }
    }
}