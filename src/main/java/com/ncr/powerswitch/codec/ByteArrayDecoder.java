package com.ncr.powerswitch.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class ByteArrayDecoder  extends CumulativeProtocolDecoder {  
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {   
		System.out.println("doDecode============= ");
		if (in.remaining() >= 0) {   
			byte[] b = new byte[in.limit()];   
			in.get(b);  
			out.write(b);
			return true;  
		}else{
			return false;  
		}		
		
		/*if(in.remaining() > 4){
			//前4字节是包头
			//标记当前position的快照标记mark,以便后继的reset操作能恢复position位置in.mark();
			byte[] l = new byte[4];
			in.get(l);
			in.get
			//包体数据长度
			int len = MyTools.bytes2int(l);
			//将byte转成int 
			//注意上面的get操作会导致下面的remaining()值发生变化
			if(in.remaining() < len){
				//如果消息内容不够,则重置恢复position位置到操作前,进入下一轮, 接收新数据,以拼凑成完整数据
				in.reset(); 
				return false;
				}
			else{
				//消息内容足够
				in.reset();
				//重置恢复position位置到操作前
				int sumlen = 4+len;
				//总长 = 包头+包体
				byte[] packArr = new byte[sumlen];
				in.get(packArr, 0 , sumlen);
				IoBuffer buffer = IoBuffer.allocate(sumlen);
				buffer.put(packArr);
				buffer.flip();
				out.write(buffer);
				buffer.free();
				if(in.remaining() > 0){
					//如果读取一个完整包内容后还粘了包,就让父类再调用一次,进行下一次解析
					return true;
					}
				}
			}
		return false;//处理成功,让父类进行接收下个包
		}*/
    }

}
