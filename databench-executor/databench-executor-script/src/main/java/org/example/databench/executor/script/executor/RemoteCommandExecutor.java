package org.example.databench.executor.script.executor;

import com.google.common.collect.Lists;
import org.apache.commons.exec.CommandLine;
import org.example.databench.executor.script.Constants;
import org.example.databench.executor.script.log.CommandLogHandler;
import org.example.databench.executor.script.utils.ShellUtils;
import org.example.databench.lib.utils.NetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Remote command executor
 * 1.wrapped remote command by ssh options
 * 2.wrapped scp command by secure options
 * 3.execute remote command in working directory
 * 4.wrapped remote command by source env
 * 5.wrapped remote command by user
 */
public class RemoteCommandExecutor extends AbstractCommandExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteCommandExecutor.class);
    /**
     * Create script file default dir path
     */
    private static final String DEFAULT_TMP_DIR_PATH = "/tmp/remote_script/";
    private static final String ANONYMOUS_REMOTE = "anonymous";
    private static final String DEFAULT_LOGIN_SOURCE_ENV_PATH = "~/.bash_profile";
    private static final String DEFAULT_ENV_SOURCE = "/etc/profile";
    /**
     * Secure auth config
     */
    private final SecureAuth secureAuth;
    /**
     * Env source paths
     */
    private final List<String> envFilePaths = Lists.newArrayList(DEFAULT_LOGIN_SOURCE_ENV_PATH, DEFAULT_ENV_SOURCE);
    /**
     * Remote working directory
     */
    private String remoteWorkingDirectory = Constants.EMPTY;
    /**
     * Destroy remote process when killed
     */
    private boolean destroyRemoteProcess = true;
    /**
     * The user who executed the command on remote server
     */
    private String remoteCommandUser;

    public RemoteCommandExecutor(SecureAuth secureAuth) {
        super();
        this.secureAuth = secureAuth;
    }

    private String getDefaultTmpDirPath() {
        return DEFAULT_TMP_DIR_PATH +
                Optional.ofNullable(NetUtils.getHostName()).orElse(ANONYMOUS_REMOTE)
                + Constants.LEFT_SLASH;
    }

    @Override
    public RemoteCommandExecutor workingDirectory(String workingDirectory) {
        return (RemoteCommandExecutor) super.workingDirectory(workingDirectory);
    }

    @Override
    public RemoteCommandExecutor logHandler(CommandLogHandler commandLogHandler) {
        return (RemoteCommandExecutor) super.logHandler(commandLogHandler);
    }

    @Override
    public RemoteCommandExecutor commandUser(String user) {
        return (RemoteCommandExecutor) super.commandUser(user);
    }

    public RemoteCommandExecutor remoteCommandUser(String user) {
        remoteCommandUser = user;
        return this;
    }

    @Override
    public RemoteCommandExecutor timeout(long timeout) {
        return (RemoteCommandExecutor) super.timeout(timeout);
    }

    public RemoteCommandExecutor addEnvSourceFilePath(String filePath) {
        if (filePath != null && !envFilePaths.isEmpty()) {
            envFilePaths.add(filePath);
        }
        return this;
    }

    public RemoteCommandExecutor destroyRemoteProcess(boolean destroyRemoteProcess) {
        this.destroyRemoteProcess = destroyRemoteProcess;
        return this;
    }

    /**
     * Set remote command working directory
     *
     * @param remoteWorkingDirectory working directory
     * @return this
     */
    public RemoteCommandExecutor remoteWorkingDirectory(String remoteWorkingDirectory) {
        this.remoteWorkingDirectory = Optional.ofNullable(remoteWorkingDirectory).orElse(Constants.EMPTY);
        return this;
    }

    @Override
    public ExecResult execScript(String script, String... scriptSaveName) {
        script = wrappedByWorkingDirectory(script);
        return super.execScript(script, scriptSaveName);
    }

    @Override
    public ExecResult exec(CommandLine commandLine) {
        commandLine = wrappedBySecureAuth(
                wrappedRemoteCommandByUser(wrappedByWorkingDirectory(commandLine), remoteCommandUser),
                false, false);
        return super.exec(commandLine);
    }

    /**
     * Secure shell copy
     *
     * @param source  file source path
     * @param target  remote target path
     * @param options scp option
     * @return execute result
     */
    public ExecResult scp(String source, String target, boolean to, String... options) {
        CommandLine commandLine = CommandLine.parse(Constants.Command.SCP);
        Arrays.asList(options).forEach(i -> {
            if (i.startsWith(Constants.HORIZONTAL_BAR)) {
                i = i.substring(1);
            }
            i = Constants.HORIZONTAL_BAR + i;
            commandLine.addArguments(i, false);
        });
        commandLine.addArguments(source, false).addArguments(target, false);
        return super.exec(wrappedBySecureAuth(commandLine, true, to));
    }

    private CommandLine wrappedByWorkingDirectory(CommandLine commandLine) {
        if (!remoteWorkingDirectory.isEmpty()) {
            return new CommandLine(Constants.Command.CD)
                    .addArgument(remoteWorkingDirectory)
                    .addArgument(Constants.SEMI_COLON)
                    .addArguments(commandLine.toStrings(), false);
        }
        return commandLine;
    }

    private String wrappedByWorkingDirectory(String script) {
        return Constants.Command.CD +
                Constants.SPACE + remoteWorkingDirectory + Constants.SEMI_COLON +
                Constants.LINE_FEED +
                script;
    }

    private CommandLine wrappedBySecureAuth(CommandLine originCommandLine, boolean isScp, boolean to) {
        String[] originCommandLines = originCommandLine.toStrings();
        // Secure shell login user & host
        var userHost = secureAuth.getUsername() + Constants.AT + secureAuth.getHost();
        var commandLine = CommandLine.parse(Constants.Command.SSH);
        var files = new ArrayList<String>();
        if (isScp) {
            var options = new ArrayList<String>();
            for (var str : originCommandLines) {
                // Filter with space & 'scp'
                if (Constants.EMPTY.equals(str) || Constants.Command.SCP.equals(str)) {
                    continue;
                }
                if (str.startsWith(Constants.HORIZONTAL_BAR)) {
                    options.add(str);
                } else {
                    files.add(str);
                }
            }
            commandLine = CommandLine.parse(Constants.Command.SCP);
            // Add original scp options
            commandLine.addArguments(options.toArray(new String[]{}), false);
            if (files.size() != 2) {
                throw new IllegalArgumentException("Illegal arguments, scp command's source/target illegal, " + files);
            }
            int remoteFileIndex = to ? 1 : 0;
            files.set(remoteFileIndex,
                    appendLeftSlash(remoteWorkingDirectory) + files.get(remoteFileIndex));
        }
        if (!isScp && destroyRemoteProcess) {
            // Set -tt
            commandLine.addArgument("-tt");
        }
        // Add ssh(scp) default shell auth conf
        commandLine
                .addArgument("-o")
                .addArgument("StrictHostKeyChecking=no")
                .addArgument("-i")
                .addArgument(secureAuth.getPrivateKeyPath())
                .addArgument(isScp ? "-P" : "-p")
                .addArgument(secureAuth.getPort() + Constants.EMPTY);
        if (!isScp) {
            // Add inner command
            commandLine.addArgument(userHost);
            // wrapped by sourcing env file paths
            for (String envFilePath : envFilePaths) {
                commandLine.addArgument(Constants.Command.SOURCE)
                        .addArgument(envFilePath)
                        .addArgument(Constants.SEMI_COLON);
            }
            commandLine.addArguments(originCommandLines, false);
        } else {
            String hostPrefix = userHost + Constants.COLON;
            // Add scp source & target file path
            commandLine.addArgument(to ? files.get(0) : (hostPrefix + files.get(0)));
            commandLine.addArgument(to ? (hostPrefix + files.get(1)) : files.get(1));
        }
        return commandLine;
    }

    /**
     * Write script file to remote server
     * <p>
     * 1.write script file to local
     * 2.copy script file to remote server
     *
     * @param path   Local script file path
     * @param script Script file content
     * @return file path
     */
    @Override
    protected String writeScriptToFile(String path, String script) {
        var filePath = super.writeScriptToFile(path, script);
        var finalTargetDirPath = getDefaultTmpDirPath() + getTargetDirPath(filePath);
        var msgFormat = "Write script file to remote server %s, " +
                "local [" + filePath + "] >>> remote [" + finalTargetDirPath + "]";
        try {
            // Mkdir target folder on remote server
            ShellUtils.execRemoteCommand(secureAuth, Constants.Command.MKDIR + " -p " + finalTargetDirPath);
            // Scp file to remote server
            ShellUtils.scp(secureAuth, filePath, finalTargetDirPath, true, "-r");
        } catch (Exception e) {
            throw new RuntimeException(String.format(msgFormat, "error"), e);
        }
        LOGGER.info(String.format(msgFormat, ""));
        log("[CREATE_SH_FILE] " + String.format(msgFormat, ""));
        return getDefaultTmpDirPath() + filePath;
    }

    private String getTargetDirPath(String filePath) {
        int i = filePath.lastIndexOf(Constants.LEFT_SLASH);
        var targetDirPath = filePath.substring(0, i);
        return remoteStartLeftSlash(targetDirPath);
    }

    private String remoteStartLeftSlash(String path) {
        if (path.startsWith(Constants.LEFT_SLASH)) {
            path = path.substring(1);
        }
        return path;
    }

    private String appendLeftSlash(String path) {
        if (path.endsWith(Constants.LEFT_SLASH)) {
            path = path.substring(0, path.length() - 1);
        }
        return path + Constants.LEFT_SLASH;
    }
}
