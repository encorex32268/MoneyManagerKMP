package core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import core.domain.*
import core.domain.model.Category
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.food
import moneymanagerkmp.composeapp.generated.resources.food_beer
import moneymanagerkmp.composeapp.generated.resources.food_boxlunch
import moneymanagerkmp.composeapp.generated.resources.food_boxlunch2
import moneymanagerkmp.composeapp.generated.resources.food_bread
import moneymanagerkmp.composeapp.generated.resources.food_buy_food
import moneymanagerkmp.composeapp.generated.resources.food_buy_food2
import moneymanagerkmp.composeapp.generated.resources.food_cake
import moneymanagerkmp.composeapp.generated.resources.food_coffee
import moneymanagerkmp.composeapp.generated.resources.food_cookie
import moneymanagerkmp.composeapp.generated.resources.food_delivery
import moneymanagerkmp.composeapp.generated.resources.food_dog
import moneymanagerkmp.composeapp.generated.resources.food_drink
import moneymanagerkmp.composeapp.generated.resources.food_fast_food
import moneymanagerkmp.composeapp.generated.resources.food_food
import moneymanagerkmp.composeapp.generated.resources.food_foodset
import moneymanagerkmp.composeapp.generated.resources.food_fruits
import moneymanagerkmp.composeapp.generated.resources.food_hamburger
import moneymanagerkmp.composeapp.generated.resources.food_hot_coffee
import moneymanagerkmp.composeapp.generated.resources.food_hot_tea
import moneymanagerkmp.composeapp.generated.resources.food_meal
import moneymanagerkmp.composeapp.generated.resources.food_meal2
import moneymanagerkmp.composeapp.generated.resources.food_noodles
import moneymanagerkmp.composeapp.generated.resources.food_pizza
import moneymanagerkmp.composeapp.generated.resources.food_rice
import moneymanagerkmp.composeapp.generated.resources.food_salad
import moneymanagerkmp.composeapp.generated.resources.food_sauce
import moneymanagerkmp.composeapp.generated.resources.food_seafood
import moneymanagerkmp.composeapp.generated.resources.food_streetfood
import moneymanagerkmp.composeapp.generated.resources.food_vegetables
import moneymanagerkmp.composeapp.generated.resources.food_water
import moneymanagerkmp.composeapp.generated.resources.food_wine
import moneymanagerkmp.composeapp.generated.resources.health
import moneymanagerkmp.composeapp.generated.resources.health_calendar
import moneymanagerkmp.composeapp.generated.resources.health_card
import moneymanagerkmp.composeapp.generated.resources.health_doctor
import moneymanagerkmp.composeapp.generated.resources.health_doctor2
import moneymanagerkmp.composeapp.generated.resources.health_health
import moneymanagerkmp.composeapp.generated.resources.health_help
import moneymanagerkmp.composeapp.generated.resources.health_hospital
import moneymanagerkmp.composeapp.generated.resources.health_hospital2
import moneymanagerkmp.composeapp.generated.resources.health_massage
import moneymanagerkmp.composeapp.generated.resources.health_pill
import moneymanagerkmp.composeapp.generated.resources.health_sick
import moneymanagerkmp.composeapp.generated.resources.health_tooth
import moneymanagerkmp.composeapp.generated.resources.life
import moneymanagerkmp.composeapp.generated.resources.life_bank
import moneymanagerkmp.composeapp.generated.resources.life_bath
import moneymanagerkmp.composeapp.generated.resources.life_car
import moneymanagerkmp.composeapp.generated.resources.life_furniture
import moneymanagerkmp.composeapp.generated.resources.life_hammer
import moneymanagerkmp.composeapp.generated.resources.life_light_fee
import moneymanagerkmp.composeapp.generated.resources.life_makeup4
import moneymanagerkmp.composeapp.generated.resources.life_money_income
import moneymanagerkmp.composeapp.generated.resources.life_money_transfer
import moneymanagerkmp.composeapp.generated.resources.life_music
import moneymanagerkmp.composeapp.generated.resources.life_network_fee
import moneymanagerkmp.composeapp.generated.resources.life_paint
import moneymanagerkmp.composeapp.generated.resources.life_phone_fee
import moneymanagerkmp.composeapp.generated.resources.life_rent
import moneymanagerkmp.composeapp.generated.resources.life_salary
import moneymanagerkmp.composeapp.generated.resources.life_save_money
import moneymanagerkmp.composeapp.generated.resources.life_shampoo
import moneymanagerkmp.composeapp.generated.resources.life_shampoo2
import moneymanagerkmp.composeapp.generated.resources.life_smart_phone_fee
import moneymanagerkmp.composeapp.generated.resources.life_stock_decrease
import moneymanagerkmp.composeapp.generated.resources.life_stock_increase
import moneymanagerkmp.composeapp.generated.resources.life_stocks
import moneymanagerkmp.composeapp.generated.resources.life_taxes
import moneymanagerkmp.composeapp.generated.resources.life_taxes2
import moneymanagerkmp.composeapp.generated.resources.life_taxes3
import moneymanagerkmp.composeapp.generated.resources.life_tube
import moneymanagerkmp.composeapp.generated.resources.life_tube2
import moneymanagerkmp.composeapp.generated.resources.life_video
import moneymanagerkmp.composeapp.generated.resources.life_water_fee
import moneymanagerkmp.composeapp.generated.resources.life_wifi
import moneymanagerkmp.composeapp.generated.resources.life_wifi_router
import moneymanagerkmp.composeapp.generated.resources.other
import moneymanagerkmp.composeapp.generated.resources.other_bill
import moneymanagerkmp.composeapp.generated.resources.other_bitcoin
import moneymanagerkmp.composeapp.generated.resources.other_book
import moneymanagerkmp.composeapp.generated.resources.other_book2
import moneymanagerkmp.composeapp.generated.resources.other_box
import moneymanagerkmp.composeapp.generated.resources.other_camera
import moneymanagerkmp.composeapp.generated.resources.other_car_fee
import moneymanagerkmp.composeapp.generated.resources.other_car_repair
import moneymanagerkmp.composeapp.generated.resources.other_car_wash
import moneymanagerkmp.composeapp.generated.resources.other_card_fee
import moneymanagerkmp.composeapp.generated.resources.other_claw
import moneymanagerkmp.composeapp.generated.resources.other_creditcard
import moneymanagerkmp.composeapp.generated.resources.other_cut
import moneymanagerkmp.composeapp.generated.resources.other_cuthair
import moneymanagerkmp.composeapp.generated.resources.other_gamble
import moneymanagerkmp.composeapp.generated.resources.other_game
import moneymanagerkmp.composeapp.generated.resources.other_giftcard
import moneymanagerkmp.composeapp.generated.resources.other_iphone
import moneymanagerkmp.composeapp.generated.resources.other_job
import moneymanagerkmp.composeapp.generated.resources.other_karaoke
import moneymanagerkmp.composeapp.generated.resources.other_lesson
import moneymanagerkmp.composeapp.generated.resources.other_lesson2
import moneymanagerkmp.composeapp.generated.resources.other_letter
import moneymanagerkmp.composeapp.generated.resources.other_light_fee
import moneymanagerkmp.composeapp.generated.resources.other_massage
import moneymanagerkmp.composeapp.generated.resources.other_money_exchange
import moneymanagerkmp.composeapp.generated.resources.other_money_income
import moneymanagerkmp.composeapp.generated.resources.other_money_transfer
import moneymanagerkmp.composeapp.generated.resources.other_movie_theater
import moneymanagerkmp.composeapp.generated.resources.other_music
import moneymanagerkmp.composeapp.generated.resources.other_music2
import moneymanagerkmp.composeapp.generated.resources.other_pet
import moneymanagerkmp.composeapp.generated.resources.other_phone_fee
import moneymanagerkmp.composeapp.generated.resources.other_post
import moneymanagerkmp.composeapp.generated.resources.other_repair
import moneymanagerkmp.composeapp.generated.resources.other_salary
import moneymanagerkmp.composeapp.generated.resources.other_save_money
import moneymanagerkmp.composeapp.generated.resources.other_stationery
import moneymanagerkmp.composeapp.generated.resources.other_stock_decrease
import moneymanagerkmp.composeapp.generated.resources.other_stock_increase
import moneymanagerkmp.composeapp.generated.resources.other_study
import moneymanagerkmp.composeapp.generated.resources.other_toys
import moneymanagerkmp.composeapp.generated.resources.other_unknown
import moneymanagerkmp.composeapp.generated.resources.other_video
import moneymanagerkmp.composeapp.generated.resources.other_washing_machine
import moneymanagerkmp.composeapp.generated.resources.other_water_fee
import moneymanagerkmp.composeapp.generated.resources.recently
import moneymanagerkmp.composeapp.generated.resources.shopping
import moneymanagerkmp.composeapp.generated.resources.shopping_bag
import moneymanagerkmp.composeapp.generated.resources.shopping_camera
import moneymanagerkmp.composeapp.generated.resources.shopping_flowers
import moneymanagerkmp.composeapp.generated.resources.shopping_gift
import moneymanagerkmp.composeapp.generated.resources.shopping_glasses
import moneymanagerkmp.composeapp.generated.resources.shopping_makeup
import moneymanagerkmp.composeapp.generated.resources.shopping_makeup2
import moneymanagerkmp.composeapp.generated.resources.shopping_makeup3
import moneymanagerkmp.composeapp.generated.resources.shopping_makeup_brush
import moneymanagerkmp.composeapp.generated.resources.shopping_necklace
import moneymanagerkmp.composeapp.generated.resources.shopping_pc
import moneymanagerkmp.composeapp.generated.resources.shopping_phone
import moneymanagerkmp.composeapp.generated.resources.shopping_plant
import moneymanagerkmp.composeapp.generated.resources.shopping_plant2
import moneymanagerkmp.composeapp.generated.resources.shopping_ring
import moneymanagerkmp.composeapp.generated.resources.shopping_shopping
import moneymanagerkmp.composeapp.generated.resources.shopping_switch
import moneymanagerkmp.composeapp.generated.resources.sports
import moneymanagerkmp.composeapp.generated.resources.sports_8ball
import moneymanagerkmp.composeapp.generated.resources.sports_baseball
import moneymanagerkmp.composeapp.generated.resources.sports_basketball
import moneymanagerkmp.composeapp.generated.resources.sports_bowling
import moneymanagerkmp.composeapp.generated.resources.sports_boxing
import moneymanagerkmp.composeapp.generated.resources.sports_camp
import moneymanagerkmp.composeapp.generated.resources.sports_football
import moneymanagerkmp.composeapp.generated.resources.sports_golf
import moneymanagerkmp.composeapp.generated.resources.sports_gym
import moneymanagerkmp.composeapp.generated.resources.sports_jumping
import moneymanagerkmp.composeapp.generated.resources.sports_paragliding
import moneymanagerkmp.composeapp.generated.resources.sports_pingpong
import moneymanagerkmp.composeapp.generated.resources.sports_skiing
import moneymanagerkmp.composeapp.generated.resources.sports_sports
import moneymanagerkmp.composeapp.generated.resources.sports_swim
import moneymanagerkmp.composeapp.generated.resources.sports_tennis
import moneymanagerkmp.composeapp.generated.resources.sports_usa_football
import moneymanagerkmp.composeapp.generated.resources.sports_volley
import moneymanagerkmp.composeapp.generated.resources.sports_yoga
import moneymanagerkmp.composeapp.generated.resources.traffic
import moneymanagerkmp.composeapp.generated.resources.traffic_bicycle
import moneymanagerkmp.composeapp.generated.resources.traffic_bus
import moneymanagerkmp.composeapp.generated.resources.traffic_cable_car
import moneymanagerkmp.composeapp.generated.resources.traffic_car
import moneymanagerkmp.composeapp.generated.resources.traffic_city_train
import moneymanagerkmp.composeapp.generated.resources.traffic_highspeed_train
import moneymanagerkmp.composeapp.generated.resources.traffic_highway_bus
import moneymanagerkmp.composeapp.generated.resources.traffic_motobike
import moneymanagerkmp.composeapp.generated.resources.traffic_parking
import moneymanagerkmp.composeapp.generated.resources.traffic_plane
import moneymanagerkmp.composeapp.generated.resources.traffic_ship
import moneymanagerkmp.composeapp.generated.resources.traffic_taxi
import moneymanagerkmp.composeapp.generated.resources.traffic_train
import moneymanagerkmp.composeapp.generated.resources.traffic_train_ticket
import moneymanagerkmp.composeapp.generated.resources.wear
import moneymanagerkmp.composeapp.generated.resources.wear_baby
import moneymanagerkmp.composeapp.generated.resources.wear_bag
import moneymanagerkmp.composeapp.generated.resources.wear_bag2
import moneymanagerkmp.composeapp.generated.resources.wear_bag3
import moneymanagerkmp.composeapp.generated.resources.wear_cap
import moneymanagerkmp.composeapp.generated.resources.wear_dress
import moneymanagerkmp.composeapp.generated.resources.wear_hat
import moneymanagerkmp.composeapp.generated.resources.wear_shirts
import moneymanagerkmp.composeapp.generated.resources.wear_shoes
import moneymanagerkmp.composeapp.generated.resources.wear_shorts
import moneymanagerkmp.composeapp.generated.resources.wear_skirt
import moneymanagerkmp.composeapp.generated.resources.wear_socks
import moneymanagerkmp.composeapp.generated.resources.wear_trousers
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


