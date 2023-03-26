package io.github.jung27.slimeball.plugin

import io.github.jung27.slimeball.event.EventListener
import org.bukkit.plugin.java.JavaPlugin

class SlimeballPlugin : JavaPlugin() {
    override fun onEnable() {
        // Plugin startup logic
        instance = this
        setModules()
    }

    private fun setModules(){
        server.pluginManager.registerEvents(EventListener(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    companion object{
        lateinit var instance: SlimeballPlugin
    }
}