package com.ncr.powerswitch.codec;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class HsmByteArrayCodecFactory {
	private HsmByteArrayDecoder decoder;
	private ByteArrayEncoder encoder;
	
	public HsmByteArrayCodecFactory() {
		encoder = new ByteArrayEncoder();
		decoder = new HsmByteArrayDecoder();
	} 
	
	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {  
        return encoder;  
    }  
  
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {  
        return decoder;  
    }  
}
