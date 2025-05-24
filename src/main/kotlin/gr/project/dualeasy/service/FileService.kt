package gr.project.dualeasy.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import gr.project.dualeasy.common.config.CloudStorageConfig
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.util.*

@Service
class FileService(
    private val yandexS3: AmazonS3,
    private val cloudStorageConfig: CloudStorageConfig,
) {
    /**
     * Загружает файл в S3 и возвращает публичную ссылку
     */
    fun uploadFileAndGetUrl(file: MultipartFile?): String? {
        if (file == null) {
            return null
        }
        val fileKey = UUID.randomUUID().toString()
        saveToS3(file, fileKey)
        return getFileUrl(fileKey)
    }

    private fun saveToS3(
        file: MultipartFile,
        key: String,
    ) {
        try {
            ByteArrayInputStream(file.bytes).use { inputStream ->
                val metadata =
                    ObjectMetadata().apply {
                        contentLength = file.size
                        contentType = file.contentType
                    }

                yandexS3.putObject(
                    cloudStorageConfig.bucketName,
                    key,
                    inputStream,
                    metadata,
                )
            }
        } catch (e: Exception) {
            throw RuntimeException("Не удалось сохранить файл в S3", e)
        }
    }

    /**
     * Получение публичного URL файла по ключу
     */
    fun getFileUrl(key: String): String = yandexS3.getUrl(cloudStorageConfig.bucketName, key).toString()
}
