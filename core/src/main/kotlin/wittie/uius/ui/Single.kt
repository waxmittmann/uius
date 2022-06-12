//package wittie.uius.ui
//
//import com.badlogic.gdx.graphics.g2d.SpriteBatch
//import wittie.uius.Position2i
//import wittie.uius.ShapeHelper
//
//class Single(private val element: UiContainer) : Layout() {
//    override fun childContainers(position: Position2i): List<PositionedContainer> =
//        listOf(PositionedContainer(element, position))
//
////    override fun children(position: Position2i): List<PositionedDrawable> = listOf()
////        return listOf(PositionedDrawable(this, position))
////    }
//
//    override fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i) {}
//
//    override fun type(): String {
//        return "Single"
//    }
//}
//
//fun toLayout(background: Background): Layout {
//    return Single(background.toRectangle())
//}