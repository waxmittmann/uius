package wittie.uius.ui.elements.containers

import arrow.core.*
import wittie.uius.Dimensions2i
import wittie.uius.Point2i
import wittie.uius.Position2i
import wittie.uius.ui.*
import wittie.uius.ui.elements.containers.LayoutFns.totals2d

class Table(val rawRows: List<FillBehavior>, val cols: List<FillBehavior>, val ascendingRows: Boolean = true) : Layout() {
    var rows = if (ascendingRows) rawRows else rawRows.reversed()
    var cells = mutableMapOf<Pair<Int, Int>, UiContainer>()

    fun set(row: Int, col: Int, element: UiContainer): StatusOr<Option<UiContainer>> {
        if (row < 0 || row >= rows.size || col < 0 || col >= cols.size)
            return orError("Out of bounds. Bounds: ${rows.size}, ${cols.size}. Set: $row, $col")

        val adjRow = if (ascendingRows) row else rows.size - row - 1
        val maybeExistingElement: Option<UiContainer> = cells[Pair(adjRow, col)].toOption()

        cells[Pair(adjRow, col)] = element
        return orOk(maybeExistingElement)
    }

    fun fSet(row: Int, col: Int, element: UiContainer): Table {
        if (set(row, col, element).isLeft())
            throw java.lang.RuntimeException("!!!")
        return this
    }

    override fun positioned(position: Position2i): PositionedContainer {
        val totals = totals2d(position.width(), position.height(), cols, rows)

        val rowExtents =
            (rows.indices).runningFold(position.yMin()) { last, rowsAt -> totals.verticalTotals.size(rows[rowsAt]) + last }.drop(1)
        val colExtents =
            (cols.indices).runningFold(position.xMin()) { last, colAt -> totals.horizontalTotals.size(cols[colAt]) + last }.drop(1)

        val result = (rows.indices).fold(PositionedContainer(this, position)) { parentContainer, rowAt ->
            (cols.indices).fold(parentContainer) { parentContainer2, colAt ->
                println("$rowAt, $colAt")
                cells[Pair(rowAt, colAt)].toOption().map {
                    val lowerLeft = Point2i(
                        if (colAt == 0) 0 else colExtents[colAt - 1],
                        if (rowAt == 0) 0 else rowExtents[rowAt - 1],
                        )
                    parentContainer2.addContainer(
                        it.positioned(
                            Position2i(
                                lowerLeft, Dimensions2i(colExtents[colAt] - lowerLeft.x, rowExtents[rowAt] - lowerLeft.y)
                            )
                        )
                    )
                }.getOrElse { parentContainer2 }
            }
        }
        return result
    }

    override fun type(): String = "Table"
}