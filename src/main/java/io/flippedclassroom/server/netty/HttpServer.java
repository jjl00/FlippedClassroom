package io.flippedclassroom.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 描述:
 * 服务器引导
 *
 * @author HASEE
 * @create 2018-10-14 13:59
 */
@Component
@Slf4j
public class HttpServer {
    @Autowired
    HttpServerHandler httpServerHandler;
    @Value("${netty.port}")
    int port;
    public void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel serverChannel) throws Exception {
                            serverChannel.pipeline()
                                    .addLast(new HttpRequestDecoder())
                                    .addLast(new HttpResponseEncoder())
                                    //  ChunkedWriteHandler的主要作用是支持异步发送大的码流,
                                    // 但不占用过多的内存,防止JAVA内存溢出
                                    .addLast("http-chunked",new ChunkedWriteHandler())
//用于出站压缩                                    .addLast("compressor", new HttpContentCompressor())
                                    .addLast(new HttpServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("Server start.....");
            future.channel().closeFuture().sync();  //等待通道关闭
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}
