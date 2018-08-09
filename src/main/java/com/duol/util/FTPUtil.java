package com.duol.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Created by geely
 */
@SuppressWarnings("unused")
public class FTPUtil {

    private static final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty("ftp.server.ip");
    private static String ftpUser = PropertiesUtil.getProperty("ftp.user");
    private static String ftpPass = PropertiesUtil.getProperty("ftp.pass");

    private static final String remotePath = "ftp";



    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPOperation ftpOperation = new FTPOperation();

        logger.info("开始连接ftp服务器");
        boolean result = ftpOperation.doUploadFile(fileList);
        logger.info("结束上传,上传结果:{}", result);
        return result;
    }

    public static Path createDirectory(String path) {
        File fileDir = new File(path);
        Path rootLocation = fileDir.toPath();
        if (!Files.isDirectory(rootLocation)) {
            try {
                Files.createDirectory(rootLocation);
            } catch (IOException e) {
                logger.error("create director fault:", e);
            }
        }
        return rootLocation;
    }

    public static boolean hasFile(String filename) {
        FTPOperation ftpOperation = new FTPOperation();
        return ftpOperation.doHasFile(filename);//todo
    }



    public static boolean deleteFile(String filename) {
        FTPOperation ftpOperation = new FTPOperation();
        boolean result = ftpOperation.doDeleteFile(filename);
        logger.info("删除结果：{}", result);
        return result;
    }



    private static class FTPOperation{
        private String ip;
        private int port;
        private String user;
        private String pwd;
        private FTPClient ftpClient;

        FTPOperation(String ip, int port, String user, String pwd) {
            this.ip = ip;
            this.port = port;
            this.user = user;
            this.pwd = pwd;
            if (this.connectServer()) throw new RuntimeException("连接失败");
            boolean change = false;
            try {
                change = ftpClient.changeWorkingDirectory(remotePath);
            } catch (IOException e) {
                logger.error("",e);
            }
            logger.info("ftp改变工作路径：{}", change);
        }

        FTPOperation() {
            this(ftpIp, 21, ftpUser, ftpPass);
        }

        boolean doHasFile(String filename) {
            try {
                FTPFile[] ftpFiles = ftpClient.listFiles(filename);
                if (ftpFiles.length > 0) {
                    return true;
                }
            } catch (IOException e) {
                logger.error("列出文件失败，", e);
            }
            return false;
        }

        boolean doDeleteFile(String filename) {
            if (!doHasFile(filename)) {
                return false;
            }
            boolean flag = false;
            try {
                logger.info("开始删除文件");
                ftpClient.dele(filename);
                ftpClient.logout();
                flag = true;
                logger.info("删除文件成功");
            } catch (Exception e) {
                logger.error("删除文件失败", e);
            } finally {
                if (ftpClient.isConnected()) {
                    try {
                        ftpClient.disconnect();
                    } catch (IOException e) {
                        logger.error("ftp断开连接", e);
                    }
                }
            }
            return flag;
        }


        boolean doUploadFile(List<File> fileList) throws IOException {
            boolean uploaded = true;
            InputStream fis = null;
            //连接FTP服务器
            try {
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
//                ftpClient.enterLocalPassiveMode();   会出错
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setBufferSize(1024);
                int replyCode = ftpClient.getReplyCode();
                logger.info("ftp.code:{}", replyCode);
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    logger.error("连接失败");
                }

                for (File fileItem : fileList) {
                    logger.info("文件名：{}", fileItem.getName());
                    fis = new FileInputStream(fileItem);
                    boolean storeSuccess = ftpClient.storeFile(fileItem.getName(), fis);
                    if (!storeSuccess) logger.error("文件上传失败");
                }

            } catch (IOException e) {
                logger.error("上传文件异常", e);
                uploaded = false;
                e.printStackTrace();
            } finally {
                Objects.requireNonNull(fis).close();
                ftpClient.disconnect();
            }
            return uploaded;
        }


        boolean connectServer() {

            boolean isSuccess = false;
            ftpClient = new FTPClient();
            try {
                ftpClient.connect(ip, port);
                isSuccess = ftpClient.login(user, pwd);
                if (!isSuccess) throw new IOException("登录失败");
            } catch (IOException e) {
                logger.error("连接FTP服务器异常", e);
            }
            return isSuccess;
        }


        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public FTPClient getFtpClient() {
            return ftpClient;
        }

        public void setFtpClient(FTPClient ftpClient) {
            this.ftpClient = ftpClient;
        }
    }



}
