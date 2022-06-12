package wittie.uius

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wittie.uius.ui.*

internal class VerticalTest {

    @Test
    fun singleContainerShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(Pair(d, p.withHeight(125)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun singleContainerWithTwoItemsShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fixed(75))
        vertical.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(Pair(d, p.withHeight(75)), Pair(d, p.withHeight(125)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun oneChildShouldFill() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(Pair(d, p.withHeight(200)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun twoChildrenShouldShareFill() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill())
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(Pair(d, p.withHeight(100)), Pair(d, p.withHeight(100)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun threeChildrenShouldShareFillProportionally() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Fill(25))
        vertical.add(d, Fill(60))
        vertical.add(d, Fill(15))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical,
                p,
                setOf(Pair(d, p.withHeight(50)), Pair(d, p.withHeight(120)), Pair(d, p.withHeight(30)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun singlePercentageShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(Pair(d, p.withHeight(120)))
            ), vertical.positioned(p)
        )
    }

    @Test
    fun percentageAndFillShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        vertical.add(d, Fill())
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(
                    Pair(d, p.withHeight(120)),
                    Pair(d, p.withHeight(80))
                )
            ), vertical.positioned(p)
        )
    }

    @Test
    fun percentageAndFixedShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(60))
        vertical.add(d, Fixed(50))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(
                vertical, p, setOf(
                    Pair(d, p.withHeight(120)),
                    Pair(d, p.withHeight(50))
                )
            ), vertical.positioned(p)
        )
    }

    private fun nonNestedContainer(container: UiContainer, position: Position2i, children: Set<Pair<Text, Position2i>>): PositionedContainer =
        PositionedContainer(container, position, setOf(), children, children)

    @Test
    fun fixedFillAndPercentageShouldWork() {
        val vertical = Vertical()
        val d = Text("A")
        vertical.add(d, Percentage(15)) // 30
        vertical.add(d, Fixed(50)) // 50
        vertical.add(d, Fill(80)) // 200 - 80 * 2/3 = 120 * 2/3 = 80
        vertical.add(d, Fill(40))
        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))

        assertEquals(
            nonNestedContainer(vertical, p, setOf(
                Pair(d, p.withHeight(30)),
                Pair(d, p.withHeight(50)),
                Pair(d, p.withHeight(80)),
                Pair(d, p.withHeight(40))
            )),
//            PositionedContainer(
//                vertical, p, setOf(), setOf(
//                    Pair(d, p.withHeight(30)),
//                    Pair(d, p.withHeight(50)),
//                    Pair(d, p.withHeight(80)),
//                    Pair(d, p.withHeight(40))
//                )
//            ),
            vertical.positioned(p)
        )
    }

//    @Test
//    fun nestedContainersShouldWork() {
//        val d = Text("A")
//
//        val child1 = Vertical()
//        child1.add(d, Fill())
//
//        val child2 = Vertical()
//        child2.add(d, Fill())
//
//        val parent = Vertical()
//        parent.add(child1, Fill(100))
//        parent.add(child2, Fill(50))
//
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            PositionedContainer(
//                parent, p, setOf(PositionedContainer(), PositionedContainer()), setOf(Pair(d, p.withHeight(125)))
//            ), parent.positioned(p)
//        )
//    }

}