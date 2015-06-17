package com.flowthings.client.domain;

public class TokenPermissions extends Permissions {
  @SuppressWarnings("hiding")
  public static final TokenPermissions ALL = new TokenPermissions(true, true, true, true, true);
  @SuppressWarnings("hiding")
  public static final TokenPermissions NONE = new TokenPermissions(false, false, false, false, false);
  @SuppressWarnings("hiding")
  public static final TokenPermissions ADMIN = ALL;

  public TokenPermissions() {
  }

  public TokenPermissions(boolean dropRead, boolean dropWrite) {
    super(true, false, dropRead, dropWrite, false);
  }

  protected TokenPermissions(boolean read, boolean write, boolean dropRead, boolean dropWrite, boolean admister) {
    super(read, write, dropRead, dropWrite, admister);
  }

  @Override
  public String toString() {
    return "TokenPermissions [dropRead=" + dropRead + ", dropWrite=" + dropWrite + "]";
  }
}
