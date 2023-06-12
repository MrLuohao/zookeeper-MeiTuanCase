package meituan;

import org.apache.zookeeper.*;

/**
 * @Title: ShopServer
 * @Author Mr.罗
 * @Package meituan
 * @Date 2023/6/12 15:11
 * @description: 商家服务类
 */
public class ShopServer {
    //定义连接信息
    private String connectString = "192.168.247.128:2181,192.168.247.129:2181,192.168.247.130:2181";
    private int sessionTimeout = 60 * 1000;
    //创建zookeeper对象，创建一个美团对象
    private ZooKeeper zKClient;

    //创建客户端，连接到zookeeper
    public void connect() throws Exception {
        zKClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
            }
        });
    }

    //注册到zookeeper
    public void register(String shopname) throws Exception {
        /*一定要创建EPHEMERAL_SEQUENTIAL  临时有序的节点（创建节点意味着营业）
        因为:1.可以自动编号2.断开时节点自动删除，删除就意味着打烊了
         */
        String s = zKClient.create("/meituan/shop", shopname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("【" + shopname + "】" + "开始营业了" + s);
    }

    //定义商家服务的方法（做买卖）
    public void business(String shopname) throws Exception {
        System.out.println("【" + shopname + "】" + "火爆营业中!");
        System.in.read();
    }

    public static void main(String[] args) throws Exception {
        //1.开一个饭店
        ShopServer shop = new ShopServer();

        //2.连接zookeeper集群（和美团取得连接）
        shop.connect();

        //3.将服务节点注册到zookeeper（入驻美团）
        shop.register(args[0]);

        //4.业务逻辑处理（做生意）
        shop.business(args[0]);
    }
}
