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
        }

fun gameArea(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.RED)
    )
}

fun ui(): RectangularArea {
    return RectangularArea(
        element = Vertical().apply {
            add(content = gameArea(), fill = Fill())
            add(content = menu(), fill = Fixed(50))
        }
//        element = Vertical().apply {
//            add(content = menu(), fill = Fixed(120))
//            add(content = gameArea(), fill = Fill())
//        }
    )
}