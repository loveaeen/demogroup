package com.example.minio;

import io.minio.MinioClient;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class MinioApplicationTests {

    @Test
    void contextLoads() {
        try {
            // 参数为：图床，账号，密码
            MinioClient minioClient = new MinioClient("http://127.0.0.1:9000", "minioadmin", "minioadmin");

            // 检查文件夹是否已经存在
            boolean isExist = minioClient.bucketExists("jiedu");
            if(isExist) {
                System.out.println("文件夹已经存在了");
            }
            else {
                // 创建一个名为managertest的文件夹
                System.out.println("文件夹还没存在");
                minioClient.makeBucket("jiedu");
            }

            // 使用putObject上传一个文件到文件夹中。
            //参数为：文件夹，要存成的名字，要存的文件
            minioClient.putObject("jiedu","ceshi.png", "/Users/hanzhiguo/Downloads/640.png");
            System.out.println("成功了");


            //使用getObject获取一个文件
            // 调用statObject()来判断对象是否存在。
            //minioClient.statObject("managertest", "1.png");
            // 获取1.png的流并保存到photo.png文件中。
            //参数为：文件夹，要获得的文件，要写入的文件
            //minioClient.getObject("managertest", "1.png", "C:/Users/Administrator/Desktop/photo.png");


        } catch(MinioException e) {
            System.out.println("错误: " + e);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
