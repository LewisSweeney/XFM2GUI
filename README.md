# XFM2GUI

## About
This project focuses on creating a Java and JavaFX based application for interacting with the [futur3soundz XFM2 Synthesizer Module](https://futur3soundz.com). The application was originally developed for submission towards my MSc dissertation project. The idea behind the project comes from the XFM2 synthesizer module not currently having a cross-platform application for interacting with it, instead relying on terminal interaction (or an Excel spreadsheet on Windows platforms). This application provides many of the same features as the mentioned Excel spreadsheet, but provides it in a more user-friendly manner. The application does not have the ability to synthesize without the XFM2 module connected. The application is available on macOS, Windows, and Linux.

## Features
- Connect to XFM2 board via USB-over-serial connection
- Change parameter values on the XFM2 board
    - Sound parameters
    - MIDI Channels and Layering
    - Active Unit
- Read, Write, and Save patches to/from the XFM2 board
- Save and Load local patch files (saved as .xfm2 files)
- Automatic detection of XFM2 device on program load

## Future Features
- Write Tuning, Bank and Program files to the XFM2
- Back compatibility with the gen 1 XFM device (and potential compatibility with the XVA)
- Live Changes to device parameters

## Installation
The application is available as a standalone, installable application on macOS (.pkg), Windows (.exe), and Linux (.deb or .rpm). The latest versions of these files can be found within the release section of the repository. 

Within this section is also a packaged custom runtime for Linux systems, as well as the source code itself. This runtime is provided for Linux in the case of wanting to package it differently for different Linux platforms.

The source code is also available within the release section.

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
- Used to create value-limited JFX TextField nodes

#### [SplashScreen](https://gist.github.com/jewelsea/2305098)
- Adapted to create the splash screen shown on application launch
