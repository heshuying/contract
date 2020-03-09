# Getting Started
打包：
    开发：mvn package -Pdev -Dmaven.test.skip=true
    生产：mvn package -Pprod -Dmaven.test.skip=true
    测试：mvn package -Ptest -Dmaven.test.skip=true
- 生产：

# 数据库
### 测试
    10.138.26.200:3909 库名：chain_contract 用户名：chcontract_tmp/Bfds8LvadCCd
### 生产
    10.135.17.239:3104 库名：chain_contract 用户名：chcontract_tmp/Bf7Dvs9hVcxds

# 网址
### 测试
    lqhy.hoptest.haier.net/api
### 生产
    lqhy.haier.net/api/

    状态，0抢入中，1抢入成功,（已审批），2已驳回，3：被踢出,4:已失效，5:已撤销，7：已过期

    举单状态  0 抢单中，1已生效  4:已失效，7：已完成
    抢单状态  1 已抢入，3：被踢出,5:已撤销，6 抢单历史 8 抢入成功

