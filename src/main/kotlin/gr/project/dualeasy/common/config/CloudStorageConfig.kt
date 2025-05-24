package gr.project.dualeasy.common.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

@Configuration
@Component
class CloudStorageConfig {
    @Value("\${storage.cloud.access.key}")
    var accessKey: String? = null

    @Value("\${storage.cloud.secret.key}")
    var secretKey: String? = null

    @Value("\${storage.cloud.bucket.name}")
    var bucketName: String? = null

    @Value("\${storage.cloud.endpoint.url}")
    var endpointUrl: String? = null

    @Value("\${storage.cloud.endpoint.region}")
    var region: String? = null

    @Bean
    fun getClient(): AmazonS3 {
        val credentials: AWSCredentials =
            BasicAWSCredentials(
                accessKey,
                secretKey,
            )

        return AmazonS3ClientBuilder
            .standard()
            .withCredentials(AWSStaticCredentialsProvider(credentials))
            .withEndpointConfiguration(
                AwsClientBuilder.EndpointConfiguration(
                    endpointUrl,
                    region,
                ),
            ).build()
    }
}
