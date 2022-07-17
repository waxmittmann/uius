package wittie.uius.ui.event

import arrow.core.None
import arrow.core.Option
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ui.PositionedContainer
import wittie.uius.ui.UiContainer

sealed interface TriggerResult {
    fun isError(): Boolean
}

class TriggerUnit : TriggerResult  {
    override fun isError(): Boolean = false
}

data class TriggerError(val message: String) : TriggerResult {
    override fun isError(): Boolean = true
}

data class TriggerDebug(val message: String) : TriggerResult {
    override fun isError(): Boolean = false
}

interface UiContainerEvent {
    fun trigger(target: PositionedContainer): TriggerResult
}

interface UiTargetEvent {
    fun trigger(target: Point2i, targetContainer: Option<UiContainer>): TriggerResult
}

// I think this is higher-kinded types, and I don't think kotlin has this.
//fun <S, T, U : Collection> appendToCollectionValueMap(map: Map<S, Collection<T>>, key: S, value: T): Map<S, U<T>> =
//    map.plus(Pair(key, map.getOrDefault(key, setOf()).plus(value)))
fun <S, T> appendToMap(map: Map<S, Set<T>>, key: S, value: T): Map<S, Set<T>> =
    map.plus(Pair(key, map.getOrDefault(key, setOf()).plus(value)))
//data class UiEvents(
//    val eventTargets: Map<UiEvent, Set<UiContainer>> = mapOf()
//) {
//    val targetEvents: Map<UiContainer, Set<UiEvent>> = eventTargets.flatMap { eventToContainers ->
//        eventToContainers.value.map { it to eventToContainers.key }
//    }.groupBy(Pair<UiContainer, UiEvent>::first).mapValues {
//            it.value.map(Pair<UiContainer, UiEvent>::second).toSet()
//        }
//
////    constructor(eventTargets: Map<UiEvent, Set<UiContainer>>) : this(eventTargets,
////        eventTargets.flatMap<UiEvent, Set<UiContainer>, Pair<UiContainer, UiEvent>> { eventToContainers ->
////            eventToContainers.value.map { it to eventToContainers.key }
////        }.groupBy(Pair<UiContainer, UiEvent>::first)
////            .mapValues {
////                it.value.map(Pair<UiContainer, UiEvent>::second).toSet()
////            }
////    )
//
//    // Not super efficient since we are rebuilding the targetEvents map every time, but we probably won't have that
//    // many UI events, nor are we adding / removing too often.
//    fun add(event: UiEvent, target: UiContainer): UiEvents = UiEvents(appendToMap(eventTargets, event, target))
////        UiEvents(eventTargets.plus(Pair(event, eventTargets.getOrDefault(event, setOf()).plus(target))), targetEvents.plus(Pair(target, event)))
////        UiEvents(eventTargets.plus(Pair(event, target)), targetEvents.plus(Pair(target, event)))
//
//    fun containersAndEventsAt(
//        hitAt: Point2i, rootContainer: PositionedContainer
//    ): Set<Pair<PositionedContainer, Set<UiEvent>>> {
//        val hitContainers = rootContainer.detectHit(hitAt)
//        return hitContainers.map { hitContainer ->
//            Pair(
//                hitContainer, targetEvents.getOrDefault(hitContainer.container, emptySet())
//            )
//        }.toSet()
//    }
//
//    fun triggerEventsAt(hitAt: Point2i, rootContainer: PositionedContainer) {
//        containersAndEventsAt(hitAt, rootContainer).forEach { (container, events) ->
//            events.forEach { it.trigger(container) }
//        }
//
////        val hitContainers: Set<PositionedContainer> = rootContainer.detectHit(hitAt)
////        return hitContainers.forEach { hitContainer ->
////            targetEvents.getOrDefault(hitContainer.container, setOf()).forEach { event -> event.trigger(hitContainer)}
////        }
//    }
//
//}d

data class EventTarget(val target: Position2i, val inverted: Boolean = false, val oneshot: Boolean = false,
                       val associatedContainer: Option<UiContainer> = None)

data class UiEvents(
    private val eventTargets: MutableMap<UiContainerEvent, MutableSet<UiContainer>> = mutableMapOf(),
    private val events: MutableList<Pair<UiTargetEvent, EventTarget>> = mutableListOf()
) {
    private val targetEvents: MutableMap<UiContainer, MutableSet<UiContainerEvent>> = eventTargets.flatMap { eventToContainers ->
        eventToContainers.value.map { it to eventToContainers.key }
    }.groupBy(Pair<UiContainer, UiContainerEvent>::first).mapValues {
        it.value.map(Pair<UiContainer, UiContainerEvent>::second).toMutableSet()
    }.toMutableMap()

    // Not super efficient since we are rebuilding the targetEvents map every time, but we probably won't have that
    // many UI events, nor are we adding / removing too often.
    fun add(event: UiContainerEvent, target: UiContainer) {
        eventTargets.getOrPut(event) { mutableSetOf() }.add(target)
        targetEvents.getOrPut(target) { mutableSetOf() }.add(event)
    }

    fun remove(event: UiContainerEvent, target: UiContainer): Boolean {
        if (!eventTargets.containsKey(event) || !targetEvents.containsKey(target))
            return false
        // Force because if they're not in the map, something is very wrong and we want an error.
        eventTargets[event]!!.remove(target)
        targetEvents[target]!!.remove(event)
        return true
    }

    fun remove(event: UiContainerEvent) {
        events.filter { it.first != event }
    }

    fun add(event: UiTargetEvent, target: EventTarget) {
        events += Pair(event, target)
    }

    fun containersAndEventsAt(
        hitAt: Point2i, rootContainer: PositionedContainer
    ): Set<Pair<PositionedContainer, Set<UiContainerEvent>>> {
        val hitContainers = rootContainer.detectHit(hitAt)
        return hitContainers.map { hitContainer ->
            Pair(
                hitContainer, targetEvents.getOrDefault(hitContainer.container, mutableSetOf())
            )
        }.toSet()
    }

    fun triggerEventsAt(hitAt: Point2i, rootContainer: PositionedContainer) {
        containersAndEventsAt(hitAt, rootContainer).forEach { (container, events) ->
            events.forEach { it.trigger(container) }
        }
    }

    fun containersAndEventsAt(
        hitAt: Point2i
    ): List<Pair<UiTargetEvent, EventTarget>> =
        events.filter { (event, target) ->
            val hit = target.target.contains(hitAt)
            if (target.inverted) !hit else hit
        }

    fun triggerEventsAt(hitAt: Point2i) {
        containersAndEventsAt(hitAt).filter { (event, target) ->
            event.trigger(hitAt, target.associatedContainer)
            target.oneshot
        }
    }

}