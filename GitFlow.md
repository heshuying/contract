

 https://www.atlassian.com/git/tutorials/comparing-workflows/gitflow-workflow 

# 1 master分支和develop分支

![Git flow workflow - Historical Branches](https://wac-cdn.atlassian.com/dam/jcr:2bef0bef-22bc-4485-94b9-a9422f70f11c/02%20(2).svg?cdnVersion=681)

 `master` 分支存储官方发布历史（有tag）， `develop` 分支作为特性(需求)开发的基地。

首先要基于默认的 `master` 建立一个 `develop` 分支branch：

```sh
git branch develop
git push -u origin develop
```

还可以在已有的库上使用git-flow，会自动创建 `develop` 分支：

```sh
$ git flow init
Initialized empty Git repository in ~/project/.git/
No branches exist yet. Base branches must be created now.
Branch name for production releases: [master]
Branch name for "next release" development: [develop]
How to name your supporting branch prefixes?
Feature branches? [feature/]
Release branches? [release/]
Hotfix branches? [hotfix/]
Support branches? [support/]
Version tag prefix? []

$ git branch
* develop
 master
```

# 2 Feature分支

每个需求都应该建立对应的feature分支，其源于 `develop` 分支，需求开发完成后合并(merge)回 `develop`分支（feature分支永远不会和master分支有关联 ）。注：可以推送到远程同名feature分支进行备份保存。

![Git flow workflow - Feature Branches](https://wac-cdn.atlassian.com/dam/jcr:b5259cce-6245-49f2-b89b-9871f9ee3fa4/03%20(2).svg?cdnVersion=681)

`Feature` 分支是从最新的 `develop` 分支创建而来。

## 新需求来临时

创建一个feature分支，两种方式

经典git命令：

```sh
git checkout develop
git checkout -b feature_branch
```

采用git-flow命令：

```sh
git flow feature start feature_branch
```

之后开始开发工作，日常的GIT操作，比如git add，git commit，git reset，git rebase等等

## 开发完需求时

当完成当前需求的开发工作时，下一步就要把对应的 `feature` 分支合并进 `develop`

经典git命令：

```sh
git checkout develop
git merge feature_branch
```

采用git-flow命令：

```sh
git flow feature finish feature_branch
```

# 3 Release分支

![Git Flow Workflow - Release Branches](https://wac-cdn.atlassian.com/dam/jcr:a9cea7b7-23c3-41a7-a4e0-affa053d9ea7/04%20(1).svg?cdnVersion=681)

用专门分支为发布做准备，可以让发布版本的测试完善与其他需求的开发并行，同时可以非常清晰的描述发布版本中包含什么功能。`release` 分支也来源于 `develop` 分支。

经典git命令：

```sh
git checkout develop
git checkout -b release/0.1.0
```

使用git-flow命令：

```sh
$ git flow release start 0.1.0
Switched to a new branch 'release/0.1.0'
```

`release`分支测试中发现的bug可以创建 `bugfix`分支进行修复，`bugfix`仅依赖于`release`。

如果测试没问题了，可以发布，则创建 `pull request`，待代码审查完毕，同意发布之后，需要把当前`release`分支合并到`master`和`develop`分支，合并成功后，删除当前`release`分支（该分支为发布而临时创建使用的）

注意，master分支上要同步打tag

经典git命令（应专人负责）：

```sh
git checkout master
git merge release/0.1.0
```

使用git-flow命令：

```
git flow release finish '0.1.0'
```

# 4 Hotfix分支

![Git flow workflow - Hotfix Branches](https://wac-cdn.atlassian.com/dam/jcr:61ccc620-5249-4338-be66-94d563f2843c/05%20(2).svg?cdnVersion=681)

该分支仅用于对生产系统bug进行快速修复。不必中断当前需求开发、新版本测试准备等工作。

经典git命令：

```sh
git checkout master
git checkout -b hotfix_branch
```

采用git-flow命令：

```sh
$ git flow hotfix start hotfix_branch
```

Bug修复完成之后， `hotfix` 分支也要同时合并到 `master` 和`develop`分支中

```sh
##经典git
git checkout master
git merge hotfix_branch

git checkout develop
git merge hotfix_branch

git branch -D hotfix_branch

##采用git-flow命令
$ git flow hotfix finish hotfix_branch
```

# 示例

如下是开发新需求的一个完整分支示例，假设已有 `master` 分支

```sh
git checkout master
git checkout -b develop
git checkout -b feature_branch
# 在 feature 分支上进行开发
git checkout develop
git merge feature_branch
git checkout master
git merge develop
git branch -d feature_branch
```

`hotfix` 分支示例：

```sh
git checkout master
git checkout -b hotfix_branch
# work is done commits are added to the hotfix_branch
git checkout develop
git merge hotfix_branch
git checkout master
git merge hotfix_branch
```

# Summary

总的来说，GitFlow流程：

1. 从 `master` 创建一个 `develop` 分支 

   ```sh
   git checkout -b develop
   ```

2. 从 `develop` 创建一个 `release` 分支

   git checkout -b release

3. 包含新功能、特性的 `Feature` 分支也是从 `develop` 创建的（分支的命名规则为开发人员姓名+所开发的功能。命名中不要使用特殊字符，不要使用点）

   ```sh
   git checkout -b $feature_name
   ```

4. 当一个 `feature` 完成时，需要合并到`develop` 分支（merge request）

5. 当 `release` 分支完成时，需要合并回 `develop` 和 `master`

6. 如果在 `master` 发现一个问题(issue)，则从 `master` 创建一个 `hotfix` 来修复

7. 快速修复的 `hotfix` 分支完成之后，同时需要合并回 `develop` 和`master`