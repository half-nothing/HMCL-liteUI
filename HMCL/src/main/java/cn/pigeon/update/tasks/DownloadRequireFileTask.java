package cn.pigeon.update.tasks;

import cn.pigeon.update.Static;
import org.jackhuang.hmcl.task.FetchTask;
import org.jackhuang.hmcl.task.FileDownloadTask;
import org.jackhuang.hmcl.task.Task;

import java.io.File;
import java.net.URL;
import java.util.*;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;
import static org.jackhuang.hmcl.task.FetchTask.DEFAULT_CONCURRENCY;

public class DownloadRequireFileTask extends Task<Void> {

    private final List<Task<?>> dependencies = new ArrayList<>();
    private final Map<URL, File> urls;

    public DownloadRequireFileTask(Map<URL, File> urls) {
        this.urls = urls;
        updateProgress(0);
    }

    @Override
    public void execute() throws Exception {
        updateProgress(1, 3);
        for (Map.Entry<URL, File> entry : urls.entrySet()) {
            FileDownloadTask task = new FileDownloadTask(entry.getKey(), entry.getValue());
            task.setName(entry.getValue().getName());
            task.setCaching(false);
            dependencies.add(task.withCounter("Download File"));
        }
        updateProgress(2, 3);
    }

    @Override
    public Void getResult() {
        updateProgress(3, 3);
        FetchTask.setDownloadExecutorConcurrency(DEFAULT_CONCURRENCY);
        return null;
    }

    @Override
    public Collection<? extends Task<?>> getDependents() {
        FetchTask.setDownloadExecutorConcurrency(Math.min(Static.updateMaxThread, config().getDownloadThreadsPigeon()));
        return Collections.emptySet();
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }
}
