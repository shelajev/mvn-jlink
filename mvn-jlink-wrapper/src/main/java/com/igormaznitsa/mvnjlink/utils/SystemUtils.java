/*
 * Copyright 2019 Igor Maznitsa.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igormaznitsa.mvnjlink.utils;

import org.apache.maven.plugin.logging.Log;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Closeable;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.Files.isRegularFile;

public final class SystemUtils {
  private SystemUtils() {

  }

  @Nullable
  public static Path findJdkExecutable(@Nonnull final Log log, @Nonnull final Path jdkFolder, @Nonnull final String jdkExecutableFileName) {
    Path binFolder = jdkFolder.resolve("bin");
    if (isDirectory(binFolder)) {
      log.debug("Detected JDK bin folder: " + binFolder);
    } else {
      log.debug("Can't find bin folder in jdk: " + jdkFolder);
      binFolder = jdkFolder.resolve("Contents/Home/bin");
      if (isDirectory(binFolder)) {
        log.debug("Detected MacOS JDK bin folder: " + jdkFolder);
      } else {
        log.debug("Can't find MacOS bin folder in jdk: " + jdkFolder);
        return null;
      }
    }

    Path result = binFolder.resolve(ensureOsExtension(jdkExecutableFileName));
    if (!isRegularFile(result)) {
      log.error("Can't find file: " + result);
      result = null;
    } else if (!Files.isExecutable(result)) {
      log.error("Can't find executable file: " + result);
      result = null;
    }
    return result;
  }

  @Nullable
  public static String ensureOsExtension(@Nullable final String text) {
    final String result;
    if (text == null) {
      result = null;
    } else {
      if (org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS) {
        if (!text.endsWith(".exe")) {
          result = text + ".exe";
        } else {
          result = text;
        }
      } else {
        result = text;
      }
    }
    return result;
  }

  public static void closeCloseable(@Nullable final Closeable closeable, @Nonnull final Log logger) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (Exception ex) {
        logger.debug("Can't close closeable object: " + closeable, ex);
      }
    }
  }


}
