package org.jconf.mx.springday.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
public class AppProperties {

  private String scriptsFolder;

  public AppProperties() {}

  public String getScriptsFolder() {
    return scriptsFolder;
  }

  public void setScriptsFolder(String scriptsFolder) {
    this.scriptsFolder = scriptsFolder;
  }
}
