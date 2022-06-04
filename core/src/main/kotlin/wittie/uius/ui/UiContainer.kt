package wittie.uius.ui

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import wittie.uius.Position2i
import wittie.uius.ShapeHelper


//abstract class PositionedElement

//data class PositionedDrawable(val drawable: UiDrawable, val position: Position2i)

data class PositionedContainer(val container: UiContainer, val position: Position2i,
                               val childContainers: Set<PositionedContainer>,
                               val childDrawables: Map<UiDrawable, Position2i>)


interface UiElement

abstract class UiDrawable : UiElement {
    abstract fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i)
}

abstract class UiContainer : UiElement {
//    abstract fun draw(batch: Batch, dims: Dimensions2i)

    abstract fun childDrawables(): List<UiDrawable>

    abstract fun childContainers(position: Position2i): List<PositionedContainer>

    abstract fun positioned(position: Position2i): PositionedContainer

//    fun positionedElements(position: Position2i)

//    fun <S, T, U> visit(position: Position2i,
//                        drawableFn: (PositionedDrawable) -> S,
//                        containerFn: (PositionedContainer) -> T,
//                        drawableFoldFn: (S, U) -> U,
//                        containerFoldFn: (T, U) -> U): U {
//        childContainers(position)
//
//    }
//
//    fun <S, T, U> visit(position: Position2i, drawableFn: (PositionedDrawable) -> S, containerFn: (PositionedContainer) -> T, drawableFoldFn: (S, U) -> U, containerFoldFn: (T, U) -> U): U {
//        return childContainers(position).fold(containerFn(PositionedContainer(this, position))) { s: S, child: PositionedContainer ->
//            val newS = child.uiContainer.visit(child.position, containerFn, foldFn)
//            foldFn(s, newS)
//        }
//    }

//    fun <S> postVisit(position: Position2i, localFn: (PositionedDrawable) -> S, foldFn: (S, S) -> S): S {
//        return children(position).fold(localFn(PositionedDrawable(this, position))) { s: S, child: PositionedDrawable ->
//            val newS = child.uiDrawable.visit(child.position, localFn, foldFn)
//            foldFn(s, newS)
//        }
//    }

    abstract fun type(): String
}