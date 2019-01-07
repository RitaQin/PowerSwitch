package com.ncr.powerswitch.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class HsmByteArrayDecoder extends CumulativeProtocolDecoder {

	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		
		System.out.println("doDecode============= ");
		
		if (in.remaining() > 0 ) {
			byte[] b = new byte[in.limit()];
			in.get(b);
			out.write(b);
			return true;
		} else {
			return false;
		}
	}

}
