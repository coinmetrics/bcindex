package com.frobro.bcindex.web.model.api;

public class SecurePublicRequest extends PublicRequest {
  public String apiKey;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;

    SecurePublicRequest that = (SecurePublicRequest) o;

    return apiKey != null ? apiKey.equals(that.apiKey) : that.apiKey == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (apiKey != null ? apiKey.hashCode() : 0);
    return result;
  }
}
