
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mongodb.kbson.ObjectId

class Task : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title : String = ""
    var isDone : Boolean = false
}

@Composable
@Preview
fun App() {


//    val config = RealmConfiguration.create(
//        schema = setOf(Task::class)
//    )
//    val realm = Realm.open(config)

    AppTheme {
        Navigator(
            RootScreen()
        )

    }
}


