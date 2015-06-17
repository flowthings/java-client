package com.flowthings.client;

import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Credentials {
  public String account;
  public String token;

  public Credentials(String account, String token) {
    this.account = account;
    this.token = token;
  }

  public Credentials() {
  }

  @Override
  public String toString() {
    return "Credentials [account=" + account + ", token=" + token + "]";
  }

  /**
   * Will attempt to get credentials from the IBM Bluemix environment. If this
   * fails, the supplied credentials will be used
   * 
   * @param credentials
   * @return
   */
  public static Credentials fromBluemixOrDefault(Credentials defaultCredentials) {
    String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
    System.out.println(VCAP_SERVICES);
    if (VCAP_SERVICES != null) {
      try {
        VcapServices services = Serializer.fromJson(VCAP_SERVICES, new TypeToken<VcapServices>() {
        });
        if (services.flowthings != null && !services.flowthings.isEmpty()) {
          return services.flowthings.get(0).credentials;
        }
      } catch (Exception e) {
        System.out.println("Couldn't deserialize bluemix VCAP_SERVICES");
      }
    }
    System.out.println("Using default credentials : " + defaultCredentials);
    return defaultCredentials;
  }

  public static void main(String[] args) {
    System.out.println("Got creds: " + Credentials.fromBluemixOrDefault(null));
  }
}

class VcapServices {
  public List<Service> flowthings;
}

class Service {
  public String boop;
  public String label;
  public String plan;
  public Credentials credentials;
}
