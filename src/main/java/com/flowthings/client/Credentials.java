package com.flowthings.client;

import java.util.List;

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
