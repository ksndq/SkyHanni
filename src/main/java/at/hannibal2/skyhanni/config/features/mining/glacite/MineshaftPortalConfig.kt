package at.hannibal2.skyhanni.config.features.mining.glacite

import at.hannibal2.skyhanni.config.FeatureToggle
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorText
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MineshaftPortalConfig {

    @Expose
    @ConfigOption(name = "Notify Party", desc = "Send a message about spawning a mineshaft in party chat.")
    @ConfigEditorBoolean
    @FeatureToggle
    var notifyParty: Boolean = false

    @Expose
    @ConfigOption(name = "Party Message", desc = "Send a message about spawning a mineshaft in party chat.")
    @ConfigEditorText
    @FeatureToggle
    var notifyMessage: String = "Spawned a Mineshaft!"

    @Expose
    @ConfigOption(name = "Waypoint", desc = "Mark spawned Mineshaft Portals within line of sight with a waypoint.")
    @ConfigEditorBoolean
    @FeatureToggle
    var waypoint: Boolean = false

    @Expose
    @ConfigOption(name = "Draw Line", desc = "Draw a line starting at your crosshair to a spawned Mineshaft Portal.")
    @ConfigEditorBoolean
    @FeatureToggle
    var drawLine: Boolean = false

    @Expose
    @ConfigOption(name = "Line Color", desc = "Color of the line.")
    @ConfigEditorColour
    var lineColor: String = "0:185:232:234:90"

    @Expose
    @ConfigOption(name = "Line Width", desc = "Width of the line.")
    @ConfigEditorSlider(minStep = 1f, minValue = 1f, maxValue = 10f)
    var lineWidth: Int = 5
}
