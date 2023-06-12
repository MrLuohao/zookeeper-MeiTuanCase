package Test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @Title: zookeeperTest
 * @Author Mr.罗
 * @Package Test
 * @Date 2023/6/12 11:02
 * @description: zookeeper测试
 */
public class zookeeperTest {
    //zookeeper集群的ip和端口号
    private String connectString = "192.168.247.128:2181,192.168.247.129:2181,192.168.247.130:2181";
    /*session超时的时间:时间不宜设置太小，因为zookeeper和加载集群环境会因为性能等原因而延迟略高，
    如果时间太少，还没有创建好客户端就开始操作节点，就会报错。*/
    private int sessionTimeout = 60 * 1000;

    //zookeeper客户端对象
    private ZooKeeper zKClient;

    @Before
    public void init() throws Exception {
        zKClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("监听并反馈，进行对应的业务处理。");
                System.out.println(watchedEvent.getType());//打印触发的监听事件
            }
        });
    }

    //创建节点
    @Test
    public void createNode() throws Exception {
        /*
         * path:以/开头，存入的数据不能为String类型，会产生乱码，要求byte类型的数组
         * ACL:表示权限  OPEN_ACL_UNSAFE:表示允许任意操作
         * CreateMode:创建的节点的模式，是持久型节点还是临时型的节点
         * */
        String str = zKClient.create("/JD", "TP".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("已创建节点：" + str);
    }

    //获取节点上的值
    @Test
    public void getNodeData() throws Exception {
        byte[] bytes = zKClient.getData("/JD", false, new Stat());
        String str = new String(bytes);
        System.out.println("/JD节点的数据为:" + str);
    }

    //修改节点上的数据
    @Test
    public void updata() throws Exception {
        /*
        version: 表示版本
         */
        Stat stat = zKClient.setData("/JD", "TPA".getBytes(), 0);
        System.out.println(stat);
    }

    //删除节点
    @Test
    public void DeleteNode() throws Exception {
        zKClient.delete("/JD", 1);
        System.out.println("删除成功");
    }

    //获取子节点
    @Test
    public void GetChildren() throws Exception {
        List<String> list = zKClient.getChildren("/china", false);
        for (String child : list) {
            System.out.println(child);
        }
    }

    //监听根节点下面的变化
    @Test
    public void WatchNode() throws Exception {
        //表示监听根节点下的所有节点的变化，增删改都会触发监听事件
        List<String> list = zKClient.getChildren("/", true);
        for (String child : list) {
            System.out.println(child);
            //让线程无限的等待下去(等待输入，不输入就一直等,即一直监听)
            System.in.read();
        }
    }

    //判断节点是否存在
    @Test
    public void exists()throws Exception{
        Stat stat = zKClient.exists("/china", false);
        if (stat==null){
            System.out.println("该节点不存在");
        }else {
            System.out.println("该节点存在");
        }
    }
}
