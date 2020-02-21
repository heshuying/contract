package com.haier.hailian.contract.service;

import com.haier.hailian.contract.dto.*;
import com.haier.hailian.contract.entity.*;

import java.util.List;

/**
 * (ZHrChainInfo)表服务接口
 *
 * @author makejava
 * @since 2019-12-17 14:52:19
 */
public interface ZHrChainInfoService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ZNodeTargetPercentInfo queryByNodeId(Integer id);

    ZHrChainInfo queryById(Integer id);

    /**
     * 根据链群查询数据
     * @param id
     * @return
     */
    ZHrChainInfoDto queryAllById(Integer id);


    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<ZHrChainInfo> queryAllByLimit(int offset, int limit);


    List<ZHrChainInfo> queryAll(ZHrChainInfo zHrChainInfo);
    /**
     * 新增数据
     *
     * @param zHrChainInfo 实例对象
     * @return 实例对象
     */
    ZHrChainInfo insert(ZHrChainInfo zHrChainInfo);

    /**
     * 修改数据
     *
     * @param zHrChainInfo 实例对象
     * @return 实例对象
     */
    ZHrChainInfo update(ZHrChainInfo zHrChainInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Integer id);

    /**
     * 校验是否存在连群
     * @param validateChainNameDTO
     * @return
     */
    R validateChainName(ValidateChainNameDTO validateChainNameDTO);


    /**
     * 查询连群架构用户信息
     * @param keyWords
     * @return
     */
    List<SysNodeEhr> searchUsersByKeyWords(String keyWords);

    /**
     * 获取节点对应的目标分享比例
     * @param nodeCodeStr
     * @return
     */
    List<TargetBasic> getNodeTargetList(String nodeCodeStr);

    /**
     * 保存链群的全部信息
     * @param zHrChainInfoDto
     * @return
     */
    ZHrChainInfoDto saveChainInfo(ZHrChainInfoDto zHrChainInfoDto);

    /**
     * 获取平台下战略最小单元
     * @return
     */
    List<TOdsMinbu> getMinbuList();

    /**
     * 更新
     * @param zNodeTargetPercentInfo
     * @return
     */
    int updateChainInfo(ZNodeTargetPercentInfo zNodeTargetPercentInfo);

    /**
     * 删除
     * @param id
     * @return
     */
    int deleteChainInfo(Integer id);

    /**
     *
     * @param userCode
     * @return
     */
    String getDepVCode(String userCode);



    /**
     * 更新
     * @param zNodeTargetPercentInfo
     * @return
     */
    int updateBatch(List<ZNodeTargetPercentInfo> zNodeTargetPercentInfo);

    /**
     * 获取平台下战略最小单元部分数据
     * @return
     */
    List<ExportChainUnitInfo> getPartMinbuList();

    List<ZHrChainInfo> searchChainListByUser(String userCode);

    /**
     * 获取平台下战略最小单元
     * @return
     */
    List<TOdsMinbu> getOtherMinbuList(String chainCode);


    int saveNewMinbu(List<ZNodeTargetPercentInfo> zNodeTargetPercentInfos);

    int saveModel(ZHrChainInfoDto zHrChainInfoDto);

    /**
     * 更新
     * @param zHrChainInfo
     * @return
     */
    int updateModelInfo(ZHrChainInfo zHrChainInfo);

    List<TOdsMinbu> getChildChainOtherMinbuList(ChainRepairInfo chainRepairInfo);
}
