package com.flowthings.client.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Permissions implements Serializable {
  protected boolean administer;
  protected boolean read;
  protected boolean write;
  protected boolean dropRead;
  protected boolean dropWrite;
  public static final Permissions ALL = new Permissions(true, true, true, true, true);
  public static final Permissions NONE = new Permissions(false, false, false, false, false);
  public static final Permissions ADMIN = ALL;

  public Permissions() {
  }

  public Permissions(boolean read, boolean write, boolean dropRead, boolean dropWrite) {
    this(read, write, dropRead, dropWrite, false);
  }

  public static Permissions tokenPermissions(boolean dropRead, boolean dropWrite) {
    return new Permissions(true, false, dropRead, dropWrite);
  }

  protected Permissions(boolean read, boolean write, boolean dropRead, boolean dropWrite, boolean admister) {
    this.read = read;
    this.write = write;
    this.dropRead = dropRead;
    this.dropWrite = dropWrite;
    this.administer = admister;
  }

  public Permissions union(Permissions permissions) {
    if (permissions.administer || administer) {
      return Permissions.ADMIN;
    }
    return new Permissions(this.canRead() || permissions.canRead(), this.canWrite() || permissions.canWrite(),
        this.canReadDrops() || permissions.canReadDrops(), this.canWriteDrops() || permissions.canWriteDrops());
  }

  public Permissions intersection(Permissions permissions) {
    if (permissions.administer && administer) {
      return permissions;
    }
    return new Permissions(this.canRead() && permissions.canRead(), this.canWrite() && permissions.canWrite(),
        this.canReadDrops() && permissions.canReadDrops(), this.canWriteDrops() && permissions.canWriteDrops());
  }

  public boolean canRead() {
    return read || administer;
  }

  public boolean canWrite() {
    return write || administer;
  }

  public boolean canReadDrops() {
    return dropRead || administer;
  }

  public boolean canWriteDrops() {
    return dropWrite || administer;
  }

  public boolean isAdministrator() {
    return administer;
  }

  @Override
  public String toString() {
    List<String> l = new ArrayList<>();
    if (read) l.add("R");
    if (write) l.add("W");
    if (dropRead) l.add("DR");
    if (dropWrite) l.add("DW");
    if (administer) l.add("A");
    return l.isEmpty() ? "<none>" : l.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (administer ? 1231 : 1237);
    result = prime * result + (dropRead ? 1231 : 1237);
    result = prime * result + (dropWrite ? 1231 : 1237);
    result = prime * result + (read ? 1231 : 1237);
    result = prime * result + (write ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Permissions other = (Permissions) obj;
    if (administer != other.administer) return false;
    if (dropRead != other.dropRead) return false;
    if (dropWrite != other.dropWrite) return false;
    if (read != other.read) return false;
    if (write != other.write) return false;
    return true;
  }

}
