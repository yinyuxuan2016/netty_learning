package handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Copyright: All Rights Reserved</p>
 * @Description
 */
public class MyHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,Object msg) throws Exception {
        String curTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        System.out.println(curTime);
        //将字节流打印到控制台
        //将接受的消息写给发送者，而不冲刷出站消息
        channelHandlerContext.writeAndFlush(curTime );
        channelHandlerContext.close();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        //……
        if(channel.isActive())ctx.close();
    }
}
