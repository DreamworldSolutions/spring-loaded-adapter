package com.dw.springloadedadapter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * This service layer provide a methods to add, replace or delete file on given specific path. <br/>
 * For add operation, user can add file on given path <br/>
 * For replace operation, user can replace file with existing file on given path. <br/>
 * For delete operation, user can delete file for given path.
 * 
 * @author Jaydeep Kumbhani
 *
 */
@Service
public class SpringLoadedAdapterService {

  private static final Logger logger = LoggerFactory.getLogger(SpringLoadedAdapterService.class);

  private String basePath;

  /**
   * Path of base-directory where this service creates/updates/deletes files. Note:: It must not end
   * with '/'.
   */
  public SpringLoadedAdapterService(
      @Value("${springloadedadapter.classDir:/app/BOOT-INF/classes}") String basePath) {

    if (!StringUtils.startsWith(basePath, "/") || StringUtils.endsWith(basePath, "/")) {
      throw new RuntimeException("Base path either not start with / or end with /");
    }

    File file = new File(basePath);
    if (!file.exists()) {
      throw new RuntimeException("Given base path not exist");
    }

    this.basePath = basePath;
  }


  /**
   * Creates intermediate directory if required. <br>
   * Creates/replaces file at the given path.
   * 
   * @param change {@link Change} instance and it contain Change.Path which is represent of path of
   *        the file. So, last segment is filename.<br/>
   *        Change.inputStream which hold upload file`s inputStream.
   * @throws {@link IllegalArgumentException} if path or file isn't provided. OR Path isn't starting
   *         with '/'.
   * @throws {@link IllegalStateException} if directory exists at a given path.
   */
  public void put(Change change) {
    logger.debug("put() :: change of file is updating to path {}", change.getPath());

    if (StringUtils.isBlank(change.getPath())) {
      throw new IllegalArgumentException("change.path is invalid");
    }

    if (!StringUtils.startsWith(change.getPath(), "/")) {
      throw new IllegalArgumentException(
          "change.path '" + change.getPath() + "' is not start with /");
    }

    String filePath = basePath + change.getPath();
    if (change.getType().equals(Type.DELETED)) {
      deleteFile(filePath);
      logger.info("put() :: deleted file or directory on given path {}", filePath);
    } else {
      putFile(filePath, change.getInputStream(), change.getOrignalFileName());
      logger.info("put() :: added or replaced new uploaded file on given path {}", filePath);
    }
  }

  /**
   * Deletes file or directory exist at a path. If directory isn't empty, all the content is also
   * deleted recursively.
   * 
   * @param path represent path of given file.
   */
  private void deleteFile(String path) {
    logger.debug("deleteFile() :: deleting file form given path {}", path);

    File file = new File(path);
    if (!file.exists()) {
      logger.debug("deleteFile() :: No file exist on path {}, so no need to delete it.", path);
      return;
    }

    if (file.isDirectory()) {
      try {
        FileUtils.deleteDirectory(file);
      } catch (IOException e) {
        throw new RuntimeException("Error occur while deleting file on path " + path, e);
      }
    } else {
      Boolean isDeleted = file.delete();
      if (!isDeleted) {
        throw new RuntimeException("File not able to delete on path " + path);
      }
    }
  }

  /**
   * Add/update files or directory exist at a path. if directory isn`t empty, all the content is
   * also add or updated recursively.
   * 
   * @param path where update/add new content.
   * @param inputStream {@link InputStream} instance.
   */
  private void putFile(String path, InputStream inputStream, String orignalFileName) {
    logger.debug("putFile() :: replaceing file {} form given path {}", orignalFileName, path);

    File file = new File(path);

    if (file.isDirectory()) {
      String tempDirPath = FileUtils.getTempDirectoryPath();
      File tempStoreInputStream = new File(tempDirPath + "/" + orignalFileName);
      try {
        FileUtils.copyInputStreamToFile(inputStream, tempStoreInputStream);
      } catch (IOException e1) {
        throw new RuntimeException("Error while inputstream tempory copy to temp file "
            + tempStoreInputStream.getAbsolutePath(), e1);
      }


      try {
        FileUtils.copyFileToDirectory(tempStoreInputStream, file);
      } catch (IOException e) {
        throw new RuntimeException(
            "Error while inputstream copy to given path " + path + " of directroy", e);
      }

      tempStoreInputStream.delete();
    } else {
      try {
        FileUtils.copyInputStreamToFile(inputStream, file);
      } catch (IOException e) {
        throw new RuntimeException(
            "Error while inputstream copy to given path " + path + " of file", e);
      }
    }
  }
}
