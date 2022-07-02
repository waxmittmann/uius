package wittie.uius.ui.event

import wittie.uius.Point2i
import wittie.uius.ui.PositionedContainer
import wittie.uius.ui.UiContainer

sealed interface TriggerResult {
    fun isError(): Boolean
}

class TriggerUnit() : TriggerResult  {
    override fun isError(): Boolean = false
}

data class TriggerError(val message: String) : TriggerResult {
    override fun isError(): Boolean = true
}

data class TriggerDebug(val message: String) : TriggerResult {
    override fun isError(): Boolean = false
}

interface UiEvent {
    fun trigger(target: PositionedContainer): TriggerResult
}

// I think this is higher-kinded types, and I don't think kotlin has this.
//fun <S, T, U : Collection> appendToCollectionValueMap(map: Map<S, Collection<T>>, key: S, value: T): Map<S, U<T>> =
//    map.plus(Pair(key, map.getOrDefault(key, setOf()).plus(value)))
fun <S, T> appendToMap(map: Map<S, Set<T>>, key: S, value: T): Map<S, Set<T>> =
    map.plus(Pair(key, map.getOrDefault(key, setOf()).plus(value)))
data class UiEvents(
    val eventTargets: Map<UiEvent, Set<UiContainer>> = mapOf()
) {
    val targetEvents: Map<UiContainer, Set<UiEvent>> = eventTargets.flatMap { eventToContainers ->
        eventToContainers.value.map { it to eventToContainers.key }
    }.groupBy(Pair<UiContainer, UiEvent>::first).mapValues {
            it.value.map(Pair<UiContainer, UiEvent>::second).toSet()
        }

//    constructor(eventTargets: Map<UiEvent, Set<UiContainer>>) : this(eventTargets,
//        eventTargets.flatMap<UiEvent, Set<UiContainer>, Pair<UiContainer, UiEvent>> { eventToContainers ->
//            eventToContainers.value.map { it to eventToContainers.key }
//        }.groupBy(Pair<UiContainer, UiEvent>::first)
//            .mapValues {
//                it.value.map(Pair<UiContainer, UiEvent>::second).toSet()
//            }
//    )

    // Not super efficient since we are rebuilding the targetEvents map every time, but we probably won't have that
    // many UI events, nor are we adding / removing too often.
    fun add(event: UiEvent, target: UiContainer): UiEvents = UiEvents(appendToMap(eventTargets, event, target))
//        UiEvents(eventTargets.plus(Pair(event, eventTargets.getOrDefault(event, setOf()).plus(target))), targetEvents.plus(Pair(target, event)))
//        UiEvents(eventTargets.plus(Pair(event, target)), targetEvents.plus(Pair(target, event)))

    fun containersAndEventsAt(
        hitAt: Point2i, rootContainer: PositionedContainer
    ): Set<Pair<PositionedContainer, Set<UiEvent>>> {
        val hitContainers = rootContainer.detectHit(hitAt)
        return hitContainers.map { hitContainer ->
            Pair(
                hitContainer, targetEvents.getOrDefault(hitContainer.container, emptySet())
            )
        }.toSet()
    }

    fun triggerEventsAt(hitAt: Point2i, rootContainer: PositionedContainer) {
        containersAndEventsAt(hitAt, rootContainer).forEach { (container, events) ->
            events.forEach { it.trigger(container) }
        }

//        val hitContainers: Set<PositionedContainer> = rootContainer.detectHit(hitAt)
//        return hitContainers.forEach { hitContainer ->
//            targetEvents.getOrDefault(hitContainer.container, setOf()).forEach { event -> event.trigger(hitContainer)}
//        }
    }

}