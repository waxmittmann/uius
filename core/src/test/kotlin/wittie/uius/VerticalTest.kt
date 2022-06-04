package wittie.uius

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wittie.uius.ui.*

internal class VerticalTest {




//    @Test
//    fun add() {
//    }

    @Test
    fun oneFixedShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(125))

        assertEquals(
            listOf(PositionedContainer(d, Position2i(Point2i(0, 0), Dimensions2i(100, 125)))),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 200)))
        )
    }

    @Test
    fun twoFixedShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(125))
        vertical.add(d, Fixed(70))

        assertEquals(
            listOf(
                PositionedContainer(d, Position2i(Point2i(0, 0), Dimensions2i(100, 125))),
                PositionedContainer(d, Position2i(Point2i(0, 125), Dimensions2i(100, 70)))
            ),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 200)))
        )
    }

    @Test
    fun oneChildShouldFill() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill())

        assertEquals(
            listOf(PositionedContainer(d, Position2i(Point2i(0, 0), Dimensions2i(100, 200)))),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 200)))
        )
    }

    @Test
    fun twoChildrenShouldShareFill() {
        val vertical = Vertical()
        val d1 = Text("A")
        val d2 = Text("A")
        vertical.add(d1, Fill())
        vertical.add(d2, Fill())

        assertEquals(
            listOf(
                PositionedContainer(d1, Position2i(Point2i(0, 0), Dimensions2i(100, 100))),
                PositionedContainer(d2, Position2i(Point2i(0, 100), Dimensions2i(100, 100))),
            ),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 200)))
        )
    }

    @Test
    fun threeChildrenShouldShareFill() {
        println("Three!")
        val vertical = Vertical()
        val d1 = Text("A")
        val d2 = Text("A")
        vertical.add(d1, Fill(100))
        vertical.add(d2, Fill(200))
        vertical.add(d2, Fill(300))

        assertEquals(
            listOf(
                PositionedContainer(d1, Position2i(Point2i(0, 0), Dimensions2i(100, 100))),
                PositionedContainer(d2, Position2i(Point2i(0, 100), Dimensions2i(100, 200))),
                PositionedContainer(d2, Position2i(Point2i(0, 300), Dimensions2i(100, 300))),
            ),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 600)))
        )
    }

    @Test
    fun onePercentageShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(25))

        assertEquals(
            listOf(PositionedContainer(d, Position2i(Point2i(0, 0), Dimensions2i(100, 50)))),
            vertical.childContainers(Position2i(Point2i(0, 0), Dimensions2i(100, 200)))
        )
    }
}