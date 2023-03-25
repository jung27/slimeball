package io.github.jung27.slimeball.event

import org.bukkit.Material
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class EventListener : Listener {
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_AIR){
            if(event.item?.type == Material.SLIME_BALL){
                event.player.launchProjectile(Snowball::class.java, event.player.location.direction.multiply(2.0)) {
                    it.item = ItemStack(Material.SLIME_BALL)
                }
            }
        }
    }
}