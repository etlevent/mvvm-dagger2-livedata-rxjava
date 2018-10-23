package ext.arch.components.network;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by ROOT on 2017/11/2.
 */

public final class SSLUtils {
    private SSLUtils() {
        throw new AssertionError("no instance.");
    }

    public static OkHttpClient getUnSafeOkHttpClient(InputStream... certificates) {
        SSLSocketFactory sslSocketFactory = SSLUtils.getSSLSocketFactory(certificates, null, null);
        return new OkHttpClient.Builder()
                .sslSocketFactory(sslSocketFactory, new SSLUtils.UnSafeTrustManager())
                .hostnameVerifier((s, sslSession) -> true).build();
    }

    public static SSLSocketFactory getSSLSocketFactory(InputStream[] certificates, InputStream pkcsFile, String password) {
        try {
            TrustManager[] trustManagers = generateTrustManagers(certificates);
            KeyManager[] keyManagers = generateKeyManagers(pkcsFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManager trustManager;
            if (trustManagers != null) {
                trustManager = new X509TrustManagerImpl(getX509TrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrustManager[] generateTrustManagers(InputStream... certificateStreams) {
        if (certificateStreams == null || certificateStreams.length == 0) {
            return null;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509", "BC");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            for (int i = 0; i < certificateStreams.length; i++) {
                final String certificateAlias = i + "";
                final InputStream certificate = certificateStreams[i];
                if (certificate != null) {
                    keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                    certificate.close();
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();
        } catch (CertificateException | KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyManager[] generateKeyManagers(InputStream pkcsFile, String password) {
        if (pkcsFile == null || password == null) {
            return null;
        }
        try {
            KeyStore clientKeyStore = KeyStore.getInstance("PKCS12");
            clientKeyStore.load(pkcsFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }
    }

    private static X509TrustManager getX509TrustManager(TrustManager[] trustManagers) {
        if (trustManagers == null || trustManagers.length == 0) {
            return null;
        }
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private static class X509TrustManagerImpl implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private final X509TrustManager localTrustManager;

        public X509TrustManagerImpl(X509TrustManager localX509TrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            this.localTrustManager = localX509TrustManager;
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            this.defaultTrustManager = getX509TrustManager(trustManagerFactory.getTrustManagers());
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                if (this.defaultTrustManager != null) {
                    this.defaultTrustManager.checkServerTrusted(chain, authType);
                } else {
                    checkServerTrustedWithLocal(chain, authType);
                }
            } catch (CertificateException e) {
                checkServerTrustedWithLocal(chain, authType);
            }
        }

        private void checkServerTrustedWithLocal(X509Certificate[] chain, String authType) throws CertificateException {
            if (this.localTrustManager != null) {
                this.localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class UnSafeTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
