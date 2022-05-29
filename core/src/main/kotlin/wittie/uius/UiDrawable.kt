package wittie.uius

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.SpriteBatch

abstract class UiDrawable {
//    abstract fun draw(batch: Batch, dims: Dimensions2i)

    abstract fun children(position: Position2i): List<PositionedDrawable> // = listOf()

    fun <S> visit(position: Position2i, localFn: (PositionedDrawable) -> S, foldFn: (S, S) -> S): S {
        return children(position).fold(localFn(PositionedDrawable(this, position))) { s: S, child: PositionedDrawable ->
            val newS = child.uiDrawable.visit(child.position, localFn, foldFn)
            foldFn(s, newS)
        }

//        return children().fold(localFn(dims, this), { c: PositionedDrawable -> c.uiDrawable.visit(c.dims) })
    }

    abstract fun drawContent(batch: SpriteBatch, shapeHelper: ShapeHelper, position: Position2i)
    abstract fun type(): String
}