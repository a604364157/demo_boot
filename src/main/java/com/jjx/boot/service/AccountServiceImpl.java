package com.jjx.boot.service;

import com.jjx.boot.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountServiceImpl {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    Map<Long, Account> datas = new HashMap<>(16);

    /**
     * 支持追加数据，no重复则修改
     *
     * @param account data
     */
    public void addAccount(Account account) {
        datas.put(account.getNo(), account);
    }

    /**
     * 获得指定no在按fee排序后的名次
     *
     * @param no no
     * @return 名次
     */
    public int getAccountAndSort(long no) {
        List<Account> accounts = new ArrayList<>(datas.values());
        accounts.sort(Comparator.comparingDouble(Account::getFee));
        Account account = datas.get(no);
        return accounts.indexOf(account);
    }

    /**
     * 获得指定no在按fee排序后其前后的name
     *
     * @param no   no
     * @param size 前后宽度
     * @return names
     */
    public List<String> getAccountByNo(long no, int size) {
        List<String> names = new ArrayList<>();
        List<Account> accounts = new ArrayList<>(datas.values());
        accounts.sort(Comparator.comparingDouble(Account::getFee));
        Account account = datas.get(no);
        int index = accounts.indexOf(account);
        for (int i = index - size; i < index + size; i++) {
            if (i < 0) {
                continue;
            }
            names.add(accounts.get(i).getName());
        }
        return names;
    }

}
