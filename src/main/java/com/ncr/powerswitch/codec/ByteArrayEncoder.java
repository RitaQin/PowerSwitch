package com.ncr.powerswitch.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession; 
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;


public class ByteArrayEncoder implements ProtocolEncoder {  
	  
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {  
    	byte[] bytes = (byte[])message;
    	IoBuffer ioBuffer = IoBuffer.allocate(bytes.length);   
        ioBuffer.put(bytes, 0, bytes.length);   
        ioBuffer.flip();   
    	out.write(ioBuffer);
		out.flush();
    }  
  
    public void dispose(IoSession session) throws Exception {  
        // nothing to dispose  
    }  
}  