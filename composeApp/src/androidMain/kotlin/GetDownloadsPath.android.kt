import android.os.Environment
import java.io.File


actual fun getDownloadsPath(): String {
    val downloadPath = Environment.getExternalStoragePublicDirectory(
        Environment.DIRECTORY_DOWNLOADS
    ).path
    val folderName = "MoneyManagerBackup"
    val newFolder = File(downloadPath , folderName)
    if (!newFolder.exists()){
        newFolder.mkdirs()
    }
    return "${downloadPath}/${folderName}"
}