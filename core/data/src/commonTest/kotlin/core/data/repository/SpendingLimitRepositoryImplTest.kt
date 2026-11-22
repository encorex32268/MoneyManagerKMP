package core.data.repository

import core.data.database.dao.SpendingLimitDao
import core.data.database.entity.SpendingLimitRoomEntity
import core.domain.model.SpendingLimit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

private class FakeSpendingLimitDao : SpendingLimitDao {
    private val items = MutableStateFlow<List<SpendingLimitRoomEntity>>(emptyList())
    var upsertAllCalled = false

    override fun getAll(): Flow<List<SpendingLimitRoomEntity>> = items

    override fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimitRoomEntity?> =
        items.map { list -> list.firstOrNull { it.year == year && it.month == month } }

    override suspend fun upsert(spendingLimit: SpendingLimitRoomEntity) {
        items.update { list -> list.filterNot { it.id == spendingLimit.id } + spendingLimit }
    }

    override suspend fun upsertAll(spendingLimits: List<SpendingLimitRoomEntity>) {
        upsertAllCalled = true
        val ids = spendingLimits.map { it.id }.toSet()
        items.update { list -> list.filterNot { it.id in ids } + spendingLimits }
    }

    override suspend fun deleteById(id: String) {
        items.update { list -> list.filterNot { it.id == id } }
    }
}

class SpendingLimitRepositoryImplTest {

    @Test
    fun `insert update and delete manage spending limits by id`() = runTest {
        val dao = FakeSpendingLimitDao()
        val repo = SpendingLimitRepositoryImpl(dao)

        val limit = SpendingLimit(id = "sl-1", year = 2025, month = 11, limit = 15000)
        repo.insert(limit)
        assertEquals(limit, repo.getSpendingLimit(2025, 11).first())

        val updated = limit.copy(limit = 20000)
        repo.update(updated)
        assertEquals(20000, repo.getSpendingLimit(2025, 11).first()?.limit)

        repo.delete(updated)
        assertNull(repo.getSpendingLimit(2025, 11).first())
    }

    @Test
    fun `restore with empty list does not call upsertAll`() = runTest {
        val dao = FakeSpendingLimitDao()
        val repo = SpendingLimitRepositoryImpl(dao)

        repo.restore(emptyList())
        assertEquals(false, dao.upsertAllCalled)
        assertEquals(emptyList(), repo.getAll().first())
    }

    @Test
    fun `restore with non-empty list upserts all limits`() = runTest {
        val dao = FakeSpendingLimitDao()
        val repo = SpendingLimitRepositoryImpl(dao)

        val limits = listOf(
            SpendingLimit(id = "1", year = 2025, month = 1, limit = 10000),
            SpendingLimit(id = "2", year = 2025, month = 2, limit = 12000)
        )
        repo.restore(limits)

        assertEquals(true, dao.upsertAllCalled)
        assertEquals(2, repo.getAll().first().size)
    }
}
