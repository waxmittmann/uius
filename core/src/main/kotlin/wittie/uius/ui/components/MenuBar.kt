package wittie.uius.ui.components

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import arrow.core.getOrElse
import com.badlogic.gdx.graphics.Color
import wittie.uius.Point2i
import wittie.uius.ui.ColorFill
import wittie.uius.ui.PositionedContainer
import wittie.uius.ui.UiContainer
import wittie.uius.ui.elements.containers.Fill
import wittie.uius.ui.elements.containers.Fixed
import wittie.uius.ui.elements.containers.RectangularArea
import wittie.uius.ui.elements.containers.Table
import wittie.uius.ui.elements.drawables.Text
import wittie.uius.ui.event.*

//data class MenuItem(val name: String)

class MenuContainerEvent(val itemAt: Int, val menuBar: MenuBar) : UiContainerEvent {
    override fun trigger(target: PositionedContainer): TriggerResult {
        println("Hit " + itemAt)
        menuBar.toggleItem(itemAt)
        return TriggerUnit()
    }
}

class HideActiveEvent(val menuBar: MenuBar) : UiTargetEvent {
    override fun trigger(target: Point2i, targetContainer: Option<UiContainer>): TriggerResult {
        menuBar.hideActive()
        return TriggerUnit()
    }
}

class MenuBar(menuElements: List<String>) {
    private val root = Table(listOf(Fixed(100), Fill()), menuElements.map { Fixed(100) } + Fill(), false)
    private val dropDown = Table(listOf(Fixed(100), Fixed(300), Fill()), menuElements.map { Fixed(100) } + Fill(), false)
//    private val active: MutableList<Boolean> = menuElements.map { false }.toMutableList()
    private var active: Option<Int> = None
    val events: UiEvents = UiEvents()

    init {
        menuElements.withIndex().forEach { text ->
            val menuButtonArea = RectangularArea(
                ColorFill(
                    Color.ORANGE
                ), Text(text.value)
            )
            events.add(MenuContainerEvent(text.index, this), menuButtonArea)
            root.set(0, text.index, menuButtonArea)
        }
    }

    fun container(): UiContainer = RectangularArea(listOf(root, dropDown))
    fun toggleItem(itemAt: Int) {
        if (active.getOrElse { -1 } == itemAt) {
            dropDown.unset(1, itemAt)
            active = None
        } else {
            active.map { activeIndex ->
                dropDown.unset(1, activeIndex)
            }
            dropDown.set(1, itemAt, RectangularArea(ColorFill(Color.ORANGE)))
            active = Some(itemAt)
        }
        events.add(Pair(HideActiveEvent(this), EventTarget()))

/*
        if (active[itemAt]) {
            dropDown.set(1, itemAt, RectangularArea(ColorFill(Color.ORANGE)))
        } else {
            dropDown.unset(1, itemAt)
        }
        active[itemAt] = !active[itemAt]
*/
    }

    fun hideActive() {
        active.map { dropDown.unset(1, it) }
        active = None
    }
}