package wittie.uius.ui

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import wittie.uius.Position2i
import wittie.uius.ui.elements.containers.RectangularArea
import wittie.uius.ui.elements.drawables.Text

internal class RectangularAreaTest {
    @Test
    fun singleDrawableShouldWork() {
        val text = Text("Abc")
        val ra = RectangularArea(listOf(text))

        val pos = Position2i(0, 0, 300, 500)
        assertEquals(
            PositionedContainer(ra, pos, listOf(), listOf(Pair(text, pos)), listOf(Pair(text, pos))),
            ra.positioned(pos)
        )
    }

    @Test
    fun twoDrawablesShouldWork() {
        val text1 = Text("Abc")
        val text2 = Text("Cde")
        val ra = RectangularArea(listOf(text1, text2))

        val pos = Position2i(0, 0, 300, 500)
        assertEquals(
            PositionedContainer(
                ra,
                pos,
                listOf(),
                listOf(Pair(text1, pos), Pair(text2, pos)),
                listOf(Pair(text1, pos), Pair(text2, pos))
            ),
            ra.positioned(pos)
        )
    }

    @Test
    fun singleContainerShouldWork() {
        val nested = RectangularArea()
        val ra = RectangularArea(listOf(nested))

        val pos = Position2i(0, 0, 300, 500)
        assertEquals(
            PositionedContainer(ra, pos, listOf(PositionedContainer(nested, pos))),
            ra.positioned(pos)
        )
    }

    @Test
    fun nestedContainerWithChildrenShouldWork() {
        val text1 = Text("abc")
        val text2 = Text("def")
        val nested = RectangularArea(listOf(text1, text2))
        val ra = RectangularArea(listOf(nested))

        val pos = Position2i(0, 0, 300, 500)
        assertEquals(
            PositionedContainer(
                ra, pos, childContainers = listOf(
                    PositionedContainer(
                        nested, pos,
                        childDrawables = listOf(Pair(text1, pos), Pair(text2, pos)),
                        descendantDrawables = listOf(Pair(text1, pos), Pair(text2, pos))
                    ),
                ),
                descendantDrawables = listOf(Pair(text1, pos), Pair(text2, pos))
            ),
            ra.positioned(pos)
        )
    }

}