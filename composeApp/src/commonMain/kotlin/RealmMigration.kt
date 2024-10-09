import io.realm.kotlin.dynamic.DynamicMutableRealmObject
import io.realm.kotlin.dynamic.DynamicRealmObject
import io.realm.kotlin.migration.AutomaticSchemaMigration

val migration_0_to_1 = AutomaticSchemaMigration {
    it.enumerate("ExpenseEntity") {
            oldObject: DynamicRealmObject,
            newObject: DynamicMutableRealmObject? ->
        newObject?.run {
          set("content","")
        }
    }
}