package cn.pigeon.update.tasks;

import cn.pigeon.update.Static;
import org.jackhuang.hmcl.task.FetchTask;
import org.jackhuang.hmcl.task.FileDownloadTask;
import org.jackhuang.hmcl.task.Task;

import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.jackhuang.hmcl.setting.ConfigHolder.config;

public class DownloadRequireFileTask extends Task<Void> {

    private final List<Task<?>> dependencies = new ArrayList<>();
    private final Path basePath;
    private final Map<URL, String> urls;

    public DownloadRequireFileTask(Path basePath, Map<URL, String> urls) {
        this.basePath = basePath;
        this.urls = urls;
        updateProgress(0);
    }

    @Override
    public void execute() throws Exception {
        updateProgress(1, 3);
        for (Map.Entry<URL, String> entry : urls.entrySet()){
            FileDownloadTask task = new FileDownloadTask(entry.getKey(), basePath.getParent().resolve(entry.getValue()).toFile());
            task.setName(entry.getValue());
            task.setCaching(false);
            dependencies.add(task.withCounter("Download File"));
        }
        FetchTask.setDownloadExecutorConcurrency(Math.min(Static.updateMaxThread, config().getDownloadThreadsPigeon()));
        updateProgress(2, 3);
    }

    @Override
    public Void getResult() {
        updateProgress(3, 3);
        return null;
    }

    @Override
    public Collection<Task<?>> getDependencies() {
        return dependencies;
    }
}
