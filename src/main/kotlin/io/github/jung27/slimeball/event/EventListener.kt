package io.github.jung27.slimeball.event

import io.github.jung27.slimeball.SlimePool
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.Random

class EventListener : Listener {
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK){
            if(event.item?.type == Material.SLIME_BALL){
                val player = event.player
                val random = Random()
                player.launchProjectile(Snowball::class.java, player.location.direction
                    .add(Vector((0.5-random.nextDouble())*0.1, 0.0, (0.5-random.nextDouble())*0.1))) {
                    it.item = ItemStack(Material.SLIME_BALL)
                }
                if(player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL){
                    event.item?.amount = event.item?.amount?.minus(1) ?: 0
                }
                player.swingMainHand()
            }
        }
    }

    @EventHandler
    fun onProjectileHit(event: ProjectileHitEvent){
        val entity = event.entity
        if (entity is Snowball && entity.item.type == Material.SLIME_BALL) {
            val location = entity.location.toBlockLocation().apply {
                x = entity.location.x
                z = entity.location.z
            }
            SlimePool(location)
        }
    }
}