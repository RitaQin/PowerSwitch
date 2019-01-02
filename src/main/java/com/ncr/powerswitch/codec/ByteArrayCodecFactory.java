package com.ncr.powerswitch.codec;

import org.apache.mina.core.session.IoSession; 
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder; 
import org.apache.mina.filter.codec.ProtocolEncoder; 
/** * @author BruceYang * */ 

public class ByteArrayCodecFactory implements ProtocolCodecFactory {
	private ByteArrayDecoder decoder;
	private ByteArrayEncoder encoder;
	
	public ByteArrayCodecFactory() {
		encoder = new ByteArrayEncoder();
		decoder = new ByteArrayDecoder();
	} 
	
	public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {  
        return encoder;  
    }  
  
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {  
        return decoder;  
    }  
}
