package spring.core.io.support;

import spring.core.io.FileSystemResource;
import spring.core.io.Resource;
import spring.util.Assert;
import spring.util.ClassUtils;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class PackageResourceLoader {

    public PackageResourceLoader() {
    }

    public Resource[] getResources(String basePackage) {
        Assert.notNull(basePackage, "base package must not be null");
        String location = ClassUtils.convertClassNameToResourcePath(basePackage);

        URL url = Thread.currentThread().getContextClassLoader().getResource(location);
        File rootDir = new File(url.getFile());

        Set<File> matchingFiles = retrieveMatchingFiles(rootDir);
        Resource[] resources = new Resource[matchingFiles.size()];
        int i = 0;
        for (File file : matchingFiles) {
            resources[i++] = new FileSystemResource(file);
        }
        return resources;
    }

    private Set<File> retrieveMatchingFiles(File rootDir) {
        if (!rootDir.exists()) {
            return Collections.emptySet();
        }
        if (!rootDir.isDirectory()) {
            return Collections.emptySet();
        }
        if (!rootDir.canRead()) {
            return Collections.emptySet();
        }
        Set<File> result = new LinkedHashSet<>();
        doRetrieveMatchingFiles(rootDir, result);
        return result;
    }

    private void doRetrieveMatchingFiles(File rootDir, Set<File> result) {
        File[] dirContents = rootDir.listFiles();
        if (dirContents == null)
            return;
        for (File content : dirContents) {
            if (content.isDirectory()) {
                if (!content.canRead()) {
                } else {
                    doRetrieveMatchingFiles(content, result);
                }
            } else {
                result.add(content);
            }
        }
    }
}
