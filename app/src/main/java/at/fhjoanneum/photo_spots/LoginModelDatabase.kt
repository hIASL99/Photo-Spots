package at.fhjoanneum.photo_spots

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LoginModel::class], version = 1)
abstract class LoginModelDatabase : RoomDatabase() {
    abstract val loginModelDao: LoginModelDao

    companion object {
        private var INSTANCE: LoginModelDatabase? = null
        fun getDatabase(context: Context): LoginModelDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): LoginModelDatabase{
            return Room.databaseBuilder(
                context,
                LoginModelDatabase::class.java, "login-model-db"
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}