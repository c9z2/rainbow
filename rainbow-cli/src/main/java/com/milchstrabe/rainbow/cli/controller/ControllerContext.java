package com.milchstrabe.rainbow.cli.controller;

import com.milchstrabe.rainbow.cli.annotion.NettyController;
import com.milchstrabe.rainbow.cli.annotion.NettyMapping;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @Author ch3ng
 * @Date 2020/5/8 14:41
 * @Version 1.0
 * @Description
 **/
public class ControllerContext {
    private static Map<Integer, Map<Integer, Invoker>> invokers = new HashMap<>();

    public ControllerContext() {
        scanPackage();
    }

    private void scanPackage() {
        String packageName = this.getClass().getPackage().getName();
        String packagePath = packageName.replace(".", "/");

        try {
            Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    File file = new File(filePath);
                    File[] fs = file.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File pathname) {
                            return pathname.getName().endsWith(".class");
                        }
                    });
                    for (File f : fs) {
                        String fName = f.getName();
                        fName = fName.substring(0,fName.lastIndexOf("."));
                        String pkgCls =  packageName + "."+ fName;
                            Class<?> c = Class.forName(pkgCls);
                            NettyController nettyController = c.getAnnotation(NettyController.class);
                            if( nettyController != null){
                                AbstractController controller = (AbstractController)c.newInstance();
                                int cmd1 = nettyController.cmd();
                                Method[] methods = c.getMethods();
                                for(Method method : methods){
                                    NettyMapping annotation = method.getAnnotation(NettyMapping.class);

                                    if(annotation == null){
                                        continue;
                                    }
                                    int cmd2 = annotation.cmd();

                                    Map<Integer, Invoker> map = invokers.get(cmd1);
                                    if(map == null){
                                        map = new HashMap<>();
                                        invokers.put(cmd1,map);
                                    }
                                    map.put(cmd2,Invoker.valueOf(method, controller));

                                }
                            }
                    }
                } else if ("jar".equals(protocol)) {
                    JarFile jar;
                    jar = ((JarURLConnection) url.openConnection()).getJarFile();
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String nameInjar = entry.getName();
                        if(nameInjar.endsWith(".class")){
                            String replace1 = nameInjar.replace("/", ".");
                            replace1 = replace1.substring(0,nameInjar.lastIndexOf("."));
                            Class<?> c = Class.forName(replace1);
                            NettyController nettyController = c.getAnnotation(NettyController.class);
                            if( nettyController != null){
                                AbstractController controller = (AbstractController)c.newInstance();
                                int cmd1 = nettyController.cmd();
                                Method[] methods = c.getMethods();
                                for(Method method : methods){
                                    NettyMapping annotation = method.getAnnotation(NettyMapping.class);

                                    if(annotation == null){
                                        continue;
                                    }
                                    int cmd2 = annotation.cmd();

                                    Map<Integer, Invoker> map = invokers.get(cmd1);
                                    if(map == null){
                                        map = new HashMap<>();
                                        invokers.put(cmd1,map);
                                    }
                                    map.put(cmd2,Invoker.valueOf(method, controller));

                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Invoker getInvoker ( int cmd1, int cmd2){
        Map<Integer, Invoker> map = invokers.get(cmd1);
        if (map != null) {
            return map.get(cmd2);
        }
        return null;
    }


}
