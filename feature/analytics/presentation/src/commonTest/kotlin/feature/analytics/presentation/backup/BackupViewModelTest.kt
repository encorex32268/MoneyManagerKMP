package feature.analytics.presentation.backup

import feature.analytics.domain.BackupRepository
import feature.analytics.domain.BackupResult
import feature.analytics.domain.util.backup.BackupError
import io.github.vinceglb.filekit.core.PlatformFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeBackupRepository : BackupRepository {
    var backupResult = BackupResult()
    var restoreResult = BackupResult()
    var restoreCalled = false

    override suspend fun backup(): BackupResult = backupResult

    override suspend fun restoreData(platformFile: PlatformFile): BackupResult {
        restoreCalled = true
        return restoreResult
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class BackupViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnBackup emits TaskSuccess when repository succeeds`() = runTest {
        val repo = FakeBackupRepository()
        val vm = BackupViewModel(repo)

        vm.onAction(BackupAction.OnBackup)

        assertEquals(BackupUiEvent.TaskSuccess, vm.uiEvent.first())
    }

    @Test
    fun `OnBackup emits SendError when repository returns error`() = runTest {
        val repo = FakeBackupRepository().apply {
            backupResult = BackupResult(error = BackupError.LocalError.DOWNLOADS_PATH_IS_EMPTY)
        }
        val vm = BackupViewModel(repo)

        vm.onAction(BackupAction.OnBackup)

        assertEquals(BackupUiEvent.SendError(BackupError.LocalError.DOWNLOADS_PATH_IS_EMPTY), vm.uiEvent.first())
    }

    @Test
    fun `OnImport with null file does not call restoreData`() = runTest {
        val repo = FakeBackupRepository()
        val vm = BackupViewModel(repo)

        vm.onAction(BackupAction.OnImport(null))

        assertEquals(false, repo.restoreCalled)
    }
}
