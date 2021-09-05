/*
 * Copyright 2005 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package mbarix4j3.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Iterator;


/**
 * <p>Generate a listing of the most trusted certification authorities used
 * by your JVM</p>
 *
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: CAList.java 265 2006-06-20 05:30:09Z hohonuuli $
 */
public class CAList {

    /**
     * <p><!-- Method description --></p>
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            // Load the JDK's cacerts keystore file
            String filename = System.getProperty("java.home") +
                "/lib/security/cacerts".replace('/', File.separatorChar);
            FileInputStream is = new FileInputStream(filename);
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            String password = "changeit";
            keystore.load(is, password.toCharArray());

            // This class retrieves the most-trusted CAs from the keystore
            PKIXParameters params = new PKIXParameters(keystore);

            // Get the set of trust anchors, which contain the most-trusted CA certificates
            Iterator it = params.getTrustAnchors().iterator();
            for (; it.hasNext(); ) {
                TrustAnchor ta = (TrustAnchor) it.next();

                // Get certificate
                X509Certificate cert = ta.getTrustedCert();
                System.out.println(
                        "<issuer>" + cert.getIssuerDN() + "</issuer>\n");
            }
        } catch (CertificateException e) {}
        catch (KeyStoreException e) {}
        catch (NoSuchAlgorithmException e) {}
        catch (InvalidAlgorithmParameterException e) {}
        catch (IOException e) {}
    }
}
