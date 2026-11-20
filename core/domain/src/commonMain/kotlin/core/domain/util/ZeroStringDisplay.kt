package core.domain.util

fun zeroStringDisplay(number: Int): String {
    return if (number < 10){
         "0${number}"
    }else{
        number.toString()
    }
}