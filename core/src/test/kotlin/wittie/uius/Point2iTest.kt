package wittie.uius

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class Point2iTest {
    @Test
    fun setWorksForX() {
        assertEquals(Point2i(2, 100), Point2i(1, 100).set(newX = 2))
    }

    @Test
    fun setWorksForY() {
        assertEquals(Point2i(1, 5), Point2i(1, 100).set(newY = 5))
    }
}