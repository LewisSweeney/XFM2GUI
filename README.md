# XFM2GUI

## About
This project focuses on creating a JavaFX based GUI for interacting with the [futur3soundz XFM2 Synthesizer Module](https://futur3soundz.com). The application was made in part submission towards my MSc dissertation project. The idea behind the project comes from the XFM2 synthesizer module not currently having a 
cross-platform application for interacting with it, instead relying on terminal interaction (or an Excel spreadsheet on Windows platforms).

## Features
- Change parameters of the XFM2 board
- Save and Load local patch files (saved as .xfm2 files)
- Automatic detection of XFM2 device on program load
- Usable on Windows, Mac and Linux

## Future Features
- Write Tuning, Bank and Program files to the XFM2
- Back compatibility with the gen 1 XFM device (and potential compatibility with the XVA)

## 3rd Party Code
### [JSSC](https://github.com/scream3r/java-simple-serial-connector)
- Used for serial interaction on Linux and macOS

### [jSerialComm](https://fazecast.github.io/jSerialComm/)
- Used for serial interaction on Windows

### [XLoad](https://www.futur3soundz.com/)
- The program created by futur3soundz to initially program the XFM2
- Used for adapting commands that are undocumented in the official XFM2 user manual

### [Save/Load](https://www.genuinecoder.com/save-files-javafx-filechooser/)
- Adapted this code to save/load local files

### Code by jewelsea
#### [IntField](https://gist.github.com/jewelsea/1962045)
#### [SplashScreen](https://gist.github.com/jewelsea/2305098)