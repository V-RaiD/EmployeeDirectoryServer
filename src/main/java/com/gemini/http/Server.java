package com.gemini.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gemini.dal.DbHelper;

@Component
public class Server {

	private static Logger l = LoggerFactory.getLogger(Server.class);
	private @Value("${http.port}") String port;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private ChannelFuture f;


	
	public void initialize() throws Exception{
		bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new HttpInitializer())
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            l.debug("Starting Server @ localhost:{}", port);
            l.debug("Db connection:{}", DbHelper.getInstance());
            f = b.bind(Integer.parseInt(port)).sync();

            f.channel().closeFuture().sync();
        } catch(Exception e){
        	l.error("Exception occured in initializing the server {}", new Object[]{e.getStackTrace()});
        } finally {
        	shutDown();
        }
	}
	
	public DbHelper getDbHelper(){
		return DbHelper.getInstance();
	}
	
	public void shutDown() {
		int exitCode = 0;
		try{
			l.debug("Shutting down gracefully");
			workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
		} catch(Exception e){
			l.error("Error in shutting down the server {}", new Object[]{e.getStackTrace()});
			exitCode = 1;
		} finally {
			System.exit(exitCode);
		}
	}
}
