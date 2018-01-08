package server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端的处理Handle
 * @author :<a href="mailto:dongbiao.zheng@renren-inc.com">zhengdb</a>
 * @date :2017-10-18 15:02:52
 * @Copyright: All Rights Reserved</p>
 * @Description
 */

/***
 * 该注解导致该Handle可以被多个channel可以共享
 */
@ChannelHandler.Sharable
public class EchoServerHandle extends ChannelInboundHandlerAdapter {
    /**
     * 接受客户端的信息
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in=(ByteBuf)msg;
        //将字节流打印到控制台
        System.out.println("Server received:"+in.toString(CharsetUtil.UTF_8));
        //将接受的消息写给发送者，而不冲刷出站消息
        ctx.write(in);
    }

    /**
     * 响应客户端信息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未结束的消息冲刷到远程节点，并且关闭该通道
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 异常信息的处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常信息
        cause.printStackTrace();
        //关闭channel
        ctx.close();
    }
}
