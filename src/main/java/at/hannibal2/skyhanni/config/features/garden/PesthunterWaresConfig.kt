package at.hannibal2.skyhanni.config.features.garden

import at.hannibal2.skyhanni.config.FeatureToggle
import at.hannibal2.skyhanni.config.core.config.Position
import at.hannibal2.skyhanni.utils.ItemPriceSource
import com.google.gson.annotations.Expose
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorBoolean
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorDropdown
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorSlider
import io.github.notenoughupdates.moulconfig.annotations.ConfigLink
import io.github.notenoughupdates.moulconfig.annotations.ConfigOption

class PesthuntersWaresConfig {
    @Expose
    @ConfigOption(name = "Pest Price", desc = "Show pest to coin prices inside the Pesthunter's Wares inventory.")
    @ConfigEditorBoolean
    @FeatureToggle
    var pestPrice: Boolean = false

    @Expose
    @ConfigOption(name = "Item Scale", desc = "Change the size of the items.")
    @ConfigEditorSlider(minValue = 0.3f, maxValue = 5f, minStep = 0.1f)
    var itemScale: Double = 1.0

    @Expose
    @ConfigLink(owner = PesthuntersWaresConfig::class, field = "pestPrice")
    var pestPricePos: Position = Position(211, 132)
}
