package feature.home.presentation

sealed interface HomeEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): HomeEvent
}