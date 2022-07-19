package com.hcc.utils;

import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastDFSUtils {

    private static Logger logger = LoggerFactory.getLogger(FastDFSUtils.class);

    //ClientGlobal.init 方法会读取配置文件，并初始化对应的属性。
    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!", e);
        }
    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     */
    public static String[] upload(MultipartFile file) {
        String name = file.getOriginalFilename();
        logger.info("File Name: " + name);

        long startTime = System.currentTimeMillis();
        String[] uploadResults = null;
        StorageClient storageClient = null;
        try {
            //获取storage客户端
            storageClient = getStorageClient();
            //上传
            uploadResults = storageClient.upload_file(file.getBytes(), name.substring(name.lastIndexOf(".") + 1),
                    null);
        } catch (IOException e) {
            logger.error("IO Exception when uploadind the file:" + name, e);
        } catch (Exception e) {
            logger.error("Non IO Exception when uploadind the file:" + name, e);
        }
        logger.info("upload_file time used:" + (System.currentTimeMillis() - startTime) + " ms");

        //验证上传结果
        if (uploadResults == null && storageClient != null) {
            logger.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        //上传文件成功会返回 groupName。
        logger.info("upload file successfully!!!" + "group_name:" + uploadResults[0] + ", remoteFileName:" + " " + uploadResults[1]);
        return uploadResults;
    }


    /**
     * 获取文件信息
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param groupName
     * @param remoteFileName
     * @return
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getStorageClient();
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            InputStream ins = new ByteArrayInputStream(fileByte);
            return ins;
        } catch (IOException e) {
            logger.error("IO Exception: Get File from Fast DFS failed", e);
        } catch (Exception e) {
            logger.error("Non IO Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param groupName
     * @param remoteFileName
     * @tadminows Exception
     */
    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        StorageClient storageClient = getStorageClient();
        int i = storageClient.delete_file(groupName, remoteFileName);
        logger.info("delete file successfully!!!" + i);
    }


    /**
     * 生成Storage客户端
     *
     * @return
     * @tadminows IOException
     */
    private static StorageClient getStorageClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        StorageClient storageClient = new StorageClient(trackerServer, null);
        return storageClient;
    }

    /**
     * 生成Tracker服务器端
     *
     * @return
     * @tadminows IOException
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return trackerServer;
    }

    /**
     * 获取文件路径
     *
     * @return
     */
    public static String getTrackerUrl() {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = null;
        StorageServer storeStorage = null;
        try {
            trackerServer = trackerClient.getTrackerServer();
            storeStorage = trackerClient.getStoreStorage(trackerServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://" + storeStorage.getInetSocketAddress().getHostString() + ":701/";
    }
}
