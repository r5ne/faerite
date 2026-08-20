# Faerite
<p>
  <a href="https://github.com/r5ne/faerite/releases/latest">
  <img src="https://img.shields.io/github/v/release/r5ne/faerite?style=flat-square"
    alt="GitHub Actions workflow status"/></a>
  <a href="https://github.com/r5ne/faerite/actions">
    <img src="https://img.shields.io/github/actions/workflow/status/r5ne/faerite/release.yml?event=push&label=workflow&style=flat-square"
      alt="GitHub Actions workflow status"/></a>
</p>
An interactive, hand-drawn pixel art atlas.

## Installation
#### v0.0.5+
Faerite can be downloaded straight from the releases tab. Simply extract the downloaded zip for the corresponding operating system.
You will find the executable inside (or in the /bin folder on linux).

Faerite can also be downloaded and ran directly from the terminal which some may find more convenient.
> [!IMPORTANT]
> Replace the LATEST `x.x.x` variable used in all of the commands with the latest version number e.g. `0.0.5`.


##### On Linux
```bash
LATEST="x.x.x" && wget -N "https://github.com/r5ne/faerite/releases/download/v$LATEST/faerite-$LATEST-linux.zip" && unzip -o "faerite-$LATEST-linux.zip" && ./Faerite/bin/Faerite
```
##### On Windows
Using Powershell:

```powershell
$LATEST="x.x.x"; Invoke-WebRequest -Uri "https://github.com/r5ne/faerite/releases/download/v$LATEST/faerite-$LATEST-windows.zip" -OutFile "faerite-$LATEST-windows.zip"; Expand-Archive -Path "faerite-$LATEST-windows.zip" -DestinationPath "." -Force; .\Faerite\Faerite.exe
```
Using Command Prompt:


> [!NOTE]
> Since variables cannot be set and used in the same line you must first set the LATEST variable in a separate line.

```cmd
set LATEST=x.x.x
curl -LO "https://github.com/r5ne/faerite/releases/download/v%LATEST%/faerite-%LATEST%-windows.zip" && tar -xf "faerite-%LATEST%-windows.zip" && .\Faerite\Faerite.exe
```

#### v0.0.1 - v0.0.4:  
```bash
# Using Java 25+.
# Note that v0.0.1 - v0.0.3 are only compiled for Linux as indicated by the filename.
java -jar faerite-[version number].jar
```
  
## Compiling from source
Requirements:
- Faerite v0.0.5+
- openjdk 25.0.4+
- Apache Maven 3.9.16+
- Git v1.8.2+
- Git LFS v3.7.1+

Faerite can be downloaded, compiled and ran all from the terminal using the following commands.  

> [!IMPORTANT]
> Replace the LATEST `x.x.x` variable used in all of the commands with the latest version number e.g. `0.0.5`.

##### On Linux
```bash
VERSION="x.x.x" && git clone --depth 1 --branch "v$VERSION" https://github.com/r5ne/faerite && cd faerite && mvn clean compile javafx:jlink && jpackage --type app-image --runtime-image target/image --name Faerite --module faerite/faerite.Main --dest out && out/Faerite/bin/Faerite
```

##### On Windows
Using Powershell:
```powershell
$LATEST="x.x.x"; git clone --depth 1 --branch "v$VERSION" https://github.com/r5ne/faerite; cd faerite; mvn clean compile javafx:jlink; jpackage --type app-image --runtime-image target\image --name Faerite --module faerite\faerite.Main --dest out; .\Faerite\Faerite.exe
```
Using Command Prompt:
> [!NOTE]
> Since variables cannot be set and used in the same line you must first set the LATEST variable in a separate line.
```cmd
:: Since variables cannot be set and used in the same line you must first set the LATEST variable in a separate line.
set LATEST=x.x.x
git clone --depth 1 --branch "v$VERSION" https://github.com/r5ne/faerite && cd faerite && mvn clean compile javafx:jlink && jpackage --type app-image --runtime-image target\image --name Faerite --module faerite\faerite.Main --dest out && out\Faerite\Faerite.exe
```
## Licence
Faerite is licenced under the [MIT Licence](https://github.com/r5ne/faerite/blob/master/LICENCE).
## Special Thanks
[Дима Дегтярёв](https://github.com/DimasikGit) for testing on Windows.
