package feature.core.domain.model

data class Category(
    val id: Int?=null,
    val nameResId: Int?=null,
    val name: String= "",
    val resIdString: String?=null,
    val typeId: Int,
){
    val uuid: String=this.hashCode().toString()
}
