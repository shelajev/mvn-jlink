package com.igormaznitsa.mvnjlink.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import javax.annotation.Nonnull;
import java.nio.file.Path;

@Mojo(name = "cache-jdk", defaultPhase = LifecyclePhase.INITIALIZE, threadSafe = true)
public class MvnCacheJdkMojo extends AbstractJdkToolMojo {

  @Parameter(name = "jdkPathProperty", defaultValue = "mvnjlink.cache.jdk.path")
  private String jdkPathProperty = "mvnjlink.cache.jdk.path";

  @Nonnull
  public String getJdkPathProperty() {
    return this.jdkPathProperty;
  }

  @Override
  public void onExecute() throws MojoExecutionException, MojoFailureException {
    final Path path = this.getSourceJdkFolderFromProvider();
    this.getLog().info("cached JDK path: " + path);
    this.getProject().getProperties().setProperty(this.getJdkPathProperty(), path.toString());
    this.getLog().info(String.format("Project property '%s' <= '%s'", this.getJdkPathProperty(), path.toString()));
  }
}