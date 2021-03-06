package wittie.uius.ui.elements.containers

sealed class FillBehavior
data class Fixed(val pixels: Int) : FillBehavior()
data class Fill(val weight: Int = 100) : FillBehavior()
data class Percentage(val percentage: Int) : FillBehavior()