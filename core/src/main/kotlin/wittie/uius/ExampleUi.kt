package wittie.uius

import com.badlogic.gdx.graphics.Color
import wittie.uius.ui.*

fun menu(): RectangularArea {
    return RectangularArea(
//        background = ColorFill(Color.DARK_GRAY),
        background = ColorFill(Color.PURPLE),
        layout = Vertical().apply {
            add(
                content = RectangularArea(
//                    background = ColorFill(Color.GRAY),
                    background = ColorFill(Color.GREEN),
                    layout = Single(Text("Game"))
                ),
                fill = Percentage(20)
            )
            add(
                content = RectangularArea(
//                    background = ColorFill(Color.LIGHT_GRAY),
                    background = ColorFill(Color.BLUE),
                    layout = Single(Text("Options"))
                ),
                fill = Percentage(20)
            )
        }
    )
}

fun gameArea(): RectangularArea {
    return RectangularArea(
        background = ColorFill(Color.RED)
    )
}

fun ui(): RectangularArea {
    return RectangularArea(
        layout = Horizontal().apply {
            add(content = menu(), fill = Fixed(120))
            add(content = gameArea(), fill = Fill())
        }
    )
}