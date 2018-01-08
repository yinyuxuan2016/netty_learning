package server;

import handle.MyHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * @Copyright: All Rights Reserved</p>
 * @Description
 */
public class NettyPushMessage {
    public void runner(){
        EventLoopGroup bossGroup=new NioEventLoopGroup();
        EventLoopGroup wokerGroup=new NioEventLoopGroup();
        ServerBootstrap b=new ServerBootstrap();

        try {
            b.group(bossGroup,wokerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ReadTimeoutHandler(10));  //如果10秒钟都没有新的数据读取，那么自动关闭
                    ch.pipeline().addLast(new WriteTimeoutHandler(1));  //写的1秒钟超时
                    ch.pipeline().addLast(new MyHandler());  //自己定义的handler
                }
            });
            //bind方法会创建一个serverchannel，并且会将当前的channel注册到eventloop上面，
            //会为其绑定本地端口，并对其进行初始化，为其的pipeline加一些默认的handler
            Channel ch=b.bind(8080).channel();
            ScheduledFuture f = ch.eventLoop().scheduleAtFixedRate(new Runnable() {
                public void run() {
                    System.out.println("run every 60 seconds");
                }
            },60,60, TimeUnit.SECONDS);
           ch.closeFuture().sync();  //相当于在这里阻塞，直到serverchannel关闭
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            wokerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new NettyPushMessage().runner();
    }
}
