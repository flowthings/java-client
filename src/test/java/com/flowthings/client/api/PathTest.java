package com.flowthings.client.api;

import org.junit.Test;

/**
 * Created by matt on 7/10/16.
 */
public class PathTest {

  @Test
  public void validPaths() throws Exception {
    Flowthings.dropFromPath("/abc");
    Flowthings.dropFromPath("/abc/");
    Flowthings.dropFromPath("/abc/def/ghi");
  }

  @Test
  public void invalidPaths() throws Exception {
    assertInvalid("/");
    assertInvalid("/abc//");
    assertInvalid("/.one");
    assertInvalid("beep");
    assertInvalid("boop/");
  }

  private static void assertInvalid(String p1) {
    try {
      Flowthings.dropFromPath("/abc");
    } catch (IllegalArgumentException e) {
    }
  }

}
