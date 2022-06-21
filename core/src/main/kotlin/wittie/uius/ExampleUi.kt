package wittie.uius

import com.badlogic.gdx.graphics.Color
import wittie.uius.ui.*

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

fun menu(): UiContainer =
    RectangularArea(
        listOf(
            Horizontal().apply {
                add(
                    content = RectangularArea(
//                    background = ColorFill(Color.GRAY),
                        background = ColorFill(Color.GREEN),
//                    layout = Single(Text("Game"))
                        element = Text("Game")
                    ),
                    fill = Fixed(80)
                )
                add(
                    content = RectangularArea(
//                    background = ColorFill(Color.LIGHT_GRAY),
                        background = ColorFill(Color.BLUE),
                        element = Text("Options")
                    ),
                    fill = Fixed(80)
                )
            },
            Absolute(
                mutableListOf(
                    Pair(Circle(Color.BLUE), Position2i(0, 0, 5, 5))
                )
            ),
        )
    )

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

fun ui(): RectangularArea {
    return RectangularArea(
        listOf(
            Vertical().apply {
                add(content = Horizontal().apply {
                    add(content = gameArea(), fill = Fill())
                    add(content = sidePanel(), fill = Fixed(150))
                }, fill = Fill())
                add(content = menu(), fill = Fixed(50))
            },
//            Absolute(
//                mutableListOf(
//                    Pair(Circle(Color.BLUE), Position2i(50, 50, 5, 5))
//                )
//            ),
        ))
}