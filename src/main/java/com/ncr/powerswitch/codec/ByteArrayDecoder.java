package com.ncr.powerswitch.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ncr.powerswitch.utils.FormatUtil;

public class ByteArrayDecoder extends CumulativeProtocolDecoder {
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		System.out.println("doDecode============= ");

		/*
		 * if (in.remaining() >= 0) { byte[] b = new byte[in.limit()]; in.get(b);
		 * out.write(b); return true; } else{ return false; }
		 */

		if (in.remaining() >= 8) {
			byte[] sizeBytes = new byte[8];
			in.mark();
			in.get(sizeBytes);
			int len = FormatUtil.bytes2int(sizeBytes);
			in.reset();
			if (in.remaining() < len) {
				return false;
			} else {
				byte[] dataBytes = new byte[len];
				in.get(dataBytes, 0, len);
				out.write(dataBytes);

				if (in.remaining() >= 0) {
					return true;
				}
			}
		}
		return false;
	}
}
