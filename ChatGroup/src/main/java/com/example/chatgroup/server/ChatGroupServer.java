package com.example.chatgroup.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatGroupServer {

    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;
    private static final int PORT = 6666;

    public ChatGroupServer(){
        try {
            // 打开选择器
            this.selector = Selector.open();
            // 打开服务器套接字通道
            this.serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.bind(new InetSocketAddress("127.0.0.1",PORT));
            // 设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 将通道注册到选择器上
            serverSocketChannel.register(selector,serverSocketChannel.validOps());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 监听客户端连接
     */
    public void listen(){
        while (true){
            try {
                // 选择器开始监听
                if(selector.select(2000) > 0){
                    // 获取选择器上所有的监听事件
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        // 如果是获取连接事件
                        if(selectionKey.isAcceptable()){
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        }
                        // 如果是读取事件
                        if(selectionKey.isReadable()){
                            readData(selectionKey);
                        }
                        // 删除当前的selectionKey，防止重复操作
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待中...");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 读取客户端消息
     * @param selectionKey 选择器上的监听事件
     */
    public void readData(SelectionKey selectionKey){
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            if(socketChannel.read(byteBuffer) > 0){
                String msg = new String(byteBuffer.array());
                System.out.println("from 客户端：" + msg);
                // 向其他客户端转发消息
                notifyOtherClients(msg,socketChannel);
            }else{
                // 客户端断开时，服务端会收到很多读事件连接，但是读不到数据，所以要关闭通道
                throw new IOException();
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress()+"客户端已经下线");
                // 取消注册
                selectionKey.cancel();
                // 关闭通道
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端
     * @param msg 消息
     * @param self 发送方自己
     */
    public void notifyOtherClients(String msg,SocketChannel self){
        System.out.println("服务器转发消息中...");
        selector.keys().forEach(selectionKey -> {
            Channel targetChannel = selectionKey.channel();
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                try {
                    ((SocketChannel) targetChannel).write(ByteBuffer.wrap(msg.getBytes()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void main(String[] args) {
        ChatGroupServer chatGroupServer = new ChatGroupServer();
        chatGroupServer.listen();
    }
}
