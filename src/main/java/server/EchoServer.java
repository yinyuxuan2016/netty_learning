package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * The type Echo server.
 *
 * @author :<a href="mailto:dongbiao.zheng@renren-inc.com">zhengdb</a>
 * @date :2017-11-17 11:24:20
 * @Copyright: All Rights Reserved</p>
 * @Description
 */
public class EchoServer {
    private final int port;

    /**
     * 使用构造器初始化端口
     * @param port
     */
    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * 服务端启动方法
     * @throws Exception
     */
    public void start() throws Exception{
        final EchoServerHandle echoServerHandle=new EchoServerHandle();
        //创建EventLoopGroup
        EventLoopGroup eventLoopGroup=new NioEventLoopGroup();
        try{
            //创建启动器
            ServerBootstrap serverBootstrap=new ServerBootstrap();
            //指定所使用的NIO传输channel
            serverBootstrap.group(eventLoopGroup).channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port)).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //添加一个Echohandler到ChannelPipe中，因为该Handle被标记为共享的，所以使用的时候都是同一个实例
                    socketChannel.pipeline().addLast(echoServerHandle);
                }
            });
            //异步绑定服务器，调用sync（）方法阻塞知道绑定完成
            ChannelFuture f=serverBootstrap.bind().sync();
            //获取channel的closeFuture,并且阻塞当前线程知道完成
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //关闭eventLoopGroup,释放资源
            eventLoopGroup.shutdownGracefully().sync();
        }
    }

    /**
     * 启动服务端
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
       /* if(args.length!=1){
            System.err.println("设置端口值");
            return;
        }*/
        int port=Integer.parseInt("8080");
        new EchoServer(port).start();
    }
}
