package wittie.uius.ui

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import wittie.uius.Dimensions2i
import wittie.uius.Point2i
import wittie.uius.Position2i

internal class HorizontalTest {
    private fun nonNestedContainer(container: UiContainer, position: Position2i, children: List<Pair<Text, Position2i>>): PositionedContainer =
        PositionedContainer(container, position, listOf(), children, children)

    private fun nestedContainer(
        root: UiContainer,
        position: Position2i,
        childContainers: List<PositionedContainer>,
        childDrawables: List<Pair<Text, Position2i>>
    ) = PositionedContainer(root, position, childContainers, childDrawables,
        childContainers.flatMap { c -> c.descendantDrawables })

    @Test
    fun singleContainerShouldWork() {
        val horizontal = Horizontal()
        val d = Text("A")
        horizontal.add(d, Fixed(125))
        val p = Position2i(Point2i(0, 0), Dimensions2i(200, 100))

        assertEquals(
            nonNestedContainer(
                horizontal, p, listOf(Pair(d, p.withWidth(125)))
            ), horizontal.positioned(p)
        )
    }
//
//    @Test
//    fun singleContainerWithTwoItemsShouldWork() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Fixed(75))
//        Horizontal.add(d, Fixed(125))
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(Pair(d, p.withHeight(75)), Pair(d, p.withHeight(125)))
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun oneChildShouldFill() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Fill())
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(Pair(d, p.withHeight(200)))
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun twoChildrenShouldShareFill() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Fill())
//        Horizontal.add(d, Fill())
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(Pair(d, p.withHeight(100)), Pair(d, p.withHeight(100)))
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun threeChildrenShouldShareFillProportionally() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Fill(25))
//        Horizontal.add(d, Fill(60))
//        Horizontal.add(d, Fill(15))
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal,
//                p,
//                listOf(Pair(d, p.withHeight(50)), Pair(d, p.withHeight(120)), Pair(d, p.withHeight(30)))
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun singlePercentageShouldWork() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Percentage(60))
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(Pair(d, p.withHeight(120)))
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun percentageAndFillShouldWork() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Percentage(60))
//        Horizontal.add(d, Fill())
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(
//                    Pair(d, p.withHeight(120)),
//                    Pair(d, p.withHeight(80))
//                )
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun percentageAndFixedShouldWork() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Percentage(60))
//        Horizontal.add(d, Fixed(50))
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(
//                Horizontal, p, listOf(
//                    Pair(d, p.withHeight(120)),
//                    Pair(d, p.withHeight(50))
//                )
//            ), Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun fixedFillAndPercentageShouldWork() {
//        val Horizontal = Horizontal()
//        val d = Text("A")
//        Horizontal.add(d, Percentage(15)) // 30
//        Horizontal.add(d, Fixed(50)) // 50
//        Horizontal.add(d, Fill(80)) // 200 - 80 * 2/3 = 120 * 2/3 = 80
//        Horizontal.add(d, Fill(40))
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nonNestedContainer(Horizontal, p, listOf(
//                Pair(d, p.withHeight(30)),
//                Pair(d, p.withHeight(50)),
//                Pair(d, p.withHeight(80)),
//                Pair(d, p.withHeight(40))
//            )),
//            Horizontal.positioned(p)
//        )
//    }
//
//    @Test
//    fun nestedContainersShouldWork() {
//        val d = Text("A")
//
//        val child1 = Horizontal()
//        child1.add(d, Fill())
//
//        val child2 = Horizontal()
//        child2.add(d, Fill())
//
//        val parent = Horizontal()
//        parent.add(child1, Fill(75))
//        parent.add(child2, Fill(25))
//
//        val p = Position2i(Point2i(0, 0), Dimensions2i(100, 200))
//
//        assertEquals(
//            nestedContainer(
//                parent,
//                p,
//                listOf(nonNestedContainer(
//                    child1, Position2i(0, 0, 100, 150),
//                    listOf(Pair(d, Position2i(0, 0, 100, 150)))
//                ), nonNestedContainer(
//                    child2, Position2i(0, 150, 100, 50),
//                    listOf(Pair(d, Position2i(0, 150, 100, 50)))
//                )
//                ),
//                listOf()
//            ), parent.positioned(p)
//        )
//    }
}