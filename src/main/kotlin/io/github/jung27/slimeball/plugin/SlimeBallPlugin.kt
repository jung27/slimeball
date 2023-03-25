package io.github.jung27.slimeball.plugin

import io.github.jung27.slimeball.event.EventListener
import org.bukkit.plugin.java.JavaPlugin

class SlimeBallPlugin : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        setModules()
    }

    private fun setModules(){
        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}