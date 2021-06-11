package org.basis.framework.jgit;


import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.basis.framework.constant.GitConsts;
import org.basis.framework.jgit.bean.GitParam;
import org.basis.framework.jgit.bean.Response;
import org.basis.framework.jgit.bean.Tag;
import org.basis.framework.utils.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @Description git操作工具类
 * @Author ChenWenJie
 * @Data 2020/11/20 2:55 下午
 **/
public class GitUtils {
    private final static String DEFAULT_EMAIL = "default@mail.com";

    /**
     * 功能：提交文件信息
     *
     * @param param 参数对象
     * @return 返回提交ID
     */
    @SneakyThrows
    public static String commit(GitParam.CommitParam param) {
        Git git = GitFactory.getInstance(param.getVersionLibrary());
        RevCommit revCommit = null;
        try {
            AddCommand addCommand = git.add();
            param.getFilePath().forEach(addCommand::addFilepattern);
            addCommand.call();
            CommitCommand commit = git.commit().setMessage(param.getMessage());
            commit.setCommitter(param.getUserName(), StringUtils.isNotBlank(param.getEmail()) ? param.getEmail() : DEFAULT_EMAIL);
            revCommit = commit.call();
            if (StringUtils.isNotBlank(param.getTagName())) {
                prepareTag(git, revCommit, param);
            }
        } finally {
            git.close();
        }
        return revCommit.getId().getName();
    }

    /**
     * 功能：根据commitId创建标签
     *
     * @param tag 请求参数
     * @return true
     */
    @SneakyThrows
    public static Boolean createTag(Tag tag) {
        Git git = GitFactory.getInstance(tag.getVersionLibrary());
        ObjectId objectId = git.getRepository().resolve(tag.getCommitId());
        RevCommit revCommit = git.getRepository().parseCommit(objectId);
        git.tag().setObjectId((RevObject) revCommit.getId())
                .setName(tag.getTagName()).setMessage(tag.getMessage()).call();
        return true;
    }

