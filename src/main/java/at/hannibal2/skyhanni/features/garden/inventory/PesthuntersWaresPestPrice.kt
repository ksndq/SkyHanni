package at.hannibal2.skyhanni.features.garden.inventory

import at.hannibal2.skyhanni.api.event.HandleEvent
import at.hannibal2.skyhanni.events.GuiRenderEvent
import at.hannibal2.skyhanni.events.InventoryCloseEvent
import at.hannibal2.skyhanni.events.InventoryFullyOpenedEvent
import at.hannibal2.skyhanni.features.garden.GardenApi
import at.hannibal2.skyhanni.skyhannimodule.SkyHanniModule
import at.hannibal2.skyhanni.utils.DisplayTableEntry
import at.hannibal2.skyhanni.utils.InventoryDetector
import at.hannibal2.skyhanni.utils.ItemPriceSource
import at.hannibal2.skyhanni.utils.ItemPriceUtils.getPrice
import at.hannibal2.skyhanni.utils.ItemPriceUtils.getPriceOrNull
import at.hannibal2.skyhanni.utils.ItemUtils.getInternalName
import at.hannibal2.skyhanni.utils.ItemUtils.getLore
import at.hannibal2.skyhanni.utils.ItemUtils.loreCosts
import at.hannibal2.skyhanni.utils.ItemUtils.repoItemName
import at.hannibal2.skyhanni.utils.NumberUtil.addSeparators
import at.hannibal2.skyhanni.utils.NumberUtil.formatInt
import at.hannibal2.skyhanni.utils.NumberUtil.shortFormat
import at.hannibal2.skyhanni.utils.RegexUtils.matchMatcher
import at.hannibal2.skyhanni.utils.RenderUtils.renderRenderables
import at.hannibal2.skyhanni.utils.renderables.Renderable
import at.hannibal2.skyhanni.utils.renderables.RenderableString
import at.hannibal2.skyhanni.utils.renderables.RenderableUtils
import at.hannibal2.skyhanni.utils.renderables.RenderableUtils.addRenderableButton
import at.hannibal2.skyhanni.utils.repopatterns.RepoPattern
import net.minecraft.item.ItemStack

@SkyHanniModule
object PesthuntersWaresPestPrice {

    private val config get() = GardenApi.config.pesthuntersWares
    private val patternGroup = RepoPattern.group("garden.inventory.pesthunterswares")
    private var display = emptyList<Renderable>()
    private var currentPriceSource = ItemPriceSource.BAZAAR_INSTANT_SELL

    private var cachedInventoryItems: Map<Int, ItemStack>? = null

    /**
     * REGEX-TEST: §225 Pests
     */
    private val pestPattern by patternGroup.pattern(
        "pests",
        "§2(?<amount>.*) Pests",
    )

    private val pesthuntersWaresInventory = InventoryDetector { name -> name == "Pesthunter's Wares" }

    @HandleEvent
    fun onInventoryFullyOpened(event: InventoryFullyOpenedEvent) {
        if (!pesthuntersWaresInventory.isInside()) return
        if (!isEnabled()) return

        cachedInventoryItems = event.inventoryItems
        updateDisplay()
    }

    @HandleEvent
    fun onInventoryClosed(event: InventoryCloseEvent) {
        cachedInventoryItems = null
        display = emptyList()
    }

    private fun updateDisplay() {
        val inventoryItems = cachedInventoryItems ?: return
        val table = buildTable(inventoryItems)
        display = buildRenderables(table)
    }

    private fun buildTable(inventoryItems: Map<Int, ItemStack>): List<DisplayTableEntry> {
        val table = mutableListOf<DisplayTableEntry>()
        inventoryItems.forEach { (slot, item) ->
            val lore = item.getLore()
            val otherItemsPrice = item.loreCosts().sumOf { it.getPrice() }.takeIf { it != -1.0 }

            lore.forEach { line ->
                pestPattern.matchMatcher(line) {
                    val pestAmount = group("amount").formatInt()
                    val internalName = item.getInternalName()
                    val itemPrice = internalName.getPriceOrNull(currentPriceSource) ?: return@matchMatcher

                    val profit = itemPrice - (otherItemsPrice ?: 0.0)
                    val factor = profit / pestAmount
                    val perFormat = factor.shortFormat()

                    val itemName = item.repoItemName
                    val hover = buildHoverText(itemName, itemPrice, otherItemsPrice, profit, pestAmount, perFormat)

                    table.add(
                        DisplayTableEntry(
                            "$itemName§f:",
                            "§6§l$perFormat",
                            factor,
                            internalName,
                            hover,
                            highlightsOnHoverSlots = listOf(slot),
                        ),
                    )
                }
            }
        }
        return table.sortedByDescending { it.sort }
    }

    private fun buildHoverText(
        itemName: String,
        itemPrice: Double,
        otherItemsPrice: Double?,
        profit: Double,
        pestAmount: Int,
        perFormat: String,
    ): List<String> = buildList {
        add(itemName)
        add("")
        add("§7Item price: §6${itemPrice.shortFormat()}")
        otherItemsPrice?.let { add("§7Additional cost: §6${it.shortFormat()}") }
        add("§7Profit per purchase: §6${profit.shortFormat()}")
        add("")
        add("§7Pest amount: §2${pestAmount.addSeparators()}")
        add("§7Profit per pest: §6$perFormat")
    }

    private fun buildRenderables(table: List<DisplayTableEntry>): List<Renderable> = buildList {
        addRenderableButton<ItemPriceSource>(
            "§ePrice Source",
            current = currentPriceSource,
            onChange = {
                currentPriceSource = it
                updateDisplay()
            },
        )
        add(RenderableString("§eCoins per Pest§f:"))
        add(RenderableUtils.fillTable(table, padding = 5, itemScale = config.itemScale))
    }

    @HandleEvent
    fun onBackgroundDraw(event: GuiRenderEvent.ChestGuiOverlayRenderEvent) {
        if (!isEnabled() || !pesthuntersWaresInventory.isInside()) return
        config.pestPricePos.renderRenderables(
            display,
            extraSpace = 5,
            posLabel = "Pesthunter's Wares Pest Price",
        )
    }

    private fun isEnabled() = GardenApi.inGarden() && config.pestPrice
}
