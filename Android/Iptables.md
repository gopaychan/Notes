1. Netfilter的5个Hook点，iptable通过配置不同的表的不同链（OUTPUT等）来Hook不同的时间段。
    ![](../MdPicture/21)

2. iptables是属于netd里面的一个部分。
    1. Frameworks（NetworkManagementService）层通过socket（netd）跟netd里面的CommandListener通信。
    2. CommandListener会调用FirewallController的对应的方法。
    3. FirewallController会调用IptablesRestoreController的execute方法，接着调用sendCommand方法把command写入子进程iptables-restore的stdin（pipe）