    /**
     * 功能：获取版本库标签信息
     *
     * @param versionLibrary 版本库
     * @param currentCommit  当前提交ID
     * @return 返回标签集
     */
    @SneakyThrows
    public static List<Tag> tags(String versionLibrary, String currentCommit) {
        List<Tag> tags = new ArrayList<>();
        Git git = GitFactory.getInstance(versionLibrary);
        List<Ref> refs = git.tagList().call();
        if (StringUtils.isNotBlank(currentCommit)) {
            ObjectId objectId = git.getRepository().resolve(currentCommit);
            refs = refs.stream().filter(ref -> {
                RevCommit revCommit = getCommit(git.getRepository(), ref.getObjectId());
                return revCommit.compareTo(objectId) > 0;
            }).collect(Collectors.toList());
        }
        for (Ref ref : refs) {
            RevWalk revWalk = new RevWalk(git.getRepository());
            RevTag revTag = revWalk.parseTag(ref.getObjectId());
            RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());
            tags.add(Tag.builder().tagName(getTagName(ref)).commitId(revCommit.getId().name())
                    .message(revTag.getShortMessage()).build());
        }
        return tags;
    }

    /**
     * 获取当前 commitId的标签 TODO 待性能优化（数据量大时）
     * @param versionLibrary 版本库
     * @param commitId 提交id
     * @return
     */
    @SneakyThrows
    public static String commitTag(String versionLibrary, String commitId) {
        String tag = "";
        Git git = GitFactory.getInstance(versionLibrary);
        List<Ref> refs = git.tagList().call();
        if (StringUtils.isNotBlank(commitId)) {
            ObjectId objectId = git.getRepository().resolve(commitId);
            for (Ref ref : refs) {
                RevCommit revCommit = getCommit(git.getRepository(), ref.getObjectId());
                if (revCommit.compareTo(objectId) == 0){
                    return getTagName(ref);
                }
            }
        }
        return tag;
    }

    /**
     * 功能：根据commitId对应的版本库历史数据
     *
     * @param versionLibrary 版本库
     * @param commitId       提交id
     * @return Response.LogResponse
     */
    public static Response.LogResponse log(String versionLibrary, String commitId) {
        return getLog(versionLibrary, commitId, (String) null);
    }

    /**
     * 功能：根据commitId获取指定路径的文件数据
     *
     * @param versionLibrary 版本库
     * @param commitId       提交id
     * @param path           路径(文件/目录)
     * @return Response.LogResponse
     */
    public static Response.LogResponse log(String versionLibrary, String commitId, String path) {
        return getLog(versionLibrary, commitId, path);
    }

    /**
     * 功能：获取指定版本库所有历史数据
     *
     * @param versionLibrary 版本库
     * @return 返回全部历史数据
     */
    public static List<Response.LogResponse> logs(String versionLibrary) {
        return getLogs(versionLibrary, (String) null, (String) null, (String) null);
    }

    /**
     * 功能：获取版本库指定路径所有历史数据
     *
     * @param versionLibrary 版本库
     * @param path           路径（目录/文件）
     * @return 返回历史数据集
     */
    public static List<Response.LogResponse> logs(String versionLibrary, String path) {
        return getLogs(versionLibrary, path, (String) null, (String) null);
    }

    /**
     * 功能：获取startCommit到endCommit之间的版本库信息集
     *
     * @param versionLibrary 版本库
     * @param startCommit    开始指针
     * @param endCommit      结束指针
     * @return 返回版本库信息集
     */
    public static List<Response.LogResponse> logs(String versionLibrary, String startCommit,
                                                  String endCommit) {

        return getLogs(versionLibrary, (String) null, startCommit, endCommit);
    }

    /**
     * 功能：获取startCommit到endCommit之间指定路径下的文件数据集
     *
     * @param versionLibrary 版本库
     * @param startCommit    开始指针
     * @param endCommit      结束指针
     * @param path           路径（文件/路径）
     * @return 返回文件数据集
     */
    public static List<Response.LogResponse> logs(String versionLibrary, String startCommit,
                                                  String endCommit, String path) {

        return getLogs(versionLibrary, path, startCommit, endCommit);
    }

    /**
     * 功能：获取startCommit到endCommit的文件数据集（整个版本库更改过的）
     *
     * @param versionLibrary 版本库
     * @param startCommit    开始指定
     * @param endCommit      结束指针
     * @return 返回文件数据集
     */
    public static List<Response.LogResponse> diffs(String versionLibrary, String startCommit,
                                                   String endCommit) {
        return getDiffLogs(versionLibrary, startCommit, endCommit, (String) null);
    }

    /**
     * 功能：获取startCommit到endCommit指定路径（文件/目录）的文件数据集（更改过的）
     *
     * @param versionLibrary 版本库
     * @param startCommit    开始指定
     * @param endCommit      结束指针
     * @param path           路径（文件/目录）
     * @return 返回文件数据集
     */
    public static List<Response.LogResponse> diffs(String versionLibrary, String startCommit,
                                                   String endCommit, String path) {
        return getDiffLogs(versionLibrary, startCommit, endCommit, path);
    }

    /**
     * 功能：获取版本库指定标签对应的数据信息
     *
     * @param versionLibrary 版本库
     * @param tag            标签名称
     * @return 返回标签对应的数据信息
     */
    public static Response.LogResponse tag(String versionLibrary, String tag) {
        List<Response.LogResponse> logResponseList = getTags(versionLibrary, tag, false);
        return logResponseList.size() > 0 ? logResponseList.get(0) : null;
    }

    /**
     * 功能:获取版本库所有标签对应的数据信息
     *
     * @param versionLibrary 版本库名称
     * @return 返回所有标签对应的数据信息
     */
    public static List<Response.LogResponse> tags(String versionLibrary) {
        return getTags(versionLibrary, (String) null, true);
    }

    /**
     * 功能：获取提交基本数据
     *
     * @param commit 提交对象
     * @return 返回提交基本数据
     */
    private static Response.LogResponse prepareLogResponse(RevCommit commit) {
        Response.LogResponse logResponse = new Response.LogResponse();
        logResponse.setCommitter(commit.getCommitterIdent().getName());
        logResponse.setMessage(commit.getShortMessage());
        logResponse.setDate(new Date(commit.getCommitTime()));
        logResponse.setCommitId(commit.getId().getName());
        return logResponse;
    }

    /**
     * 功能：获取文件数据
     *
     * @param repository 仓库对象
     * @param commit     提交对象
     * @param filePath   文件路径
     * @return Response.LogData
     */
    @SneakyThrows
    private static Response.LogData getLogData(Repository repository, RevCommit commit, String filePath) {
        Response.LogData logData = Response.LogData.builder().id(FileUtils.getId(filePath))
                .code(FileUtils.getCode(filePath)).build();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, commit.getTree());
        ObjectLoader objectLoader = repository.open(treeWalk.getObjectId(0));
        objectLoader.copyTo(outputStream);
        logData.setData(outputStream.toString(GitConsts.CHARSET_NAME));
        outputStream.reset();
        return logData;
    }

    /**
     * 功能：获取提交对象
     *
     * @param repository 仓库对象
     * @param commitId   提交ID
     * @return 返回提交对象
     */
    @SneakyThrows
    private static RevCommit getCommit(Repository repository, String commitId) {
        ObjectId objectId = repository.resolve(commitId);
        RevWalk revWalk = new RevWalk(repository);
        return revWalk.parseCommit(objectId);
    }

    @SneakyThrows
    public static RevCommit getCommit(Repository repository, AnyObjectId objectId) {
        return repository.parseCommit(objectId);
    }

    /**
     * 功能：获取TreeWalk（提交内容）
     *
     * @param repository 仓库对象
     * @param revCommit  提交对象
     * @param path       文件夹路径
     * @return TreeWalk
     */
    @SneakyThrows
    private static TreeWalk getTreeWalk(Repository repository, RevCommit revCommit, String path) {
        TreeWalk treeWalk = null;
        treeWalk = new TreeWalk(repository);
        treeWalk.addTree(revCommit.getTree());
        if (StringUtils.isNotBlank(path)) {
            treeWalk.setFilter(PathFilter.create(path));
        }
        treeWalk.setRecursive(true);
        return treeWalk;
    }

    /**
     * 功能：获取版本库所有历史数据(当path不为null时查询指定path下的所有历史数据)
     *
     * @param versionLibrary 版本库
     * @param path           路径（目录/文件）
     * @return 返回历史数据集
     */
    @SneakyThrows
    private static List<Response.LogResponse> getLogs(String versionLibrary, String path,
                                                      String startCommit, String endCommit) {
        Git git = null;
        List<Response.LogResponse> logResponseList = null;
        try {
            git = GitFactory.getInstance(versionLibrary);
            Repository repository = git.getRepository();
            logResponseList = new ArrayList<>();
            LogCommand logCommand = git.log();
            addFilter(logCommand, startCommit, endCommit, path);
            Iterator<RevCommit> iterator = logCommand.call().iterator();
            while (iterator.hasNext()) {
                RevCommit revCommit = iterator.next();
                Response.LogResponse logResponse = getLogResponse(revCommit, repository, path);
                logResponse.setTagName(commitTag(versionLibrary, revCommit.getId().getName()));
                logResponseList.add(logResponse);
            }
        } finally {
            if (git != null) {
                git.close();
            }
        }
        return logResponseList;
    }

    /**
     * 功能：根据commitId获取指定路径（文件/目录）下历史数据(当path不为null时查询指定path下的所有历史数据)
     *
     * @param versionLibrary 版本库
     * @param commitId       提交ID
     * @param path           路径
     * @return 返回历史数据
     */
    @SneakyThrows
    private static Response.LogResponse getLog(String versionLibrary, String commitId, String path) {
        Git git = null;
        Response.LogResponse logResponse = null;
        try {
            git = GitFactory.getInstance(versionLibrary);
            Repository repository = git.getRepository();
            RevCommit revCommit = getCommit(repository, commitId);
            logResponse=getLogResponse(revCommit,repository,path);
        } finally {
            if (git != null) {
                git.close();
            }
        }
        return logResponse;
    }

    /**
     * 功能：添加过滤条件
     *
     * @param logCommand  对象
     * @param startCommit 开始提交指针
     * @param endCommit   结束提交指针
     * @param path        路径（文件/目录）
     */
    @SneakyThrows
    private static void addFilter(LogCommand logCommand, String startCommit,
                                  String endCommit, String path) {
        Repository repository = logCommand.getRepository();
        if (StringUtils.isNotBlank(path)) {
            logCommand.addPath(path);
        }
        if (StringUtils.isNotBlank(startCommit) && StringUtils.isNotBlank(endCommit)) {
            ObjectId startObject = repository.resolve(startCommit),
                    endObject = repository.resolve(endCommit);
            logCommand.addRange(startObject, endObject);
        } else if (StringUtils.isNotBlank(endCommit)) {
            logCommand.add(repository.resolve(endCommit));
        } else if (StringUtils.isNotBlank(startCommit)) {
            logCommand.not(repository.resolve(startCommit));
        }
    }

    /**
     * 功能：获取提交集
     *
     * @param logCommand 命令对象
     * @return 返回提交集
     */
    @SneakyThrows
    private static List<RevCommit> getCommits(LogCommand logCommand, RevCommit startCommit) {
        List<RevCommit> revCommitList = new ArrayList<>();
        if (startCommit != null) {
            revCommitList.add(startCommit);
        }
        Iterator<RevCommit> iterator = logCommand.call().iterator();
        while (iterator.hasNext()) {
            revCommitList.add(iterator.next());
        }
        revCommitList.sort(Comparator.comparing(RevCommit::getCommitTime));
        return revCommitList;
    }

    /**
     * 功能:获取树形数据解析器
     *
     * @param repository 仓库对象
     * @param revCommit  提交对象
     * @return 获取数据解析器
     */
    @SneakyThrows
    private static AbstractTreeIterator getAbstractTreeIterator(Repository repository, RevCommit revCommit) {
        RevWalk revWalk = new RevWalk(repository);
        CanonicalTreeParser treeParser = null;
        RevTree revTree = revWalk.parseTree(revCommit.getTree().getId());
        treeParser = new CanonicalTreeParser();
        treeParser.reset(repository.newObjectReader(), revTree);
        revWalk.dispose();
        return treeParser;
    }

    /**
     * 功能：获取startCommit到endCommit指定路径（文件/目录）的文件数据集（更改过的）
     *
     * @param versionLibrary 版本库
     * @param startCommit    开始指定
     * @param endCommit      结束指针
     * @param path           路径（文件/目录）
     * @return 返回文件数据集
     */
    @SneakyThrows
    private static List<Response.LogResponse> getDiffLogs(String versionLibrary, String startCommit,
                                                          String endCommit, String path) {
        Git git = GitFactory.getInstance(versionLibrary);
        List<Response.LogResponse> logResponseList = new ArrayList<>();
        try {
            LogCommand logCommand = git.log();
            addFilter(logCommand, startCommit, endCommit, path);
            Repository repository = git.getRepository();
            RevCommit revCommit = null;
            if (StringUtils.isNotBlank(startCommit)) {
                revCommit = getCommit(repository, startCommit);
            }
            List<RevCommit> revCommitList = getCommits(logCommand, revCommit);
            if (revCommitList.size() > 1) {
                revCommitList.stream().reduce((commit1, commit2) -> {
                    Response.LogResponse logResponse = prepareLogResponse(commit2);
                    logResponse.setLogDataList(getLogDataList(git, commit1, commit2, path));
                    logResponseList.add(logResponse);
                    return commit2;
                });
            } else {
                revCommitList.forEach(commit -> {
                    logResponseList.add(getLogResponse(commit, git.getRepository(), (String)null));
                });
            }

        } finally {
            if (git != null) {
                git.close();
            }
        }
        return logResponseList;
    }

    /**
     * 功能：获取文件数据集（更新过的）
     *
     * @param git     Git对象
     * @param commit1 被比较提交对象
     * @param commit2 比较提交对象
     * @return 返回文件数据集
     */
    @SneakyThrows
    private static List<Response.LogData> getLogDataList(Git git, RevCommit commit1,
                                                         RevCommit commit2, String path) {
        List<Response.LogData> logDataList = new ArrayList<>();
        AbstractTreeIterator oldTreeIterator = getAbstractTreeIterator(git.getRepository(), commit1),
                newTreeIterator = getAbstractTreeIterator(git.getRepository(), commit2);
        List<DiffEntry> diffEntries = git.diff().setOldTree(oldTreeIterator).setNewTree(newTreeIterator).call();
        for (DiffEntry diffEntry : diffEntries) {
            if (StringUtils.isNotBlank(path)) {
                if (diffEntry.getNewPath().contains(path)) {
                    logDataList.add(getLogData(git.getRepository(), commit2, diffEntry.getNewPath()));
                }
                continue;
            }
            logDataList.add(getLogData(git.getRepository(), commit2, diffEntry.getNewPath()));
        }
        return logDataList;
    }

    @SneakyThrows
    private static void prepareTag(Git git, RevCommit commit, GitParam.CommitParam commitParam) {
        git.tag().setName(commitParam.getTagName())
                .setObjectId((RevObject) commit.getId()).call();
    }

    @SneakyThrows
    private static List<Response.LogResponse> getTags(String versionLibrary, String tag,
                                                      Boolean isMulti) {
        Git git = GitFactory.getInstance(versionLibrary);
        List<Response.LogResponse> logResponseList = new ArrayList<>();
        try {
            List<Ref> refs = git.tagList().call();
            if (!isMulti) {
                Optional<Ref> optional = refs.stream().filter(ref -> ref.getName().contains(tag)).findFirst();
                refs.clear();
                optional.ifPresent(ref -> {
                    refs.add(ref);
                });
            }
            for (Ref ref : refs) {
                RevWalk revWalk = new RevWalk(git.getRepository());
                RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());
                Response.LogResponse logResponse = prepareLogResponse(revCommit);
                logResponse.setTagName(getTagName(ref));
                List<Response.LogData> logDataList = new ArrayList<>();
                TreeWalk treeWalk = new TreeWalk(git.getRepository());
                treeWalk.addTree(revCommit.getTree());
                treeWalk.setRecursive(true);
                while (treeWalk.next()) {
                    logDataList.add(getLogData(git.getRepository(), revCommit, treeWalk.getPathString()));
                }
                logResponse.setLogDataList(logDataList);
                logResponseList.add(logResponse);
            }
        } finally {
            git.close();
        }
        return logResponseList;
    }

    private static String getTagName(Ref ref) {
        return ref.getName().replaceAll("refs/tags/", "");
    }

    @SneakyThrows
    private static Response.LogResponse getLogResponse(RevCommit revCommit, Repository repository,
                                                String path) {

        Response.LogResponse logResponse = prepareLogResponse(revCommit);
        List<Response.LogData> logDataList = new ArrayList<>();
        TreeWalk treeWalk = getTreeWalk(repository, revCommit, path);
        while (treeWalk.next()) {
            String filePath = treeWalk.getPathString();
            logDataList.add(getLogData(repository, revCommit, filePath));
        }
        logResponse.setLogDataList(logDataList);
        return logResponse;
    }
}
