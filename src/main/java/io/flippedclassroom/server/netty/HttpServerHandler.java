package io.flippedclassroom.server.netty;


import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * 描述:
 * 处理逻辑
 *
 * @author HASEE
 * @create 2018-10-14 14:04
 */
@ChannelHandler.Sharable
@Component
public class HttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {
    ThreadLocal<String> filePath=new ThreadLocal<String>();
    ThreadLocal<String> fileName=new ThreadLocal<String>();

    private HttpRequest request;
    private boolean readingChunks=true;
    private final StringBuilder responseContent = new StringBuilder();

    private static HttpDataFactory factory =new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    private HttpPostRequestDecoder decoder;

    //配置文件上传参数
    static {
        DiskFileUpload.deleteOnExitTemporaryFile = true;  //退出时删除临时文件
        DiskFileUpload.baseDirectory = null;              //系统临时目录
        DiskAttribute.deleteOnExitTemporaryFile = true;
        DiskAttribute.baseDirectory = null;
    }



    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HttpObject httpObject) throws Exception {
        if(httpObject instanceof HttpRequest){
            this.request=(HttpRequest)httpObject;
            decoder = new HttpPostRequestDecoder(factory, request);
            if(request.method()==HttpMethod.GET){
              //  resolveUri(request);
                doGet(channelHandlerContext,request);
            }
        }else if(httpObject instanceof HttpContent){
            HttpContent httpContent=(HttpContent)httpObject;
            if(httpContent.content().readableBytes()==0){
                return;
            }
            doPost(channelHandlerContext,httpContent);
        }
    }





    /**
     * @Author HASEE
     * @Description 处理get请求,文件下载
     * @Date 14:37 2018/10/14
     * @Param [httpRequest]
     * @return void
     **/
