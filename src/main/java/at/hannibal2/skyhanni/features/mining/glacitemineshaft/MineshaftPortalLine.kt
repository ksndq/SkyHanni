package at.hannibal2.skyhanni.features.mining.glacitemineshaft

import at.hannibal2.skyhanni.SkyHanniMod
import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.data.IslandType
import at.hannibal2.skyhanni.events.chat.SkyHanniChatEvent
import at.hannibal2.skyhanni.events.minecraft.SkyHanniRenderWorldEvent
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.EntityUtils
import at.hannibal2.skyhanni.utils.LocationUtils.distanceToPlayer
import at.hannibal2.skyhanni.utils.LorenzUtils
import at.hannibal2.skyhanni.utils.LorenzVec
import at.hannibal2.skyhanni.utils.RegexUtils.matches
import at.hannibal2.skyhanni.utils.RenderUtils.drawLineToEye
import at.hannibal2.skyhanni.utils.SpecialColor.toSpecialColor
import at.hannibal2.skyhanni.utils.repopatterns.RepoPattern
import at.hannibal2.skyhanni.utils.toLorenzVec

@SkyHanniModule
object MineshaftPortalLine {
    private val config get() = SkyHanniMod.feature.mining.glaciteMineshaft.mineshaftPortal

    private val patternGroup = RepoPattern.group("features.mining.glacitemineshaft.portalline")

    private var portalLocation: LorenzVec? = null

    /**
     * REGEX-TEST: §b§lMINESHAFT! §r§7A Mineshaft portal spawned nearby!
     * REGEX-TEST: §r§aYou found a §r§bGlacite Mineshaft §r§aportal!
     */
    private val spawnPattern by patternGroup.pattern(
        "spawn",
        "§b§lMINESHAFT! §r§7A Mineshaft portal spawned nearby!",
    )

    /**
     * REGEX-TEST: §b§lMINESHAFT! §r§7A Mineshaft portal spawned nearby!
     */
    private val portalPattern by patternGroup.pattern(
        "portal",
        ".*§f§a's Mineshaft Portal",
    )


    @HandleEvent(onlyOnIsland = IslandType.DWARVEN_MINES)
    fun onChatMessage(event: SkyHanniChatEvent) {
        if (!config.drawLine) return
        if (!spawnPattern.matches(event.message)) return

        for (entity in EntityUtils.getAllEntities()) {
            if (!portalPattern.matches(entity.name)) continue
            if (!entity.name.contains(LorenzUtils.getPlayerName())) continue
            portalLocation = entity.positionVector.toLorenzVec()
        }
    }


    @HandleEvent(onlyOnIsland = IslandType.DWARVEN_MINES)
    fun onRenderWorld(event: SkyHanniRenderWorldEvent) {
        if (!config.drawLine) return
        val location = portalLocation ?: return
        location.distanceToPlayer().let {
            if (it < 15) {
                event.drawLineToEye(
                    location.up(1.54),
                    config.lineColor.toSpecialColor(),
                    config.lineWidth,
                    true,
                )
            }
        }
    }

}
