package com.miner.block;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AddBlock {

	public BigInteger getTarget(String extra_diff, String difficulty) {
		if(difficulty == null) return generateTarget(1);
		return generateTarget(Long.parseLong(difficulty));
		/*if(extra_diff != null){
			return generateTarget(Long.parseLong(extra_diff, 16));
		} else {
			return generateTarget(Long.parseLong(difficulty));
		}*/
	}

	private BigInteger generateTarget(long compact) {
		int size = ((int) (compact >> 24)) & 0xFF;
	    byte[] bytes = new byte[4 + size];
	    bytes[3] = (byte) size;
	    if (size >= 1) bytes[4] = (byte) ((compact >> 16) & 0xFF);
	    if (size >= 2) bytes[5] = (byte) ((compact >> 8) & 0xFF);
	    if (size >= 3) bytes[6] = (byte) (compact & 0xFF);
	    return decodeMPI(bytes, true);
	}
	
    private BigInteger decodeMPI(byte[] mpi, boolean hasLength) {
        byte[] buf;
        if (hasLength) {
            int length = (int) readUint32BE(mpi, 0);
            buf = new byte[length];
            System.arraycopy(mpi, 4, buf, 0, length);
        } else
            buf = mpi;
        if (buf.length == 0)
            return BigInteger.ZERO;
        boolean isNegative = (buf[0] & 0x80) == 0x80;
        if (isNegative)
            buf[0] &= 0x7f;
        BigInteger result = new BigInteger(buf);
        return isNegative ? result.negate() : result;
    }
    
    private long readUint32BE(byte[] bytes, int offset) {
        return ((bytes[offset] & 0xffl) << 24) |
                ((bytes[offset + 1] & 0xffl) << 16) |
                ((bytes[offset + 2] & 0xffl) << 8) |
                (bytes[offset + 3] & 0xffl);
    }

    public BigInteger getHash(String version, String prevhash, String merkle, String ntime, long nonce) {
    	StringBuilder sb = new StringBuilder();
    	sb.append(toByte(Long.parseLong(version)));
    	sb.append(reverseByte(prevhash));
    	sb.append(reverseByte(merkle));
    	sb.append(toByte(Long.parseLong(ntime, 16)));
    	sb.append(toByte(nonce));
    	
    	byte[] hash = calculateHash(sb.toString());
        return toBigInteger(hash);
    }
    
    private String reverseByte(String s) {
    	byte[] bytes = s.getBytes();
    	byte[] buf = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
            buf[i] = bytes[bytes.length - 1 - i];
        return new String(buf);
	}

	private String toByte(long val) {
    	StringBuilder sb = new StringBuilder();
    	sb.append((int) (0xFF & val));
    	sb.append((int) (0xFF & (val >> 8)));
    	sb.append((int) (0xFF & (val >> 16)));
    	sb.append((int) (0xFF & (val >> 24)));
    	return sb.toString();
	}

	private byte[] calculateHash(String s) {
		String hashtwice = sha256(sha256(s));
		String wrap = reverseByte(hashtwice);
		return wrap.getBytes();
	}

	private BigInteger toBigInteger(byte[] bytes) {
        return new BigInteger(1, bytes);
    }
	
	private String sha256(String msg) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
        md.update(msg.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
}
