import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import common.UnionFind;

// 给定一个列表 accounts，每个元素 accounts[i] 是一个字符串列表，其中第一个元素 accounts[i][0] 是 名称(name)，其余元素是 emails 表示该账户的邮箱地址。
// 现在，我们想合并这些账户。如果两个账户都有一些共同的邮箱地址，则两个账户必定属于同一个人。请注意，即使两个账户具有相同的名称，它们也可能属于不同的人，因为人们可能具有相同的名称。一个人最初可以拥有任意数量的账户，但其所有账户都具有相同的名称。
// 合并账户后，按以下格式返回账户：每个账户的第一个元素是名称，其余元素是按字符 ASCII 顺序排列的邮箱地址。账户本身可以以任意顺序返回。

// 示例：
// 输入：accounts=[["John","johnsmith@mail.com","john00@mail.com"],["John","johnnybravo@mail.com"],["John","johnsmith@mail.com","john_newyork@mail.com"],["Mary","mary@mail.com"]]
// 输出：[["John",'john00@mail.com','john_newyork @mail.com', 'johnsmith @mail.com'],["John","johnnybravo@mail.com"],["Mary","mary@mail.com"]]
// 解释：第一个和第三个 John 是同一个人，因为他们有共同的邮箱地址"johnsmith@mail.com"。第二个 John 和 Mary 是不同的人，因为他们的邮箱地址没有被其他帐户使用。可以以任何顺序返回这些列表，例如答案[['Mary'，'mary@mail.com']，['John'，'johnnybravo@mail.com']，['John'，'john00@mail.com'，'john_newyork@mail.com'，'johnsmith@mail.com']] 也是正确的。

public class AccountsMerge {

    public static void main(String[] args) {
        List<List<String>> accounts = new ArrayList<>();

        List<String> account1 = new ArrayList<>();
        account1.add("john");
        account1.add("johnsmith@mail.com");// 0
        account1.add("john00@mail.com");// 1
        accounts.add(account1);

        List<String> account2 = new ArrayList<>();
        account2.add("john");
        account2.add("johnnybravo@mail.com");// 2
        accounts.add(account2);

        List<String> account3 = new ArrayList<>();
        account3.add("john");
        account3.add("johnsmith@mail.com");
        account3.add("john_newyork@mail.com");// 3
        accounts.add(account3);

        List<String> account4 = new ArrayList<>();
        account4.add("Mary");
        account4.add("mary@mail.com");// 4
        accounts.add(account4);

        List<List<String>> result = new AccountsMerge().accountsMerge(accounts);
        System.out.println(result);
    }

    public List<List<String>> accountsMerge(List<List<String>> accounts) {
        Map<String, Integer> emailToIndex = new HashMap<>();
        int index = 0;
        for (List<String> list : accounts) {
            for (int i = 1; i < list.size(); i++) {
                if (!emailToIndex.containsKey(list.get(i))) {
                    emailToIndex.put(list.get(i), index);
                    index += 1;
                }
            }
        }

        UnionFind uf = new UnionFind(index);
        for (List<String> list : accounts) {
            for (int i = 1; i < list.size() - 1; i++) {
                uf.union(emailToIndex.get(list.get(i)), emailToIndex.get(list.get(i + 1)));
            }
        }

        Map<Integer, List<String>> indexToEmail = new HashMap<>();
        Map<Integer, String> indexToName = new HashMap<>();
        for (List<String> list : accounts) {
            for (int i = 1; i < list.size(); i++) {
                int root = uf.root(emailToIndex.get(list.get(i)));
                List<String> emails = indexToEmail.get(root);
                if (emails == null) {
                    emails = new ArrayList<>();
                    indexToEmail.put(root, emails);
                    indexToName.put(root, list.get(0));
                }
                if (!emails.contains(list.get(i))) {
                    emails.add(list.get(i));
                }
            }
        }

        List<List<String>> result = new ArrayList<>();

        Iterator<Integer> iterator = indexToEmail.keySet().iterator();
        while (iterator.hasNext()) {
            int root = iterator.next();
            List<String> emails = indexToEmail.get(root);
            Collections.sort(emails);
            List<String> account = new ArrayList<>();
            account.add(indexToName.get(root));
            account.addAll(emails);
            result.add(account);
        }

        return result;
    }

    // 错误答案
    // public List<List<String>> accountsMerge(List<List<String>> accounts) {
    // List<List<String>> result = new ArrayList<>();
    // List<Integer> skip = new ArrayList<>();
    // for (int i = 0; i < accounts.size(); i++) {
    // if (skip.contains(i))
    // continue;
    // List<String> account = new ArrayList<>();
    // account.add(accounts.get(i).get(0));
    // List<String> addr = new ArrayList<>();
    // for (int n = 1; n < accounts.get(i).size(); n++) {
    // if (!addr.contains(accounts.get(i).get(n)))
    // addr.add(accounts.get(i).get(n));
    // }
    // for (int j = i + 1; j < accounts.size(); j++) {
    // for (int k = 0; k < addr.size(); k++) {
    // if (accounts.get(j).indexOf(addr.get(k)) <= 0)
    // continue;
    // skip.add(j);
    // for (int l = 1; l < accounts.get(j).size(); l++) {
    // if (!addr.contains(accounts.get(j).get(l))) {
    // addr.add(accounts.get(j).get(l));
    // }
    // }
    // break;
    // }
    // }
    // Collections.sort(addr);
    // account.addAll(addr);
    // result.add(account);
    // }
    // return result;
    // }
}
