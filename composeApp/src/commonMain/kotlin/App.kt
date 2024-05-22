
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideOrientation
import cafe.adriel.voyager.transitions.SlideTransition
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mongodb.kbson.ObjectId

@Composable
@Preview
fun App() {

    AppTheme {
        Navigator(RootScreen()){
            SlideTransition(
                navigator = it,
                orientation = SlideOrientation.Vertical
            )
        }

    }
}


