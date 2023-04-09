package io.github.jung27.slimeball.event

import io.github.jung27.slimeball.SlimePool
import io.github.jung27.slimeball.plugin.SlimeballPlugin
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.Dispenser
import org.bukkit.entity.Player
import org.bukkit.entity.Snowball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector
import java.util.Random

class EventListener : Listener {
    private val random = Random()

    @EventHandler
    fun onRightClick(event: PlayerInteractEvent){
        if(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK){
            if(event.item?.type == Material.SLIME_BALL){
                val player = event.player
                shootSlimeball(player)
                if(player.gameMode == GameMode.ADVENTURE || player.gameMode == GameMode.SURVIVAL){
                    event.item?.amount = event.item?.amount?.minus(1) ?: 0
                }
                player.swingMainHand()
            }
        }
    }

    @EventHandler
    fun onDispense(event: BlockDispenseEvent){
        if(event.item.type == Material.SLIME_BALL){
            event.isCancelled = true
            val block = event.block
            val state = block.state
            val blockData = block.blockData
            if(blockData is Dispenser){
                shootSlimeball(blockData, event.block.location)
            }
            Bukkit.getScheduler().runTaskLater(SlimeballPlugin.instance, Runnable {
                if(state is org.bukkit.block.Dispenser){
                    state.inventory.removeItem(event.item)
                }
            }, 1)
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

    private fun shootSlimeball(player: Player){
        player.launchProjectile(Snowball::class.java, player.location.direction
            .add(Vector((0.5-random.nextDouble())*0.1, (0.5-random.nextDouble())*0.1, (0.5-random.nextDouble())*0.1))) {
            it.item = ItemStack(Material.SLIME_BALL)
        }
    }

    private fun shootSlimeball(block: Dispenser, loc: Location){
        val vector = when(block.facing){
            BlockFace.DOWN -> Vector(0.0, -1.0, 0.0)
            BlockFace.UP -> Vector(0.0, 1.0, 0.0)
            BlockFace.NORTH -> Vector(0.0, 0.0, -1.0)
            BlockFace.SOUTH -> Vector(0.0, 0.0, 1.0)
            BlockFace.WEST -> Vector(-1.0, 0.0, 0.0)
            BlockFace.EAST -> Vector(1.0, 0.0, 0.0)
            else -> Vector(0.0, 0.0, 0.0)
        }.add(Vector((0.5-random.nextDouble())*0.1, (0.5-random.nextDouble())*0.1, (0.5-random.nextDouble())*0.1))

        val location = loc.add(vector).add(0.5, 0.5, 0.5)
        location.world?.spawn(location, Snowball::class.java) {
            it.item = ItemStack(Material.SLIME_BALL)
            it.velocity = vector
        }
    }
}