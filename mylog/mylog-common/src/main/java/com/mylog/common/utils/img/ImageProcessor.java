package com.mylog.common.utils.img;

import com.mylog.common.constant.Constants;
import io.minio.errors.MinioException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author pss
 * @date 2025/3/31
 */
public class ImageProcessor {

    // 目标尺寸配置（示例：1200x630 常见文章封面尺寸）
//    private static final int TARGET_WIDTH = 1200;
//    private static final int TARGET_HEIGHT = 630;

    /**
     * 处理并上传图片到MinIO
     *
     * @param originalImage 原始图片字节数组
     * @return MinIO存储路径
     */
    public static byte[] processAndUpload(byte[] originalImage) throws IOException, MinioException {
        // 1. 图片处理
//        byte[] processedImage = processImage(originalImage);

        // 2. 上传MinIO
//        return uploadToMinio(processedImage);
        return processImage(originalImage);
    }

    /**
     * 核心图片处理逻辑
     */
    private static byte[] processImage(byte[] imageData) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            BufferedImage image = ImageIO.read(inputStream);

            Thumbnails.of(image)
                    // 按比例缩放（保持宽高比）
                    .size(Constants.TARGET_WIDTH, Constants.TARGET_HEIGHT)
                    // 当图片比例不一致时，从中心裁剪
                    .crop(Positions.CENTER)
                    // 输出质量设置（0.8为推荐值）
                    .outputQuality(0.8)
                    // 输出格式统一为JPEG
                    .outputFormat("jpg").toOutputStream(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * 上传到MinIO
     */
//    private static String uploadToMinio(byte[] imageData) throws MinioException {
//        try {
//            MinioClient minioClient = MinioClient.builder()
//                    .endpoint(MINIO_ENDPOINT)
//                    .credentials(MINIO_ACCESS_KEY, MINIO_SECRET_KEY)
//                    .build();
//
//            // 生成唯一文件名
//            String objectName = UUID.randomUUID() + ".jpg";
//
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(MINIO_BUCKET)
//                            .object(objectName)
//                            .stream(new ByteArrayInputStream(imageData), imageData.length, -1)
//                            .contentType("image/jpeg")
//                            .build());
//
//            return String.format("%s/%s/%s", MINIO_ENDPOINT, MINIO_BUCKET, objectName);
//        } catch (InvalidKeyException | NoSuchAlgorithmException |
//                 InsufficientDataException | InternalException |
//                 ErrorResponseException | InvalidResponseException |
//                 XmlParserException | ServerException e) {
//            throw new MinioException("文件上传失败: " + e.getMessage());
//        }
//    }


}
