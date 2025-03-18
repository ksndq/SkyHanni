package at.hannibal2.skyhanni.features.mining.glacitemineshaft

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.data.PartyApi
import at.hannibal2.skyhanni.events.SecondPassedEvent
import at.hannibal2.skyhanni.events.chat.SkyHanniChatEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniRenderWorldEvent
import at.hannibal2.skyhanni.events.minecraft.WorldChangeEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.ChatUtils
import at.hannibal2.skyhanni.utils.EntityUtils
import at.hannibal2.skyhanni.utils.EntityUtils.canBeSeen
import at.hannibal2.skyhanni.utils.HypixelCommands
import at.hannibal2.skyhanni.utils.LorenzColor
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.RegexUtils.matches
import at.hannibal2.skyhanni.utils.RenderUtils.drawDynamicText
import at.hannibal2.skyhanni.utils.RenderUtils.drawLineToEye
import at.hannibal2.skyhanni.utils.RenderUtils.drawWaypointFilled
import at.hannibal2.skyhanni.utils.SimpleTimeMark
import at.hannibal2.skyhanni.utils.SpecialColor.toSpecialColor
import at.hannibal2.skyhanni.utils.getLorenzVec
import at.hannibal2.skyhanni.utils.repopatterns.RepoPattern
import net.minecraft.entity.item.EntityArmorStand
import kotlin.time.Duration.Companion.seconds

@SkyHanniModule
object MineshaftPortal {
    private val config get() = SkyHanniMod.feature.mining.glaciteMineshaft.mineshaftPortal

    private val patternGroup = RepoPattern.group("features.mining.glacitemineshaft.portalline")

    private var portalEntity: EntityArmorStand? = null
    private var lastSpawn = SimpleTimeMark.farPast()
    private var active = false

    private val timeOut = 30.seconds

    /**
     * REGEX-TEST: §b§lMINESHAFT! §r§7A Mineshaft portal spawned nearby!
     */
    private val spawnPattern by patternGroup.pattern(
        "spawn",
        "§b§lMINESHAFT! §r§7A Mineshaft portal spawned nearby!",
    )

    /**
     * REGEX-TEST: §b[MVP§3+§b] ksndq§f§a's Mineshaft Portal
     */
    private val portalPattern by patternGroup.pattern(
        "portal",
        ".*§f§a's Mineshaft Portal",
    )


    @HandleEvent(onlyOnIsland = IslandType.DWARVEN_MINES)
    fun onChatMessage(event: SkyHanniChatEvent) {
        if (!spawnPattern.matches(event.message)) return

        if (config.notifyParty) {
            if(!PartyApi.partyMembers.isEmpty()) {
                HypixelCommands.partyChat(config.notifyMessage)
            }
        }
        //TODO corpse count and avarge for tracker
        //TODO Pity display move config

        if (config.waypoint) {
            lastSpawn = SimpleTimeMark.now()
            findPortal()
        }
    }

    @HandleEvent(onlyOnIsland = IslandType.DWARVEN_MINES)
    fun onSecondPassed(event: SecondPassedEvent) {
        if (!config.waypoint) return
        if (lastSpawn.passedSince() > timeOut) {
            reset()
            return
        }
        portalEntity?.takeIf { it.canBeSeen() }?.let {
            if (!active) {
                active = true
                ChatUtils.chat("Located the Mineshaft Portal!")
            }
        }
    }


    private fun findPortal() {
        val playerName = LorenzUtils.getPlayerName()
        portalEntity = EntityUtils.getAllEntities()
            .filterIsInstance<EntityArmorStand>()
            .firstOrNull { portalPattern.matches(it.name) && it.name.contains(playerName) }
    }

    @HandleEvent(onlyOnIsland = IslandType.DWARVEN_MINES)
    fun onRenderWorld(event: SkyHanniRenderWorldEvent) {
        if (lastSpawn.passedSince() > timeOut) {
            reset()
            return
        }

        val location = portalEntity?.getLorenzVec() ?: return

        if (config.drawLine) event.drawLineToEye(
            location,
            config.lineColor.toSpecialColor(),
            config.lineWidth,
            true,
        )

        if (!config.waypoint) return

        val remainingSeconds = lastSpawn.plus(timeOut).timeUntil().inWholeSeconds
        val formattedTime = formatRemainingTime(remainingSeconds)

        event.drawWaypointFilled(location.add(-0.5, 0.5, -0.5), LorenzColor.BLUE.toColor(), seeThroughBlocks = true)
        event.drawDynamicText(location.add(-0.5, 0.75, -0.5), "§3Mineshaft Portal$formattedTime", 1.0, hideTooCloseAt = 2.0)
    }

    @HandleEvent
    fun onWorldChange(event: WorldChangeEvent) = reset()

    private fun formatRemainingTime(seconds: Long) =
        if (seconds != 1L) "§7(${seconds}s)" else "§7(${seconds})"

    private fun reset() {
        portalEntity = null
        lastSpawn = SimpleTimeMark.farPast()
        active = false
    }
}
