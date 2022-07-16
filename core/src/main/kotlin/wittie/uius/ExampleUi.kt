package wittie.uius

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Colors
import wittie.uius.ui.*
import wittie.uius.ui.components.MenuBar
import wittie.uius.ui.elements.containers.*
import wittie.uius.ui.elements.drawables.Circle
import wittie.uius.ui.elements.drawables.Text
import wittie.uius.ui.event.TriggerResult
import wittie.uius.ui.event.TriggerUnit
import wittie.uius.ui.event.UiEvent
import wittie.uius.ui.event.UiEvents
import kotlin.random.Random

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

    return Pair(
        RectangularArea(
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
        ), events
    )
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

fun ui2(): Pair<UiContainer, UiEvents> {
    val colors = Colors.getColors()
//    val table = Table(
//        (0 until 10).map { Fill(Random.nextInt(100)) },
//        (0 until 10).map { Fill(Random.nextInt(100)) }
//    )
//    (0 until 10).map { row ->
//        (0 until 10).map { col ->
//            table.fSet(row, col, RectangularArea(ColorFill(Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat(), 1f))))
//        }
//    }

    val ui = Table(
        listOf(Fixed(100), Fixed(600), Fill()),
        listOf(Fixed(150), Fixed(150), Fill()),
    )

    val table = MenuBar(listOf("First", "Second")).container()

//    val table = Table(
//        listOf(Fixed(250), Percentage(10), Percentage(20), Fill()),
//        listOf(Fill(), Fixed(250), Percentage(10), Percentage(20)),
//    )
//        .fSet(0, 0, RectangularArea(ColorFill(Color.RED)))
//        .fSet(0, 1, RectangularArea(ColorFill(Color.GREEN)))
//        .fSet(0, 2, RectangularArea(ColorFill(Color.BLUE)))
//        .fSet(0, 3, RectangularArea(ColorFill(Color.CHARTREUSE)))
//        .fSet(1, 0, RectangularArea(ColorFill(Color.BLACK)))
//        .fSet(1, 1, RectangularArea(ColorFill(Color.ORANGE)))
//        .fSet(1, 2, RectangularArea(ColorFill(Color.BROWN)))
//        .fSet(1, 3, RectangularArea(ColorFill(Color.LIGHT_GRAY)))
//        .fSet(2, 0, RectangularArea(ColorFill(Color.YELLOW)))
//        .fSet(2, 1, RectangularArea(ColorFill(Color.CORAL)))
//        .fSet(2, 2, RectangularArea(ColorFill(Color.FOREST)))
//        .fSet(2, 3, RectangularArea(ColorFill(Color.NAVY)))
//        .fSet(3, 0, RectangularArea(ColorFill(Color.FIREBRICK)))
//        .fSet(3, 1, RectangularArea(ColorFill(Color.VIOLET)))
//        .fSet(3, 2, RectangularArea(ColorFill(Color.WHITE)))
//        .fSet(3, 3, RectangularArea(ColorFill(Color.ROYAL)))

//    val table = Table(
//        listOf(Fixed(250), Fill()),
//        listOf(Fill(), Fixed(250)),
//    )
//        .fSet(0, 0, RectangularArea(ColorFill(Color.RED)))
//        .fSet(0, 1, RectangularArea(ColorFill(Color.GREEN)))
//        .fSet(1, 0, RectangularArea(ColorFill(Color.BLUE)))
//        .fSet(1, 1, RectangularArea(ColorFill(Color.CHARTREUSE)))

    return Pair(table, UiEvents())
}

fun ui(): Pair<RectangularArea, UiEvents> {
    val (menu, events) = menu()
    return Pair(
        RectangularArea(
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
            )
        ), events
    )
}