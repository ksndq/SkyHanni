package at.hannibal2.skyhanni.config.features.mining.glacite

import at.hannibal2.skyhanni.config.FeatureToggle
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.Accordion
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption
import org.lwjgl.input.Keyboard

class GlaciteMineshaftConfig {
    @Expose
    @ConfigOption(name = "Mineshaft Pity Display", desc = "")
    @Accordion
    var mineshaftPityDisplay: MineshaftPityDisplayConfig = MineshaftPityDisplayConfig()

    @Expose
    @ConfigOption(name = "Mineshaft Waypoints", desc = "General waypoints inside the Mineshaft.")
    @Accordion
    var mineshaftWaypoints: MineshaftWaypointsConfig = MineshaftWaypointsConfig()

    @Expose
    @ConfigOption(name = "Mineshaft Portal", desc = "")
    @Accordion
    var mineshaftPortal: MineshaftPortalConfig = MineshaftPortalConfig()

    @Expose
    @ConfigOption(name = "Corpse Locator", desc = "")
    @Accordion
    var corpseLocator: CorpseLocatorConfig = CorpseLocatorConfig()

    @Expose
    @ConfigOption(name = "Corpse Tracker", desc = "")
    @Accordion
    var corpseTracker: CorpseTrackerConfig = CorpseTrackerConfig()

    @Expose
    @ConfigOption(
        name = "Profit Per Corpse",
        desc = "Show profit/loss in chat after each looted corpse in the Mineshaft. Also includes breakdown information on hover."
    )
    @ConfigEditorBoolean
    @FeatureToggle
    var profitPerCorpseLoot: Boolean = true

    @Expose
    @ConfigOption(
        name = "Share Waypoint Location", desc = "Share the location of the nearest waypoint upon key press.\n" +
                "§eYou can share the location even if it has already been shared!"
    )
    @ConfigEditorKeybind(defaultKey = Keyboard.KEY_NONE)
    var shareWaypointLocation: Int = Keyboard.KEY_NONE
}
