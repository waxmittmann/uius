package wittie.uius

import com.badlogic.gdx.graphics.Color
import wittie.uius.ui.*
import wittie.uius.ui.event.TriggerResult
import wittie.uius.ui.event.TriggerUnit
import wittie.uius.ui.event.UiEvent
import wittie.uius.ui.event.UiEvents

//fun menu(): RectangularArea {
//    return RectangularArea(
////        background = ColorFill(Color.DARK_GRAY),
//        background = ColorFill(Color.PURPLE),
//        element = Vertical().apply {
//            add(
//                content = RectangularArea(
////                    background = ColorFill(Color.GRAY),
//                    background = ColorFill(Color.GREEN),
////                    layout = Single(Text("Game"))
//                    element = Text("Game")
//                ),
//                fill = Percentage(20)
//            )
//            add(
//                content = RectangularArea(
////                    background = ColorFill(Color.LIGHT_GRAY),
//                    background = ColorFill(Color.BLUE),
//                    element = Text("Options")
//                ),
//                fill = Percentage(20)
//            )
//        }
//    )
//}

fun menu(): Pair<UiContainer, UiEvents> {
    /*
       rectangular().apply {
        .horizontal().apply {
           it.rectangular(fill = Fixed(80), background = Color.GREEN).apply {
                it.text("Game")
           }.rectangular(fill = Fixed(80), background = Color.GREEN).apply {
                it.text("Options")
           }
         }
      }



     */

    val gameKey = RectangularArea(
        background = ColorFill(Color.GREEN),
        element = Text("Game")
    )
    val optionsKey = RectangularArea(
        background = ColorFill(Color.BLUE),
        element = Text("Options")
    )

    val events = UiEvents(mapOf(object : UiEvent {
        override fun trigger(target: PositionedContainer): TriggerResult {
            println("Pressed.")
            return TriggerUnit()
        }
    } to setOf(gameKey)))

    return Pair(RectangularArea(
        listOf(
            Horizontal().apply {
                add(
                    content = gameKey,
                    fill = Fixed(80)
                )
                add(
                    content = optionsKey,
                    fill = Fixed(80)
                )
            },
            Absolute(
                mutableListOf(
                    Pair(Circle(Color.BLUE), Position2i(0, 0, 5, 5))
                )
            ),
        )
    ), events)
}

fun gameArea(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.RED)
    )
}

fun sidePanel(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.PURPLE)
    )
}

fun ui(): Pair<RectangularArea, UiEvents> {
    val (menu, events) = menu()
    return Pair(RectangularArea(
        listOf(
            Vertical().apply {
                add(content = Horizontal().apply {
                    add(content = gameArea(), fill = Fill())
                    add(content = sidePanel(), fill = Fixed(150))
                }, fill = Fill())
                add(content = menu, fill = Fixed(50))
            },
//            Absolute(
//                mutableListOf(
//                    Pair(Circle(Color.BLUE), Position2i(50, 50, 5, 5))
//                )
//            ),
        )), events)
}