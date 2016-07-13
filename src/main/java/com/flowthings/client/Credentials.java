package com.flowthings.client;

import java.util.List;

import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds credentials necessary to authenticate with flowthings.io any of our
 * APIs.
 * 
 * <p>
 * Credentials consist of an "account" and "token". The account is your
 * flowthings.io username. The token is either:
 * </p>
 * <ul>
 * <li>Your <a href="https://flowthings.io/docs/master-token">Master Token</a></li>
 * <li>A generated <a
 * href="https://flowthings.io/docs/token-object-overview">Token Object</a></li>
 * </ul>
 * 
 * <p>
 * This class also gives a convenience method for retrieving credentials from <a
 * href="https://console.ng.bluemix.net">IBM Bluemix</a>
 * </p>
 *
 * <pre>
 * {@code
 *  
 *  // Using vanilla credentials
 *  Credentials creds = new Credentials("matt", "SSOjDZ4VMHS2JcwT1sIpE8x91QfG");
 *  
 *  // Or get from Bluemix, or revert to provided if not available
 *  Credentials bluemixCreds = Credentials.fromBluemixOrDefault(creds);
 *  
 *  RestApi api = new RestApi(bluemixCreds);
 * }}
 * </pre>
 *
 * @author matt
 */
public class Credentials {
  protected static Logger logger = LoggerFactory.getLogger(Credentials.class);
  public String account;
  public String token;

  public Credentials(String account, String token) {
    this.account = account;
    this.token = token;
  }

  public Credentials() {
  }

  public boolean masterTokenSupplied(){
    return token != null && token.length() == 32;
  }

  @Override
  public String toString() {
    return "Credentials [account=" + account + ", token=" + token + "]";
  }

  /**
   * Will attempt to get credentials from the IBM Bluemix environment. If this
   * fails, the supplied credentials will be used
   * 
   * @param defaultCredentials
   *          - to be used if Bluemix credentials cannot be sourced
   * @return a credentials object
   */
  public static Credentials fromBluemixOrDefault(Credentials defaultCredentials) {
    String VCAP_SERVICES = System.getenv("VCAP_SERVICES");
    logger.info(VCAP_SERVICES);
    if (VCAP_SERVICES != null) {
      try {
        VcapServices services = Serializer.fromJson(VCAP_SERVICES, new TypeToken<VcapServices>() {
        });
        if (services.flowthings != null && !services.flowthings.isEmpty()) {
          return services.flowthings.get(0).credentials;
        }
      } catch (Exception e) {
        logger.info("Couldn't deserialize bluemix VCAP_SERVICES");
      }
    }
    logger.info("Using default credentials : " + defaultCredentials);
    return defaultCredentials;
  }

  public static void main(String[] args) {
    logger.info("Got creds: " + Credentials.fromBluemixOrDefault(null));
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