object CategoryList {

    val items = kotlin.run {
        (1..31).map {
            Category(id = (FOOD + it).toInt(), typeId = FOOD)
        } +
                (1..14).map {
                    Category(id = (TRAFFIC + it).toInt(), typeId = TRAFFIC)
                } +
                (1..32).map {
                    Category(id = (LIFE + it).toInt(), typeId = LIFE)
                } +
                (1..12).map {
                    Category(id = (HEALTH + it).toInt(), typeId = HEALTH)
                } +
                (1..19).map {
                    Category(id = (SPORTS + it).toInt(), typeId = SPORTS)
                } +
                (1..13).map {
                    Category(id = (WEAR + it).toInt(), typeId = WEAR)
                } +
                (1..17).map {
                    Category(id = (SHOPPING + it).toInt(), typeId = SHOPPING)
                } +
                (1..48).map {
                    Category(id = (OTHER + it).toInt(), typeId = OTHER)
                }

    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun getCategoryIconById(
        id: Long
    ): Painter {
        return when (id) {
            FOOD + 1 -> {
                painterResource(Res.drawable.food_food)
            }

            FOOD + 2 -> {
                painterResource(Res.drawable.food_fast_food)
            }

            FOOD + 3 -> {
                painterResource(Res.drawable.food_rice)
            }

            FOOD + 4 -> {
                painterResource(Res.drawable.food_noodles)
            }

            FOOD + 5 -> {
                painterResource(Res.drawable.food_fruits)
            }

            FOOD + 6 -> {
                painterResource(Res.drawable.food_salad)
            }

            FOOD + 7 -> {
                painterResource(Res.drawable.food_seafood)
            }

            FOOD + 8 -> {
                painterResource(Res.drawable.food_buy_food)
            }

            FOOD + 9 -> {
                painterResource(Res.drawable.food_pizza)
            }

            FOOD + 10 -> {
                painterResource(Res.drawable.food_hamburger)
            }

            FOOD + 11 -> {
                painterResource(Res.drawable.food_buy_food2)
            }

            FOOD + 12 -> {
                painterResource(Res.drawable.food_vegetables)
            }

            FOOD + 13 -> {
                painterResource(Res.drawable.food_cake)
            }

            FOOD + 14 -> {
                painterResource(Res.drawable.food_cookie)
            }

            FOOD + 15 -> {
                painterResource(Res.drawable.food_bread)
            }

            FOOD + 16 -> {
                painterResource(Res.drawable.food_beer)
            }

            FOOD + 17 -> {
                painterResource(Res.drawable.food_drink)
            }

            FOOD + 18 -> {
                painterResource(Res.drawable.food_wine)
            }

            FOOD + 19 -> {
                painterResource(Res.drawable.food_coffee)
            }

            FOOD + 20 -> {
                painterResource(Res.drawable.food_hot_coffee)
            }

            FOOD + 21 -> { painterResource(Res.drawable.food_hot_tea) }

            FOOD + 22 -> { painterResource(Res.drawable.food_boxlunch) }
            FOOD + 23 -> { painterResource(Res.drawable.food_boxlunch2) }
            FOOD + 24 -> { painterResource(Res.drawable.food_delivery) }
            FOOD + 25 -> { painterResource(Res.drawable.food_dog) }
            FOOD + 26 -> { painterResource(Res.drawable.food_foodset) }
            FOOD + 27 -> { painterResource(Res.drawable.food_meal) }
            FOOD + 28 -> { painterResource(Res.drawable.food_meal2) }
            FOOD + 29 -> { painterResource(Res.drawable.food_sauce) }
            FOOD + 30 -> { painterResource(Res.drawable.food_streetfood) }
            FOOD + 31 -> { painterResource(Res.drawable.food_water) }

            TRAFFIC + 1 -> {
                painterResource(Res.drawable.traffic_train)
            }

            TRAFFIC + 2 -> {
                painterResource(Res.drawable.traffic_city_train)
            }

            TRAFFIC + 3 -> {
                painterResource(Res.drawable.traffic_highspeed_train)
            }

            TRAFFIC + 4 -> {
                painterResource(Res.drawable.traffic_bus)
            }

            TRAFFIC + 5 -> {
                painterResource(Res.drawable.traffic_highway_bus)
            }

            TRAFFIC + 6 -> {
                painterResource(Res.drawable.traffic_bicycle)
            }

            TRAFFIC + 7 -> {
                painterResource(Res.drawable.traffic_motobike)
            }

            TRAFFIC + 8 -> {
                painterResource(Res.drawable.traffic_car)
            }

            TRAFFIC + 9 -> {
                painterResource(Res.drawable.traffic_taxi)
            }

            TRAFFIC + 10 -> {
                painterResource(Res.drawable.traffic_cable_car)
            }

            TRAFFIC + 11 -> {
                painterResource(Res.drawable.traffic_train_ticket)
            }

            TRAFFIC + 12 -> {
                painterResource(Res.drawable.traffic_ship)
            }

            TRAFFIC + 13 -> {
                painterResource(Res.drawable.traffic_parking)
            }

            TRAFFIC + 14 -> {
                painterResource(Res.drawable.traffic_plane)
            }

            LIFE + 1 -> {
                painterResource(Res.drawable.life_rent)
            }

            LIFE + 2 -> {
                painterResource(Res.drawable.life_car)
            }

            LIFE + 3 -> {
                painterResource(Res.drawable.life_bank)
            }

            LIFE + 4 -> {
                painterResource(Res.drawable.life_light_fee)
            }

            LIFE + 5 -> {
                painterResource(Res.drawable.life_water_fee)
            }

            LIFE + 6 -> {
                painterResource(Res.drawable.life_phone_fee)
            }

            LIFE + 7 -> {
                painterResource(Res.drawable.life_smart_phone_fee)
            }

            LIFE + 8 -> {
                painterResource(Res.drawable.life_network_fee)
            }

            LIFE + 9 -> {
                painterResource(Res.drawable.life_wifi)
            }

            LIFE + 10 -> {
                painterResource(Res.drawable.life_wifi_router)
            }

            LIFE + 11 -> {
                painterResource(Res.drawable.life_money_transfer)
            }

            LIFE + 12 -> {
                painterResource(Res.drawable.life_money_income)
            }

            LIFE + 13 -> {
                painterResource(Res.drawable.life_rent)
            }

            LIFE + 14 -> {
                painterResource(Res.drawable.life_salary)
            }

            LIFE + 15 -> {
                painterResource(Res.drawable.life_music)
            }

            LIFE + 16 -> {
                painterResource(Res.drawable.life_save_money)
            }

            LIFE + 17 -> {
                painterResource(Res.drawable.life_stock_increase)
            }

            LIFE + 18 -> {
                painterResource(Res.drawable.life_stock_decrease)
            }

            LIFE + 19 -> {
                painterResource(Res.drawable.life_stocks)
            }

            LIFE + 20 -> {
                painterResource(Res.drawable.life_taxes)
            }

            LIFE + 21 -> {
                painterResource(Res.drawable.life_taxes2)
            }

            LIFE + 22 -> {
                painterResource(Res.drawable.life_taxes3)
            }

            LIFE + 23 -> {
                painterResource(Res.drawable.life_video)
            }

            LIFE + 24 -> {
                painterResource(Res.drawable.life_paint)
            }

            LIFE + 25 -> { painterResource(Res.drawable.life_hammer) }
            LIFE + 26 -> { painterResource(Res.drawable.life_shampoo) }
            LIFE + 27 -> { painterResource(Res.drawable.life_shampoo2) }
            LIFE + 28 -> { painterResource(Res.drawable.life_furniture) }
            LIFE + 29 -> { painterResource(Res.drawable.life_makeup4) }
            LIFE + 30 -> { painterResource(Res.drawable.life_bath) }
            LIFE + 31 -> { painterResource(Res.drawable.life_tube) }
            LIFE + 32 -> { painterResource(Res.drawable.life_tube2) }

            HEALTH + 1 -> {
                painterResource(Res.drawable.health_doctor2)
            }

            HEALTH + 2 -> {
                painterResource(Res.drawable.health_doctor)
            }

            HEALTH + 3 -> {
                painterResource(Res.drawable.health_sick)
            }

            HEALTH + 4 -> {
                painterResource(Res.drawable.health_pill)
            }

            HEALTH + 5 -> {
                painterResource(Res.drawable.health_health)
            }

            HEALTH + 6 -> {
                painterResource(Res.drawable.health_tooth)
            }

            HEALTH + 7 -> {
                painterResource(Res.drawable.health_help)
            }

            HEALTH + 8 -> {
                painterResource(Res.drawable.health_hospital)
            }

            HEALTH + 9 -> {
                painterResource(Res.drawable.health_hospital2)
            }

            HEALTH + 10 -> {
                painterResource(Res.drawable.health_massage)
            }

            SPORTS + 1 -> {
                painterResource(Res.drawable.sports_sports)
            }

            SPORTS + 2 -> {
                painterResource(Res.drawable.sports_boxing)
            }

            SPORTS + 3 -> {
                painterResource(Res.drawable.sports_basketball)
            }

            SPORTS + 4 -> {
                painterResource(Res.drawable.sports_baseball)
            }

            SPORTS + 5 -> {
                painterResource(Res.drawable.sports_gym)
            }

            SPORTS + 6 -> {
                painterResource(Res.drawable.sports_8ball)
            }

            SPORTS + 7 -> {
                painterResource(Res.drawable.sports_tennis)
            }

            SPORTS + 8 -> {
                painterResource(Res.drawable.sports_usa_football)
            }

            SPORTS + 9 -> {
                painterResource(Res.drawable.sports_football)
            }

            SPORTS + 10 -> {
                painterResource(Res.drawable.sports_yoga)
            }

            SPORTS + 11 -> {
                painterResource(Res.drawable.sports_volley)
            }

            SPORTS + 12 -> {
                painterResource(Res.drawable.sports_golf)
            }

            SPORTS + 13 -> {
                painterResource(Res.drawable.sports_pingpong)
            }

            SPORTS + 14 -> {
                painterResource(Res.drawable.sports_skiing)
            }

            SPORTS + 15 -> {
                painterResource(Res.drawable.sports_bowling)
            }

            SPORTS + 16 -> {
                painterResource(Res.drawable.sports_swim)
            }

            SPORTS + 17 -> {
                painterResource(Res.drawable.sports_camp)
            }
            SPORTS + 18 -> {
                painterResource(Res.drawable.sports_jumping)
            }
            SPORTS + 19 -> {
                painterResource(Res.drawable.sports_paragliding)
            }

            WEAR + 1 -> {
                painterResource(Res.drawable.wear_shirts)
            }

            WEAR + 2 -> {
                painterResource(Res.drawable.wear_shorts)
            }

            WEAR + 3 -> {
                painterResource(Res.drawable.wear_dress)
            }

            WEAR + 4 -> {
                painterResource(Res.drawable.wear_skirt)
            }

            WEAR + 5 -> {
                painterResource(Res.drawable.wear_shoes)
            }

            WEAR + 6 -> {
                painterResource(Res.drawable.wear_socks)
            }

            WEAR + 7 -> {
                painterResource(Res.drawable.wear_trousers)
            }

            WEAR + 8 -> {
                painterResource(Res.drawable.wear_cap)
            }

            WEAR + 9 -> {
                painterResource(Res.drawable.wear_hat)
            }

            WEAR + 10 -> {
                painterResource(Res.drawable.wear_baby)
            }

            WEAR + 11 -> {
                painterResource(Res.drawable.wear_bag)
            }

            WEAR + 12 -> {
                painterResource(Res.drawable.wear_bag2)
            }

            WEAR + 13 -> {
                painterResource(Res.drawable.wear_bag3)
            }

            SHOPPING + 1 -> {
                painterResource(Res.drawable.shopping_shopping)
            }

            SHOPPING + 2 -> {
                painterResource(Res.drawable.shopping_gift)
            }

            SHOPPING + 3 -> {
                painterResource(Res.drawable.shopping_glasses)
            }

            SHOPPING + 4 -> {
                painterResource(Res.drawable.shopping_flowers)
            }

            SHOPPING + 5 -> {
                painterResource(Res.drawable.shopping_makeup)
            }

            SHOPPING + 6 -> {
                painterResource(Res.drawable.shopping_makeup2)
            }

            SHOPPING + 7 -> {
                painterResource(Res.drawable.shopping_makeup3)
            }

            SHOPPING + 8 -> {
                painterResource(Res.drawable.shopping_makeup_brush)
            }

            SHOPPING + 9 -> {
                painterResource(Res.drawable.shopping_necklace)
            }

            SHOPPING + 10 -> {
                painterResource(Res.drawable.shopping_ring)
            }

            SHOPPING + 11 -> {
                painterResource(Res.drawable.shopping_pc)
            }

            SHOPPING + 12 -> {
                painterResource(Res.drawable.shopping_phone)
            }

            SHOPPING + 13 -> {
                painterResource(Res.drawable.shopping_camera)
            }

            SHOPPING + 14 -> {
                painterResource(Res.drawable.shopping_switch)
            }

            SHOPPING + 15 -> {
                painterResource(Res.drawable.shopping_bag)
            }

            SHOPPING + 16 -> {
                painterResource(Res.drawable.shopping_plant)
            }

            SHOPPING + 17 -> {
                painterResource(Res.drawable.shopping_plant2)
            }

            OTHER + 1 -> {
                painterResource(Res.drawable.other_save_money)
            }

            OTHER + 2 -> {
                painterResource(Res.drawable.other_washing_machine)
            }

            OTHER + 3 -> {
                painterResource(Res.drawable.other_bill)
            }

            OTHER + 4 -> {
                painterResource(Res.drawable.other_book)
            }

            OTHER + 5 -> {
                painterResource(Res.drawable.other_book2)
            }

            OTHER + 6 -> {
                painterResource(Res.drawable.other_car_repair)
            }

            OTHER + 7 -> {
                painterResource(Res.drawable.other_car_wash)
            }

            OTHER + 8 -> {
                painterResource(Res.drawable.other_car_fee)
            }

            OTHER + 9 -> {
                painterResource(Res.drawable.other_card_fee)
            }

            OTHER + 10 -> {
                painterResource(Res.drawable.other_creditcard)
            }

            OTHER + 11 -> {
                painterResource(Res.drawable.other_light_fee)
            }

            OTHER + 12 -> {
                painterResource(Res.drawable.other_water_fee)
            }

            OTHER + 13 -> {
                painterResource(Res.drawable.other_book2)
            }

            OTHER + 14 -> {
                painterResource(Res.drawable.other_pet)
            }

            OTHER + 15 -> {
                painterResource(Res.drawable.other_phone_fee)
            }

            OTHER + 16 -> {
                painterResource(Res.drawable.other_bill)
            }

            OTHER + 17 -> {
                painterResource(Res.drawable.other_music)
            }

            OTHER + 18 -> {
                painterResource(Res.drawable.other_music2)
            }

            OTHER + 19 -> {
                painterResource(Res.drawable.other_unknown)
            }

            OTHER + 20 -> {
                painterResource(Res.drawable.other_video)
            }

            OTHER + 21 -> {
                painterResource(Res.drawable.other_toys)
            }

            OTHER + 22 -> {
                painterResource(Res.drawable.other_repair)
            }

            OTHER + 23 -> {
                painterResource(Res.drawable.other_salary)
            }

            OTHER + 24 -> {
                painterResource(Res.drawable.other_stock_decrease)
            }

            OTHER + 25 -> {
                painterResource(Res.drawable.other_stock_increase)
            }

            OTHER + 26 -> {
                painterResource(Res.drawable.other_movie_theater)
            }

            OTHER + 27 -> {
                painterResource(Res.drawable.other_money_transfer)
            }

            OTHER + 28 -> {
                painterResource(Res.drawable.other_money_income)
            }

            OTHER + 29 -> {
                painterResource(Res.drawable.other_money_exchange)
            }

            OTHER + 30 -> {
                painterResource(Res.drawable.other_massage)
            }

            OTHER + 31 -> {
                painterResource(Res.drawable.other_cuthair)
            }

            OTHER + 32 -> {
                painterResource(Res.drawable.other_gamble)
            }

            OTHER + 33 -> {
                painterResource(Res.drawable.other_game)
            }

            OTHER + 34 -> {
                painterResource(Res.drawable.other_iphone)
            }

            OTHER + 35 -> {
                painterResource(Res.drawable.other_job)
            }

            OTHER + 36 -> {
                painterResource(Res.drawable.other_karaoke)
            }

            OTHER + 37 -> { painterResource(Res.drawable.other_bitcoin) }
            OTHER + 38 -> { painterResource(Res.drawable.other_box) }
            OTHER + 39 -> { painterResource(Res.drawable.other_camera) }
            OTHER + 40 -> { painterResource(Res.drawable.other_claw) }
            OTHER + 41 -> { painterResource(Res.drawable.other_cut) }
            OTHER + 42 -> { painterResource(Res.drawable.other_giftcard) }
            OTHER + 43 -> { painterResource(Res.drawable.other_lesson) }
            OTHER + 44 -> { painterResource(Res.drawable.other_lesson2) }
            OTHER + 45 -> { painterResource(Res.drawable.other_letter) }
            OTHER + 46 -> { painterResource(Res.drawable.other_post) }
            OTHER + 47 -> { painterResource(Res.drawable.other_stationery) }
            OTHER + 48 -> { painterResource(Res.drawable.other_study) }
            HEALTH + 11 -> {
                painterResource(Res.drawable.health_calendar)
            }
            HEALTH + 12 -> {
                painterResource(Res.drawable.health_card)
            }

            else -> {
                painterResource(Res.drawable.other_unknown)
            }
        }
    }


    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun getCategoryDescriptionById(
        id: Long
    ): String {
        return when (id) {
            FOOD + 1 -> {
                stringResource(Res.string.food_food)
            }

            FOOD + 2 -> {
                stringResource(Res.string.food_fast_food)
            }

            FOOD + 3 -> {
                stringResource(Res.string.food_rice)
            }

            FOOD + 4 -> {
                stringResource(Res.string.food_noodles)
            }

            FOOD + 5 -> {
                stringResource(Res.string.food_fruits)
            }

            FOOD + 6 -> {
                stringResource(Res.string.food_salad)
            }

            FOOD + 7 -> {
                stringResource(Res.string.food_seafood)
            }

            FOOD + 8 -> {
                stringResource(Res.string.food_buy_food)
            }

            FOOD + 9 -> {
                stringResource(Res.string.food_pizza)
            }

            FOOD + 10 -> {
                stringResource(Res.string.food_hamburger)
            }

            FOOD + 11 -> {
                stringResource(Res.string.food_buy_food2)
            }

            FOOD + 12 -> {
                stringResource(Res.string.food_vegetables)
            }

            FOOD + 13 -> {
                stringResource(Res.string.food_cake)
            }

            FOOD + 14 -> {
                stringResource(Res.string.food_cookie)
            }

            FOOD + 15 -> {
                stringResource(Res.string.food_bread)
            }

            FOOD + 16 -> {
                stringResource(Res.string.food_beer)
            }

            FOOD + 17 -> {
                stringResource(Res.string.food_drink)
            }

            FOOD + 18 -> {
                stringResource(Res.string.food_wine)
            }

            FOOD + 19 -> {
                stringResource(Res.string.food_coffee)
            }

            FOOD + 20 -> {
                stringResource(Res.string.food_hot_coffee)
            }

            FOOD + 21 -> {
                stringResource(Res.string.food_hot_tea)
            }
            TRAFFIC + 1 -> {
                stringResource(Res.string.traffic_train)
            }

            TRAFFIC + 2 -> {
                stringResource(Res.string.traffic_city_train)
            }

            TRAFFIC + 3 -> {
                stringResource(Res.string.traffic_highspeed_train)
            }

            TRAFFIC + 4 -> {
                stringResource(Res.string.traffic_bus)
            }

            TRAFFIC + 5 -> {
                stringResource(Res.string.traffic_highway_bus)
            }

            TRAFFIC + 6 -> {
                stringResource(Res.string.traffic_bicycle)
            }

            TRAFFIC + 7 -> {
                stringResource(Res.string.traffic_motobike)
            }

            TRAFFIC + 8 -> {
                stringResource(Res.string.traffic_car)
            }

            TRAFFIC + 9 -> {
                stringResource(Res.string.traffic_taxi)
            }

            TRAFFIC + 10 -> {
                stringResource(Res.string.traffic_cable_car)
            }

            TRAFFIC + 11 -> {
                stringResource(Res.string.traffic_train_ticket)
            }

            TRAFFIC + 12 -> {
                stringResource(Res.string.traffic_ship)
            }

            TRAFFIC + 13 -> {
                stringResource(Res.string.traffic_parking)
            }

            TRAFFIC + 14 -> {
                stringResource(Res.string.traffic_plane)
            }

            LIFE + 1 -> {
                stringResource(Res.string.life_rent)
            }

            LIFE + 2 -> {
                stringResource(Res.string.life_car)
            }

            LIFE + 3 -> {
                stringResource(Res.string.life_bank)
            }

            LIFE + 4 -> {
                stringResource(Res.string.life_light_fee)
            }

            LIFE + 5 -> {
                stringResource(Res.string.life_water_fee)
            }

            LIFE + 6 -> {
                stringResource(Res.string.life_phone_fee)
            }

            LIFE + 7 -> {
                stringResource(Res.string.life_smart_phone_fee)
            }

            LIFE + 8 -> {
                stringResource(Res.string.life_network_fee)
            }

            LIFE + 9 -> {
                stringResource(Res.string.life_wifi)
            }

            LIFE + 10 -> {
                stringResource(Res.string.life_wifi_router)
            }

            LIFE + 11 -> {
                stringResource(Res.string.life_money_transfer)
            }

            LIFE + 12 -> {
                stringResource(Res.string.life_money_income)
            }
            LIFE + 13 -> {
                stringResource(Res.string.life_rent)
            }
            LIFE + 14 -> {
                stringResource(Res.string.life_salary)
            }

            LIFE + 15 -> {
                stringResource(Res.string.life_music)
            }

            LIFE + 16 -> {
                stringResource(Res.string.life_save_money)
            }

            LIFE + 17 -> {
                stringResource(Res.string.life_stock_increase)
            }

            LIFE + 18 -> {
                stringResource(Res.string.life_stock_decrease)
            }

            LIFE + 19 -> {
                stringResource(Res.string.life_stocks)
            }

            LIFE + 20 -> {
                stringResource(Res.string.life_taxes)
            }

            LIFE + 21 -> {
                stringResource(Res.string.life_taxes2)
            }

            LIFE + 22 -> {
                stringResource(Res.string.life_taxes3)
            }

            LIFE + 23 -> {
                stringResource(Res.string.life_video)
            }

            LIFE + 24 -> {
                stringResource(Res.string.life_paint)
            }

            LIFE + 25 -> {
                stringResource(Res.string.life_hammer)
            }

            HEALTH + 1 -> {
                stringResource(Res.string.health_doctor2)
            }

            HEALTH + 2 -> {
                stringResource(Res.string.health_doctor)
            }

            HEALTH + 3 -> {
                stringResource(Res.string.health_sick)
            }

            HEALTH + 4 -> {
                stringResource(Res.string.health_pill)
            }

            HEALTH + 5 -> {
                stringResource(Res.string.health_health)
            }

            HEALTH + 6 -> {
                stringResource(Res.string.health_tooth)
            }

            HEALTH + 7 -> {
                stringResource(Res.string.health_help)
            }

            HEALTH + 8 -> {
                stringResource(Res.string.health_hospital)
            }

            HEALTH + 9 -> {
                stringResource(Res.string.health_hospital2)
            }

            HEALTH + 10 -> {
                stringResource(Res.string.health_massage)
            }



            SPORTS + 1 -> {
                stringResource(Res.string.sports_sports)
            }

            SPORTS + 2 -> {
                stringResource(Res.string.sports_boxing)
            }

            SPORTS + 3 -> {
                stringResource(Res.string.sports_basketball)
            }

            SPORTS + 4 -> {
                stringResource(Res.string.sports_baseball)
            }

            SPORTS + 5 -> {
                stringResource(Res.string.sports_gym)
            }

            SPORTS + 6 -> {
                stringResource(Res.string.sports_8ball)
            }

            SPORTS + 7 -> {
                stringResource(Res.string.sports_tennis)
            }

            SPORTS + 8 -> {
                stringResource(Res.string.sports_usa_football)
            }

            SPORTS + 9 -> {
                stringResource(Res.string.sports_football)
            }

            SPORTS + 10 -> {
                stringResource(Res.string.sports_yoga)
            }

            SPORTS + 11 -> {
                stringResource(Res.string.sports_volley)
            }

            SPORTS + 12 -> {
                stringResource(Res.string.sports_golf)
            }

            SPORTS + 13 -> {
                stringResource(Res.string.sports_pingpong)
            }

            SPORTS + 14 -> {
                stringResource(Res.string.sports_skiing)
            }

            SPORTS + 15 -> {
                stringResource(Res.string.sports_bowling)
            }

            SPORTS + 16 -> {
                stringResource(Res.string.sports_swim)
            }

            SPORTS + 17 -> {
                stringResource(Res.string.sports_camp)
            }


            WEAR + 1 -> {
                stringResource(Res.string.wear_shirts)
            }

            WEAR + 2 -> {
                stringResource(Res.string.wear_shorts)
            }

            WEAR + 3 -> {
                stringResource(Res.string.wear_dress)
            }

            WEAR + 4 -> {
                stringResource(Res.string.wear_skirt)
            }

            WEAR + 5 -> {
                stringResource(Res.string.wear_shoes)
            }

            WEAR + 6 -> {
                stringResource(Res.string.wear_socks)
            }

            WEAR + 7 -> {
                stringResource(Res.string.wear_trousers)
            }

            WEAR + 8 -> {
                stringResource(Res.string.wear_cap)
            }

            WEAR + 9 -> {
                stringResource(Res.string.wear_hat)
            }

            WEAR + 10 -> {
                stringResource(Res.string.wear_baby)
            }

            WEAR + 11 -> {
                stringResource(Res.string.wear_bag)
            }

            WEAR + 12 -> {
                stringResource(Res.string.wear_bag2)
            }

            WEAR + 13 -> {
                stringResource(Res.string.wear_bag3)
            }

            SHOPPING + 1 -> {
                stringResource(Res.string.shopping_shopping)
            }

            SHOPPING + 2 -> {
                stringResource(Res.string.shopping_gift)
            }

            SHOPPING + 3 -> {
                stringResource(Res.string.shopping_glasses)
            }

            SHOPPING + 4 -> {
                stringResource(Res.string.shopping_flowers)
            }

            SHOPPING + 5 -> {
                stringResource(Res.string.shopping_makeup)
            }

            SHOPPING + 6 -> {
                stringResource(Res.string.shopping_makeup2)
            }

            SHOPPING + 7 -> {
                stringResource(Res.string.shopping_makeup3)
            }

            SHOPPING + 8 -> {
                stringResource(Res.string.shopping_makeup_brush)
            }

            SHOPPING + 9 -> {
                stringResource(Res.string.shopping_necklace)
            }

            SHOPPING + 10 -> {
                stringResource(Res.string.shopping_ring)
            }

            SHOPPING + 11 -> {
                stringResource(Res.string.shopping_pc)
            }

            SHOPPING + 12 -> {
                stringResource(Res.string.shopping_phone)
            }

            SHOPPING + 13 -> {
                stringResource(Res.string.shopping_camera)
            }

            SHOPPING + 14 -> {
                stringResource(Res.string.shopping_switch)
            }

            SHOPPING + 15 -> {
                stringResource(Res.string.shopping_bag)
            }

            SHOPPING + 16 -> {
                stringResource(Res.string.shopping_plant)
            }

            SHOPPING + 17 -> {
                stringResource(Res.string.shopping_plant2)
            }

            OTHER + 1 -> {
                stringResource(Res.string.other_save_money)
            }

            OTHER + 2 -> {
                stringResource(Res.string.other_washing_machine)
            }

            OTHER + 3 -> {
                stringResource(Res.string.other_bill)
            }

            OTHER + 4 -> {
                stringResource(Res.string.other_book)
            }

            OTHER + 5 -> {
                stringResource(Res.string.other_book2)
            }

            OTHER + 6 -> {
                stringResource(Res.string.other_car_repair)
            }

            OTHER + 7 -> {
                stringResource(Res.string.other_car_wash)
            }

            OTHER + 8 -> {
                stringResource(Res.string.other_car_fee)
            }

            OTHER + 9 -> {
                stringResource(Res.string.other_card_fee)
            }

            OTHER + 10 -> {
                stringResource(Res.string.other_creditcard)
            }

            OTHER + 11 -> {
                stringResource(Res.string.other_light_fee)
            }

            OTHER + 12 -> {
                stringResource(Res.string.other_water_fee)
            }

            OTHER + 13 -> {
                stringResource(Res.string.other_book2)
            }

            OTHER + 14 -> {
                stringResource(Res.string.other_pet)
            }

            OTHER + 15 -> {
                stringResource(Res.string.other_phone_fee)
            }

            OTHER + 16 -> {
                stringResource(Res.string.other_bill)
            }

            OTHER + 17 -> {
                stringResource(Res.string.other_music)
            }

            OTHER + 18 -> {
                stringResource(Res.string.other_music2)
            }

            OTHER + 19 -> {
                stringResource(Res.string.other_unknown)
            }

            OTHER + 20 -> {
                stringResource(Res.string.other_video)
            }

            OTHER + 21 -> {
                stringResource(Res.string.other_toys)
            }

            OTHER + 22 -> {
                stringResource(Res.string.other_repair)
            }

            OTHER + 23 -> {
                stringResource(Res.string.other_salary)
            }

            OTHER + 24 -> {
                stringResource(Res.string.other_stock_decrease)
            }

            OTHER + 25 -> {
                stringResource(Res.string.other_stock_increase)
            }

            OTHER + 26 -> {
                stringResource(Res.string.other_movie_theater)
            }

            OTHER + 27 -> {
                stringResource(Res.string.other_money_transfer)
            }

            OTHER + 28 -> {
                stringResource(Res.string.other_money_income)
            }

            OTHER + 29 -> {
                stringResource(Res.string.other_money_exchange)
            }

            OTHER + 30 -> {
                stringResource(Res.string.other_massage)
            }

            OTHER + 31 -> {
                stringResource(Res.string.other_cuthair)
            }

            OTHER + 32 -> {
                stringResource(Res.string.other_gamble)
            }

            OTHER + 33 -> {
                stringResource(Res.string.other_game)
            }

            OTHER + 34 -> {
                stringResource(Res.string.other_iphone)
            }

            OTHER + 35 -> {
                stringResource(Res.string.other_job)
            }

            OTHER + 36 -> {
                stringResource(Res.string.other_karaoke)
            }

            OTHER + 37 -> { stringResource(Res.string.other_bitcoin) }
            OTHER + 38 -> { stringResource(Res.string.other_box) }
            OTHER + 39 -> { stringResource(Res.string.other_camera) }
            OTHER + 40 -> { stringResource(Res.string.other_claw) }
            OTHER + 41 -> { stringResource(Res.string.other_cut) }
            OTHER + 42 -> { stringResource(Res.string.other_giftcard) }
            OTHER + 43 -> { stringResource(Res.string.other_lesson) }
            OTHER + 44 -> { stringResource(Res.string.other_lesson2) }
            OTHER + 45 -> { stringResource(Res.string.other_letter) }
            OTHER + 46 -> { stringResource(Res.string.other_post) }
            OTHER + 47 -> { stringResource(Res.string.other_stationery) }
            OTHER + 48 -> { stringResource(Res.string.other_study) }
            SPORTS + 18 -> { stringResource(Res.string.sports_jumping) }
            SPORTS + 19 -> { stringResource(Res.string.sports_paragliding) }
            LIFE + 26 -> { stringResource(Res.string.life_shampoo) }
            LIFE + 27 -> { stringResource(Res.string.life_shampoo2) }
            LIFE + 28 -> { stringResource(Res.string.life_furniture) }
            LIFE + 29 -> { stringResource(Res.string.life_makeup4) }
            LIFE + 30 -> { stringResource(Res.string.life_bath) }
            LIFE + 31 -> { stringResource(Res.string.life_tube) }
            LIFE + 32 -> { stringResource(Res.string.life_tube2) }
            FOOD + 22 -> { stringResource(Res.string.food_boxlunch) }
            FOOD + 23 -> { stringResource(Res.string.food_boxlunch2) }
            FOOD + 24 -> { stringResource(Res.string.food_delivery) }
            FOOD + 25 -> { stringResource(Res.string.food_dog) }
            FOOD + 26 -> { stringResource(Res.string.food_foodset) }
            FOOD + 27 -> { stringResource(Res.string.food_meal) }
            FOOD + 28 -> { stringResource(Res.string.food_meal2) }
            FOOD + 29 -> { stringResource(Res.string.food_sauce) }
            FOOD + 30 -> { stringResource(Res.string.food_streetfood) }
            FOOD + 31 -> { stringResource(Res.string.food_water) }
            HEALTH + 11 -> {
                stringResource(Res.string.health_calendar)
            }
            HEALTH + 12 -> {
                stringResource(Res.string.health_card)
            }
            else -> {
                ""
            }
        }
    }


    suspend fun getCategoryNameById(
        id: Long
    ): String {
        return when (id) {
            FOOD + 1 -> {
                getString(Res.string.food_food)
            }

            FOOD + 2 -> {
                getString(Res.string.food_fast_food)
            }

            FOOD + 3 -> {
                getString(Res.string.food_rice)
            }

            FOOD + 4 -> {
                getString(Res.string.food_noodles)
            }

            FOOD + 5 -> {
                getString(Res.string.food_fruits)
            }

            FOOD + 6 -> {
                getString(Res.string.food_salad)
            }

            FOOD + 7 -> {
                getString(Res.string.food_seafood)
            }

            FOOD + 8 -> {
                getString(Res.string.food_buy_food)
            }

            FOOD + 9 -> {
                getString(Res.string.food_pizza)
            }

            FOOD + 10 -> {
                getString(Res.string.food_hamburger)
            }

            FOOD + 11 -> {
                getString(Res.string.food_buy_food2)
            }

            FOOD + 12 -> {
                getString(Res.string.food_vegetables)
            }

            FOOD + 13 -> {
                getString(Res.string.food_cake)
            }

            FOOD + 14 -> {
                getString(Res.string.food_cookie)
            }

            FOOD + 15 -> {
                getString(Res.string.food_bread)
            }

            FOOD + 16 -> {
                getString(Res.string.food_beer)
            }

            FOOD + 17 -> {
                getString(Res.string.food_drink)
            }

            FOOD + 18 -> {
                getString(Res.string.food_wine)
            }

            FOOD + 19 -> {
                getString(Res.string.food_coffee)
            }

            FOOD + 20 -> {
                getString(Res.string.food_hot_coffee)
            }

            FOOD + 21 -> {
                getString(Res.string.food_hot_tea)
            }



            TRAFFIC + 1 -> {
                getString(Res.string.traffic_train)
            }

            TRAFFIC + 2 -> {
                getString(Res.string.traffic_city_train)
            }

            TRAFFIC + 3 -> {
                getString(Res.string.traffic_highspeed_train)
            }

            TRAFFIC + 4 -> {
                getString(Res.string.traffic_bus)
            }

            TRAFFIC + 5 -> {
                getString(Res.string.traffic_highway_bus)
            }

            TRAFFIC + 6 -> {
                getString(Res.string.traffic_bicycle)
            }

            TRAFFIC + 7 -> {
                getString(Res.string.traffic_motobike)
            }

            TRAFFIC + 8 -> {
                getString(Res.string.traffic_car)
            }

            TRAFFIC + 9 -> {
                getString(Res.string.traffic_taxi)
            }

            TRAFFIC + 10 -> {
                getString(Res.string.traffic_cable_car)
            }

            TRAFFIC + 11 -> {
                getString(Res.string.traffic_train_ticket)
            }

            TRAFFIC + 12 -> {
                getString(Res.string.traffic_ship)
            }

            TRAFFIC + 13 -> {
                getString(Res.string.traffic_parking)
            }

            TRAFFIC + 14 -> {
                getString(Res.string.traffic_plane)
            }

            LIFE + 1 -> {
                getString(Res.string.life_rent)
            }

            LIFE + 2 -> {
                getString(Res.string.life_car)
            }

            LIFE + 3 -> {
                getString(Res.string.life_bank)
            }

            LIFE + 4 -> {
                getString(Res.string.life_light_fee)
            }

            LIFE + 5 -> {
                getString(Res.string.life_water_fee)
            }

            LIFE + 6 -> {
                getString(Res.string.life_phone_fee)
            }

            LIFE + 7 -> {
                getString(Res.string.life_smart_phone_fee)
            }

            LIFE + 8 -> {
                getString(Res.string.life_network_fee)
            }

            LIFE + 9 -> {
                getString(Res.string.life_wifi)
            }

            LIFE + 10 -> {
                getString(Res.string.life_wifi_router)
            }

            LIFE + 11 -> {
                getString(Res.string.life_money_transfer)
            }

            LIFE + 12 -> {
                getString(Res.string.life_money_income)
            }
            LIFE + 13 -> {
                getString(Res.string.life_rent)
            }
            LIFE + 14 -> {
                getString(Res.string.life_salary)
            }

            LIFE + 15 -> {
                getString(Res.string.life_music)
            }

            LIFE + 16 -> {
                getString(Res.string.life_save_money)
            }

            LIFE + 17 -> {
                getString(Res.string.life_stock_increase)
            }

            LIFE + 18 -> {
                getString(Res.string.life_stock_decrease)
            }

            LIFE + 19 -> {
                getString(Res.string.life_stocks)
            }

            LIFE + 20 -> {
                getString(Res.string.life_taxes)
            }

            LIFE + 21 -> {
                getString(Res.string.life_taxes2)
            }

            LIFE + 22 -> {
                getString(Res.string.life_taxes3)
            }

            LIFE + 23 -> {
                getString(Res.string.life_video)
            }

            LIFE + 24 -> {
                getString(Res.string.life_paint)
            }

            LIFE + 25 -> {
                getString(Res.string.life_hammer)
            }

            HEALTH + 1 -> {
                getString(Res.string.health_doctor2)
            }

            HEALTH + 2 -> {
                getString(Res.string.health_doctor)
            }

            HEALTH + 3 -> {
                getString(Res.string.health_sick)
            }

            HEALTH + 4 -> {
                getString(Res.string.health_pill)
            }

            HEALTH + 5 -> {
                getString(Res.string.health_health)
            }

            HEALTH + 6 -> {
                getString(Res.string.health_tooth)
            }

            HEALTH + 7 -> {
                getString(Res.string.health_help)
            }

            HEALTH + 8 -> {
                getString(Res.string.health_hospital)
            }

            HEALTH + 9 -> {
                getString(Res.string.health_hospital2)
            }

            HEALTH + 10 -> {
                getString(Res.string.health_massage)
            }

            SPORTS + 1 -> {
                getString(Res.string.sports_sports)
            }

            SPORTS + 2 -> {
                getString(Res.string.sports_boxing)
            }

            SPORTS + 3 -> {
                getString(Res.string.sports_basketball)
            }

            SPORTS + 4 -> {
                getString(Res.string.sports_baseball)
            }

            SPORTS + 5 -> {
                getString(Res.string.sports_gym)
            }

            SPORTS + 6 -> {
                getString(Res.string.sports_8ball)
            }

            SPORTS + 7 -> {
                getString(Res.string.sports_tennis)
            }

            SPORTS + 8 -> {
                getString(Res.string.sports_usa_football)
            }

            SPORTS + 9 -> {
                getString(Res.string.sports_football)
            }

            SPORTS + 10 -> {
                getString(Res.string.sports_yoga)
            }

            SPORTS + 11 -> {
                getString(Res.string.sports_volley)
            }

            SPORTS + 12 -> {
                getString(Res.string.sports_golf)
            }

            SPORTS + 13 -> {
                getString(Res.string.sports_pingpong)
            }

            SPORTS + 14 -> {
                getString(Res.string.sports_skiing)
            }

            SPORTS + 15 -> {
                getString(Res.string.sports_bowling)
            }

            SPORTS + 16 -> {
                getString(Res.string.sports_swim)
            }

            SPORTS + 17 -> {
                getString(Res.string.sports_camp)
            }

            WEAR + 1 -> {
                getString(Res.string.wear_shirts)
            }

            WEAR + 2 -> {
                getString(Res.string.wear_shorts)
            }

            WEAR + 3 -> {
                getString(Res.string.wear_dress)
            }

            WEAR + 4 -> {
                getString(Res.string.wear_skirt)
            }

            WEAR + 5 -> {
                getString(Res.string.wear_shoes)
            }

            WEAR + 6 -> {
                getString(Res.string.wear_socks)
            }

            WEAR + 7 -> {
                getString(Res.string.wear_trousers)
            }

            WEAR + 8 -> {
                getString(Res.string.wear_cap)
            }

            WEAR + 9 -> {
                getString(Res.string.wear_hat)
            }

            WEAR + 10 -> {
                getString(Res.string.wear_baby)
            }

            WEAR + 11 -> {
                getString(Res.string.wear_bag)
            }

            WEAR + 12 -> {
                getString(Res.string.wear_bag2)
            }

            WEAR + 13 -> {
                getString(Res.string.wear_bag3)
            }

            SHOPPING + 1 -> {
                getString(Res.string.shopping_shopping)
            }

            SHOPPING + 2 -> {
                getString(Res.string.shopping_gift)
            }

            SHOPPING + 3 -> {
                getString(Res.string.shopping_glasses)
            }

            SHOPPING + 4 -> {
                getString(Res.string.shopping_flowers)
            }

            SHOPPING + 5 -> {
                getString(Res.string.shopping_makeup)
            }

            SHOPPING + 6 -> {
                getString(Res.string.shopping_makeup2)
            }

            SHOPPING + 7 -> {
                getString(Res.string.shopping_makeup3)
            }

            SHOPPING + 8 -> {
                getString(Res.string.shopping_makeup_brush)
            }

            SHOPPING + 9 -> {
                getString(Res.string.shopping_necklace)
            }

            SHOPPING + 10 -> {
                getString(Res.string.shopping_ring)
            }

            SHOPPING + 11 -> {
                getString(Res.string.shopping_pc)
            }

            SHOPPING + 12 -> {
                getString(Res.string.shopping_phone)
            }

            SHOPPING + 13 -> {
                getString(Res.string.shopping_camera)
            }

            SHOPPING + 14 -> {
                getString(Res.string.shopping_switch)
            }

            SHOPPING + 15 -> {
                getString(Res.string.shopping_bag)
            }

            SHOPPING + 16 -> {
                getString(Res.string.shopping_plant)
            }

            SHOPPING + 17 -> {
                getString(Res.string.shopping_plant2)
            }

            OTHER + 1 -> {
                getString(Res.string.other_save_money)
            }

            OTHER + 2 -> {
                getString(Res.string.other_washing_machine)
            }

            OTHER + 3 -> {
                getString(Res.string.other_bill)
            }

            OTHER + 4 -> {
                getString(Res.string.other_book)
            }

            OTHER + 5 -> {
                getString(Res.string.other_book2)
            }

            OTHER + 6 -> {
                getString(Res.string.other_car_repair)
            }

            OTHER + 7 -> {
                getString(Res.string.other_car_wash)
            }

            OTHER + 8 -> {
                getString(Res.string.other_car_fee)
            }

            OTHER + 9 -> {
                getString(Res.string.other_card_fee)
            }

            OTHER + 10 -> {
                getString(Res.string.other_creditcard)
            }

            OTHER + 11 -> {
                getString(Res.string.other_light_fee)
            }

            OTHER + 12 -> {
                getString(Res.string.other_water_fee)
            }

            OTHER + 13 -> {
                getString(Res.string.other_book2)
            }

            OTHER + 14 -> {
                getString(Res.string.other_pet)
            }

            OTHER + 15 -> {
                getString(Res.string.other_phone_fee)
            }

            OTHER + 16 -> {
                getString(Res.string.other_bill)
            }

            OTHER + 17 -> {
                getString(Res.string.other_music)
            }

            OTHER + 18 -> {
                getString(Res.string.other_music2)
            }

            OTHER + 19 -> {
                getString(Res.string.other_unknown)
            }

            OTHER + 20 -> {
                getString(Res.string.other_video)
            }

            OTHER + 21 -> {
                getString(Res.string.other_toys)
            }

            OTHER + 22 -> {
                getString(Res.string.other_repair)
            }

            OTHER + 23 -> {
                getString(Res.string.other_salary)
            }

            OTHER + 24 -> {
                getString(Res.string.other_stock_decrease)
            }

            OTHER + 25 -> {
                getString(Res.string.other_stock_increase)
            }

            OTHER + 26 -> {
                getString(Res.string.other_movie_theater)
            }

            OTHER + 27 -> {
                getString(Res.string.other_money_transfer)
            }

            OTHER + 28 -> {
                getString(Res.string.other_money_income)
            }

            OTHER + 29 -> {
                getString(Res.string.other_money_exchange)
            }

            OTHER + 30 -> {
                getString(Res.string.other_massage)
            }

            OTHER + 31 -> {
                getString(Res.string.other_cuthair)
            }

            OTHER + 32 -> {
                getString(Res.string.other_gamble)
            }

            OTHER + 33 -> {
                getString(Res.string.other_game)
            }

            OTHER + 34 -> {
                getString(Res.string.other_iphone)
            }

            OTHER + 35 -> {
                getString(Res.string.other_job)
            }

            OTHER + 36 -> {
                getString(Res.string.other_karaoke)
            }
            OTHER + 37 -> { getString(Res.string.other_bitcoin) }
            OTHER + 38 -> { getString(Res.string.other_box) }
            OTHER + 39 -> { getString(Res.string.other_camera) }
            OTHER + 40 -> { getString(Res.string.other_claw) }
            OTHER + 41 -> { getString(Res.string.other_cut) }
            OTHER + 42 -> { getString(Res.string.other_giftcard) }
            OTHER + 43 -> { getString(Res.string.other_lesson) }
            OTHER + 44 -> { getString(Res.string.other_lesson2) }
            OTHER + 45 -> { getString(Res.string.other_letter) }
            OTHER + 46 -> { getString(Res.string.other_post) }
            OTHER + 47 -> { getString(Res.string.other_stationery) }
            OTHER + 48 -> { getString(Res.string.other_study) }
            SPORTS + 18 -> { getString(Res.string.sports_jumping) }
            SPORTS + 19 -> { getString(Res.string.sports_paragliding) }
            LIFE + 26 -> { getString(Res.string.life_shampoo) }
            LIFE + 27 -> { getString(Res.string.life_shampoo2) }
            LIFE + 28 -> { getString(Res.string.life_furniture) }
            LIFE + 29 -> { getString(Res.string.life_makeup4) }
            LIFE + 30 -> { getString(Res.string.life_bath) }
            LIFE + 31 -> { getString(Res.string.life_tube) }
            LIFE + 32 -> { getString(Res.string.life_tube2) }
            FOOD + 22 -> { getString(Res.string.food_boxlunch) }
            FOOD + 23 -> { getString(Res.string.food_boxlunch2) }
            FOOD + 24 -> { getString(Res.string.food_delivery) }
            FOOD + 25 -> { getString(Res.string.food_dog) }
            FOOD + 26 -> { getString(Res.string.food_foodset) }
            FOOD + 27 -> { getString(Res.string.food_meal) }
            FOOD + 28 -> { getString(Res.string.food_meal2) }
            FOOD + 29 -> { getString(Res.string.food_sauce) }
            FOOD + 30 -> { getString(Res.string.food_streetfood) }
            FOOD + 31 -> { getString(Res.string.food_water) }
            HEALTH + 11 -> { getString(Res.string.health_calendar) }
            HEALTH + 12 -> { getString(Res.string.health_card) }
            else -> {
                "?"
            }
        }
    }



    fun getColorByTypeId(id: Long): Color {
        return Color(
            when (id) {
                FOOD -> 0xFFE5E671
                TRAFFIC -> 0xFFB3E5E6
                WEAR -> 0xFFD1B3FF
                SPORTS -> 0xFFB3E6F2
                SHOPPING -> 0xFFE6B3F1
                HEALTH -> 0xFFE6B3B3
                LIFE -> 0xFFE6C2B3
                else -> 0xFFB3FFB3
            }
        )
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun getTypeStringByTypeId(typeId: Long): String {
        return when(typeId){
            RECENTLY -> { stringResource(Res.string.recently) }
            FOOD -> { stringResource(Res.string.food)  }
            TRAFFIC -> {  stringResource(Res.string.traffic)  }
            LIFE -> { stringResource(Res.string.life)  }
            HEALTH -> { stringResource(Res.string.health)  }
            SPORTS -> { stringResource(Res.string.sports)  }
            WEAR -> { stringResource(Res.string.wear)  }
            SHOPPING -> { stringResource(Res.string.shopping)  }
            OTHER -> { stringResource(Res.string.other)  }
            else -> ""
        }
    }

    suspend fun getTypeStringById(typeId: Long): String {
        return when(typeId){
            RECENTLY -> { getString(Res.string.recently) }
            FOOD -> { getString(Res.string.food)  }
            TRAFFIC -> {  getString(Res.string.traffic)  }
            LIFE -> { getString(Res.string.life)  }
            HEALTH -> { getString(Res.string.health)  }
            SPORTS -> { getString(Res.string.sports)  }
            WEAR -> { getString(Res.string.wear)  }
            SHOPPING -> { getString(Res.string.shopping)  }
            OTHER -> { getString(Res.string.other)  }
            else -> ""
        }
    }

}