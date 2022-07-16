package wittie.uius.ui.event

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ui.*
import wittie.uius.ui.elements.containers.Fill
import wittie.uius.ui.elements.containers.Fixed
import wittie.uius.ui.elements.containers.Vertical
import wittie.uius.ui.elements.drawables.Text

internal class UiEventsTest {

    data class TestEvent(val id: Int) : UiEvent {
        override fun trigger(target: PositionedContainer): TriggerResult = TriggerError("Don't call me.")
    }

    @Test
    fun targetEventsShouldMirrorEventTargetsForOneEventToManyTargets() {
        val event = TestEvent(1)
        val containerA = Vertical(mutableListOf(Text("a") to Fill()))
        val containerB = Vertical(mutableListOf(Text("b") to Fill()))

        assertEquals(
            UiEvents(
                mapOf(
                    event to setOf(
                        containerA,
                        containerB
                    )
                )
            ).targetEvents, mapOf(containerA to setOf(event), containerB to setOf(event))
        )
    }

    @Test
    fun targetEventsShouldMirrorEventTargetsForManyEventsToOneTarget() {
        val eventA = TestEvent(1)
        val eventB = TestEvent(2)
        val container = Vertical(mutableListOf(Text("a") to Fill()))

        assertEquals(
            UiEvents(
                mapOf(
                    eventA to setOf(
                        container
                    ), eventB to setOf(container)
                )
            ).targetEvents, mapOf(container to setOf(eventA, eventB))
        )
    }

    @Test
    fun targetEventsShouldMirrorEventTargetsForManyToMany() {
        val eventA = TestEvent(1)
        val eventB = TestEvent(2)
        val eventC = TestEvent(3)
        val eventD = TestEvent(4)
        val containerA = Vertical(mutableListOf(Text("a") to Fill()))
        val containerB = Vertical(mutableListOf(Text("a") to Fill()))

        assertEquals(
            UiEvents(
                mapOf(
                    eventA to setOf(containerA, containerB),
                    eventB to setOf(containerA, containerB),
                    eventC to setOf(containerA),
                    eventD to setOf(containerB),
                )
            ).targetEvents,
            mapOf(containerA to setOf(eventA, eventB, eventC), containerB to setOf(eventA, eventB, eventD))
        )
    }

    @Test
    fun containersAndEventsAtShouldReturnHitContainersAndEvents() {
        val eventA = TestEvent(1)
        val eventB = TestEvent(2)
        val eventC = TestEvent(3)
        val childContainerA = Vertical(mutableListOf(Text("a") to Fill()))
        val childContainerB = Vertical(mutableListOf(Text("b") to Fill()))
        val parentContainer = Vertical(mutableListOf(childContainerA to Fixed(100), childContainerA to Fixed(100)))
        val positionedParent = parentContainer.positioned(Position2i(0, 0, 200, 200))

        assertEquals(
            UiEvents(
                mapOf(
                    eventA to setOf(parentContainer),
                    eventB to setOf(childContainerA),
                    eventC to setOf(childContainerB),
                )
            ).containersAndEventsAt(Point2i(20, 90), positionedParent), setOf(
                positionedParent to setOf(eventA),
                positionedParent.childContainers.first() to setOf(eventB)
            )
        )
    }

    @Test
    fun containersAndEventsAtShouldReturnEmptyIfNothingWasHit() {
        val eventA = TestEvent(1)
        val eventB = TestEvent(2)
        val eventC = TestEvent(3)
        val childContainerA = Vertical(mutableListOf(Text("a") to Fill()))
        val childContainerB = Vertical(mutableListOf(Text("b") to Fill()))
        val parentContainer = Vertical(mutableListOf(childContainerA to Fixed(100), childContainerA to Fixed(100)))
        val positionedParent = parentContainer.positioned(Position2i(0, 0, 200, 200))

        assertEquals(
            UiEvents(
                mapOf(
                    eventA to setOf(parentContainer),
                    eventB to setOf(childContainerA),
                    eventC to setOf(childContainerB),
                )
            ).containersAndEventsAt(Point2i(210, 90), positionedParent), emptySet<Pair<PositionedContainer, UiEvent>>()
        )
    }
}