//    private void doGet(ChannelHandlerContext channelHandlerContext, HttpRequest httpRequest)throws Exception{
//        String uri=URLDecoder.decode(httpRequest.uri(), "UTF-8");
//        String realPath=uri.substring(uri.indexOf("=")+1);
//        File file = new File(realPath);
//        if (!file.exists() || !file.isFile()) {
//            doResponse(channelHandlerContext, HttpResponseStatus.NOT_FOUND,"请求的文件不存在");
//            return;
//        }
//        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
//        FileChannel channel = new FileInputStream(file).getChannel();
//        ByteBuffer buffer = ByteBuffer.allocateDirect(1024*1024);
//        while (channel.read(buffer) != -1) {
//            buffer.flip();                  //开启读模式:pos=0，limit=(上次pos位置,即写的最后位置)
//            response.content().writeBytes(buffer);
//            buffer.clear();                 //清空,将pos=0,limit=capacity
//        }
//        channel.close();
//        response.headers().set("Content-Disposition", "attachment; filename=\"" +realPath.substring(realPath.lastIndexOf("/")+1));
//        if(realPath.endsWith(".jpg") || realPath.endsWith(".png") || realPath.endsWith(".jpeg")){
//            response.headers().set(CONTENT_TYPE, "image/png; charset=UTF-8");
//        }else {
//            response.headers().set(CONTENT_TYPE, "application/octet-stream;charset=UTF-8");
//        }
//        long fileLength = response.content().readableBytes();
//        HttpUtil.setContentLength(response, fileLength);
//        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
//    }
    private void doGet(ChannelHandlerContext channelHandlerContext,HttpRequest httpRequest)throws Exception {
        String uri=URLDecoder.decode(httpRequest.uri(), "UTF-8");
        String realPath=uri.substring(uri.indexOf("=")+1);
        File file = new File(realPath);
        if (!file.exists() || !file.isFile()) {
            doResponse(channelHandlerContext, HttpResponseStatus.NOT_FOUND,"请求的文件不存在");
            return;
        }
        RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
        long length = randomAccessFile.length();
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.OK);
        HttpUtil.setContentLength(response, length);
        if (HttpUtil.isKeepAlive(request)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        response.headers().set("Content-Disposition", "attachment; filename=\"" +realPath.substring(realPath.lastIndexOf("/")+1));
        if(realPath.endsWith(".jpg") || realPath.endsWith(".png") || realPath.endsWith(".jpeg")){
            response.headers().set(CONTENT_TYPE, "image/png; charset=UTF-8");
        }else {
            response.headers().set(CONTENT_TYPE, "application/octet-stream;charset=UTF-8");
        }
        channelHandlerContext.write(response);
        channelHandlerContext.write(new HttpChunkedInput(new ChunkedFile(randomAccessFile, 0, length, 8192)), channelHandlerContext.newProgressivePromise())
                .addListener(new ChannelProgressiveFutureListener() {

                    public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                        if (total < 0) {
                            System.err.println("Transfer progress: " + progress);
                        } else {
                            System.err.println("Transfer progress: " + progress + " / " + total);
                        }
                    }

                    public void operationComplete(ChannelProgressiveFuture future) throws Exception {
                        System.out.println("Transfer complete.");
                    }

                });
        ChannelFuture lastContentFuture = channelHandlerContext.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        if (!HttpUtil.isKeepAlive(request)) {
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }
    /**
     * @Author HASEE
     * @Description 处理post请求,完成文件上传
     * @Date 14:38 2018/10/14
     * @Param [httpRequest]
     * @return void
     **/
    private void doPost(ChannelHandlerContext channelHandlerContext, HttpContent httpContent){

        if(readingChunks) {
            try{
                decoder.offer(httpContent);
            }catch(HttpPostRequestDecoder.ErrorDataDecoderException e1){
                reset();
                doResponse(channelHandlerContext,HttpResponseStatus.BAD_REQUEST,"错误2");
                return;
            }
            while (decoder.hasNext()) {
                InterfaceHttpData data = decoder.next();
                if (data != null) {
                    try {
                        writeHttpData(data);
                    } finally {
                        data.release();
                    }
                }

            }
            if (httpContent instanceof LastHttpContent) {
                doResponse(channelHandlerContext,HttpResponseStatus.OK,"成功1");
                readingChunks = false;
                reset();
            }
        }else {
            doResponse(channelHandlerContext,HttpResponseStatus.BAD_REQUEST,"错误3");
            return;
        }

    }

    private void doResponse(ChannelHandlerContext channelHandlerContext,
                            HttpResponseStatus status, String msg) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1,
                status, Unpooled.copiedBuffer("响应信息: " +"\r\n"+ msg
                + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /************************ Post请求部分 ***********************************/

    /**
     * @Author HASEE
     * @Description 解析post请求的参数
     * @Date 14:50 2018/10/14
     * @Param [httpRequest]
     * @return void
     **/
    private void resolveParam(InterfaceHttpData data) {
        String content = data.toString();
        String temp = content.substring(0, content.indexOf("\n"));
        String[] params=temp.split(";");
        filePath.set(params[1].substring(params[1].indexOf("=")+2,params[1].lastIndexOf("\"")));
        fileName.set(temp.substring(temp.lastIndexOf("=") + 2, temp.lastIndexOf("\"")));
    }


    private void writeHttpData(InterfaceHttpData data) {
        // Attribute就是form表单里带的各种 name= 的属性
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
        } else if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.InternalAttribute){
        }else{
            resolveParam(data);
            FileUpload fileUpload = (FileUpload) data;
            if (fileUpload.isCompleted()) {
                File dir = new File(filePath.get());
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File dest = new File(dir,fileName.get());
                try {
                    fileUpload.renameTo(dest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void reset() {
        request = null;
        decoder.destroy();
        decoder = null;
    }

    /*************************Get请求部分***********************************************/


    /**
     * @Author HASEE
     * @Description 解析uri
     * @Date 14:29 2018/10/14
     * @Param [httpRequest]
     * @return void
     **/
    private void resolveUri(HttpRequest httpRequest) throws Exception{
        String uri=URLDecoder.decode(httpRequest.uri(), "UTF-8");
        String uri_1=uri.substring(uri.indexOf("?")+1);    //截取？后面一段uri
        int index=uri_1.indexOf("&");
        String file_path_temp=uri_1.substring(0,index);
        String file_name_temp=uri_1.substring(index+1);
        //第一个参数为文件路径，第二个参数为文件名字
        filePath.set(file_path_temp.substring(file_path_temp.indexOf("=")+1));
        fileName.set(file_name_temp.substring(file_name_temp.indexOf("=")+1));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        cause.printStackTrace();
        ctx.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
    }
}
