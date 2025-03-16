package at.hannibal2.skyhanni.config.features.mining.glacite

import at.hannibal2.skyhanni.config.FeatureToggle
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorColour
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class MineshaftPortalConfig {

    @Expose
    @ConfigOption(name = "Spawn Alert", desc = "Show an alert when you spawn a mineshaft entrance.")
    @ConfigEditorBoolean
    @FeatureToggle
    var alert: Boolean = false

    @Expose
    @ConfigOption(name = "Draw Line", desc = "Draw a line starting at your crosshair to a spawned mineshaft entrance.")
    @ConfigEditorBoolean
    @FeatureToggle
    var drawLine: Boolean = false

    @Expose
    @ConfigOption(name = "Line Color", desc = "Color of the line.")
    @ConfigEditorColour
    var lineColor: String = "0:100:135:195:58"

    @Expose
    @ConfigOption(name = "Line Width", desc = "Width of the line.")
    @ConfigEditorSlider(minStep = 1f, minValue = 1f, maxValue = 10f)
    var lineWidth: Int = 1
}
