## Universal-image-loader Mason 修复版

------
1、基于版本universal-image-loader-1.9.2

2、修复了下面两个bug
###1、Could not validate certificate signature

 org.bouncycastle.jce.exception.ExtCertPathValidatorException: Could not validate certificate signature.
 javax.net.ssl.SSLHandshakeException: org.bouncycastle.jce.exception.ExtCertPathValidatorException: Could not validate certificate signature.
 	at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:477)
 	at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:328)
 	at org.apache.harmony.luni.internal.net.www.protocol.http.HttpConnection.setupSecureSocket(HttpConnection.java:185)
 	at org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl$HttpsEngine.makeSslConnection(HttpsURLConnectionImpl.java:433)
 	at org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl$HttpsEngine.makeConnection(HttpsURLConnectionImpl.java:378)
 	at org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl.retrieveResponse(HttpURLConnectionImpl.java:1018)
 	at org.apache.harmony.luni.internal.net.www.protocol.http.HttpURLConnectionImpl.getResponseCode(HttpURLConnectionImpl.java:726)
 	at org.apache.harmony.luni.internal.net.www.protocol.https.HttpsURLConnectionImpl.getResponseCode(HttpsURLConnectionImpl.java:121)
 	at com.nostra13.universalimageloader.core.download.BaseImageDownloader.getStreamFromNetwork(BaseImageDownloader.java:105)
 	at com.nostra13.universalimageloader.core.download.BaseImageDownloader.getStream(BaseImageDownloader.java:76)
 	at com.nostra13.universalimageloader.core.decode.BaseImageDecoder.getImageStream(BaseImageDecoder.java:83)
 	at com.nostra13.universalimageloader.core.decode.BaseImageDecoder.decode(BaseImageDecoder.java:69)
 	at com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.decodeImage(LoadAndDisplayImageTask.java:307)
 	at com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.tryLoadBitmap(LoadAndDisplayImageTask.java:262)
 	at com.nostra13.universalimageloader.core.LoadAndDisplayImageTask.run(LoadAndDisplayImageTask.java:128)
 	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1088)
 	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:581)
 	at java.lang.Thread.run(Thread.java:1027)
 Caused by: java.security.cert.CertificateException: org.bouncycastle.jce.exception.ExtCertPathValidatorException: Could not validate certificate signature.
 	at org.apache.harmony.xnet.provider.jsse.TrustManagerImpl.checkServerTrusted(TrustManagerImpl.java:161)
 	at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.verifyCertificateChain(OpenSSLSocketImpl.java:664)
 	at org.apache.harmony.xnet.provider.jsse.NativeCrypto.SSL_do_handshake(Native Method)
 	at org.apache.harmony.xnet.provider.jsse.OpenSSLSocketImpl.startHandshake(OpenSSLSocketImpl.java:474)
 	... 17 more
 Caused by: org.bouncycastle.jce.exception.ExtCertPathValidatorException: Could not validate certificate signature.
 	at org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertA(RFC3280CertPathUtilities.java:1504)
 	at org.bouncycastle.jce.provider.PKIXCertPathValidatorSpi.engineValidate(PKIXCertPathValidatorSpi.java:293)
 	at java.security.cert.CertPathValidator.validate(CertPathValidator.java:197)
 	at org.apache.harmony.xnet.provider.jsse.TrustManagerImpl.checkServerTrusted(TrustManagerImpl.java:156)
 	... 20 more
 Caused by: java.security.SignatureException: Signature was not verified
 	at org.apache.harmony.security.provider.cert.X509CertImpl.fastVerify(X509CertImpl.java:595)
 	at org.apache.harmony.security.provider.cert.X509CertImpl.verify(X509CertImpl.java:508)
 	at org.bouncycastle.jce.provider.CertPathValidatorUtilities.verifyX509Certificate(CertPathValidatorUtilities.java:1551)
 	at org.bouncycastle.jce.provider.RFC3280CertPathUtilities.processCertA(RFC3280CertPathUtilities.java:1496)
 	... 23 more

##2、ImageLoader Image can't be decoded


------
##欢迎批评指正

博客：http://blog.csdn.net/masonblog
邮箱：MasonLiuChn@gmail.com
