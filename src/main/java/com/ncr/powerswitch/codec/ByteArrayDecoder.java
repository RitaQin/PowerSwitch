package com.ncr.powerswitch.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ncr.powerswitch.utils.FormatUtil;

public class ByteArrayDecoder extends CumulativeProtocolDecoder {
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		//System.out.println("doDecode============= ");

		/*
		 * if (in.remaining() >= 0) { byte[] b = new byte[in.limit()]; in.get(b);
		 * out.write(b); return true; } else{ return false; }
		 */
		
		if (in.remaining() >= 8) {
			byte[] sizeBytes = new byte[8];
			in.mark();
			in.get(sizeBytes);
			in.reset();
			
			int len = FormatUtil.bytes2int(sizeBytes) + 8; //前8个字节不算在长度内
			
			if (len == 8) {
				out.write(null);
				System.out.println("Codec length: " + 0);
				return true;
			}		
			
			if (in.remaining() < len) {
				return false;
			} else {
				byte[] dataBytes = new byte[len];
				in.get(dataBytes, 0, len);
				out.write(dataBytes);
				if (in.remaining() >= 0) {
					System.out.println("Codec length: " + len);
					return true;
				}
			}
		}
		return false;
	}
}
