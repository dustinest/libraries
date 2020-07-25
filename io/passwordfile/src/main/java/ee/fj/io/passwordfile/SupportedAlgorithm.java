package ee.fj.io.passwordfile;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.SecretKeyFactory;

public enum SupportedAlgorithm {
	PBEWithHmacSHA512AndAES_256, // SHA512
	PBEWithHmacSHA512AndAES_128, // SHA512
	PBEWithHmacSHA384AndAES_256, // SHA384
	PBEWithHmacSHA384AndAES_128, // SHA384
	PBEWithHmacSHA256AndAES_256, // SHA256
	PBEWithHmacSHA256AndAES_128, // SHA256
	PBEWithHmacSHA224AndAES_256, // SHA224
	PBEWithHmacSHA224AndAES_128, // SHA224
	// weaks:
	PBEWithHmacSHA1AndAES_128,
	PBEWithHmacSHA1AndAES_256, // SHA1 weak
	PBEWithSHA1AndRC2_40,
	PBEWithSHA1AndRC4_40,
	PBEWithSHA1AndRC2_128,
	PBEWithSHA1AndPC4_128,
	PBEWithSHA1AndDESede, // SHA1 weak
	PBEWithMD5AndTripleDES, // insecure
	PBEWithMD5AndDES; // insecure

	public static final SupportedAlgorithm[] SUPPORTED_ALGORITHMS;
	public static final SupportedAlgorithm BEST_ALGORITHM;
	static {
		List<SupportedAlgorithm> supported = new ArrayList<>();
		SupportedAlgorithm best = null;
		for (SupportedAlgorithm a : new SupportedAlgorithm[] {
				PBEWithHmacSHA512AndAES_256,
				PBEWithHmacSHA512AndAES_128,
				PBEWithHmacSHA384AndAES_256,
				PBEWithHmacSHA384AndAES_128,
				PBEWithHmacSHA256AndAES_256,
				PBEWithHmacSHA256AndAES_128,
				PBEWithHmacSHA224AndAES_256,
				PBEWithHmacSHA224AndAES_128,
				PBEWithHmacSHA1AndAES_128,
				PBEWithHmacSHA1AndAES_256,
				PBEWithSHA1AndRC2_40,
				PBEWithSHA1AndRC4_40,
				PBEWithSHA1AndRC2_128,
				PBEWithSHA1AndPC4_128,
				PBEWithSHA1AndDESede,
				PBEWithMD5AndTripleDES,
				PBEWithMD5AndDES
		}) {
			if (a.supported) {
				if (best == null) {
					best = a;
				}
				supported.add(a);
			}
		}
		BEST_ALGORITHM = best;
		SUPPORTED_ALGORITHMS = supported.toArray(new SupportedAlgorithm[0]);
	}
	public final boolean supported;
	SupportedAlgorithm() {
		boolean _supported = true;
		try {
			SecretKeyFactory.getInstance(name());
		} catch (NoSuchAlgorithmException e) {
			_supported = false;
		}
		this.supported = _supported;
	}
}
