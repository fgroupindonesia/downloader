package utils;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JProgressBar;

/**
 * This file is made to make process of downloading become easier
 * it can be easily integrated to any JAVA SWING application 
 * @author FGroupIndonesia.com
 */
public class Downloader {

    private String sourceURL;
    private String sourceAbsoluteFile;
    private String targetAbsoluteDirectory;
    private String targetFileName;

    private int percentage;
    private long totalSize;
    private int currentSize;

    // whether this source is url or normal file?
    private boolean usageURL = true;
    private boolean openDirAfterComplete = false;

    private JProgressBar progressBar;

    public void start() {

        percentage = 0;
        totalSize = 0;
        currentSize = 0;

        if (isUsageURL() == true) {
            downloadByURL();
        } else {
            downloadByNormalFile();
        }

    }

    public void openDirCompleted() {

        if (isOpenDirAfterComplete()) {
            Desktop desktop = Desktop.getDesktop();
            File dirToOpen = null;
            try {
                dirToOpen = new File(getTargetAbsoluteDirectory());
                desktop.open(dirToOpen);
            } catch (Exception iae) {
                System.out.println("Directory Not Found");
            }
        }

    }

    private void downloadByURL() {
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(getSourceURL());

                    URLConnection conexion = url.openConnection();

                    conexion.connect();

                    obtainAllHeader(conexion);

                    setTotalSize(conexion.getContentLength());
                    double lengthPerPercent = 100.0 / getTotalSize();

                    InputStream input = new BufferedInputStream(url.openStream());

                    ensureSuffixDirectory();
                    File saveFile = new File(getTargetAbsoluteDirectory() + getTargetFileName());
                    OutputStream output = new FileOutputStream(saveFile);

                    byte data[] = new byte[1024];
                    int count;

                    while ((count = input.read(data)) != -1) {
                        setCurrentSize(getCurrentSize() + count);
                        output.write(data, 0, count);
                        //System.out.println("terbaca " + total);

                        setPercentage((int) Math.round(lengthPerPercent * getCurrentSize()));
                        System.out.println(getPercentage() + " %");

                        setProgressBarValue(getPercentage());

                    }

                    output.flush();
                    output.close();
                    input.close();
                    
                    openDirCompleted();

                } catch (Exception ex) {
                    System.out.println("Error " + ex.getMessage());
                }
            }
        }.start();

    }

    private void setProgressBarValue(int nilaiPersen) {
        if (getProgressBar() != null) {
            getProgressBar().setValue(nilaiPersen);
        }
    }

    private void downloadByNormalFile() {
        new Thread() {
            public void run() {

                try {

                    File target = new File(getSourceAbsoluteFile());
                    setTotalSize(target.length());
                    double lengthPerPercent = 100.0 / getTotalSize();

                    int index = getSourceAbsoluteFile().lastIndexOf("\\");
                    targetFileName = getSourceAbsoluteFile().substring(index + 1);

                    InputStream in = new FileInputStream(target);

                    ensureSuffixDirectory();
                    File saveFile = new File(getTargetAbsoluteDirectory() + getTargetFileName());

                    OutputStream output = new FileOutputStream(saveFile);

                    int terbaca;
                    byte data[] = new byte[1024];

                    while ((terbaca = in.read(data)) != -1) {
                        // do something
                        output.write(data, 0, terbaca);

                        setCurrentSize(getCurrentSize() + terbaca);
                        setPercentage((int) Math.round(lengthPerPercent * getCurrentSize()));
                        System.out.println(getPercentage() + " %");

                        setProgressBarValue(getPercentage());
                        //Thread.sleep(100);
                    }

                    output.flush();
                    output.close();
                    in.close(); 
                    
                    openDirCompleted();
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }.start();

    }

    private void ensureSuffixDirectory() {

        // detect whether this is / or \\ string
        if (getTargetAbsoluteDirectory().endsWith("\\") == false) {
            setTargetAbsoluteDirectory(getTargetAbsoluteDirectory() + "\\");
        } else if (getTargetAbsoluteDirectory().endsWith("/") == false) {
            setTargetAbsoluteDirectory(getTargetAbsoluteDirectory() + "/");
        }

    }

    private void obtainAllHeader(URLConnection connection) {

        /* obtaining
        
        Keep-Alive = timeout=5, max=100, 
        null = HTTP/1.1 200 OK, 
        Server = Apache/2.4.10 (Win32) OpenSSL/1.0.1i PHP/5.6.3, 
        Connection = Keep-Alive, 
        Pragma = public, 
        Content-Description = File Transfer, 
        Date = Sat, 19 May 2018 05:42:39 GMT, 
        Cache-Control = must-revalidate, 
        Content-Disposition = attachment; filename="dino.3gp", 
        Expires = 0, 
        Content-Length = 2040798, 
        Content-Type = application/octet-stream, 
        X-Powered-By = PHP/5.6.3, 
        
         */
        String text2Find = "Content-Disposition";
        String text2Remove = "attachment; filename=";

        Map responseMap = connection.getHeaderFields();
        for (Iterator iterator = responseMap.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();
            System.out.print(key + " = ");

            List values = (List) responseMap.get(key);
            for (int i = 0; i < values.size(); i++) {
                Object o = values.get(i);
                String nilai = String.valueOf(o);
                System.out.println(nilai + ", ");

                if (key != null) {
                    if (key.equalsIgnoreCase(text2Find)) {
                        nilai = nilai.replace("\"", "");
                        nilai = nilai.replace(text2Remove, "");
                        setTargetFileName(nilai);
                    }

                }

            }
        }

    }

    /**
     * @return the sourceURL
     */
    public String getSourceURL() {
        return sourceURL;
    }

    /**
     * @param sourceURL the sourceURL to set
     */
    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    /**
     * @return the sourceAbsoluteFile
     */
    public String getSourceAbsoluteFile() {
        return sourceAbsoluteFile;
    }

    /**
     * @param sourceAbsoluteFile the sourceAbsoluteFile to set
     */
    public void setSourceAbsoluteFile(String sourceAbsoluteFile) {
        this.sourceAbsoluteFile = sourceAbsoluteFile;
    }

    /**
     * @return the targetAbsoluteDirectory
     */
    public String getTargetAbsoluteDirectory() {
        return targetAbsoluteDirectory;
    }

    /**
     * @param targetAbsoluteDirectory the targetAbsoluteDirectory to set
     */
    public void setTargetAbsoluteDirectory(String targetAbsoluteDirectory) {
        this.targetAbsoluteDirectory = targetAbsoluteDirectory;
    }

    /**
     * @return the targetFileName
     */
    public String getTargetFileName() {
        return targetFileName;
    }

    /**
     * @param targetFileName the targetFileName to set
     */
    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    /**
     * @return the percentage
     */
    public int getPercentage() {
        return percentage;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    /**
     * @return the totalSize
     */
    public long getTotalSize() {
        return totalSize;
    }

    /**
     * @param totalSize the totalSize to set
     */
    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    /**
     * @return the currentSize
     */
    public int getCurrentSize() {
        return currentSize;
    }

    /**
     * @param currentSize the currentSize to set
     */
    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    /**
     * @return the usageURL
     */
    public boolean isUsageURL() {
        return usageURL;
    }

    /**
     * @param usageURL the usageURL to set
     */
    public void setUsageURL(boolean usageURL) {
        this.usageURL = usageURL;
    }

    /**
     * @return the progressBar
     */
    public JProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * @param progressBar the progressBar to set
     */
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    /**
     * @return the openDirAfterComplete
     */
    public boolean isOpenDirAfterComplete() {
        return openDirAfterComplete;
    }

    /**
     * @param openDirAfterComplete the openDirAfterComplete to set
     */
    public void setOpenDirAfterComplete(boolean openDirAfterComplete) {
        this.openDirAfterComplete = openDirAfterComplete;
    }
}
