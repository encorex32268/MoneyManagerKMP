import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.domain.KeySettings
import feature.core.domain.model.Type
import feature.core.domain.repository.TypeRepository
import feature.core.presentation.CategoryList
import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmUUID
import kotlinx.coroutines.launch

class AppViewModel(
    private val keySettings: KeySettings,
): ViewModel() {
    init {
        AdMobBannerController.setCloseAdMobBanner(
            isClose = keySettings.getCloseAdBanner()
        )
    }
}