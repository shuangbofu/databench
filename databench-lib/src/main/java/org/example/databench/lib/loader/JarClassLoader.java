package org.example.databench.lib.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by shuangbofu on 2022/4/14 19:51
 */
public class JarClassLoader<T> {
    private final Class<T> interfaceClass;
    private final List<File> jarFiles = new ArrayList<>();

    public JarClassLoader(String jarDirPath, Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
        setUpFiles(jarDirPath);
    }

    public List<T> load(String... name) throws Exception {
        ClassLoader classLoader = getClassLoader(Optional.ofNullable(name).map(Arrays::asList).orElse(new ArrayList<>()));
        return ClassLoaderCallBackMethod.callbackAndReset(() -> {
            ServiceLoader<T> serviceLoader = ServiceLoader.load(interfaceClass);
            List<T> classList = new ArrayList<>();
            serviceLoader.iterator().forEachRemaining(classList::add);
            return classList;
        }, classLoader, true);
    }

    public Optional<T> loadFirst(String... name) throws Exception {
        List<T> list = load(name);
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    private void setUpFiles(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || file.isFile()) {
            throw new RuntimeException("Not found class dir path " + dirPath);
        }
        File[] fileArray = file.listFiles();
        if (fileArray != null) {
            jarFiles.addAll(Arrays.stream(fileArray)
                    .filter(i -> i.isFile() && i.getName().endsWith(".jar"))
                    .collect(Collectors.toList()));
        }
        if (jarFiles.isEmpty()) {
            throw new RuntimeException("Not found jar file from " + dirPath);
        }
    }

    private ClassLoader getClassLoader(List<String> names) {
        URL[] urls = jarFiles
                .stream().filter(i -> {
                    if (names.isEmpty()) {
                        return true;
                    } else {
                        return names.contains(i.getName().replace(".jar", ""));
                    }
                }).map(i -> {
                    try {
                        return i.toURI().toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException("File to url error", e);
                    }
                }).toArray(URL[]::new);
        return new PathClassLoader(urls, interfaceClass.getClassLoader());
    }
}
