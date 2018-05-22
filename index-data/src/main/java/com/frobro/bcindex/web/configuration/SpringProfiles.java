package com.frobro.bcindex.web.configuration;

/**
 * Created by rise on 5/20/17.
 */
public enum SpringProfiles {
  DEV {
    @Override
    public boolean isActive() {
      String profile = getProfile();
      return isNull(profile) || getValue().equals(profile) || "devNoPop".equals(profile);
    }
    /*
     * dev is our default profile so
     * activate it if no profile is
     * set
     */
    @Override
    public boolean shouldActivate() {
      return getProfile() == null;
    }
  },
  POSTGRES,
  RELEASE;

  public String getValue() {
    return name().toLowerCase();
  }

  /*
   * this exists so the dev profile
   * will be activated in the default
   * case when no profile is set. all
   * other profiles should return false
   */
  public boolean shouldActivate() {
    return false;
  }

  public static void setProfileIfNeeded() {
    if (DEV.shouldActivate()) {
      System.setProperty(DEV.getKey(),
          DEV.getValue());
    }
  }

  public boolean isActive() {
    String profile = getProfile();
    return notNull(profile) && getValue().equals(profile);
  }

  public String getKey() {
    return "spring.profiles.active";
  }

  protected boolean isNull(Object obj) {
    return obj == null;
  }

  private boolean notNull(Object obj) {
    return obj != null;
  }

  protected String getProfile() {
    return System.getProperty(getKey());
  }
}
