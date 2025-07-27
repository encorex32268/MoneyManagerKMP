package core.domain



const val RECENTLY = 9010L
const val FOOD = 10010L
const val TRAFFIC = 20010L
const val LIFE = 30010L
const val HEALTH = 40010L
const val SPORTS = 50010L
const val WEAR = 60010L
const val SHOPPING = 70010L
const val OTHER = 80010L


val categoryIds = listOf(
    RECENTLY,
    FOOD,
    TRAFFIC,
    LIFE,
    HEALTH,
    SPORTS,
    WEAR,
    SHOPPING,
    OTHER
)

val categoryRange = LongRange(start = categoryIds.first() , endInclusive = categoryIds.last())
