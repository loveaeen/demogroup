package com.example.chatgroup.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ChatGroupClient {

    private final Selector selector;
    private final SocketChannel socketChannel;
    private static final int PORT = 6666;
    private final String userName;

    public ChatGroupClient(){
        try {
            this.selector = Selector.open();
            this.socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",PORT));
            // 设置为非阻塞
            this.socketChannel.configureBlocking(false);
            // 将通道注册到选择器上
            socketChannel.register(selector,socketChannel.validOps());
            // 获取用户名
            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName + " is ok~");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 向服务器发送消息
     * @param msg 消息
     */
    public void sendMsg(String msg){
        msg = userName + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器读取信息
     */
    public void readMsg(){
        try {
            if(selector.select() > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while(iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ChatGroupClient chatGroupClient = new ChatGroupClient();
        new Thread(() -> {
            while (true){
                chatGroupClient.readMsg();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String in = scanner.nextLine();
            chatGroupClient.sendMsg(in);
        }
    }
}
