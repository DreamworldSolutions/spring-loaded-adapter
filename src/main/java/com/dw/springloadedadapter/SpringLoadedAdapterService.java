package com.dw.springloadedadapter;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

  /**
   * Path of base-directory where this service creates/updates/deletes files.
   * Note:: It must not end with '/'.
   */
  private String basePath;// TODO: autowire using @Value.


  /**
   * Creates intermediate directory if required. <br>
   * Creates/replaces file at the given path.
   * 
   * @param path Path of the file. So, last segment is filename.
   * @param file a temporary (source) file which is to be copied.
   * @throws {@link IllegalArgumentException} if path or file isn't provided. OR Path isn't starting
   *         with '/'.
   * @throws {@link IllegalStateException} if directory exists at a given path.
   */
  public void put(String path, File file) {
    // TODO :: adds/updates file at given path.
  }


  /**
   * Deletes file or directory exist at a path. If directory isn't empty, all the content is also
   * deleted recursively.
   * 
   * @param file
   * @throws {@link IllegalArgumentException} if path or file isn't provided. OR Path isn't starting
   *         with '/'.
   */
  public void delete(String path) {
    // TODO :: delete file to classpath for given path
  }
}
