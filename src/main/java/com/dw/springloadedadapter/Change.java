package com.dw.springloadedadapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.InputStream;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Change {

  private String path;
  private InputStream inputStream;
  private Type type;
  private String orignalFileName;

  /**
   * Initializing new instance of {@link Change}.
   * 
   * @param path {@link #path}
   * @param type {@link Type}
   */
  public Change(String path, Type type) {
    super();
    this.path = path;
    this.type = type;
  }

  public Change() {
    // NO-OP
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public InputStream getInputStream() {
    return inputStream;
  }

  public void setInputStream(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getOrignalFileName() {
    return orignalFileName;
  }

  public void setOrignalFileName(String orignalFileName) {
    this.orignalFileName = orignalFileName;
  }
}
