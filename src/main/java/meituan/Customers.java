package meituan;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Customers
 * @Author Mr.罗
 * @Package meituan
 * @Date 2023/6/12 15:32
 * @description: 客户消费者
 */
public class Customers {
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
                //1.重新再次获取商家列表
                try {
                    getShopList();
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });
    }

    //获取商家列表（获取子节点列表）
    private void getShopList() throws Exception {
        //1.获取服务器的子节点信息，并且对父节点进行监听
        List<String> shops = zKClient.getChildren("/meituan", true);
        //2.声明存储服务器信息的集合
        ArrayList<String> shopList = new ArrayList<>();

        for (String shop : shops) {
            byte[] bytes = zKClient.getData("/meituan/" + shop, false, new Stat());
            shopList.add(new String(bytes));
        }
        System.out.println("目前正在营业的商家：" + shopList);
    }

    public static void main(String[] args) throws Exception {
        Customers client = new Customers();
        //1.获得消费者的连接（用户打开美团APP）
        client.connect();
        //2.获取美团下的所有子节点列表（获取商家列表）
        client.getShopList();
        //3.业务逻辑处理（对比商家下单点餐）
        client.business();
    }


    private void business() throws Exception {
        System.out.println("用户正在浏览商家....");
        System.in.read();
    }
}
