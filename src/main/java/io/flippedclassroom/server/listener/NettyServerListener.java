package io.flippedclassroom.server.listener;

import io.flippedclassroom.server.netty.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 描述:
 *
 * @author HASEE
 * @create 2018-10-18 9:41
 */


@WebListener
public class NettyServerListener implements ServletContextListener {

    @Autowired
    private HttpServer httpServer;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Thread thread = new Thread(new NettyServerThread());
        thread.start();
    }

    @Override
    public void contextDestroyed(
            ServletContextEvent sce) {

    }

    private class NettyServerThread implements Runnable {
        @Override
        public void run() {
            httpServer.start();
        }
    }
}

