# What's Inside?
- Downloader
- WebRequest

# 1. WEBREQUEST

###### :exclamation: merupakan library termudah dalam melakukan proses request POST / GET dalam Java
###### :exclamation: it is the easiest library for processing GET / POST method in Java

## WebRequest Tutorial Text :sunny:
The usage is quite simple, you have to prepare these components:
1) Destination URL
2) Key-Value parameter data


## Quick code:
```
 WebRequest coba = new WebRequest();      
 coba.setDestinationURL("http://localhost/testing/terpanggil.php");
 coba.addData("nilaiPOST", "29292");
 coba.setMethod("POST");
 coba.sendNow();
 System.out.println(coba.getObtainedResult());
```

-----------------------------------------------------------

# 2. DOWNLOADER

###### :exclamation: merupakan library mempermudah proses download beserta penampakan progress bar dalam Java Swing
###### :exclamation: it is a library that helps you to download and show the progress bar within Java Swing

## Downloader Tutorial Text :sunny:
The usage is quite simple, you have to prepare these components:
1) JProgressBar
2) Source (URL or AbsolutePath)
3) Target Directory (for saving purpose)

## Quick code:
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

## Super Quick code: :sunglasses:
```
Downloader tool = new Downloader(textfieldSource.getText(), textfieldDirSave.getText(), jProgressBar1);
tool.start();
```

:zap::zap: :zap::zap: -------------------------------- :zap::zap: :zap::zap: 


## Preview
![GUI](src/preview/Downloader_Library2.PNG)


## Credits
Goes back to (c) [FGroupIndonesia](http://fgroupindonesia.com) for educational -class / general projects' purposes.
Any questions or discussions please send via Whatsapp to the admin directly :+1: **+62857-9556-9337** for schedule arrangements.