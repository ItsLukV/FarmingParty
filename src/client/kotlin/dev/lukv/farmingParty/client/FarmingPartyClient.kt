package dev.lukv.farmingParty.client

import com.google.common.util.concurrent.AbstractScheduledService
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component

class FarmingPartyClient : ClientModInitializer {

    override fun onInitializeClient() {
           ClientReceiveMessageEvents.CHAT.register { message, signedMessage, sender, params, timestamp ->
            val text = ChatFormatting.stripFormatting(message.string) ?: return@register
            handleMessage(text)
        }

        ClientReceiveMessageEvents.GAME.register { message, overlay ->
            if (overlay) return@register // action bar text, not chat
            val text = ChatFormatting.stripFormatting(message.string) ?: return@register
            handleMessage(text)
        }
    }

    val partyCommandRegex = Regex("""^Party > (?:\[([^\]]+)\] )?(\w{1,16}): !(\w+)(?: (.*))?$""")

    fun handleMessage(rawMessage: String) {
        val message = rawMessage.replace(Regex("§."), "")

        val match = partyCommandRegex.matchEntire(message) ?: return
        val (rank, username, command, args) = match.destructured
        val connection = Minecraft.getInstance().player?.connection ?: return

        when (command.lowercase()) {
            "pp" -> {
                val profit = ProfitHolder.pestProfit
                connection.sendCommand("pc Pest profit: ${profit.toLong()}")
            }
            else -> println("Unknown command: $command")
        }
    }
}
