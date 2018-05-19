# URLFGroupLib aka. Downloader

###### merupakan library mempermudah proses download beserta penampakan progress bar dalam Java Swing
###### it is a library that helps you to download and show the progress bar within Java Swing

## English Tutorial Text 
The usage is quite simple, what you have to prepare are:
1) JProgressBar
2) Source (URL or AbsolutePath)
3) Target Directory (for saving purpose)

Here is the quick code:
```
Downloader tool = new Downloader();
tool.setProgressBar(jProgressBar1);
tool.setTargetAbsoluteDirectory(textfieldDirSave.getText());

tool.setSourceAbsoluteFile(textfieldSource.getText());
// or 
tool.setSourceURL(textfieldSource.getText());

tool.setOpenDirAfterComplete(true);
tool.start();
```

## Tutorial Text Indonesia
Penggunaannya cukup simple kamu cuma butuhin cuma:
1) JProgressBar
2) Source (URL or AbsolutePath)
3) Target Directory (for saving purpose)

Sederhananya gini code:
```
Downloader tool = new Downloader();
tool.setProgressBar(jProgressBar1);
tool.setTargetAbsoluteDirectory(textfieldDirSave.getText());

tool.setSourceAbsoluteFile(textfieldSource.getText());
// or 
tool.setSourceURL(textfieldSource.getText());

tool.setOpenDirAfterComplete(true);
tool.start();
```


## Preview
![GUI](src/preview/Downloader_Library.PNG)
