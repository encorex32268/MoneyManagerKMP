import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


@Composable
expect fun AdMobBanner(
    modifier: Modifier = Modifier
)

object AdMobBannerController {

    private val _isCloseAdMobBanner = MutableStateFlow(false)
    val isCloseAdMobBanner = _isCloseAdMobBanner.asStateFlow()

    fun setCloseAdMobBanner(isClose: Boolean){
        _isCloseAdMobBanner.update {
            isClose
        }
    }

    @Composable
    fun AdMobBannerCompose(
        modifier: Modifier = Modifier
    ){
        if (!isCloseAdMobBanner.value){
            AdMobBanner(modifier = modifier)
        }
    }

}

