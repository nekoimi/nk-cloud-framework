package com.nekoimi.nk.framework.mybatis.service;

import com.nekoimi.nk.framework.mybatis.dto.TableInfor;

import java.util.List;

/**
 * nekoimi  2022/1/16 20:56
 *
 * 获取数据库信息
 */
public interface DbInforService {

    /**
     * 数据库名称
     * @return
     */
    String name();

    /**
     * 全部表空间
     * @return
     */
    List<String> schemas();

    /**
     * 全部数据表
     * @return
     */
    List<TableInfor> tables(String schema);
}
