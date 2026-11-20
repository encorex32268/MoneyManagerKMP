package core.domain.exts

fun Int.formatAddZero(): String {
    return this.toString().padStart(2,'0')
}