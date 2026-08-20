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
Faerite can be downloaded and ran straight from the releases tab. Simply extract the downloaded zip for the corresponding operating system.

Quick install on linux:
```bash
# Replace with latest version number e.g. 0.0.5.
LATEST=x.x.x
```
```bash
wget -N "https://github.com/r5ne/faerite/releases/download/v$LATEST/faerite-$LATEST-linux.zip"
unzip "faerite-$LATEST-linux.zip"
./Faerite/bin/Faerite
```

#### v0.0.1 - v0.0.4:  
```bash
# Using Java 25+.
# Note that v0.0.1 - v0.0.3 are only compiled for Linux.
java -jar faerite-[version number].jar
```
  
## Compiling from source
Requirements:
- Faerite v0.0.5+
- openjdk 25.0.4+
- Apache Maven 3.9.16+
- Git v1.8.2+
- Git LFS v3.7.1+
```bash
# From the project root:
mvn clean compile javafx:jlink
jpackage --type app-image --runtime-image target/image --name Faerite --module faerite/faerite.Main --dest out
```

Quick compile on linux:
```
# Replace with latest version number e.g. 0.0.5.
VERSION=x.x.x
```
```bash
git clone --depth 1 --branch "v$VERSION" https://github.com/r5ne/faerite
cd faerite
mvn clean compile javafx:jlink
jpackage --type app-image --runtime-image target/image --name Faerite --module faerite/faerite.Main --dest out
out/Faerite/bin/Faerite
```
## Licence
Faerite is licenced under the [MIT Licence](https://github.com/r5ne/faerite/blob/master/LICENCE).
## Special Thanks
[Дима Дегтярёв](https://github.com/DimasikGit) for testing on Windows.
